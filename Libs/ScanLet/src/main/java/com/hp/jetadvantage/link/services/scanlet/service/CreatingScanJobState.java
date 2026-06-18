/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.services.scanlet.service;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.VisibleForTesting;

import com.hp.jetadvantage.link.api.ILetObserver;
import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.api.job.JobInfo;
import com.hp.jetadvantage.link.api.job.Joblet;
import com.hp.jetadvantage.link.api.job.ScanJobData;
import com.hp.jetadvantage.link.api.job.ScanJobState;
import com.hp.jetadvantage.link.api.massstorage.MassStorageInfo;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes;
import com.hp.jetadvantage.link.api.scanner.ScanAttributesReader;
import com.hp.jetadvantage.link.api.scanner.Scanlet;
import com.hp.jetadvantage.link.api.scanner.intent.ScanToRequestIntent;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.constants.ErrorMessages;
import com.hp.jetadvantage.link.common.helper.SelectedPrinterHelper;
import com.hp.jetadvantage.link.common.model.PrinterInfo;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceEmailService;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceScanJobService;
import com.hp.jetadvantage.link.device.services.standard.StandardDeviceEmailService;
import com.hp.jetadvantage.link.device.services.standard.StandardDeviceScanJobService;
import com.hp.jetadvantage.link.device.services.types.EmailSettingsData;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentServiceState;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentServiceStateMachine;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.CreatingJobState;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.EndState;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.ReportErrorState;
import com.hp.jetadvantage.link.services.joblet.model.JobSource;
import com.hp.jetadvantage.link.services.joblet.service.JobletService;
import com.hp.jetadvantage.link.services.scanlet.ScanConstants;
import com.hp.jetadvantage.link.services.scanlet.adapter.ScanTicketAdapter;
import com.hp.jetadvantage.link.services.storagelet.IStorage;
import com.hp.jetadvantage.link.services.storagelet.StorageFactory;

public class CreatingScanJobState extends CreatingJobState {

    protected CreatingScanJobState(Intent jobIntent) {
        super(jobIntent);
        TAG = TAG + "Scan";
    }

    @Override
    protected BaseJobIntentServiceState initializeJob(final Intent intent, BaseJobIntentServiceStateMachine stateMachine) {
        SLog.d(TAG, "initializeJob() ENTER");
        try {
            final Bundle extraParams = intent.getBundleExtra(ScanJobIntentService.EXTRA_PARAMS);
            ScanToRequestIntent.IntentParams reqParams = ScanToRequestIntent.getIntentParams(extraParams);
            if (reqParams == null || reqParams.getScanAttributes() == null) {
                SLog.e(TAG, "createJob : Expected parameters not found");
                // finish service (we don't know target package - so nowhere to report
                return new EndState();
            }
            stateMachine.setJobRid(reqParams.getReqId());
            stateMachine.getReporterToApp().setTargetPackage(reqParams.getPackageName());
            stateMachine.setTargetPackageName(reqParams.getPackageName());

            // Step1. check connected device
            final PrinterInfo pi = SelectedPrinterHelper.get(stateMachine.getContentResolver());
            if (PrinterInfo.isEmpty(pi)) {
                return new ReportErrorState(Result.RESULT_FAIL, Result.ErrorCode.CONNECTION_ERROR, "Device is not connected");
            }
            stateMachine.setDeviceDisconnectedOrJobFail(false);

            // Step2. check scanner service support
            if (!isSupported(stateMachine.getContentResolver())) {
                return new ReportErrorState(Result.RESULT_FAIL, Result.ErrorCode.NOT_SUPPORTED, "ScannerService is not supported");
            }

            // Step3. verify scan attributes
            SLog.d(TAG, "Scan Attributes: " + reqParams.getScanAttributes().toString());
            ScanAttributesReader scanAttributesReader = new ScanAttributesReader(reqParams.getScanAttributes());
            ReportErrorState errorState = validateScanDestinationAttributes(reqParams.getApiLevel(), scanAttributesReader);
            if (errorState != null) {
                return errorState;
            }

            stateMachine.setJobBundle(createJobBundle(reqParams, scanAttributesReader));
            stateMachine.setExtraJobBundle(createExtraJobBundle(reqParams, scanAttributesReader));
            stateMachine.setJobAttributesReader(scanAttributesReader);

            // TODO: We will add a new API key (e.g. Scanlet.Keys.KEY_USE_DESTINATION_DEFAULTS)
            // to allow apps to explicitly request destination-based defaults.
            // For now, we default to false to maintain backward compatibility.
            // reference: doc/ScannerService_submit_job_with_default_options.puml
            boolean useDestinationFromScanAttrs = false;
            int clientVersion = reqParams.getApiLevel() != null ? reqParams.getApiLevel() : Sdk.VERSION.LEVEL;
            return createScanJob(stateMachine, reqParams.getPackageName(), scanAttributesReader, useDestinationFromScanAttrs, clientVersion);
        } catch (Exception e) {
            SLog.e(TAG, "Failed to prepare job " + e.getMessage());
            return new ReportErrorState(Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        } finally {
            SLog.d(TAG, "initializeJob() EXIT");
        }
    }

    private ReportErrorState validateScanDestinationAttributes(int clientVersion, ScanAttributesReader scanAttributes) {
        if (ScanConstants.LOCAL_FOLDER_DESTINATIONS.contains(scanAttributes.getDestination())) {
            if (clientVersion <= Sdk.VERSION_LEVEL.THREE) {
                return new ReportErrorState(Result.RESULT_FAIL, Result.ErrorCode.NOT_SUPPORTED,
                        ErrorMessages.NotSupported.CLIENT_VERSION_NOT_SUPPORTED);
            }
        }

        ReportErrorState errorState = null;
        switch (scanAttributes.getDestination()) {
            case ME:
                break;
            case HTTP:
                errorState = validateUri(scanAttributes.getUri());
                break;
            case FTP:
            case NETWORK_FOLDER:
                // TransmissionMode.Image is not supported for FTP or Network folder
                if (scanAttributes.getTransmissionMode() == ScanAttributes.TransmissionMode.IMAGE) {
                    errorState = new ReportErrorState(Result.RESULT_FAIL, Result.ErrorCode.INVALID_PARAM, "Transmission mode Image is not supported for" +
                            " FTP or Network folder destination");
                } else {
                    errorState = validateUri(scanAttributes.getUri());
                }
                break;
            case EMAIL:
                errorState = validateEmailSettings(scanAttributes);
                break;
            case USB:
                IStorage usbStorage = StorageFactory.INSTANCE.getStorageForPath(MassStorageInfo.StorageType.USB, scanAttributes.getUsbLocation());
                if (usbStorage == null) {
                    errorState = new ReportErrorState(Result.RESULT_FAIL, Result.ErrorCode.INVALID_PARAM,
                            "USB storage containing path " + scanAttributes.getUsbLocation() + " not found");
                }
                break;
            default:
                break;
        }
        return errorState;
    }

    private ReportErrorState validateUri(Uri uri) {
        try {
            ScanAttributes.validateUri(uri);
            return null;
        } catch (Exception e) {
            return new ReportErrorState(Result.RESULT_FAIL, Result.ErrorCode.INVALID_PARAM, "Destination URL is not valid");
        }
    }

    @VisibleForTesting
    protected boolean isSupported(ContentResolver contentResolver) {
        final Bundle returnBundle = contentResolver.call(Scanlet.CONTENT_OXP_URI, Scanlet.Method.IS_SUPPORTED, null, null);

        return returnBundle != null && Result.parse(returnBundle, new Result()).getCode() == Result.RESULT_OK && returnBundle.containsKey(Scanlet.IS_SUPPORTED_EXTRA) && returnBundle.getBoolean(Scanlet.IS_SUPPORTED_EXTRA);
    }

    /**
     * Executes main scan operation
     */
    private BaseJobIntentServiceState createScanJob(final BaseJobIntentServiceStateMachine stateMachine, final String packageName,
                                                    ScanAttributesReader scanAttributes, boolean useDestinationFromScanAttrs,
                                                    int clientVersion) throws Exception {
        SLog.d(TAG, "createScanJob() ENTER");

        //create ScanTicket
        IDeviceScanJobService scanJobService = new StandardDeviceScanJobService();
        ScanAttributesReader defaultScanAttributes = ScanTicketAdapter.getDefaultScanAttributes(packageName, scanJobService,
                scanAttributes.getDestination(), useDestinationFromScanAttrs, clientVersion);

        String jobId = ScanTicketAdapter.createScanJob(packageName, scanJobService, scanAttributes, defaultScanAttributes);
        if (jobId == null || jobId.isEmpty()) {
            SLog.e(TAG, "createScanJob() EXIT - JobId is null ");
            return new ReportErrorState(Result.RESULT_FAIL, Result.ErrorCode.SYSTEM_ERROR, "Failed to create scan job");
        }
        stateMachine.setJobId(jobId);

        // TODO DUNE-291837: Scan metadata: jobName handling
        // TODO: We need to add a function to properly retrieve and return the job name from metadata.
        stateMachine.getJobInfo().setJobName(ScanConstants.DEFAULT_JOB_NAME);
        SLog.d(TAG, "createScanJob() EXIT - JobId[" + jobId + "]");
        return new MonitoringScanJobState(jobId);
    }

    private Bundle createJobBundle(ScanToRequestIntent.IntentParams mReqParams, ScanAttributesReader scanAttributes) {
        int clientLevel = mReqParams.getApiLevel() != null ? mReqParams.getApiLevel() : scanAttributes.getVersion();

        ScanJobData scanJobData = new ScanJobData();
        scanJobData.setDestination(scanAttributes.getDestination());
        scanJobData.setDuplex(scanAttributes.getPlex());
        scanJobData.setImagesScanned(0);
        scanJobData.setImagesProcessed(0);
        scanJobData.setImagesTransmitted(0);
        scanJobData.setJobState(new ScanJobState(ScanJobState.State.PENDING));
        scanJobData.setScanSize(scanAttributes.getScanSize());

        JobInfo jobInfo = new JobInfo();
        jobInfo.setJobData(scanJobData);
        jobInfo.setJobName(ScanConstants.DEFAULT_JOB_NAME);
        jobInfo.setJobType(JobInfo.JobType.SCAN);
        jobInfo.setVersion(clientLevel);

        Bundle jobBundle = new Bundle();
        jobBundle.putInt(Joblet.Keys.KEY_CLIENTS_VERSION, clientLevel);
        jobBundle.putSerializable(Joblet.Keys.KEY_JOB_TYPE, JobInfo.JobType.SCAN);
        jobBundle.putInt(Joblet.Keys.KEY_SCAN_IMAGE_COUNT, 0);
        jobBundle.putInt(Joblet.Keys.KEY_DST_TOTAL, 0);
        jobBundle.putBoolean(JobletService.JOB_EMULATED_TAG, true);
        jobBundle.putString(JobletService.JOB_SOURCE_TAG, JobSource.SDK.name());
        jobBundle.putParcelable(ILetObserver.Keys.KEY_JOB_INFO, jobInfo);
        Result.pack(jobBundle, Result.RESULT_OK);
        return jobBundle;
    }

    private Bundle createExtraJobBundle(ScanToRequestIntent.IntentParams mReqParams, ScanAttributesReader scanAttributes) {
        int clientLevel = mReqParams.getApiLevel() != null ? mReqParams.getApiLevel() : scanAttributes.getVersion();
        // Monitor the job completion
        Bundle mExtraBundle = new Bundle();
        mExtraBundle.putString(Joblet.Keys.KEY_RID, mReqParams.getReqId());
        mExtraBundle.putSerializable(Joblet.Keys.KEY_JOB_TYPE, JobInfo.JobType.SCAN);

        // Put clients version to determine further Joblet behaviour
        mExtraBundle.putInt(Joblet.Keys.KEY_CLIENTS_VERSION, clientLevel);
        // Put clients package to let callbacks sending only to assigned packages
        mExtraBundle.putString(Joblet.Keys.KEY_CLIENT_PACKAGE, mReqParams.getPackageName());
        return mExtraBundle;
    }

    private ReportErrorState validateEmailSettings(ScanAttributesReader scanAttributes) {
        if (scanAttributes.getDestination() != ScanAttributes.Destination.EMAIL) {
            return null;
        }

        EmailSettingsData emailSettingsData;
        if (scanAttributes.getSmtpAttributes() == null) {
            // getting email settings (on Panel only and if smtp attributes were not provided)
            try {
                // for email destination we need to retrieve data from device before
                IDeviceEmailService deviceEmailService = new StandardDeviceEmailService();
                emailSettingsData = deviceEmailService.getEmailSettings();

                // pre-validate data
                if (emailSettingsData == null) {
                    return new ReportErrorState(Result.RESULT_FAIL, Result.ErrorCode.SYSTEM_ERROR, "Failed to retrieve email settings");
                } else if (!emailSettingsData.isSendToEmailEnabled() || emailSettingsData.getSmtpServerHostName() == null || emailSettingsData.getSmtpServerPort() == null) {
                    return new ReportErrorState(Result.RESULT_FAIL, Result.ErrorCode.SYSTEM_ERROR, "Email settings are not configured");
                } else if (scanAttributes.getEmailAttributes().getFrom() == null && TextUtils.isEmpty(emailSettingsData.getDefaultFrom().getEmailAddress()) && TextUtils.isEmpty(emailSettingsData.getDefaultFrom().getDefaultEmailAddress())) {
                    return new ReportErrorState(Result.RESULT_FAIL, Result.ErrorCode.INVALID_PARAM, "Email FROM value is missing in parameters and default " +
                            "value is not configured");
                }
            } catch (Exception e) {
                SLog.w(TAG, e.getMessage());
                return new ReportErrorState(Result.RESULT_FAIL, Result.ErrorCode.SYSTEM_ERROR, "Unable to retrieve email settings");
            }
        } else {
            emailSettingsData = null;

            // re-check if host was set
            if (scanAttributes.getSmtpAttributes().getHost() == null) {
                return new ReportErrorState(Result.RESULT_FAIL, Result.ErrorCode.INVALID_PARAM, "SMTP host value must be provided");
            }

            // re-check if FROM was set
            if (scanAttributes.getEmailAttributes().getFrom() == null) {
                return new ReportErrorState(Result.RESULT_FAIL, Result.ErrorCode.INVALID_PARAM, "Email FROM value must be provided");
            }
        }
        return null;
    }
}
