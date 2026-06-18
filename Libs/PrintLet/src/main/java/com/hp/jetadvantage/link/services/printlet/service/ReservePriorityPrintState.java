package com.hp.jetadvantage.link.services.printlet.service;

import android.content.Context;
import android.os.Message;
import android.text.TextUtils;

import com.hp.jetadvantage.link.api.ILetObserver;
import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.api.job.JobInfo;
import com.hp.jetadvantage.link.api.job.PrintJobData;
import com.hp.jetadvantage.link.api.job.PrintJobState;
import com.hp.jetadvantage.link.api.massstorage.MassStorageInfo;
import com.hp.jetadvantage.link.api.printer.PrintAttributes;
import com.hp.jetadvantage.link.api.printer.PrintAttributesReader;
import com.hp.jetadvantage.link.api.printer.intent.PrintRequestIntent;
import com.hp.jetadvantage.link.common.Platform;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentServiceState;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentServiceStateMachine;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.ReportErrorState;
import com.hp.jetadvantage.link.services.printlet.ipp.IppClient;
import com.hp.jetadvantage.link.services.printlet.ipp.IppConnector;
import com.hp.jetadvantage.link.services.printlet.model.CollateMode;
import com.hp.jetadvantage.link.services.printlet.model.ColorMode;
import com.hp.jetadvantage.link.services.printlet.model.Error;
import com.hp.jetadvantage.link.services.printlet.model.FileType;
import com.hp.jetadvantage.link.services.printlet.model.Finishings;
import com.hp.jetadvantage.link.services.printlet.model.MediaSize;
import com.hp.jetadvantage.link.services.printlet.model.MediaSource;
import com.hp.jetadvantage.link.services.printlet.model.MediaType;
import com.hp.jetadvantage.link.services.printlet.model.Orientation;
import com.hp.jetadvantage.link.services.printlet.model.OutputBin;
import com.hp.jetadvantage.link.services.printlet.model.PlexMode;
import com.hp.jetadvantage.link.services.printlet.model.PrintJobID;
import com.hp.jetadvantage.link.services.printlet.model.PrintQuality;
import com.hp.jetadvantage.link.services.printlet.model.PrinterAttributes;
import com.hp.jetadvantage.link.services.printlet.model.PrintTicket;
import com.hp.jetadvantage.link.services.printlet.model.ScalingMode;
import com.hp.jetadvantage.link.services.printlet.model.StapleMode;
import com.hp.jetadvantage.link.services.printlet.util.PrintAttributesConverter;
import com.hp.jetadvantage.link.services.storagelet.IStorage;
import com.hp.jetadvantage.link.services.storagelet.StorageFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.util.Enumeration;
import java.util.List;

public class ReservePriorityPrintState extends PriorityPrintState {
    private static final String LOCAL_HOST = "localhost";

    protected ReservePriorityPrintState() {
        super(ReservePriorityPrintState.class.getSimpleName());
        TAG = TAG + "/ReservePriorityPrint";
        this.possibleNextStates.add(StartPrintState.class.getSimpleName());
    }

    @Override
    protected BaseJobIntentServiceState action(BaseJobIntentServiceStateMachine stateMachine) {
        PrintJobIntentServiceStateMachine sm = (PrintJobIntentServiceStateMachine) stateMachine;
        PrintAttributesReader attributesReader = sm.getJobAttributesReader(PrintAttributesReader.class);
        PrintRequestIntent.IntentParams reqParams = getReqParams(stateMachine);

        String fileUri = "";

        if (attributesReader.getSource() == PrintAttributes.Source.STORAGE) {
            if (attributesReader.getUri().toString().startsWith(PrintJobIntentService.DOWNLOAD_FILE_PATH)) {
                fileUri = attributesReader.getUri().toString();
            } else {
                fileUri = reqParams.getExtraUri();
            }
        } else {
            fileUri = attributesReader.getUri().toString();
        }

        if (attributesReader.getSource() == PrintAttributes.Source.USB) {
            IStorage usbStorage = StorageFactory.INSTANCE.getStorageForPath(MassStorageInfo.StorageType.USB, fileUri);

            if (usbStorage == null) {
                return new ReportErrorState(Result.RESULT_FAIL, Result.ErrorCode.INVALID_PARAM, "USB storage containing path " + fileUri + " not found");
            }

            if (!(new File(fileUri)).exists()) {
                return new ReportErrorState(Result.RESULT_FAIL, Result.ErrorCode.INVALID_PARAM, "File " + fileUri + " not found");
            }
        }

        String uiContext = sm.getPrintJobService().getUiContextToken(reqParams.getPackageName());
        boolean isBackgroundJob = sm.isBackgroundJob();//sm.getJobBundle().getBoolean(OXPCreatePrintSpoolerIntentService.EXTRA_IS_BACKGROUNDJOB);
        boolean isLastJob = sm.isLastJob();//sm.getJobBundle().getBoolean(OXPCreatePrintSpoolerIntentService.EXTRA_IS_LASTJOB);
        try{
            final PrintTicket ticket = getPrintTicket(fileUri, attributesReader, reqParams, sm.getContext());

            Message response = null;
            if (!sm.isBackgroundJob()) {
                response = IppClient.getInstance().priorityPrintUri(stateMachine.getContext(), uiContext, ticket, 0);
            } else {
                response = IppClient.getInstance().printUri(stateMachine.getContext(), ticket, 0);
            }
            if (response.arg1 == IppConnector.Constants.REQUEST_RETURN_CODE__OK) {
                PrintJobID jobId = ((PrintJobID) response.obj);
                sm.setPrintJobID(jobId);
            } else if (response.arg1 == IppConnector.Constants.REQUEST_RETURN_CODE__EXCEPTION) {
                // Something wrong.
                SLog.i(TAG, "Reserve priority print return exception.");
                sm.sendLocalPrintResult(OXPCreatePrintSpoolerIntentService.ACTION_CANCEL_CURRENT_BATCH, false);

                IppClient.getInstance().exitPriorityPrint(stateMachine.getContext(), uiContext, 0, isBackgroundJob, isLastJob);
                return new ReportErrorState(Result.RESULT_FAIL, Result.ErrorCode.UNKNOWN, (response != null? ((Error) response.obj).getMessage() : "Reserve priority print return exception."));
            }

            return new StartPrintState();
        } catch (Exception e) {
            SLog.e(TAG, "Error during print operation 1", e);
            JobInfo jobinfo = stateMachine.getJobBundle().getParcelable(ILetObserver.Keys.KEY_JOB_INFO);
            if(jobinfo != null) {
                ((PrintJobData) jobinfo.getJobData()).setJobState(new PrintJobState(PrintJobState.State.COMPLETED));
            }

            if (uiContext != null) {
                try {
                    IppClient.getInstance().exitPriorityPrint(stateMachine.getContext(), uiContext, 0, isBackgroundJob, isLastJob);
                } catch (Throwable throwable) {
                    SLog.e(TAG, "Error during print exit operation" + throwable.getMessage());
                }
            }

            return new ReportErrorState(Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, e.getMessage());
        }
    }

    private PrintTicket getPrintTicket(String docUri, PrintAttributesReader mAttrsReader, PrintRequestIntent.IntentParams reqParams, Context context) throws IOException, Error {
        PrintTicket.Builder ticketBuilder = new PrintTicket.Builder();

        PrinterAttributes defaultAttrs = null;
        try {
            defaultAttrs = (PrinterAttributes) IppClient.getInstance().getPrinterAttributes(context, true, 0).obj;
        } catch (Exception e) {
            SLog.w(TAG, "getPrintTicket: failed to get printer default attributes: " + e.getMessage());
        }

        if (mAttrsReader.getSource() == PrintAttributes.Source.STORAGE) {
            if(docUri.startsWith(PrintJobIntentService.DOWNLOAD_FILE_PATH)) {
                ticketBuilder.setDocumentUri(new File(docUri).toURI().toString());
            } else {
                ticketBuilder.setDocumentUri(docUri);
            }
        } else if (mAttrsReader.getSource() == PrintAttributes.Source.USB) {
            ticketBuilder.setDocumentUri(new File(docUri).toURI().toString());
        } else if (mAttrsReader.getSource() == PrintAttributes.Source.HTTP) {
            ticketBuilder.setDocumentUri(docUri);
            if (mAttrsReader.getNetworkCredentials() != null) {
                ticketBuilder.setUserName(mAttrsReader.getNetworkCredentials().getUsername());
                ticketBuilder.setPassword(mAttrsReader.getNetworkCredentials().getPassword());

                // replace empty string with null for cookie
                ticketBuilder.setCookie(
                        !TextUtils.isEmpty(mAttrsReader.getNetworkCredentials().getCookie()) ?
                                mAttrsReader.getNetworkCredentials().getCookie() : null);
            }
        } else if (mAttrsReader.getSource() == PrintAttributes.Source.STREAM) {
            InputStream inputStream = getInputStream(reqParams.getReqId());
            ticketBuilder.setPrintStream(inputStream);
        }

        ColorMode colorMode = PrintAttributesConverter.getColorMode(mAttrsReader);
        if (colorMode != null) {
            ticketBuilder.setColorMode(colorMode);
        }

        PlexMode plexMode = PrintAttributesConverter.getPlexMode(mAttrsReader);
        if (plexMode != null) {
            ticketBuilder.setPlexMode(plexMode);
        }

        StapleMode stapleMode = PrintAttributesConverter.getStapleMode(mAttrsReader);
        if (stapleMode != null) {
            ticketBuilder.setStapleMode(stapleMode);
        }

        List<Finishings> finishingsList = PrintAttributesConverter.getFinishingsList(mAttrsReader);
        if (finishingsList != null) {
            ticketBuilder.setFinishingsList(finishingsList);
        }

        ScalingMode scalingMode = PrintAttributesConverter.getScalingMode(mAttrsReader);
        if (scalingMode != null) {
            ticketBuilder.setScalingMode(scalingMode);
        }

        MediaSource mediaSource = PrintAttributesConverter.getMediaSource(mAttrsReader);
        if (mediaSource != null) {
            ticketBuilder.setMediaSource(mediaSource);
        }

        MediaSize mediaSize = PrintAttributesConverter.getMediaSize(mAttrsReader);
        if (mediaSize != null) {
            ticketBuilder.setMediaSize(mediaSize);
        } else if (defaultAttrs != null && defaultAttrs.defaultMediaSize != null) {
            ticketBuilder.setMediaSize(defaultAttrs.defaultMediaSize);
            SLog.d(TAG, "Set default media size: " + defaultAttrs.defaultMediaSize);
        } else {
            SLog.w(TAG, "Media size is not specified, and failed to get default media size.");
        }

        MediaType mediaType = PrintAttributesConverter.getMediaType(mAttrsReader);
        if (mediaType != null) {
            ticketBuilder.setMediaType(mediaType);
        }

        FileType fileType = PrintAttributesConverter.getFileType(mAttrsReader, docUri);
        if (fileType != null) {
            ticketBuilder.setFileType(fileType);
        }

        CollateMode collateMode = PrintAttributesConverter.getCollateMode(mAttrsReader);
        if (collateMode != null) {
            ticketBuilder.setCollateMode(collateMode);
        }

        ticketBuilder.setCopies(mAttrsReader.getCopies());
        ticketBuilder.setJobName(mAttrsReader.getJobName());

        Orientation orientation = PrintAttributesConverter.getOrientation(mAttrsReader);
        if (orientation != null) {
            ticketBuilder.setOrientation(orientation);
        }

        PrintQuality printQuality = PrintAttributesConverter.getPrintQuality(mAttrsReader);
        if (printQuality != null) {
            ticketBuilder.setPrintQuality(printQuality);
        }

        OutputBin outputBin = PrintAttributesConverter.getOutputBin(mAttrsReader);
        if (outputBin != null) {
            ticketBuilder.setOutputBin(outputBin);
        }

        ticketBuilder.setStartPageRanges(mAttrsReader.getStartPageRanges());
        ticketBuilder.setEndPageRanges(mAttrsReader.getEndPageRanges());

        return ticketBuilder.build();
    }



    private InputStream getInputStream(String rid) throws IOException {
        if(rid != null) {
            int socketaddr = 12345; //default
            try {
                socketaddr = hashCode(rid.substring(0, 2));
                if (socketaddr > 65000) socketaddr = socketaddr / 2;
                SLog.d(TAG, "(Socket)getInputStream for socket to " + socketaddr);

                Socket receiver = new Socket(LOCAL_HOST, socketaddr);
                if (receiver.isConnected()) {
                    SLog.d(TAG, "Success to connect with socket, " + socketaddr);
                    receiver.setSendBufferSize(Platform.BUFFER_SIZE);
                    receiver.setReceiveBufferSize(Platform.BUFFER_SIZE);
                    return new BufferedInputStream(receiver.getInputStream());
                }
            } catch (Throwable ignored) {}
            SLog.e(TAG, "getInputStream failed : rid=" + rid + ", port= " + socketaddr);
        }
        throw new IllegalStateException("Failed to receive stream data");
    }

    private static int hashCode(String string) {
        return string != null ? string.hashCode() * 31 : 0;
    }
}
