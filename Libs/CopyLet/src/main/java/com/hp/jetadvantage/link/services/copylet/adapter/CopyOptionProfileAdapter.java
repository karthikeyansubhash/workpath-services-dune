package com.hp.jetadvantage.link.services.copylet.adapter;

import static com.hp.jetadvantage.link.services.copylet.adapter.CopyDeviceAdapter.getStampOption;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.core.util.Pair;

import com.hp.ext.service.copy.CopyOptions;
import com.hp.ext.service.copy.CopyTicketHelper;
import com.hp.ext.service.copy.Profile;
import com.hp.ext.types.imaging.BindingFormat;
import com.hp.ext.types.imaging.PlexMode;
import com.hp.ext.types.optionProfile.OptionProfile;
import com.hp.ext.types.optionProfile.optionProfileHelper.OptionProfileHelper;
import com.hp.ext.types.optionProfile.optionProfileHelper.OptionProfileHelperException;
import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.api.copier.CopyAttributes;
import com.hp.jetadvantage.link.api.copier.CopyAttributesCaps;
import com.hp.jetadvantage.link.api.copier.CopyAttributesCapsCreator;
import com.hp.jetadvantage.link.api.copier.Copylet;
import com.hp.jetadvantage.link.api.copier.JobCredentialsAttributes;
import com.hp.jetadvantage.link.common.utils.JsonParser;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.device.services.converter.BaseOptionProfileAdapter;
import com.hp.jetadvantage.link.device.services.converter.ITypeConverter;
import com.hp.jetadvantage.link.device.services.exceptions.BoundDeviceException;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceCopyJobService;
import com.hp.jetadvantage.link.services.common.exception.SdkConnectionErrorException;
import com.hp.jetadvantage.link.services.common.exception.SdkException;
import com.hp.jetadvantage.link.services.common.exception.SdkServiceErrorException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CopyOptionProfileAdapter extends BaseOptionProfileAdapter {
    private static final String TAG = Copylet.TAG + "/OPAdap";

    /**
     * get copy capabilities (profile) from the connected device
     *
     * @param deviceService connected device service
     * @param clientVersion client version
     * @return
     * @throws Exception SdkConnectionErrorException if device is not connected
     */
    @VisibleForTesting
    public static String getCaps(String packageName, IDeviceCopyJobService deviceService, final int clientVersion) throws SdkException {
        try {
            Profile profile = deviceService.getProfile(packageName);
            if (profile != null) {
                CopyAttributesCapsCreator.Builder builder = createCopyAttributeCapsBuilder(profile, clientVersion);
                CopyAttributesCaps copyAttributesCaps = new CopyAttributesCaps(builder.build());
                return JsonParser.getInstance().toJson(copyAttributesCaps);
            } else {
                SLog.i(TAG, "CopyDeviceAdapter.getCaps: null copy profile !!");
            }
        } catch (BoundDeviceException e) {
            throw new SdkConnectionErrorException("Device is not connected");
        } catch (RuntimeException e) {
            SLog.i(TAG, "CopyDeviceAdapter.getCaps: RuntimeException = " + e.getMessage());
            throw new SdkServiceErrorException("Failed to retrieve copy caps from the device. " + e.getMessage());
        }
        throw new SdkServiceErrorException("Failed to retrieve copy caps from the device");
    }

    protected static CopyAttributesCapsCreator.Builder createCopyAttributeCapsBuilder(@NonNull Profile copyOptionsProfile,
                                                                                      int clientVersion) {
        CopyAttributesCapsCreator.Builder builder = new CopyAttributesCapsCreator.Builder();
        OptionProfileHelper<CopyOptions> optionProfileHelper = getOptionProfileHelper(copyOptionsProfile);
        if (optionProfileHelper == null) {
            SLog.e(TAG, "createCopyAttributeCapsBuilder: optionProfileHelper is null");
            return builder;
        }

        addPossibleScanDuplexOptions(optionProfileHelper, builder);
        setNumericRangeOptions(optionProfileHelper, CopyTypeMapping.copies, builder::setCopiesRange);

        addPossibleOptions(optionProfileHelper, CopyTypeMapping.colorMode, builder::addColorMode);
        addPossibleOptions(optionProfileHelper, CopyTypeMapping.originalMediaSize, builder::addScanSize);
        addPossibleOptions(optionProfileHelper, CopyTypeMapping.originalMediaSource, builder::addScanSource);
        addPossibleOptions(optionProfileHelper, CopyTypeMapping.collationType, builder::addCollateMode);
        addPossibleOptions(optionProfileHelper, CopyTypeMapping.contentOrientation, builder::addOrientation);
        addPossibleOptions(optionProfileHelper, CopyTypeMapping.contentType, builder::addTextGraphicsOptimization);
        addPossibleOptions(optionProfileHelper, CopyTypeMapping.imagePreviewMode, builder::addCopyPreview);
        addPossibleOptions(optionProfileHelper, CopyTypeMapping.outputDuplexBinding, builder::addPrintDuplex);
        addPossibleOptions(optionProfileHelper, CopyTypeMapping.outputMediaDestination, builder::addOutputBin);
        addPossibleOptions(optionProfileHelper, CopyTypeMapping.outputMediaSize, builder::addPrintSize);
        addPossibleOptions(optionProfileHelper, CopyTypeMapping.outputMediaSource, builder::addPaperSource);
        addPossibleOptions(optionProfileHelper, CopyTypeMapping.outputMediaType, builder::addPaperType);
        //TODO : need to add numberUpDirection if NumberUpMode is not DEFAULT
        addPossibleOptions(optionProfileHelper, CopyTypeMapping.pagesPerSheet, builder::addNumberUpMode);
        addPossibleOptions(optionProfileHelper, CopyTypeMapping.scaleSelection, builder::addScaleMode);
        addPossibleOptions(optionProfileHelper, CopyTypeMapping.scanCaptureMode, builder::addCaptureMode);
        addPossibleOptions(optionProfileHelper, CopyTypeMapping.scanProgressMode, builder::addProgressDialogMode);
        //TODO : dune - add other copy options same way as upper options

        // Set scale percent range for all scan sources reported by the device profile
        setScalePercentRangeForAllScanSources(optionProfileHelper, builder);
        builder.addJobExecutionMode(CopyAttributes.JobExecutionMode.NORMAL);
        builder.addJobExecutionMode(CopyAttributes.JobExecutionMode.STORE);
        builder.addStapleOption(CopyAttributes.StapleOption.NONE);
        builder.addPunchMode(CopyAttributes.PunchMode.NONE);
        builder.addFoldMode(CopyAttributes.FoldMode.NONE);
        builder.addStampOption(CopyAttributes.StampPosition.TOP_LEFT, getStampOption());
        builder.addWatermarkType(CopyAttributes.WatermarkType.NONE);
        builder.addWatermarkTextSize(10);

        // Stored Copy Job: add all supported password types
        SLog.d(TAG, "addPasswordType : Adding NONE, NUMERIC, ALPHA_NUMERIC to caps builder");
        builder.addPasswordType(JobCredentialsAttributes.PasswordType.NONE);
        builder.addPasswordType(JobCredentialsAttributes.PasswordType.NUMERIC);
        builder.addPasswordType(JobCredentialsAttributes.PasswordType.ALPHA_NUMERIC);

        return builder;
    }

    // ==================================================================
    //         Private Methods
    // ==================================================================
    private static void addPossibleScanDuplexOptions(@NonNull OptionProfileHelper<CopyOptions> optionProfileHelper,
                                                     CopyAttributesCapsCreator.Builder builder) {
        try {
            String originalPlexModeOptionName = CopyTypeMapping.originalPlexMode.name();

            if (!optionProfileHelper.getOptionDefinition(originalPlexModeOptionName).getIsAvailable()) {
                SLog.d(TAG, "addPossibleDuplexOptions : plexMode is not available");
                return;
            }
            if (!optionProfileHelper.getOptionDefinition("mediaBindingFormat").getIsAvailable()) {
                SLog.d(TAG, "addPossibleDuplexOptions : mediaBindingFormat is not available");
                return;
            }
            Set<CopyAttributes.Duplex> workpathOptions = new HashSet<>();
            ITypeConverter<Pair<PlexMode, BindingFormat>, CopyAttributes.Duplex> converter =
                    CopyTypeMapping.originalPlexMode.getConverter();
            if (converter != null) {
                List<PlexMode> plexModes = optionProfileHelper.getPossibleValues(originalPlexModeOptionName,
                        PlexMode.class);
                List<BindingFormat> bindingFormats = optionProfileHelper.getPossibleValues("mediaBindingFormat",
                        BindingFormat.class);
                if (plexModes == null) {
                    plexModes = getOptionsFromFields(optionProfileHelper, originalPlexModeOptionName, PlexMode.class);
                }
                if (bindingFormats == null) {
                    bindingFormats = getOptionsFromFields(optionProfileHelper, "mediaBindingFormat",
                            BindingFormat.class);
                }
                if (plexModes != null && bindingFormats != null) {
                    for (PlexMode plexMode : plexModes) {
                        for (BindingFormat bindingFormat : bindingFormats) {
                            CopyAttributes.Duplex workpathOption = converter.convertEtoW(new Pair<>(plexMode,
                                    bindingFormat));
                            if (workpathOption != null) workpathOptions.add(workpathOption);
                        }
                    }
                    for (CopyAttributes.Duplex workpathOption : workpathOptions) {
                        builder.addScanDuplex(workpathOption);
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

    /**
     * Sets the scale percent range (1-100) for all scan sources reported by the E2 profile.
     * Without this, only the Builder default (DEFAULT → 100-100) exists in the map,
     * causing NPE when the app selects ADF, FLATBED, or AUTO.
     */
    private static void setScalePercentRangeForAllScanSources(
            @NonNull OptionProfileHelper<CopyOptions> optionProfileHelper,
            CopyAttributesCapsCreator.Builder builder) {
        List<CopyAttributes.ScanSource> scanSources =
                getPossibleWorkpathOptions(optionProfileHelper, CopyTypeMapping.originalMediaSource);
        if (scanSources != null) {
            for (CopyAttributes.ScanSource source : scanSources) {
                builder.setScalePercentRange(source, 1, 100);
            }
        }
    }

    private static OptionProfileHelper<CopyOptions> getOptionProfileHelper(@NonNull Profile copyOptionsProfile) {
        OptionProfileHelper<CopyOptions> optionProfileHelper = null;
        try {
            OptionProfile baseOptionProfile = new OptionProfile();
            baseOptionProfile.setDefinitions(copyOptionsProfile.getBase().getDefinitions()); //TODO Copy also need to distinguish base(normal) and store job
            CopyTicketHelper copyHelper = new CopyTicketHelper(baseOptionProfile);
            optionProfileHelper = copyHelper.getBaseCopyOptionsProfileHelper();
        } catch (Exception e) {
            SLog.i(TAG, "getOptionHelper: fail to create CopyTicketHelper e=" + e);
        }
        return optionProfileHelper;
    }
}
