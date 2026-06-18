// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.printlet.provider;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.api.massstorage.MassStorageInfo;
import com.hp.jetadvantage.link.api.printer.PrintAttributes;
import com.hp.jetadvantage.link.api.printer.PrintAttributesCaps;
import com.hp.jetadvantage.link.api.printer.PrintAttributesCapsCreator;
import com.hp.jetadvantage.link.api.printer.Printlet;
import com.hp.jetadvantage.link.api.printer.StatusInfo;
import com.hp.jetadvantage.link.api.printer.TrayInfo;
import com.hp.jetadvantage.link.common.Platform;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.helper.SelectedPrinterHelper;
import com.hp.jetadvantage.link.common.model.PrinterInfo;
import com.hp.jetadvantage.link.common.utils.JsonParser;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.device.services.interfaces.IDevicePrintJobService;
import com.hp.jetadvantage.link.device.services.standard.StandardDevicePrintJobService;
import com.hp.jetadvantage.link.services.common.exception.SdkConnectionErrorException;
import com.hp.jetadvantage.link.services.common.exception.SdkException;
import com.hp.jetadvantage.link.services.common.exception.SdkInvalidParamException;
import com.hp.jetadvantage.link.services.common.exception.SdkNotSupportedException;
import com.hp.jetadvantage.link.services.common.exception.SdkServiceErrorException;
import com.hp.jetadvantage.link.services.common.providers.SpsPermissionHelper;
import com.hp.jetadvantage.link.services.common.util.ErrorCodeResolver;
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
import com.hp.jetadvantage.link.services.printlet.model.PrintQuality;
import com.hp.jetadvantage.link.services.printlet.model.PrinterAttributes;
import com.hp.jetadvantage.link.services.printlet.model.PrinterState;
import com.hp.jetadvantage.link.services.printlet.model.ScalingMode;
import com.hp.jetadvantage.link.services.printlet.model.StapleMode;
import com.hp.jetadvantage.link.services.printlet.service.OXPCreatePrintSpoolerIntentService;
import com.hp.jetadvantage.link.services.storagelet.IStorage;
import com.hp.jetadvantage.link.services.storagelet.StorageFactory;

import java.util.ArrayList;
import java.util.List;

public class OXPPrintletContentProvider extends ContentProvider {
    private static final String TAG = Printlet.TAG + "/OXPPrintletCP";

    private static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.sec.Printlet";

    private static final int Printlet_CODE = 1;

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    private static final String REASON_POSTFIX_WARNING = "-warning";
    private static final String REASON_POSTFIX_REPORT = "-report";
    private static final String REASON_POSTFIX_ERROR = "-error";

    static {
        URI_MATCHER.addURI(Printlet.AUTHORITY_OXP, Printlet.DIR_PATH_SEGMENT, Printlet_CODE);
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] strings, String s, String[] strings1, String s1) {
        return null;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case Printlet_CODE:
                SLog.d(Printlet.TAG, " in Printlet_CODE");
                return CONTENT_TYPE;
            default:
                throw new IllegalArgumentException("Unknown or Invalid URI " + uri);
        }
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }

    @Nullable
    @Override
    public Bundle call(@NonNull String method, String arg, Bundle extras) {
        final Bundle bundle = new Bundle();
        Result.pack(bundle, Result.RESULT_OK);

        StrictMode.ThreadPolicy originalPolicy = StrictMode.getThreadPolicy();
        try {
            // if extras is null - it means it's old API 1
            int clientVersion = extras != null && extras.containsKey(Printlet.Keys.KEY_CLIENT_VERSION) ?
                    extras.getInt(Printlet.Keys.KEY_CLIENT_VERSION, Sdk.VERSION_LEVEL.ONE) : Sdk.VERSION_LEVEL.ONE;

            if (getContext() == null) {
                throw new SdkInvalidParamException("Context is null");
            }

            SpsPermissionHelper.ensurePermission(getContext());
            final PrinterInfo pi = SelectedPrinterHelper.get(getContext().getContentResolver());
            if (PrinterInfo.isEmpty(pi)) {
                throw new SdkConnectionErrorException("Device is not connected");
            }

            //[[mobile, priorityPrint is supported only.
            if(Platform.isMobile() && clientVersion < Sdk.VERSION_LEVEL.THREE) {
                throw new SdkNotSupportedException("Mobile supports priorityPrint only. Please update the client library.");
            }
            //]]

            final StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
            StrictMode.setThreadPolicy(policy);

            boolean serviceSupported = isSupported(pi);

            if (Printlet.Method.IS_SUPPORTED.equals(method) || Printlet.Method.IS_STATUS_SUPPORTED.equals(method)) {
                Log.d(TAG, "call: IS_SUPPORTED returns " + serviceSupported);
                bundle.putBoolean(Printlet.IS_SUPPORTED_EXTRA, serviceSupported);
            } else if (Printlet.Method.GET_JOB_COUNT.equals(method)) {
                bundle.putInt(Printlet.GET_JOB_COUNT_EXTRA, OXPCreatePrintSpoolerIntentService.getJobCount());
            } else if (Printlet.Method.GET_AVAILABLE_JOB_COUNT.equals(method)) {
                bundle.putInt(Printlet.GET_AVAILABLE_JOB_COUNT_EXTRA, OXPCreatePrintSpoolerIntentService.getAvailableJobCount());
            } else {
                if (!serviceSupported) {
                    throw new SdkNotSupportedException("PrinterService is not supported");
                }

                String resultStr;
                switch (method) {
                    case Printlet.Method.GET_CAPS:
                        resultStr = getCaps(pi, clientVersion);
                        Log.d(TAG, "call: GET_CAPS returns " + resultStr);
                        break;
                    case Printlet.Method.GET_DEFAULTS:
                        resultStr = getDefaults(pi, clientVersion);
                        Log.d(TAG, "call: GET_DEFAULTS returns " + resultStr);
                        break;
                    case Printlet.Method.GET_STATUS:
                        resultStr = getStatus(pi);
                        Log.d(TAG, "call: GET_STATUS returns " + resultStr);
                        break;
                    case Printlet.Method.GET_TRAY_INFO:
                        resultStr = getTrayInfo(pi, clientVersion);
                        break;
                    default:
                        throw new SdkInvalidParamException("Method " + method + " is not supported");
                }

                bundle.putString(Result.KEY_RESULT, resultStr);
            }
        } catch (SdkException e) {
            Log.e("[SIM]",e.getMessage(),e);
            Result.pack(bundle, e.getResult());
        } catch (Exception e){
            Log.e("[SIM]",e.getMessage(),e);
            Result.pack(bundle, Result.RESULT_FAIL, ErrorCodeResolver.resolve(e, Result.ErrorCode.SERVICE_ERROR), e.getMessage());
        } finally {
            if(originalPolicy != null)
                StrictMode.setThreadPolicy(originalPolicy);
        }
        return bundle;
    }

    /**
     * Helper for getting devices caps
     *
     * @param pi     {@link PrinterInfo} connected
     * @return String with caps string
     */
    private String getCaps(final PrinterInfo pi, final int clientVersion) throws Exception {
        Message resultMessage = IppClient.getInstance().getPrinterAttributes(getContext(), false, 0);
        if (resultMessage.arg1 == IppConnector.Constants.REQUEST_RETURN_CODE__OK) {
            if (resultMessage.obj instanceof PrinterAttributes) {
                PrinterAttributes printerAttributes = (PrinterAttributes) resultMessage.obj;
                PrintAttributesCapsCreator.Builder builder = createPrintAttributeBuilder(printerAttributes, clientVersion);
                PrintAttributesCaps printAttributesCaps = new PrintAttributesCaps(builder.build());
                return JsonParser.getInstance().toJson(printAttributesCaps);
            }
        } else {
            if (resultMessage.obj instanceof Error) {
                throw new SdkServiceErrorException(((Error) resultMessage.obj).getMessage());
            }
        }

        throw new SdkServiceErrorException("Failed to retrieve print caps from the device");
    }

    private String getDefaults(final PrinterInfo pi, final int clientVersion) throws Exception {
        PrinterAttributes printerAttributes = (PrinterAttributes) IppClient.getInstance().getPrinterAttributes(getContext(), true, 0).obj;

        if (printerAttributes != null) {
            PrintAttributesCapsCreator.Builder builder = createPrintAttributeBuilder(printerAttributes, clientVersion);
            PrintAttributesCaps caps = new PrintAttributesCaps(builder.build());

            String downloadPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();

            PrintAttributes.PaperSize paperSize = getPaperSize(clientVersion, printerAttributes.defaultMediaSize);

            PrintAttributes.PaperType paperType = getPaperType(clientVersion, printerAttributes.defaultMediaType);

            PrintAttributes.PaperSource paperSource = getPaperSource(printerAttributes.defaultMediaSource);
            if (paperSource == null) {
                paperSource = PrintAttributes.PaperSource.DEFAULT;
            }

            PrintAttributes.AutoFit autoFit = getAutoFit(printerAttributes.defaultScalingMode);
            if (autoFit == null) {
                autoFit = PrintAttributes.AutoFit.DEFAULT;
            }

            PrintAttributes.ColorMode colorMode = getColorMode(printerAttributes.defaultColorMode);
            if (colorMode == null) {
                colorMode = PrintAttributes.ColorMode.DEFAULT;
            }

            PrintAttributes.DocumentFormat documentFormat = getDocumentFormat(printerAttributes.defaultFileType);
            if (documentFormat == null) {
                documentFormat = PrintAttributes.DocumentFormat.AUTO;
            }

            if (getColorMode(printerAttributes.defaultColorMode) != null) {
                colorMode = getColorMode(printerAttributes.defaultColorMode);
            }

            PrintAttributes.StapleMode stapleMode = getStapleMode(printerAttributes.defaultStapleMode);
            if (stapleMode == null) {
                if (printerAttributes.supportedStapleModes != null
                        && (printerAttributes.supportedStapleModes.contains(StapleMode.None)
                        || printerAttributes.supportedStapleModes.size() > 0)) {
                    stapleMode = PrintAttributes.StapleMode.NONE;
                } else {
                    stapleMode = PrintAttributes.StapleMode.DEFAULT;
                }
            }

            PrintAttributes.Duplex duplex = getDuplex(printerAttributes.defaultPlexMode);
            if (duplex == null) {
                duplex = PrintAttributes.Duplex.NONE;
            }

            PrintAttributes.CollateMode collateMode = getCollateMode(printerAttributes.defaultCollateMode);
            if (collateMode == null) {
                collateMode = PrintAttributes.CollateMode.DEFAULT;
            }

            int copies = printerAttributes.defaultCopies;


            PrintAttributes.Orientation orientation = getOrientation(printerAttributes.defaultOrientation);
            if(orientation == null) {
                orientation = PrintAttributes.Orientation.DEFAULT;
            }

            PrintAttributes.PrintQuality printQuality = getPrintQuality(printerAttributes.defaultPrintQuality);
            if(printQuality == null) {
                printQuality = PrintAttributes.PrintQuality.DEFAULT;
            }

            PrintAttributes.OutputBin outputBin = getOutputBin(printerAttributes.defaultOutputBin);
            if(outputBin == null) {
                outputBin = PrintAttributes.OutputBin.DEFAULT;
            }
//            Log.i("[SIM]","OutputBin : "+outputBin.toString());
//            Log.i("[SIM]","printerAttributes.defaultFinishings : "+printerAttributes.defaultFinishings);
            List<PrintAttributes.Finishings> finishingList = new ArrayList<>();
            if(printerAttributes.defaultFinishings != null){
                for(Finishings finishing: printerAttributes.defaultFinishings) {
                    finishingList.add(getFinishings(finishing));
                }
            }

//            Log.i("[SIM]","Finishings : "+finishingList.toString());
            if (finishingList.size() == 0) {
                if (printerAttributes.supportedFinishingsOptions != null
                        && (printerAttributes.supportedFinishingsOptions.contains(Finishings.None)
                        || printerAttributes.supportedFinishingsOptions.size() > 0)) {
                    finishingList.add(PrintAttributes.Finishings.NONE);
                } else {
                    finishingList.add(PrintAttributes.Finishings.DEFAULT);
                }
            }
//            Log.i("[SIM]","Finishings : "+finishingList.toString());

            PrintAttributes attributes = new PrintAttributes.PrintFromStorageBuilder(Uri.parse(downloadPath))
                    .setCollateMode(collateMode)
                    .setAutoFit(autoFit)
                    .setColorMode(colorMode)
                    .setDocumentFormat(documentFormat)
                    .setPaperSize(paperSize)
                    .setPaperType(paperType)
                    .setDuplex(duplex)
                    .setPaperSource(paperSource)
                    .setStapleMode(stapleMode)
                    .setCopies(copies)
                    .setOrientation(orientation)
                    .setPrintQuality(printQuality)
                    .setOutputBin(outputBin)
                    .setFinishingsList(finishingList)
                    .build(caps);

//            Log.i("[SIM]","Get default : "+attributes.toString());
            return JsonParser.getInstance().toJson(attributes);
        }
        throw new SdkServiceErrorException("Failed to retrieve print attributes from the device");
    }

    private String getStatus(final PrinterInfo pi) throws Exception {
        PrinterAttributes printerAttributes = (PrinterAttributes) IppClient.getInstance().getPrinterAttributes(getContext(), false, 0).obj;

        if (printerAttributes != null && printerAttributes.printerState != null && printerAttributes.printerStateReasons != null) {
            SLog.d(TAG, "printerState=" + printerAttributes.printerState + ", reasons=" + printerAttributes.printerStateReasons);
            StatusInfo.Status status = getPrinterStatus(printerAttributes.printerState);
            List<StatusInfo.StatusReason> statusConditions =
                    getPrinterStatusReasons(printerAttributes.printerStateReasons);

            StatusInfo printerStatus = new StatusInfo(status, statusConditions);
            return JsonParser.getInstance().toJson(printerStatus);
        }

        throw new SdkServiceErrorException("Failed to retrieve print caps from the device");
    }

    private StatusInfo.Status getPrinterStatus(PrinterState printerState) {
        if (printerState != null) {
            switch (printerState) {
                case Idle:
                    return StatusInfo.Status.IDLE;
                case Processing:
                    return StatusInfo.Status.PROCESSING;
                case Stopped:
                    return StatusInfo.Status.STOPPED;
            }
        }
        return null;
    }

    private List<StatusInfo.StatusReason> getPrinterStatusReasons(List<String> printerStateReasons) {
        if (printerStateReasons != null) {
            List<StatusInfo.StatusReason> statusReasons = new ArrayList<>();
            for (String printerStateReason : printerStateReasons) {
                String reason = printerStateReason;
                if (reason.endsWith(REASON_POSTFIX_WARNING)) {
                    reason = reason.substring(0, reason.length() - REASON_POSTFIX_WARNING.length());
                } else if (reason.endsWith(REASON_POSTFIX_REPORT)) {
                    reason = reason.substring(0, reason.length() - REASON_POSTFIX_REPORT.length());
                } else if (reason.endsWith(REASON_POSTFIX_ERROR)) {
                    reason = reason.substring(0, reason.length() - REASON_POSTFIX_ERROR.length());
                }

                switch (reason) {
                    case "cover-open":
                        statusReasons.add(StatusInfo.StatusReason.COVER_OPEN);
                        break;
                    case "door-open":
                        statusReasons.add(StatusInfo.StatusReason.DOOR_OPEN);
                        break;
                    case "media-empty":
                        statusReasons.add(StatusInfo.StatusReason.MEDIA_EMPTY);
                        break;
                    case "media-jam":
                        statusReasons.add(StatusInfo.StatusReason.MEDIA_JAM);
                        break;
                    case "media-low":
                        statusReasons.add(StatusInfo.StatusReason.MEDIA_LOW);
                        break;
                    case "media-needed":
                        statusReasons.add(StatusInfo.StatusReason.MEDIA_NEEDED);
                        break;
                    case "none":
                        statusReasons.add(StatusInfo.StatusReason.NONE);
                        break;
                    case "other":
                        statusReasons.add(StatusInfo.StatusReason.OTHER);
                        break;
                    default:
                        if (!statusReasons.contains(StatusInfo.StatusReason.UNKNOWN)) {
                            statusReasons.add(StatusInfo.StatusReason.UNKNOWN);
                        }
                        break;
                }
            }

            return statusReasons;
        }
        return null;
    }

    private String getTrayInfo(final PrinterInfo pi, final int clientVersion) throws Exception {
        PrinterAttributes printerAttributes = (PrinterAttributes) IppClient.getInstance().getPrinterAttributes(getContext(), true, 0).obj;

        if (printerAttributes != null && printerAttributes.availableMediaSizes != null
                && printerAttributes.availableMediaTypes != null
                && printerAttributes.availableMediaSourcesData != null) {
            SLog.d(TAG, "mediaSizes=" + printerAttributes.availableMediaSizes + ", mediaSourceData=" + printerAttributes.availableMediaSourcesData);

            List<TrayInfo> trayInfoList = new ArrayList<>();
            for (int i = 0; i < printerAttributes.supportedMediaSources.size(); i++) {
                MediaSource mediaSource = printerAttributes.supportedMediaSources.get(i);

                String data = i < printerAttributes.availableMediaSourcesData.size() ? printerAttributes.availableMediaSourcesData.get(i) : null;
                if (data != null && data.contains(";")) {
                    int level = 0;
                    int capacity = 0;
                    TrayInfo.Status status = TrayInfo.Status.UNAVAILABLE;

                    String[] dataParts = data.split(";");
                    for (String dataPart : dataParts) {
                        if (dataPart.contains("=")) {
                            String[] pair = dataPart.split("=");
                            if (pair.length == 2) {
                                switch (pair[0]) {
                                    case "maxcapacity":
                                        capacity = Integer.parseInt(pair[1]);
                                        break;
                                    case "level":
                                        // Level in percent
                                        // -1 means no value and no restriction
                                        // -2 means unknown
                                        // -3 means at least one unit remains
                                        level = Integer.parseInt(pair[1]);
                                        if (level == -3)
                                            level = 1; // just indicate that there is some paper
                                        else if (level < 0)
                                            level = 0;
                                        break;
                                    case "status":
                                        int statusValue = Integer.parseInt(pair[1]);
                                        status = (statusValue & 1) == 0 ? TrayInfo.Status.AVAILABLE : TrayInfo.Status.UNAVAILABLE;
                                }
                            }
                        }
                    }

                    MediaSize mediaSize = printerAttributes.availableMediaSizes.get(mediaSource);
                    MediaType mediaType = printerAttributes.availableMediaTypes.get(mediaSource);

                    TrayInfo trayInfo = new TrayInfo(getPaperSource(mediaSource), getPaperSize(clientVersion, mediaSize), getPaperType(clientVersion, mediaType),
                            status, level, capacity);

                    trayInfoList.add(trayInfo);
                }
            }

            return JsonParser.getInstance().toJson(trayInfoList);
        }

        throw new SdkServiceErrorException("Failed to retrieve print caps from the device");
    }

    private boolean isSupported(final PrinterInfo pi) {

        IDevicePrintJobService devicePrintJobService = new StandardDevicePrintJobService();
        boolean isSupported = devicePrintJobService.isSupported();
        SLog.d(TAG, "Device supports PRINT feature: " + isSupported);
        return isSupported;
    }

    private PrintAttributesCapsCreator.Builder createPrintAttributeBuilder(PrinterAttributes printerAttributes,
                                                                           int clientVersion) {
        final PrintAttributesCapsCreator.Builder builder = new PrintAttributesCapsCreator.Builder();

        builder.setMaxCopies(printerAttributes.supportedCopies);

        builder.addSource(PrintAttributes.Source.STORAGE);
        builder.addSource(PrintAttributes.Source.HTTP);

        if (clientVersion >= Sdk.VERSION_LEVEL.TWO) {
            List<IStorage> storageList = StorageFactory.INSTANCE.getStorageList(MassStorageInfo.StorageType.USB);
            if (!storageList.isEmpty() && storageList.get(0).getInfo().isMounted()) {
                builder.addSource(PrintAttributes.Source.USB);
            }

            builder.addSource(PrintAttributes.Source.STREAM);
        }

        for (ColorMode fileTypesByColorMode : printerAttributes.supportedColorModes) {
            PrintAttributes.ColorMode colorMode = getColorMode(fileTypesByColorMode);
            if (colorMode != null) {
                builder.addColorMode(colorMode);
            }
        }

        for (PlexMode plexMode : printerAttributes.supportedPlexModes) {
            PrintAttributes.Duplex duplex = getDuplex(plexMode);
            if (duplex != null) {
                builder.addPlex(duplex);
            }
        }

        ArrayList<PrintAttributes.AutoFit> autoFits = new ArrayList<>();
        for (ScalingMode mode : printerAttributes.supportedScalingModes) {
            PrintAttributes.AutoFit autoFit = getAutoFit(mode);
            if (autoFit != null && !autoFits.contains(autoFit)) {
                builder.addAutoFit(autoFit);
                autoFits.add(autoFit);
            }
        }

        if (printerAttributes.supportedStapleModes.size() > 0) {
            builder.addStapleMode(PrintAttributes.StapleMode.NONE); // temporary
        }

        for (StapleMode stapleMode : printerAttributes.supportedStapleModes) {
            PrintAttributes.StapleMode staple = getStapleMode(stapleMode);
            if (staple != null) {
                builder.addStapleMode(staple);
            }
        }

        for (MediaSource mediaSource : printerAttributes.supportedMediaSources) {
            PrintAttributes.PaperSource paperSource = getPaperSource(mediaSource);
            if (paperSource != null) {
                builder.addPaperSource(paperSource);
            }
        }

        for (MediaSize mediaSize : printerAttributes.supportedMediaSizes) {
            PrintAttributes.PaperSize paperSize = getPaperSize(clientVersion, mediaSize);
            if (!paperSize.equals(PrintAttributes.PaperSize.DEFAULT)) {
                builder.addPaperSize(paperSize);
            }
        }

        for (MediaType mediaType : printerAttributes.supportedMediaTypes) {
            PrintAttributes.PaperType paperType = getPaperType(clientVersion, mediaType);
            if (!paperType.equals(PrintAttributes.PaperType.DEFAULT)) {
                builder.addPaperType(paperType);
            }
        }

        for (FileType fileType : printerAttributes.supportedFileTypes) {
            PrintAttributes.DocumentFormat format = getDocumentFormat(fileType);
            if (format != null) {
                builder.addDocumentFormat(format);
            }
        }

        for (CollateMode collateMode : printerAttributes.supportedCollateModes) {
            PrintAttributes.CollateMode collate = getCollateMode(collateMode);
            if (collate != null) {
                builder.addCollateMode(collate);
            }
        }

        for (Orientation orientation : printerAttributes.supportedOrientations) {
            PrintAttributes.Orientation originalOrientation = getOrientation(orientation);
            if (originalOrientation != null) {
                builder.addOrientation(originalOrientation);
            }
        }

        for (PrintQuality printQuality : printerAttributes.supportedPrintQualities) {
            PrintAttributes.PrintQuality quality = getPrintQuality(printQuality);
            if (quality != null) {
                builder.addPrintQuality(quality);
            }
        }

        for (OutputBin outputBin : printerAttributes.supportedOutputBins) {
            PrintAttributes.OutputBin bin = getOutputBin(outputBin);
            if (bin != null) {
                builder.addOutputBin(bin);
            }
        }

        for (Finishings finishings : printerAttributes.supportedFinishingsOptions) {
            PrintAttributes.Finishings options = getFinishings(finishings);
            if (options != null) {
                builder.addFinishings(options);
            }
        }

        return builder;
    }

    private PrintAttributes.AutoFit getAutoFit(ScalingMode mode) {
        if(mode != null) {
            switch (mode) {
                case Auto:
                case AutoFit:
                case Fill:
                case Fit:
                    return PrintAttributes.AutoFit.TRUE;
                case None:
                    return PrintAttributes.AutoFit.FALSE;
            }
        }
        return null;
    }

    private PrintAttributes.ColorMode getColorMode(ColorMode fileTypesByColorMode) {
        if (fileTypesByColorMode != null) {
            switch (fileTypesByColorMode) {
                case Auto:
                    return PrintAttributes.ColorMode.AUTO;
                case Monochrome:
                    return PrintAttributes.ColorMode.MONO;
                case Color:
                    return PrintAttributes.ColorMode.COLOR;
            }
        }
        return null;
    }

    private PrintAttributes.DocumentFormat getDocumentFormat(FileType fileType) {
        if (fileType != null) {
            switch (fileType) {
                case Jpeg:
                    return PrintAttributes.DocumentFormat.JPEG;
                case PDF:
                    return PrintAttributes.DocumentFormat.PDF;
                case Tiff:
                    return PrintAttributes.DocumentFormat.TIFF;
                case PCL5:
                    return PrintAttributes.DocumentFormat.PCL5;
                case PCL6:
                    return PrintAttributes.DocumentFormat.PCL6;
                case PS:
                    return PrintAttributes.DocumentFormat.PS;
                case Text:
                    return PrintAttributes.DocumentFormat.TEXT;
            }
        }
        return null;
    }

    private PrintAttributes.PaperSize getPaperSize(int clientVersion, MediaSize mediaSize) {
        if (mediaSize != null) {
            switch (mediaSize) {
                case Default:
                    return PrintAttributes.PaperSize.DEFAULT;
                case A3:
                    return PrintAttributes.PaperSize.A3;
                case A4:
                    return PrintAttributes.PaperSize.A4;
                case A5:
                    return PrintAttributes.PaperSize.A5;
                case A6:
                    return PrintAttributes.PaperSize.A6;
                case B4:
                    return PrintAttributes.PaperSize.B4;
                case B5:
                    return PrintAttributes.PaperSize.B5;
                case B6:
                    return PrintAttributes.PaperSize.B6;
                case JB4:
                    return PrintAttributes.PaperSize.JB4;
                case JB5:
                    return PrintAttributes.PaperSize.JB5;
                case JB6:
                    return PrintAttributes.PaperSize.JB6;
                case Ledger:
                    return PrintAttributes.PaperSize.LEDGER;
                case Legal:
                    return PrintAttributes.PaperSize.LEGAL;
                case Letter:
                    return PrintAttributes.PaperSize.LETTER;
                case PK8:
                    return PrintAttributes.PaperSize.PK8;
                case PK16:
                    return PrintAttributes.PaperSize.PK16;
                case ArchitecturalB:
                    return PrintAttributes.PaperSize.INCH12X18;
                case Exec:
                    return PrintAttributes.PaperSize.EXECUTIVE;
                case Statement:
                    return PrintAttributes.PaperSize.STATEMENT;
                case Foolscap:
                    return PrintAttributes.PaperSize.INCH8POINT5X13;
            }

            if(clientVersion >= Sdk.VERSION_LEVEL.SIX){
                switch (mediaSize) {
                    case Envelope9:
                        return PrintAttributes.PaperSize.ENVELOPE_9;
                    case EnvelopeComm10:
                        return PrintAttributes.PaperSize.ENVELOPE_COMM10;
                    case EnvelopeMonarch:
                        return PrintAttributes.PaperSize.ENVELOPE_MONARCH;
                    case EnvelopeC5:
                        return PrintAttributes.PaperSize.C5;
                    case EnvelopeC6:
                        return PrintAttributes.PaperSize.C6;
                    case EnvelopeDL:
                        return PrintAttributes.PaperSize.ENVELOPE_DL;
                    case EnvelopeJChou3:
                        return PrintAttributes.PaperSize.JCHOU3;
                    case EnvelopeJChou4:
                        return PrintAttributes.PaperSize.JCHOU4;
                    case JDoublePostcard:
                        return PrintAttributes.PaperSize.JDOUBLE_POSTCARD;
                    case Jpostcard:
                        return PrintAttributes.PaperSize.JPOSTCARD;
                    case K8:
                        return PrintAttributes.PaperSize.K8;
                    case K8_260x368mm:
                        return PrintAttributes.PaperSize.K8_260x368mm;
                    case K16:
                        return PrintAttributes.PaperSize.K16;
                    case K16_184x260mm:
                        return PrintAttributes.PaperSize.K16_184x260mm;
                    case Oficio_216x340mm:
                        return PrintAttributes.PaperSize.OFICIO;
                    case RA3:
                        return PrintAttributes.PaperSize.RA3;
                    case RA4:
                        return PrintAttributes.PaperSize.RA4;
                    case Size10x15cm:
                        return PrintAttributes.PaperSize.GENERAL_10X15cm;
                    case Size3x5in:
                        return PrintAttributes.PaperSize.GENERAL_3X5in;
                    case Size4x6in:
                        return PrintAttributes.PaperSize.GENERAL_4X6in;
                    case Size5x7in:
                        return PrintAttributes.PaperSize.GENERAL_5X7in;
                    case Size5x8in:
                        return PrintAttributes.PaperSize.GENERAL_5X8in;
                    case Sra3:
                        return PrintAttributes.PaperSize.SRA3;
                    case Sra4:
                        return PrintAttributes.PaperSize.SRA4;
                }
            }
        }
        return PrintAttributes.PaperSize.DEFAULT;
    }

    private PrintAttributes.PaperType getPaperType(int clientVersion, MediaType mediaType) {
        if (mediaType != null) {
            switch (mediaType) {
                case Bond:
                    return PrintAttributes.PaperType.BOND;
                case Cardstock_176to220g:
                    return PrintAttributes.PaperType.CARD_STOCK;
                case CardstockGloss_176to220g:
                    return PrintAttributes.PaperType.CARD_STOCK_GLOSSY;
                case Color:
                    return PrintAttributes.PaperType.COLORED;
                case Envelope:
                    return PrintAttributes.PaperType.ENVELOPE;
                case ExtraHeavy_131to175g:
                    return PrintAttributes.PaperType.EXTRA_HEAVY;
                case ExtraHeavyGloss_131to175g:
                    return PrintAttributes.PaperType.EXTRA_HEAVY_GLOSSY;
                case Heavy_111to130g:
                    return PrintAttributes.PaperType.HEAVY;
                case HeavyEnvelope:
                    return PrintAttributes.PaperType.HEAVY_ENVELOPE;
                case HeavyRough:
                    return PrintAttributes.PaperType.HEAVY_ROUGH;
                case HPAdvancedPhoto:
                    return PrintAttributes.PaperType.HP_ADVANCED_PHOTO;
                case HPBrochureGloss_180g:
                    return PrintAttributes.PaperType.HP_BROCHURE_GLOSSY;
                case HPBrochureMatte_180g:
                    return PrintAttributes.PaperType.HP_BROCHURE_MATTE_180G;
                case HPEcoFFICIENT:
                    return PrintAttributes.PaperType.HP_ECOFFICIENT;
                case HPGloss_130g:
                    return PrintAttributes.PaperType.HP_GLOSSY_120G;
                case HPGloss_160g:
                    return PrintAttributes.PaperType.HP_GLOSSY_150G;
                case HPGloss_220g:
                    return PrintAttributes.PaperType.HP_GLOSSY_200G;
                case HPMatte_90g:
                    return PrintAttributes.PaperType.HP_MATTE_90G;
                case HPMatte_105g:
                    return PrintAttributes.PaperType.HP_MATTE_105G;
                case HPMatte_120g:
                    return PrintAttributes.PaperType.HP_MATTE_120G;
                case HPMatte_160g:
                    return PrintAttributes.PaperType.HP_MATTE_150G;
                case HPMatte_200g:
                    return PrintAttributes.PaperType.HP_MATTE_200G;
                case HPPremiumMatte_120g:
                    return PrintAttributes.PaperType.HP_INKJET_MATTE_120G;
                case HPSoftGloss_120g:
                    return PrintAttributes.PaperType.HP_SOFT_GLOSS_120G;
                case Intermediate_85to95g:
                    return PrintAttributes.PaperType.INTERMEDIATE;
                case Labels:
                    return PrintAttributes.PaperType.LABELS;
                case Letterhead:
                    return PrintAttributes.PaperType.LETTERHEAD;
                case Light_60to74g:
                    return PrintAttributes.PaperType.LIGHT;
                case Midweight_96to110g:
                    return PrintAttributes.PaperType.MID_WEIGHT;
                case Plain:
                    return PrintAttributes.PaperType.PLAIN;
                case Preprinted:
                    return PrintAttributes.PaperType.PREPRINTED;
                case Prepunched:
                    return PrintAttributes.PaperType.PREPUNCHED;
                case Recycled:
                    return PrintAttributes.PaperType.RECYCLED;
                case Rough:
                    return PrintAttributes.PaperType.ROUGH;
                case Transparency:
                    return PrintAttributes.PaperType.TRANSPARENCY;
            }
            if(clientVersion >= Sdk.VERSION_LEVEL.SIX){
                switch (mediaType) {
                    case HeavyGloss_111to130g:
                        return PrintAttributes.PaperType.HEAVY_GLOSSY;
                    case HeavyPaperboard:
                        return PrintAttributes.PaperType.HEAVY_PAPERBOARD;
                    case LightBond:
                        return PrintAttributes.PaperType.LIGHT_BOND;
                    case LightRough:
                        return PrintAttributes.PaperType.LIGHT_ROUGH;
                    case LightPaperboard:
                        return PrintAttributes.PaperType.LIGHT_PAPERBOARD;
                    case MidweightGloss_96to110g:
                        return PrintAttributes.PaperType.MID_WEIGHT_GLOSSY;
                    case OpaqueFilm:
                        return PrintAttributes.PaperType.OPAQUE_FILM;
                    case Paperboard:
                        return PrintAttributes.PaperType.PAPERBOARD;
                    case Tab:
                        return PrintAttributes.PaperType.TAB;
                    case Vellum:
                        return PrintAttributes.PaperType.VELLUM;
                }
            }
        }
        return PrintAttributes.PaperType.DEFAULT;
    }

    private PrintAttributes.Duplex getDuplex(PlexMode plexMode) {
        if (plexMode != null) {
            switch (plexMode) {
                case Simplex:
                    return PrintAttributes.Duplex.NONE;
                case DuplexLongEdge:
                    return PrintAttributes.Duplex.BOOK;
                case DuplexShortEdge:
                    return PrintAttributes.Duplex.FLIP;
            }
        }
        return null;
    }

    private PrintAttributes.CollateMode getCollateMode(CollateMode collateMode) {
        if (collateMode != null) {
            switch (collateMode) {
                case Collated:
                    return PrintAttributes.CollateMode.COLLATED;
                case Uncollated:
                    return PrintAttributes.CollateMode.UNCOLLATED;
            }
        }
        return null;
    }

    private PrintAttributes.StapleMode getStapleMode(StapleMode stapleMode) {
        if (stapleMode != null) {
            switch (stapleMode) {
                case None:
                    return PrintAttributes.StapleMode.NONE;
                case Staple:
                    return PrintAttributes.StapleMode.STAPLE;
                case StapleTopLeft:
                    return PrintAttributes.StapleMode.TOP_LEFT;
                case StapleBottomLeft:
                    return PrintAttributes.StapleMode.BOTTOM_LEFT;
                case StapleTopRight:
                    return PrintAttributes.StapleMode.TOP_RIGHT;
                case StapleBottomRight:
                    return PrintAttributes.StapleMode.BOTTOM_RIGHT;
                case DualLeft:
                    return PrintAttributes.StapleMode.DUAL_LEFT;
                case DualRight:
                    return PrintAttributes.StapleMode.DUAL_RIGHT;
                case DualTop:
                    return PrintAttributes.StapleMode.DUAL_TOP;
                case Punch:
                    return PrintAttributes.StapleMode.PUNCH;
                case Cover:
                    return PrintAttributes.StapleMode.COVER;
                case Bind:
                    return PrintAttributes.StapleMode.BIND;
                case SaddleStitch:
                    return PrintAttributes.StapleMode.SADDLE_STITCH;
                case EdgeStitch:
                    return PrintAttributes.StapleMode.EDGE_STITCH;
                case EdgeStitchLeft:
                    return PrintAttributes.StapleMode.EDGE_STITCH_LEFT;
                case EdgeStitchRight:
                    return PrintAttributes.StapleMode.EDGE_STITCH_RIGHT;
                case EdgeStitchTop:
                    return PrintAttributes.StapleMode.EDGE_STITCH_TOP;
                case EdgeStitchBottom:
                    return PrintAttributes.StapleMode.EDGE_STITCH_BOTTOM;
            }
        }
        return null;
    }

    private PrintAttributes.PaperSource getPaperSource(MediaSource mediaSource) {
        if (mediaSource != null) {
            switch (mediaSource) {
                case Auto:
                    return PrintAttributes.PaperSource.AUTO;
                case Tray1:
                    return PrintAttributes.PaperSource.TRAY_1;
                case Tray2:
                    return PrintAttributes.PaperSource.TRAY_2;
                case Tray3:
                    return PrintAttributes.PaperSource.TRAY_3;
                case Tray4:
                    return PrintAttributes.PaperSource.TRAY_4;
                case Tray5:
                    return PrintAttributes.PaperSource.TRAY_5;
                case Tray6:
                    return PrintAttributes.PaperSource.TRAY_6;
                case Tray7:
                    return PrintAttributes.PaperSource.TRAY_7;
                case Tray8:
                    return PrintAttributes.PaperSource.TRAY_8;
                case Tray9:
                    return PrintAttributes.PaperSource.TRAY_9;
                case Tray10:
                    return PrintAttributes.PaperSource.TRAY_10;
                case Manual:
                    return PrintAttributes.PaperSource.MANUAL_FEED;
            }
        }
        return null;
    }

    private PrintAttributes.Orientation getOrientation(Orientation orientation) {
        if (orientation != null) {
            switch (orientation) {
                case Portrait:
                    return PrintAttributes.Orientation.PORTRAIT;
                case Landscape:
                    return PrintAttributes.Orientation.LANDSCAPE;
                case ReversePortrait:
                    return PrintAttributes.Orientation.REVERSE_PORTRAIT;
                case ReverseLandscape:
                    return PrintAttributes.Orientation.REVERSE_LANDSCAPE;
                case None:
                    return PrintAttributes.Orientation.NONE;
            }
        }
        return null;
    }

    private PrintAttributes.PrintQuality getPrintQuality(PrintQuality printQuality) {
        if (printQuality != null) {
            switch (printQuality) {
                case Draft:
                    return PrintAttributes.PrintQuality.DRAFT;
                case Normal:
                    return PrintAttributes.PrintQuality.NORMAL;
                case High:
                    return PrintAttributes.PrintQuality.HIGH;
            }
        }
        return null;
    }

    private PrintAttributes.OutputBin getOutputBin(OutputBin outputBin) {
        if (outputBin != null) {
            switch (outputBin) {
                case Auto:
                    return PrintAttributes.OutputBin.AUTO;
                case Bottom:
                    return PrintAttributes.OutputBin.BOTTOM;
                case Center:
                    return PrintAttributes.OutputBin.CENTER;
                case FaceDown:
                    return PrintAttributes.OutputBin.FACE_DOWN;
                case FaceUp:
                    return PrintAttributes.OutputBin.FACE_UP;
                case LargeCapacity:
                    return PrintAttributes.OutputBin.LARGE_CAPACITY;
                case Left:
                    return PrintAttributes.OutputBin.LEFT;
                case Mailbox1:
                    return PrintAttributes.OutputBin.OUTPUT_BIN_1;
                case Mailbox2:
                    return PrintAttributes.OutputBin.OUTPUT_BIN_2;
                case Mailbox3:
                    return PrintAttributes.OutputBin.OUTPUT_BIN_3;
                case Mailbox4:
                    return PrintAttributes.OutputBin.OUTPUT_BIN_4;
                case Mailbox5:
                    return PrintAttributes.OutputBin.OUTPUT_BIN_5;
                case Mailbox6:
                    return PrintAttributes.OutputBin.OUTPUT_BIN_6;
                case Mailbox7:
                    return PrintAttributes.OutputBin.OUTPUT_BIN_7;
                case Mailbox8:
                    return PrintAttributes.OutputBin.OUTPUT_BIN_8;
                case Mailbox9:
                    return PrintAttributes.OutputBin.OUTPUT_BIN_9;
                case Mailbox10:
                    return PrintAttributes.OutputBin.OUTPUT_BIN_10;
                case Middle:
                    return PrintAttributes.OutputBin.MIDDLE;
                case MyMailbox:
                    return PrintAttributes.OutputBin.MY_MAILBOX;
                case Rear:
                    return PrintAttributes.OutputBin.REAR;
                case Right:
                    return PrintAttributes.OutputBin.RIGHT;
                case Side:
                    return PrintAttributes.OutputBin.SIDE;
                case Stacker1:
                    return PrintAttributes.OutputBin.STACKER_1;
                case Stacker2:
                    return PrintAttributes.OutputBin.STACKER_2;
                case Stacker3:
                    return PrintAttributes.OutputBin.STACKER_3;
                case Stacker4:
                    return PrintAttributes.OutputBin.STACKER_4;
                case Stacker5:
                    return PrintAttributes.OutputBin.STACKER_5;
                case Stacker6:
                    return PrintAttributes.OutputBin.STACKER_6;
                case Stacker7:
                    return PrintAttributes.OutputBin.STACKER_7;
                case Stacker8:
                    return PrintAttributes.OutputBin.STACKER_8;
                case Stacker9:
                    return PrintAttributes.OutputBin.STACKER_9;
                case Stacker10:
                    return PrintAttributes.OutputBin.STACKER_10;
                case Top:
                    return PrintAttributes.OutputBin.TOP;
                case Tray1:
                    return PrintAttributes.OutputBin.TRAY_1;
                case Tray2:
                    return PrintAttributes.OutputBin.TRAY_2;
                case Tray3:
                    return PrintAttributes.OutputBin.TRAY_3;
                case Tray4:
                    return PrintAttributes.OutputBin.TRAY_4;
                case Tray5:
                    return PrintAttributes.OutputBin.TRAY_5;
                case Tray6:
                    return PrintAttributes.OutputBin.TRAY_6;
                case Tray7:
                    return PrintAttributes.OutputBin.TRAY_7;
                case Tray8:
                    return PrintAttributes.OutputBin.TRAY_8;
                case Tray9:
                    return PrintAttributes.OutputBin.TRAY_9;
                case Tray10:
                    return PrintAttributes.OutputBin.TRAY_10;
            }
        }
        return null;
    }

    private PrintAttributes.Finishings getFinishings(Finishings finishings) {
        if (finishings != null) {
            switch (finishings) {
                case None:
                    return PrintAttributes.Finishings.NONE;
                case Staple:
                    return PrintAttributes.Finishings.STAPLE;
                case Punch:
                    return PrintAttributes.Finishings.PUNCH;
                case Cover:
                    return PrintAttributes.Finishings.COVER;
                case Bind:
                    return PrintAttributes.Finishings.BIND;
                case SaddleStitch:
                    return PrintAttributes.Finishings.SADDLE_STITCH;
                case EdgeStitch:
                    return PrintAttributes.Finishings.EDGE_STITCH;
                case Fold:
                    return PrintAttributes.Finishings.FOLD;
                case Trim:
                    return PrintAttributes.Finishings.TRIM;
                case Bale:
                    return PrintAttributes.Finishings.BALE;
                case BookletMaker:
                    return PrintAttributes.Finishings.BOOKLET_MAKER;
                case JogOffset:
                    return PrintAttributes.Finishings.JOG_OFFSET;
                case Coat:
                    return PrintAttributes.Finishings.COAT;
                case Laminate:
                    return PrintAttributes.Finishings.LAMINATE;
                case StapleTopLeft:
                    return PrintAttributes.Finishings.STAPLE_TOP_LEFT;
                case StapleBottomLeft:
                    return PrintAttributes.Finishings.STAPLE_BOTTOM_LEFT;
                case StapleTopRight:
                    return PrintAttributes.Finishings.STAPLE_TOP_RIGHT;
                case StapleBottomRight:
                    return PrintAttributes.Finishings.STAPLE_BOTTOM_RIGHT;
                case EdgeStitchLeft:
                    return PrintAttributes.Finishings.EDGE_STITCH_LEFT;
                case EdgeStitchTop:
                    return PrintAttributes.Finishings.EDGE_STITCH_TOP;
                case EdgeStitchRight:
                    return PrintAttributes.Finishings.EDGE_STITCH_RIGHT;
                case EdgeStitchBottom:
                    return PrintAttributes.Finishings.EDGE_STITCH_BOTTOM;
                case StapleDualLeft:
                    return PrintAttributes.Finishings.STAPLE_DUAL_LEFT;
                case StapleDualTop:
                    return PrintAttributes.Finishings.STAPLE_DUAL_TOP;
                case StapleDualRight:
                    return PrintAttributes.Finishings.STAPLE_DUAL_RIGHT;
                case StapleDualBottom:
                    return PrintAttributes.Finishings.STAPLE_DUAL_BOTTOM;
                case StapleTripleLeft:
                    return PrintAttributes.Finishings.STAPLE_TRIPLE_LEFT;
                case StapleTripleTop:
                    return PrintAttributes.Finishings.STAPLE_TRIPLE_TOP;
                case StapleTripleRight:
                    return PrintAttributes.Finishings.STAPLE_TRIPLE_RIGHT;
                case StapleTripleBottom:
                    return PrintAttributes.Finishings.STAPLE_TRIPLE_BOTTOM;
                case BindLeft:
                    return PrintAttributes.Finishings.BIND_LEFT;
                case BindTop:
                    return PrintAttributes.Finishings.BIND_TOP;
                case BindRight:
                    return PrintAttributes.Finishings.BIND_RIGHT;
                case BindBottom:
                    return PrintAttributes.Finishings.BIND_BOTTOM;
                case TrimAfterPages:
                    return PrintAttributes.Finishings.TRIM_AFTER_PAGES;
                case TrimAfterDocuments:
                    return PrintAttributes.Finishings.TRIM_AFTER_DOCUMENTS;
                case TrimAfterCopies:
                    return PrintAttributes.Finishings.TRIM_AFTER_COPIES;
                case TrimAfterJob:
                    return PrintAttributes.Finishings.TRIM_AFTER_JOB;
                case PunchTopLeft:
                    return PrintAttributes.Finishings.PUNCH_TOP_LEFT;
                case PunchBottomLeft:
                    return PrintAttributes.Finishings.PUNCH_BOTTOM_LEFT;
                case PunchTopRight:
                    return PrintAttributes.Finishings.PUNCH_TOP_RIGHT;
                case PunchBottomRight:
                    return PrintAttributes.Finishings.PUNCH_BOTTOM_RIGHT;
                case PunchDualLeft:
                    return PrintAttributes.Finishings.PUNCH_DUAL_LEFT;
                case PunchDualTop:
                    return PrintAttributes.Finishings.PUNCH_DUAL_TOP;
                case PunchDualRight:
                    return PrintAttributes.Finishings.PUNCH_DUAL_RIGHT;
                case PunchDualBottom:
                    return PrintAttributes.Finishings.PUNCH_DUAL_BOTTOM;
                case PunchTripleLeft:
                    return PrintAttributes.Finishings.PUNCH_TRIPLE_LEFT;
                case PunchTripleTop:
                    return PrintAttributes.Finishings.PUNCH_TRIPLE_TOP;
                case PunchTripleRight:
                    return PrintAttributes.Finishings.PUNCH_TRIPLE_RIGHT;
                case PunchTripleBottom:
                    return PrintAttributes.Finishings.PUNCH_TRIPLE_BOTTOM;
                case PunchQuadLeft:
                    return PrintAttributes.Finishings.PUNCH_QUAD_LEFT;
                case PunchQuadTop:
                    return PrintAttributes.Finishings.PUNCH_QUAD_TOP;
                case PunchQuadRight:
                    return PrintAttributes.Finishings.PUNCH_QUAD_RIGHT;
                case PunchQuadBottom:
                    return PrintAttributes.Finishings.PUNCH_QUAD_BOTTOM;
                case PunchMultipleLeft:
                    return PrintAttributes.Finishings.PUNCH_MULTIPLE_LEFT;
                case PunchMultipleTop:
                    return PrintAttributes.Finishings.PUNCH_MULTIPLE_TOP;
                case PunchMultipleRight:
                    return PrintAttributes.Finishings.PUNCH_MULTIPLE_RIGHT;
                case PunchMultipleBottom:
                    return PrintAttributes.Finishings.PUNCH_MULTIPLE_BOTTOM;
                case FoldAccordion:
                    return PrintAttributes.Finishings.FOLD_ACCORDION;
                case FoldDoubleGate:
                    return PrintAttributes.Finishings.FOLD_DOUBLE_GATE;
                case FoldGate:
                    return PrintAttributes.Finishings.FOLD_GATE;
                case FoldHalf:
                    return PrintAttributes.Finishings.FOLD_HALF;
                case FoldHalfZ:
                    return PrintAttributes.Finishings.FOLD_HALF_Z;
                case FoldLeftGate:
                    return PrintAttributes.Finishings.FOLD_LEFT_GATE;
                case FoldLetter:
                    return PrintAttributes.Finishings.FOLD_LETTER;
                case FoldParallel:
                    return PrintAttributes.Finishings.FOLD_PARALLEL;
                case FoldPoster:
                    return PrintAttributes.Finishings.FOLD_POSTER;
                case FoldRightGate:
                    return PrintAttributes.Finishings.FOLD_RIGHT_GATE;
                case FoldZ:
                    return PrintAttributes.Finishings.FOLD_Z;
                case FoldEngineeringZ:
                    return PrintAttributes.Finishings.FOLD_ENGINEERING_Z;
            }
        }
        return null;
    }
}
