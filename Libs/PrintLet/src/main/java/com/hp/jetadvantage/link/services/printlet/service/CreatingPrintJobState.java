package com.hp.jetadvantage.link.services.printlet.service;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.hp.jetadvantage.link.api.ILetObserver;
import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.api.job.JobInfo;
import com.hp.jetadvantage.link.api.job.Joblet;
import com.hp.jetadvantage.link.api.job.PrintJobData;
import com.hp.jetadvantage.link.api.job.PrintJobState;
import com.hp.jetadvantage.link.api.massstorage.MassStorageInfo;
import com.hp.jetadvantage.link.api.printer.PrintAttributes;
import com.hp.jetadvantage.link.api.printer.PrintAttributesReader;
import com.hp.jetadvantage.link.api.printer.Printlet;
import com.hp.jetadvantage.link.api.printer.intent.PrintRequestIntent;
import com.hp.jetadvantage.link.common.helper.SelectedPrinterHelper;
import com.hp.jetadvantage.link.common.model.PrinterInfo;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentServiceState;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentServiceStateMachine;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.CreatingJobState;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.EndState;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.ReportErrorState;
import com.hp.jetadvantage.link.services.joblet.model.JobSource;
import com.hp.jetadvantage.link.services.joblet.service.JobletService;
import com.hp.jetadvantage.link.services.printlet.ipp.IppClient;
import com.hp.jetadvantage.link.services.printlet.ipp.IppConnector;
import com.hp.jetadvantage.link.services.printlet.model.Error;
import com.hp.jetadvantage.link.services.printlet.model.PrinterAttributes;
import com.hp.jetadvantage.link.services.printlet.util.PrintAttributesConverter;
import com.hp.jetadvantage.link.services.storagelet.IStorage;
import com.hp.jetadvantage.link.services.storagelet.StorageFactory;

import java.io.File;
import java.io.FileNotFoundException;

public class CreatingPrintJobState extends CreatingJobState {
    protected CreatingPrintJobState(Intent jobIntent) {
        super(jobIntent);
        TAG = TAG + "/Print";
        this.possibleNextStates.add(ReservePriorityPrintState.class.getSimpleName());
    }

    @Override
    protected BaseJobIntentServiceState initializeJob(Intent intent, BaseJobIntentServiceStateMachine stateMachine) {
        SLog.e(TAG, "CreatingPrintJobState.initializeJob");
        try {
            PrintJobIntentServiceStateMachine sm = (PrintJobIntentServiceStateMachine) stateMachine;
            final Bundle extraParams = intent.getBundleExtra(PrintJobIntentService.EXTRA_PARAMS);
            sm.setExtraParams(extraParams);
            sm.setIsBackgroundJob(extraParams.getBoolean(OXPCreatePrintSpoolerIntentService.EXTRA_IS_BACKGROUNDJOB));
            sm.setIsFirstJob(extraParams.getBoolean(OXPCreatePrintSpoolerIntentService.EXTRA_IS_FIRSTJOB));
            sm.setIsLastJob(extraParams.getBoolean(OXPCreatePrintSpoolerIntentService.EXTRA_IS_LASTJOB));


            PrintRequestIntent.IntentParams reqParams = PrintRequestIntent.getIntentParams(extraParams);
            if (reqParams == null || reqParams.getPrintAttributes() == null) {
                SLog.e(TAG, "createJob : Expected parameters not found");
                // finish service (we don't know target package - so nowhere to report
                return new EndState();
            }
            sm.setJobRid(reqParams.getReqId());
            sm.getReporterToApp().setTargetPackage(reqParams.getPackageName());

            // Step1. check connected device
            final PrinterInfo pi = SelectedPrinterHelper.get(sm.getContentResolver());
            if (PrinterInfo.isEmpty(pi)) {
                return new ReportErrorState(Result.RESULT_FAIL, Result.ErrorCode.CONNECTION_ERROR, "Device is not connected");
            }

            sm.setDeviceDisconnectedOrJobFail(false);

            // Step2. check scanner service support
            if (!isSupported(sm.getContentResolver())) {
                return new ReportErrorState(Result.RESULT_FAIL, Result.ErrorCode.NOT_SUPPORTED, "PrinterService is not supported");
            }

            // Step3. verify scan attributes
            PrintAttributesReader printAttributesReader = new PrintAttributesReader(reqParams.getPrintAttributes());
            ReportErrorState errorState = validatePrintAttribute(printAttributesReader, reqParams, stateMachine);
            if (errorState != null) {
                return errorState;
            }

            sm.setJobBundle(createJobBundle(reqParams, printAttributesReader));
            sm.setExtraJobBundle(createExtraJobBundle(reqParams, printAttributesReader));
            sm.setJobAttributesReader(printAttributesReader);

            sm.initDefaultPrintAttributesReceived();

            return createPrintJob(sm, intent);
        } catch (Exception e) {
            SLog.e(TAG, "Failed to prepare job " + e.getMessage());
            return new ReportErrorState(Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        }
    }

    private ReportErrorState validatePrintAttribute(PrintAttributesReader printAttributes, PrintRequestIntent.IntentParams reqParams, BaseJobIntentServiceStateMachine stateMachine) {

        try {
            if (printAttributes != null && printAttributes.getUri() != null && !TextUtils.isEmpty(printAttributes.getUri().getPath())) {
                if (printAttributes.getUri().getPath().indexOf("..") >= 0) {
                    return new ReportErrorState(Result.RESULT_FAIL, Result.ErrorCode.INVALID_PARAM, "File name is not valid.");
                }
            }

            if (printAttributes.getSource() == PrintAttributes.Source.STORAGE && !TextUtils.isEmpty(reqParams.getExtraUri())) { // For fileprovider
                if (reqParams.getExtraUri().toString().startsWith("content://")) {
                    // it is impossible(maybe?) to check whether a file on a application exist or not by File api
                    // so check it by opening an inputStream
                    if (stateMachine.getContentResolver().openInputStream(Uri.parse(reqParams.getExtraUri())) == null) {
                        throw new IllegalArgumentException("There is no file \"" + reqParams.getExtraUri() + "\" in the storage.");
                    }
                } else {
                    PrintAttributes.validateUri(printAttributes.getSource(), Uri.parse(new File(printAttributes.getUri().toString()).getPath()), printAttributes.getDocumentFormat());
                }
            } else {
                PrintAttributes.validateUri(printAttributes.getSource(), printAttributes.getUri(), printAttributes.getDocumentFormat());
            }
        } catch (Exception e) {
            SLog.e(TAG, "Failed to validate print attributes " + e.getMessage());
            return new ReportErrorState(Result.RESULT_FAIL, Result.ErrorCode.INVALID_PARAM, e.getMessage());
        }

        return null;
    }

    private boolean isSupported(ContentResolver contentResolver) {
        final Bundle returnBundle = contentResolver.call(Printlet.CONTENT_OXP_URI, Printlet.Method.IS_SUPPORTED, null, null);

        return returnBundle != null && Result.parse(returnBundle, new Result()).getCode() == Result.RESULT_OK && returnBundle.containsKey(Printlet.IS_SUPPORTED_EXTRA) && returnBundle.getBoolean(Printlet.IS_SUPPORTED_EXTRA);
    }

    private BaseJobIntentServiceState createPrintJob(final BaseJobIntentServiceStateMachine stateMachine, final Intent intent) throws Exception {

        SLog.d(TAG, "CreatingPrintJobState.createPrintJob");
        PrintJobIntentServiceStateMachine sm = (PrintJobIntentServiceStateMachine) stateMachine;
        final Bundle extraParams = intent.getBundleExtra(PrintJobIntentService.EXTRA_PARAMS);
        PrintAttributesReader printAttributesReader = stateMachine.getJobAttributesReader(PrintAttributesReader.class);
        PrintRequestIntent.IntentParams reqParams = PrintRequestIntent.getIntentParams(extraParams);

        String fileUri = printAttributesReader.getUri().toString();

        if (printAttributesReader.getSource() == PrintAttributes.Source.USB) {
            IStorage usbStorage = StorageFactory.INSTANCE.getStorageForPath(MassStorageInfo.StorageType.USB, fileUri);

            if (usbStorage == null) {
                return new ReportErrorState(Result.RESULT_FAIL, Result.ErrorCode.INVALID_PARAM, "USB storage containing path " + fileUri + " not found");
            }

            if (!(new File(fileUri)).exists()) {
                return new ReportErrorState(Result.RESULT_FAIL, Result.ErrorCode.INVALID_PARAM, "File " + fileUri + " not found");
            }
        }

        String uiContextID = sm.getPrintJobService().getUiContextToken(reqParams.getPackageName());
        //if ((uiContext == null || uiContext.isEmpty()) && extraParams.getBoolean(OXPCreatePrintSpoolerIntentService.EXTRA_IS_BACKGROUNDJOB)) {
        if ((uiContextID == null || uiContextID.isEmpty()) && !sm.isBackgroundJob()) {
            return new ReportErrorState(Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, "Failed to get UI context");
        }

        PrintJobData printJobData = ((JobInfo) stateMachine.getJobBundle().getParcelable(ILetObserver.Keys.KEY_JOB_INFO)).getJobData();
        if (printJobData.getDuplex() == PrintAttributes.Duplex.DEFAULT) {
            Message response = IppClient.getInstance().getPrinterAttributes(stateMachine.getContext(), true, 0);
            PrinterAttributes printAttributes = (PrinterAttributes) response.obj;

            PrintAttributes.Duplex duplex = PrintAttributesConverter.getDuplex(printAttributes.defaultPlexMode);
            if (duplex != null) {
                printJobData.setDuplex(duplex);
            }
        }

        //return enterPriorityPrint(sm.getContext(), uiContext, sm.isBackgroundJob(), sm.isFirstJob());
        return enterPriorityPrint(sm);
    }

    private BaseJobIntentServiceState enterPriorityPrint(PrintJobIntentServiceStateMachine sm) {
        Message response = null;
        try {
            final Bundle extraParams = sm.getExtraParams();
            PrintRequestIntent.IntentParams reqParams = PrintRequestIntent.getIntentParams(extraParams);
            String uiContextID = sm.getPrintJobService().getUiContextToken(reqParams.getPackageName());
            response = IppClient.getInstance().enterPriorityPrint(sm.getContext(), uiContextID, 0, sm.isBackgroundJob(), sm.isFirstJob());
        } catch (Error e) {
            SLog.e(TAG, "Error entering priority print: " + e.getMessage());
            response = null;
        }
        if (response != null && response.arg1 == IppConnector.Constants.REQUEST_RETURN_CODE__OK) {
            return new ReservePriorityPrintState();
        } else {
            sm.sendLocalPrintResult(OXPCreatePrintSpoolerIntentService.ACTION_CANCEL_CURRENT_BATCH, false);
            return new ReportErrorState(Result.RESULT_FAIL, Result.ErrorCode.UNKNOWN, "Error entering priority print");
        }
    }

    private Bundle createJobBundle(PrintRequestIntent.IntentParams reqParams, PrintAttributesReader printAttributes) {
        Bundle jobBundle = new Bundle();
        Result.pack(jobBundle, Result.RESULT_OK);

        // check requestIntent version first (it can contain overridden value from Setting UI) then check attributes
        int clientLevel = reqParams.getApiLevel() != null ? reqParams.getApiLevel() : printAttributes.getVersion();

        JobInfo jobInfo = new JobInfo();
        jobInfo.setJobType(JobInfo.JobType.PRINT);
        jobInfo.setJobName(printAttributes.getJobName());

        PrintJobData printJobData = new PrintJobData();
        printJobData.setCopies(printAttributes.getCopies());
        printJobData.setDuplex(printAttributes.getPlex());
        printJobData.setJobState(new PrintJobState(PrintJobState.State.PROCESSING));
        printJobData.setSource(printAttributes.getSource());
        jobInfo.setJobData(printJobData);

        jobBundle.putInt(Joblet.Keys.KEY_CLIENTS_VERSION, clientLevel);
        jobBundle.putSerializable(Joblet.Keys.KEY_JOB_TYPE, JobInfo.JobType.PRINT);
        jobBundle.putInt(Joblet.Keys.KEY_PRINT_IMAGE_COUNT, -1);
        jobBundle.putInt(Joblet.Keys.KEY_SHEET_COUNT, 0);
        jobBundle.putInt(JobletService.KEY_EMULATED_NUM_COPIES_TAG, printAttributes.getCopies());
        jobBundle.putBoolean(JobletService.JOB_EMULATED_TAG, true);
        jobBundle.putString(JobletService.JOB_SOURCE_TAG, JobSource.SDK.name());
        jobBundle.putParcelable(ILetObserver.Keys.KEY_JOB_INFO, jobInfo);

        return jobBundle;
    }

    private Bundle createExtraJobBundle(PrintRequestIntent.IntentParams reqParams, PrintAttributesReader printAttributes) {
        int clientLevel = reqParams.getApiLevel() != null ? reqParams.getApiLevel() : printAttributes.getVersion();
        // Monitor the job completion
        // Set the Device Job Type
        Bundle extraBundle = new Bundle();
        extraBundle.putString(Joblet.Keys.KEY_RID, reqParams.getReqId());
        extraBundle.putSerializable(Joblet.Keys.KEY_JOB_TYPE, JobInfo.JobType.PRINT);

        // Put clients version toi determine further Joblet behaviour
        extraBundle.putInt(Joblet.Keys.KEY_CLIENTS_VERSION, clientLevel);
        // Put clients package to let callbacks sending only to assigned packages
        extraBundle.putString(Joblet.Keys.KEY_CLIENT_PACKAGE, reqParams.getPackageName());

        return extraBundle;
    }
}
