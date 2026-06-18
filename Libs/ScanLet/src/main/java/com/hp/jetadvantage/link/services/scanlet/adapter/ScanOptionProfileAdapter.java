/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.services.scanlet.adapter;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;

import com.hp.ext.service.scanJob.DefaultOptions;
import com.hp.ext.service.scanJob.Profile;
import com.hp.ext.service.scanJob.ScanOptions;
import com.hp.ext.service.scanJob.ScanTicketHelper;
import com.hp.ext.types.imaging.BindingFormat;
import com.hp.ext.types.imaging.ColorMode;
import com.hp.ext.types.imaging.FileFormat;
import com.hp.ext.types.imaging.PlexMode;
import com.hp.ext.types.optionProfile.OptionDefinition;
import com.hp.ext.types.optionProfile.optionProfileHelper.OptionProfileHelper;
import com.hp.ext.types.optionProfile.optionProfileHelper.OptionProfileHelperException;
import com.hp.jetadvantage.link.api.massstorage.MassStorageInfo;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes;
import com.hp.jetadvantage.link.api.scanner.ScanAttributesCaps;
import com.hp.jetadvantage.link.api.scanner.ScanAttributesCapsCreator;
import com.hp.jetadvantage.link.api.scanner.Scanlet;
import com.hp.jetadvantage.link.common.utils.JsonParser;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.device.services.converter.BaseOptionProfileAdapter;
import com.hp.jetadvantage.link.device.services.converter.ITypeConverter;
import com.hp.jetadvantage.link.device.services.exceptions.BoundDeviceException;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceScanJobService;
import com.hp.jetadvantage.link.services.common.exception.SdkConnectionErrorException;
import com.hp.jetadvantage.link.services.common.exception.SdkException;
import com.hp.jetadvantage.link.services.common.exception.SdkServiceErrorException;
import com.hp.jetadvantage.link.services.storagelet.IStorage;
import com.hp.jetadvantage.link.services.storagelet.StorageFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Scan Option Profile Adapter Class :
 * - interact with a connected device by calling the Device Service Interface
 * - return converted data types from Device types (E2) to Workpath API types by using ScanTypeMapping
 */
public class ScanOptionProfileAdapter extends BaseOptionProfileAdapter {
    private static final String TAG = Scanlet.TAG + "/OPAdap";
    private static IStorageProvider storageProvider = new DefaultStorageProvider();

    // Inject storage provider : Only for test (package-private)
    static void setStorageProvider(IStorageProvider provider) {
        storageProvider = provider;
    }

    /**
     * get scan capabilities (profile) from the connected device
     *
     * @param packageName      app's package name
     * @param deviceService    connected device service
     * @param transmissionMode transmissionMode
     * @param destination      destination type for profile selection
     * @return json strings
     * @throws SdkException SdkConnectionErrorException if device is not connected
     */
    public static String getCapabilities(@NonNull String packageName, @NonNull IDeviceScanJobService deviceService,
                                         ScanAttributes.TransmissionMode transmissionMode,
                                         @NonNull ScanAttributes.Destination destination,
                                         int clientVersion) throws SdkException {
        String result = "";
        try {
            SLog.d(TAG, "getCaps: ENTER - " + packageName + ", destination=" + destination);
            Profile scanDeviceProfile = deviceService.getProfile(packageName);
            if (scanDeviceProfile != null) {
                DefaultOptions e2DefaultOptions = deviceService.getDefaultOptions(packageName);
                ScanAttributesCapsCreator.Builder builder = createScanAttributesCapsBuilder(scanDeviceProfile,
                        transmissionMode, destination, e2DefaultOptions, clientVersion);
                ScanAttributesCaps scanAttributesCaps = new ScanAttributesCaps(builder.build());
                result = JsonParser.getInstance().toJson(scanAttributesCaps);
                return result;
            } else {
                SLog.e(TAG, "getCaps: fail to get scan profile from the device");
                throw new SdkServiceErrorException("Failed to retrieve scan profile from the device");
            }
        } catch (BoundDeviceException e) {
            SLog.i(TAG, "getCaps: BoundDeviceException - " + e.getMessage());
            throw new SdkConnectionErrorException("Device is not connected");
        } catch (RuntimeException e) {
            SLog.i(TAG, "getCaps: RuntimeException = " + e);
            throw new SdkServiceErrorException("Failed to retrieve scan caps from the device");
        } finally {
            SLog.d(TAG, "getCaps: result " + result);
            SLog.d(TAG, "getCaps: EXIT - " + packageName);
        }
    }

    protected static ScanAttributesCapsCreator.Builder createScanAttributesCapsBuilder(@NonNull Profile scanProfile,
                                                                                       ScanAttributes.TransmissionMode transmissionMode,
                                                                                       @NonNull ScanAttributes.Destination destination,
                                                                                       DefaultOptions e2DefaultOptions,
                                                                                       int clientVersion) {
        ScanAttributesCapsCreator.Builder builder = new ScanAttributesCapsCreator.Builder();

        // Select OptionProfileHelper based on destination
        OptionProfileHelper<ScanOptions> optionProfileHelper = getOptionProfileHelper(scanProfile, destination);
        if (optionProfileHelper == null) {
            SLog.e(TAG, "createScanAttributeBuilder: optionProfileHelper is null for destination=" + destination);
            return builder;
        }

        addPossibleDestinations(builder, transmissionMode, e2DefaultOptions);
        addPossibleDuplexOptions(optionProfileHelper, builder);
        addDocumentFormatsByColorModeOption(optionProfileHelper, builder);

        addPossibleOptions(optionProfileHelper, ScanTypeMapping.autoDeskew, builder::addAutomaticStraightenMode);
        addPossibleOptions(optionProfileHelper, ScanTypeMapping.backgroundCleanup, builder::addBackgroundCleanup);
        addPossibleOptions(optionProfileHelper, ScanTypeMapping.blankPageSuppression,
                builder::addBlankImageRemovalMode);
        addPossibleOptions(optionProfileHelper, ScanTypeMapping.colorMode, builder::addColorMode);
        addPossibleOptions(optionProfileHelper, ScanTypeMapping.contentOrientation, builder::addOrientation);
        addPossibleOptions(optionProfileHelper, ScanTypeMapping.contentType, builder::addTextPhotoOptimization);
        addPossibleOptions(optionProfileHelper, ScanTypeMapping.contrast, builder::addContrastAdjustment);
        addPossibleOptions(optionProfileHelper, ScanTypeMapping.exposureLevel, builder::addDarknessAdjustment);
        addPossibleOptions(optionProfileHelper, ScanTypeMapping.fileTransmissionMode, builder::addTransmissionMode);
        addPossibleOptions(optionProfileHelper, ScanTypeMapping.imagePreviewMode, builder::addScanPreview);
        addPossibleOptions(optionProfileHelper, ScanTypeMapping.mediaSize, builder::addScanSize);
        addPossibleOptions(optionProfileHelper, ScanTypeMapping.mediaSource, builder::addMediaSource);
        addPossibleOptions(optionProfileHelper, ScanTypeMapping.misfeedDetection, builder::addMisfeedDetectionMode);
        addPossibleOptions(optionProfileHelper, ScanTypeMapping.outputFileFormat, builder::addMeDocumentFormat);
        addPossibleOptions(optionProfileHelper, ScanTypeMapping.outputQualityVsSize, builder::addOutputQuality);
        addPossibleOptions(optionProfileHelper, ScanTypeMapping.resolution, builder::addResolution);
        addPossibleOptions(optionProfileHelper, ScanTypeMapping.scanCaptureMode, builder::addCaptureMode);
        addPossibleOptions(optionProfileHelper, ScanTypeMapping.scanProgressMode, builder::addProgressDialogMode);
        addPossibleOptions(optionProfileHelper, ScanTypeMapping.sharpness, builder::addSharpnessAdjustment);
        addPossibleCropModeOptions(optionProfileHelper, clientVersion, builder::addCropMode);

        // No matchedScanAttributesCaps with Dune ScanOptions * addAutomaticToneMode
        // No matchedScanAttributesCaps with Dune ScanOptions * addColorDropoutMode
        // No matchedScanAttributesCaps with Dune ScanOptions * addEraseMarginUnits
        // No matchedScanAttributesCaps with Dune ScanOptions * addJobAssemblyMode
        // No matchedScanAttributesCaps with Dune ScanOptions * addMediaWeightAdjustment
        // No matchedScanAttributesCaps with Dune ScanOptions * addSplitAttachmentByPage
        // No matchedScanAttributesCaps with Dune ScanOptions * setCustomLengthRange
        // No matchedScanAttributesCaps with Dune ScanOptions * setCustomWidthRange
        // No matchedScanAttributesCaps with Dune ScanOptions * setEraseBackBottomRange
        // No matchedScanAttributesCaps with Dune ScanOptions * setEraseBackLeftRange
        // No matchedScanAttributesCaps with Dune ScanOptions * setEraseBackRightRange
        // No matchedScanAttributesCaps with Dune ScanOptions * setEraseBackTopRange
        // No matchedScanAttributesCaps with Dune ScanOptions * setEraseFrontBottomRange
        // No matchedScanAttributesCaps with Dune ScanOptions * setEraseFrontLeftRange
        // No matchedScanAttributesCaps with Dune ScanOptions * setEraseFrontRightRange
        // No matchedScanAttributesCaps with Dune ScanOptions * setEraseFrontTopRange
        // No matchedScanAttributesCaps with Dune ScanOptions * setMaxPagesPerAttachmentRange

        return builder;
    }

    @SuppressWarnings("unchecked")
    private static void addPossibleCropModeOptions(OptionProfileHelper<ScanOptions> optionProfileHelper,
                                                   int clientVersion,
                                                   AddToBuilder<ScanAttributes.CropMode, ?> addFunc) {
        if (!isOptionAvailable(optionProfileHelper, ScanTypeMapping.autoCropMode.name())) return;
        try {
            List<ScanAttributes.CropMode> options = convertToWorkpathOptionList(
                    optionProfileHelper,
                    ScanTypeMapping.autoCropMode.name(),
                    ScanTypeMapping.autoCropMode.getConverter(clientVersion));
            options.forEach(addFunc::add);
        } catch (Exception e) {
            SLog.e(TAG, "addCropModeOptions: Exception " + e.getMessage());
        }
    }

    private static void addDocumentFormatsByColorModeOption(OptionProfileHelper<ScanOptions> optionProfileHelper,
                                                            ScanAttributesCapsCreator.Builder builder) {
        try {
            ScanOptions scanOptions = new ScanOptions();

            // Get possible color modes
            List<ColorMode> possibleE2Colors = optionProfileHelper.getPossibleValues(ScanTypeMapping.colorMode.name(),
                    ScanTypeMapping.colorMode.getE2Type());
            for (ColorMode e2ColorMode : possibleE2Colors) {
                ScanAttributes.ColorMode workpathColorMode = ScanTypeMapping.colorMode.convertEtoW(e2ColorMode);
                if (workpathColorMode == null) continue;

                scanOptions.setColorMode(e2ColorMode);

                // Get available file formats for the current color mode
                List<FileFormat> possibleE2FileFormats =
                        optionProfileHelper.getCurrentAvailableValues(ScanTypeMapping.outputFileFormat.name(),
                                scanOptions,
                                ScanTypeMapping.outputFileFormat.getE2Type());

                List<ScanAttributes.DocumentFormat> workpathDocumentFormats = new ArrayList<>();
                for (FileFormat e2FileFormat : possibleE2FileFormats) {
                    scanOptions.setOutputFileFormat(e2FileFormat);
                    // Evaluate the option rule for the  color mode and file format combination
                    if (optionProfileHelper.isValid(scanOptions)) {
                        ScanAttributes.DocumentFormat workpathFormat =
                                ScanTypeMapping.outputFileFormat.convertEtoW(e2FileFormat);
                        if (workpathFormat != null) {
                            workpathDocumentFormats.add(workpathFormat);
                        }
                    }
                }
                builder.addDocumentFormatsByColorMode(workpathColorMode, workpathDocumentFormats);
            }
        } catch (Exception e) {
            SLog.d(TAG, "addDocumentFormatsByColorModeOption : exception = " + e);
            throw new RuntimeException(e);
        }
    }

    private static void addPossibleDestinations(ScanAttributesCapsCreator.Builder builder,
                                                ScanAttributes.TransmissionMode transmissionMode,
                                                DefaultOptions e2DefaultOptions) {
        addDestinationIfSupported(builder, e2DefaultOptions, ScanAttributes.Destination.ME);
        builder.addDestination(ScanAttributes.Destination.HTTP);
        addDestinationIfSupported(builder, e2DefaultOptions, ScanAttributes.Destination.EMAIL);
        addDestinationIfSupported(builder, e2DefaultOptions, ScanAttributes.Destination.USB);
        if (transmissionMode != ScanAttributes.TransmissionMode.IMAGE) {
            addDestinationIfSupported(builder, e2DefaultOptions, ScanAttributes.Destination.FTP);
            addDestinationIfSupported(builder, e2DefaultOptions, ScanAttributes.Destination.NETWORK_FOLDER);
        }
    }

    private static void addPossibleDuplexOptions(@NonNull OptionProfileHelper<ScanOptions> optionProfileHelper,
                                                 ScanAttributesCapsCreator.Builder builder) {
        try {
            if (!optionProfileHelper.getOptionDefinition("plexMode").getIsAvailable()) {
                SLog.d(TAG, "addPossibleDuplexOptions : plexMode is not available");
                return;
            }
            if (!optionProfileHelper.getOptionDefinition("mediaBindingFormat").getIsAvailable()) {
                SLog.d(TAG, "addPossibleDuplexOptions : mediaBindingFormat is not available");
                return;
            }
            Set<ScanAttributes.Duplex> workpathOptions = new HashSet<>();
            ITypeConverter<Pair<PlexMode, BindingFormat>, ScanAttributes.Duplex> converter =
                    ScanTypeMapping.duplex.getConverter();
            if (converter != null) {
                List<PlexMode> plexModes = optionProfileHelper.getPossibleValues("plexMode", PlexMode.class);
                List<BindingFormat> bindingFormats = optionProfileHelper.getPossibleValues("mediaBindingFormat",
                        BindingFormat.class);
                if (plexModes == null) {
                    plexModes = getOptionsFromFields(optionProfileHelper, "plexMode", PlexMode.class);
                }
                if (bindingFormats == null) {
                    bindingFormats = getOptionsFromFields(optionProfileHelper, "mediaBindingFormat",
                            BindingFormat.class);
                }
                if (plexModes != null && bindingFormats != null) {
                    for (PlexMode plexMode : plexModes) {
                        for (BindingFormat bindingFormat : bindingFormats) {
                            ScanAttributes.Duplex workpathOption = converter.convertEtoW(new Pair<>(plexMode,
                                    bindingFormat));
                            if (workpathOption != null) workpathOptions.add(workpathOption);
                        }
                    }
                    for (ScanAttributes.Duplex workpathOption : workpathOptions) {
                        builder.addPlex(workpathOption);
                    }
                }
            }
        } catch (OptionProfileHelperException e) {
            SLog.e(TAG, "addPossibleDuplexOptions : OptionProfileHelperException = " + e.getMessage());
            // cannot find request optionName : skip it
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static OptionProfileHelper<ScanOptions> getOptionProfileHelper(@NonNull Profile scanProfile,
                                                                           ScanAttributes.Destination destination) {
        ScanTicketHelper scanTicketHelper;
        OptionProfileHelper<ScanOptions> optionProfileHelper = null;
        try {
            switch (destination) {
                case HTTP:
                    scanTicketHelper = new ScanTicketHelper(scanProfile.getBase(), scanProfile.getHttp());
                    optionProfileHelper = scanTicketHelper.getHttpScanOptionsProfileHelper();
                    break;
                case FTP:
                case NETWORK_FOLDER:
                    scanTicketHelper = new ScanTicketHelper(scanProfile.getBase(), scanProfile.getHttp(),
                            scanProfile.getNetworkFolder(), scanProfile.getJobStorage());
                    optionProfileHelper = scanTicketHelper.getNetworkFolderScanOptionsProfileHelper();
                    break;
                case ME:
                case EMAIL:
                case USB:
                    scanTicketHelper = new ScanTicketHelper(scanProfile.getBase(), scanProfile.getHttp(),
                            scanProfile.getNetworkFolder(), scanProfile.getJobStorage());
                    optionProfileHelper = scanTicketHelper.getJobStorageScanOptionsProfileHelper();
                    break;
                default:
                    //base option profile
                    Map<String, OptionDefinition> mergedMap = scanProfile.getBase().getDefinitions().stream()
                            .collect(Collectors.toMap(entry -> entry.getOptionName().toLowerCase(), entry -> entry));
                    optionProfileHelper = new OptionProfileHelper<>(new ScanOptions(), mergedMap, ScanOptions.class);
                    break;
            }
        } catch (Exception e) {
            SLog.i(TAG, "getOptionProfileHelper: fail to create optionProfileHelper e=" + e);
        }
        return optionProfileHelper;
    }

    private static void addDestinationIfSupported(ScanAttributesCapsCreator.Builder builder,
                                                  DefaultOptions e2DefaultOptions,
                                                  ScanAttributes.Destination destination) {
        if (destination == ScanAttributes.Destination.USB) {
            addUsbDestinationIfAvailable(builder, e2DefaultOptions);
        } else if (isDestinationSupported(e2DefaultOptions, destination)) {
            builder.addDestination(destination);
            SLog.d(TAG, "addPossibleDestinations: Added " + destination);
        }
    }

    private static void addUsbDestinationIfAvailable(ScanAttributesCapsCreator.Builder builder,
                                                     DefaultOptions e2DefaultOptions) {
        if (!isDestinationSupported(e2DefaultOptions, ScanAttributes.Destination.USB)) {
            return;
        }

        List<IStorage> storageList = storageProvider.getStorageList(MassStorageInfo.StorageType.USB);
        boolean hasMountedUsb = storageList.stream()
                .anyMatch(storage -> storage.getInfo().isMounted());

        if (hasMountedUsb) {
            builder.addDestination(ScanAttributes.Destination.USB);
            SLog.d(TAG, "addPossibleDestinations: Added USB (mounted storage found)");
        } else {
            SLog.d(TAG, "addPossibleDestinations: USB not added (no mounted storage)");
        }
    }

    private static boolean isDestinationSupported(DefaultOptions e2DefaultOptions, ScanAttributes.Destination destination) {
        return true;
        /**
         * TODO:
         * Currently, no explicit API exists to fetch possible destinations from E2/FW.
         * However, since E2 defaultOptions returns options per destination,
         * this can be used to identify available destinations that may vary by product.
         * Enable this logic if needed.
         * Current firmware/E2 does not support default options for all destinations, temporary block this.

         if (e2DefaultOptions == null) {
         SLog.e(TAG, "isDestinationSupported: input e2DefaultOptions is null");
         return false;
         }

         if (destination == ScanAttributes.Destination.HTTP) {
         return e2DefaultOptions.getHttp() != null;
         } else if (LOCAL_FOLDER_DESTINATIONS.contains(destination)) {
         return e2DefaultOptions.getLocalFolder() != null;
         } else if (NETWORK_FOLDER_DESTINATIONS.contains(destination)) {
         return e2DefaultOptions.getNetworkFolder() != null;
         }
         return false;
         */
    }

    public interface IStorageProvider {
        List<IStorage> getStorageList(MassStorageInfo.StorageType type);
    }

    public static class DefaultStorageProvider implements IStorageProvider {
        @Override
        public List<IStorage> getStorageList(MassStorageInfo.StorageType type) {
            return StorageFactory.INSTANCE.getStorageList(type);
        }
    }
}
