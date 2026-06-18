/**
 * (C) Copyright 2025 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.services.scanlet.adapter;

import static com.hp.jetadvantage.link.services.scanlet.adapter.ScanOptionProfileAdapter.createScanAttributesCapsBuilder;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;

import com.hp.ext.service.scanJob.DefaultOptions;
import com.hp.ext.service.scanJob.Profile;
import com.hp.ext.service.scanJob.ScanOptions;
import com.hp.ext.service.scanJob.ScanTicketHelper;
import com.hp.ext.types.imaging.BindingFormat;
import com.hp.ext.types.imaging.PlexMode;
import com.hp.ext.types.optionProfile.optionProfileHelper.OptionProfileHelper;
import com.hp.jetadvantage.link.api.CapabilitiesExceededException;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes;
import com.hp.jetadvantage.link.api.scanner.ScanAttributesCaps;
import com.hp.jetadvantage.link.api.scanner.ScanAttributesCapsCreator;
import com.hp.jetadvantage.link.api.scanner.Scanlet;
import com.hp.jetadvantage.link.common.utils.JsonParser;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.device.services.converter.ITypeConverter;
import com.hp.jetadvantage.link.device.services.exceptions.BoundDeviceException;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceScanJobService;
import com.hp.jetadvantage.link.services.common.exception.SdkConnectionErrorException;
import com.hp.jetadvantage.link.services.common.exception.SdkException;
import com.hp.jetadvantage.link.services.common.exception.SdkServiceErrorException;

public class ScanDefaultOptionAdapter {
    private static final String TAG = Scanlet.TAG + "/DOAdap";

    /**
     * get default scan options from a connected device
     *
     * @param packageName   app's package name
     * @param deviceService connected device service
     * @param destination   destination type
     * @return json strings
     * @throws SdkException SdkConnectionErrorException if device is not connected
     */
    public static String getDefaults(String packageName, IDeviceScanJobService deviceService,
                                     ScanAttributes.Destination destination,
                                     int clientVersion) throws SdkException {
        ScanAttributes scanAttributes = getDefaultScanAttributes(packageName, deviceService, destination, clientVersion);
        return JsonParser.getInstance().toJson(scanAttributes);
    }

    /**
     * get default scan attributes from a connected device for a specific destination
     *
     * @param packageName   app's package name
     * @param deviceService connected device service
     * @param destination   destination type
     * @return ScanAttributes default scan attributes
     * @throws SdkException SdkConnectionErrorException if device is not connected
     */
    static ScanAttributes getDefaultScanAttributes(String packageName, IDeviceScanJobService deviceService,
                                                   ScanAttributes.Destination destination,
                                                   int clientVersion) throws SdkException {
        try {
            Profile e2ScanDeviceProfile = deviceService.getProfile(packageName);
            if (e2ScanDeviceProfile != null) {
                DefaultOptions e2DefaultOptions = deviceService.getDefaultOptions(packageName);
                if (e2DefaultOptions != null) {
                    return getDefaultsForDestination(e2ScanDeviceProfile, e2DefaultOptions, destination, clientVersion);
                } else {
                    SLog.i(TAG, "getDefaults: null DefaultOptions from device service !!");
                }
            } else {
                SLog.i(TAG, "getDefaults: null scan profile from device service !!");
            }
        } catch (BoundDeviceException e) {
            SLog.e(TAG, "getDefaults: BoundDeviceException:" + e);
            throw new SdkConnectionErrorException("Failed to connect to the device." + e.getMessage());
        } catch (SdkException e) {
            SLog.e(TAG, "getDefaults: SdkException:" + e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            SLog.e(TAG, "getDefaults: Unexpected Exception:" + e.getMessage(), e);
            throw new SdkServiceErrorException("An unexpected error occurred while retrieving scan attributes:" + e.getMessage());
        }
        throw new SdkServiceErrorException("Could not retrieve scan attributes for destination: " + destination);
    }

    @NonNull
    private static ScanAttributes getDefaultsForDestination(@NonNull Profile e2ScanProfile,
                                                            @NonNull DefaultOptions e2DefaultOptions,
                                                            ScanAttributes.Destination destination,
                                                            int clientVersion) throws SdkException {
        ScanAttributesCapsCreator.Builder capsBuilder = createScanAttributesCapsBuilder(e2ScanProfile,
                ScanAttributes.TransmissionMode.JOB, destination, e2DefaultOptions, clientVersion);
        ScanAttributesCaps scanAttributesCaps = new ScanAttributesCaps(capsBuilder.build());
        ScanAttributes.MeBuilder builder = new ScanAttributes.MeBuilder();

        ScanTicketHelper e2ScanHelper;
        try {
            switch (destination) {
                case HTTP:
                    e2ScanHelper = new ScanTicketHelper(e2ScanProfile.getBase(), e2ScanProfile.getHttp());
                    return buildScanAttributes(e2ScanHelper.getHttpScanOptionsProfileHelper(),
                            e2DefaultOptions.getHttp(), scanAttributesCaps, builder, clientVersion);
                case FTP:
                case NETWORK_FOLDER:
                    builder.setTransmissionMode(ScanAttributes.TransmissionMode.JOB);
                    e2ScanHelper = new ScanTicketHelper(e2ScanProfile.getBase(), e2ScanProfile.getHttp(),
                            e2ScanProfile.getNetworkFolder(), e2ScanProfile.getJobStorage());
                    return buildScanAttributes(e2ScanHelper.getNetworkFolderScanOptionsProfileHelper(),
                            e2DefaultOptions.getNetworkFolder(), scanAttributesCaps, builder, clientVersion);
                case ME:
                case EMAIL:
                case USB:
                    //TODO : [DUNE-169946] ME,EMAIL,USB Destination not supported yet
                    SLog.e(TAG, "getDefaultsForDestination: Destination not supported yet : " + destination.name() +
                            ", use http destination options " + "temporary");
                    e2ScanHelper = new ScanTicketHelper(e2ScanProfile.getBase(), e2ScanProfile.getHttp());
                    return buildScanAttributes(e2ScanHelper.getHttpScanOptionsProfileHelper(),
                            e2DefaultOptions.getHttp(), scanAttributesCaps, builder, clientVersion);
                default:
                    SLog.i(TAG, "getDefaults: unknown destination type !!");
                    break;
            }
        } catch (Exception e) {
            SLog.e(TAG, "getDefaultsForDestination: fail to build scan attributes e=" + e);
            throw new SdkServiceErrorException("Fail to build scan attributes for destination: " + destination.name() + ", " + e.getMessage());
        }
        throw new SdkServiceErrorException("Failed to retrieve scan attributes for destination: " + destination);
    }

    @NonNull
    private static ScanAttributes buildScanAttributes(OptionProfileHelper<ScanOptions> e2OptionProfileHelper,
                                                      ScanOptions e2ScanOptions,
                                                      ScanAttributesCaps scanAttributesCaps,
                                                      ScanAttributes.MeBuilder builder,
                                                      int clientVersion) throws CapabilitiesExceededException, SdkServiceErrorException {
        if (e2ScanOptions == null) {
            throw new SdkServiceErrorException("Missing default scan options retrieved from the device.");
        }
        if (e2OptionProfileHelper == null) {
            throw new SdkServiceErrorException("Scan profile helper is unavailable.");
        }

        if (e2ScanOptions.getFileName() != null && e2ScanOptions.getFileName().isExplicit()) {
            builder.setFileName(e2ScanOptions.getFileName().getExplicit().getExplicitValue());
        }
        setDefaultDuplexOption(e2OptionProfileHelper, new Pair<>(e2ScanOptions.getPlexMode(),
                e2ScanOptions.getMediaBindingFormat()), builder::setDuplex);
        setDefaultOption(e2OptionProfileHelper, ScanTypeMapping.autoDeskew,
                e2ScanOptions.getAutoDeskew(), builder::setAutomaticStraightenMode);
        setDefaultOption(e2OptionProfileHelper, ScanTypeMapping.backgroundCleanup,
                e2ScanOptions.getBackgroundCleanup(), builder::setBackgroundCleanup);
        setDefaultOption(e2OptionProfileHelper, ScanTypeMapping.blankPageSuppression,
                e2ScanOptions.getBlankPageSuppression(), builder::setBlankImageRemovalMode);
        setDefaultOption(e2OptionProfileHelper, ScanTypeMapping.colorMode,
                e2ScanOptions.getColorMode(), builder::setColorMode);
        setDefaultOption(e2OptionProfileHelper, ScanTypeMapping.contrast,
                e2ScanOptions.getContrast(), builder::setContrastAdjustment);
        setDefaultOption(e2OptionProfileHelper, ScanTypeMapping.exposureLevel,
                e2ScanOptions.getExposureLevel(), builder::setDarknessAdjustment);
        setDefaultOption(e2OptionProfileHelper, ScanTypeMapping.contentOrientation,
                e2ScanOptions.getContentOrientation(), builder::setOrientation);
        setDefaultOption(e2OptionProfileHelper, ScanTypeMapping.contentType,
                e2ScanOptions.getContentType(), builder::setTextPhotoOptimization);
        setDefaultOption(e2OptionProfileHelper, ScanTypeMapping.fileTransmissionMode,
                e2ScanOptions.getFileTransmissionMode(), builder::setTransmissionMode);
        setDefaultOption(e2OptionProfileHelper, ScanTypeMapping.imagePreviewMode,
                e2ScanOptions.getImagePreviewMode(), builder::setScanPreview);
        setDefaultOption(e2OptionProfileHelper, ScanTypeMapping.mediaSize,
                e2ScanOptions.getMediaSize(), builder::setScanSize);
        setDefaultOption(e2OptionProfileHelper, ScanTypeMapping.mediaSource,
                e2ScanOptions.getMediaSource(), builder::setMediaSource);
        setDefaultOption(e2OptionProfileHelper, ScanTypeMapping.misfeedDetection,
                e2ScanOptions.getMisfeedDetection(), builder::setMisfeedDetectionMode);
        setDefaultOption(e2OptionProfileHelper, ScanTypeMapping.outputFileFormat,
                e2ScanOptions.getOutputFileFormat(), builder::setDocumentFormat);
        setDefaultOption(e2OptionProfileHelper, ScanTypeMapping.outputQualityVsSize,
                e2ScanOptions.getOutputQualityVsSize(), builder::setOutputQuality);
        setDefaultOption(e2OptionProfileHelper, ScanTypeMapping.resolution,
                e2ScanOptions.getResolution(), builder::setResolution);
        setDefaultOption(e2OptionProfileHelper, ScanTypeMapping.scanCaptureMode,
                e2ScanOptions.getScanCaptureMode(), builder::setCaptureMode);
        setDefaultOption(e2OptionProfileHelper, ScanTypeMapping.scanProgressMode,
                e2ScanOptions.getScanProgressMode(), builder::setProgressDialogMode);
        setDefaultOption(e2OptionProfileHelper, ScanTypeMapping.sharpness,
                e2ScanOptions.getSharpness(), builder::setSharpnessAdjustment);
        setDefaultOption(e2OptionProfileHelper, ScanTypeMapping.autoCropMode,
                ScanTypeMapping.autoCropMode.getConverter(clientVersion),
                e2ScanOptions.getAutoCropMode(), builder::setCropMode);

        // No default DocumentFormatsByColorMode
        // No matchedScanAttributesCaps with Dune ScanOptions * addAutomaticToneMode
        // No matchedScanAttributesCaps with Dune ScanOptions * addColorDropoutMode
        // No matchedScanAttributesCaps with Dune ScanOptions * addCropMode
        // No matchedScanAttributesCaps with Dune ScanOptions * addDarknessAdjustment
        // No matchedScanAttributesCaps with Dune ScanOptions * addEraseMarginUnits
        // No matchedScanAttributesCaps with Dune ScanOptions * addJobAssemblyMode
        // No matchedScanAttributesCaps with Dune ScanOptions * addMediaWeightAdjustment
        // No matchedScanAttributesCaps with Dune ScanOptions * addSplitAttachmentByPage
        // No matchedScanAttributesCaps with Dune ScanOptions * setEraseBackBottomRange
        // No matchedScanAttributesCaps with Dune ScanOptions * setEraseBackLeftRange
        // No matchedScanAttributesCaps with Dune ScanOptions * setEraseBackRightRange
        // No matchedScanAttributesCaps with Dune ScanOptions * setEraseBackTopRange
        // No matchedScanAttributesCaps with Dune ScanOptions * setEraseFrontBottomRange
        // No matchedScanAttributesCaps with Dune ScanOptions * setEraseFrontLeftRange
        // No matchedScanAttributesCaps with Dune ScanOptions * setEraseFrontRightRange
        // No matchedScanAttributesCaps with Dune ScanOptions * setEraseFrontTopRange
        // No matchedScanAttributesCaps with Dune ScanOptions * setMaxPagesPerAttachmentRange

        return builder.build(scanAttributesCaps);
    }

    private static <E, W> void setDefaultOption(OptionProfileHelper<ScanOptions> optionProfileHelper,
                                                ScanTypeMapping option, E optionValue,
                                                SetScanAttributeBuilder<W> setFunc) {
        setDefaultOption(optionProfileHelper, option, option.getConverter(), optionValue, setFunc);
    }

    @SuppressWarnings("unchecked")
    private static <E, W> void setDefaultOption(OptionProfileHelper<ScanOptions> optionProfileHelper,
                                                ScanTypeMapping option, ITypeConverter converter, E optionValue,
                                                SetScanAttributeBuilder<W> setFunc) {
        try {
            // check option availability.
            if (optionProfileHelper.getOptionDefinition(option.name()) == null ||
                    !optionProfileHelper.getOptionDefinition(option.name()).getIsAvailable()) {
                SLog.d(TAG, "setDefaultOption : optionName = " + option + " is not available");
                return;
            }

            W workpathOption = (W) converter.convertEtoW(optionValue);

            if (workpathOption != null) {
                setFunc.set(workpathOption);
            }

        } catch (Exception e) {
            SLog.e(TAG, "setDefaultOption: Error setting option '" + option.name() + "': " + e.getMessage(), e);
            throw new RuntimeException("Error setting default scan option '" + option.name() + "'.", e);
        }
    }

    private static void setDefaultDuplexOption(OptionProfileHelper<ScanOptions> optionProfileHelper, Pair<PlexMode,
            BindingFormat> optionValue, SetScanAttributeBuilder<ScanAttributes.Duplex> setFunc) {
        try {
            // check option availability.
            if (!optionProfileHelper.getOptionDefinition("plexMode").getIsAvailable()) {
                SLog.d(TAG, "setDefaultDuplexOption : optionName = plexMode is not available");
                return;
            }
            if (!optionProfileHelper.getOptionDefinition("mediaBindingFormat").getIsAvailable()) {
                SLog.d(TAG, "setDefaultDuplexOption : optionName = mediaBindingFormat is not available");
                return;
            }
            ScanAttributes.Duplex workpathOption =
                    (ScanAttributes.Duplex) ScanTypeMapping.duplex.getConverter().convertEtoW(optionValue);
            if (workpathOption != null) {
                setFunc.set(workpathOption);
            }
        } catch (Exception e) {
            SLog.e(TAG, "setDefaultDuplexOption: Error setting duplex option: " + e.getMessage(), e);
            throw new RuntimeException("Error setting duplex option.", e);
        }
    }

    @FunctionalInterface
    private interface SetScanAttributeBuilder<T> {
        ScanAttributes.ScanAttributesBuilder set(T t);
    }
}
