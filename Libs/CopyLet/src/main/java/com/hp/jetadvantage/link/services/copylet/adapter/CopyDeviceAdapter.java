/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.services.copylet.adapter;

import static com.hp.jetadvantage.link.services.copylet.adapter.CopyOptionProfileAdapter.createCopyAttributeCapsBuilder;

import androidx.core.util.Pair;

import com.hp.ext.service.copy.CopyOptions;
import com.hp.ext.service.copy.CopyTicketHelper;
import com.hp.ext.service.copy.DefaultOptions;
import com.hp.ext.service.copy.Profile;
import com.hp.ext.types.imaging.BindingFormat;
import com.hp.ext.types.imaging.PlexMode;
import com.hp.ext.types.optionProfile.OptionProfile;
import com.hp.ext.types.optionProfile.optionProfileHelper.OptionProfileHelper;
import com.hp.jetadvantage.link.api.copier.CopyAttributes;
import com.hp.jetadvantage.link.api.copier.CopyAttributesCaps;
import com.hp.jetadvantage.link.api.copier.CopyAttributesCapsCreator;
import com.hp.jetadvantage.link.api.copier.Copylet;
import com.hp.jetadvantage.link.api.copier.StampFormat;
import com.hp.jetadvantage.link.api.copier.StampOption;
import com.hp.jetadvantage.link.api.copier.StampPolicyType;
import com.hp.jetadvantage.link.api.copier.StampType;
import com.hp.jetadvantage.link.common.utils.JsonParser;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.device.services.exceptions.BoundDeviceException;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceCopyJobService;
import com.hp.jetadvantage.link.services.common.exception.SdkConnectionErrorException;
import com.hp.jetadvantage.link.services.common.exception.SdkException;
import com.hp.jetadvantage.link.services.common.exception.SdkServiceErrorException;

/**
 * Copy Device Adapter Class :
 * - interact with a connected device by calling the Device Service Interface
 * - return converted data types from Device types (E2) to Workpath API types by using CopyTypeConverter
 */
public class CopyDeviceAdapter {
    private static final String TAG = Copylet.TAG + "/DAdap";

    /**
     * get default copy options from a connected device
     *
     * @param deviceService connected device service
     * @param clientVersion client version
     * @return json strings
     * @throws SdkException SdkConnectionErrorException if device is not connected
     */
    public static String getDefaults(String packageName, IDeviceCopyJobService deviceService, final int clientVersion) throws SdkException {
        try {
            Profile profile = deviceService.getProfile(packageName);
            if (profile != null) {
                DefaultOptions e2DefaultOptions = deviceService.getDefaultOptions(packageName);
                if (e2DefaultOptions != null) {
                    OptionProfile baseOptionProfile = new OptionProfile();
                    baseOptionProfile.setDefinitions(profile.getBase().getDefinitions());
                    CopyTicketHelper ticketHelper = new CopyTicketHelper(baseOptionProfile);
                    OptionProfileHelper<CopyOptions> optionsHelper = ticketHelper.getBaseCopyOptionsProfileHelper();

                    //convert copy device options to Workpath API data Type
                    CopyAttributes.CopyBuilder copyBuilder = new CopyAttributes.CopyBuilder();
                    setDefaultOption(optionsHelper, CopyTypeMapping.copies,
                            e2DefaultOptions.getCopies(), copyBuilder::setCopies);
                    setDefaultOption(optionsHelper, CopyTypeMapping.colorMode,
                            e2DefaultOptions.getColorMode(), copyBuilder::setColorMode);
                    setDefaultOption(optionsHelper, CopyTypeMapping.originalMediaSize,
                            e2DefaultOptions.getOriginalMediaSize(), copyBuilder::setScanSize);
                    setDefaultOption(optionsHelper, CopyTypeMapping.originalMediaSource,
                            e2DefaultOptions.getOriginalMediaSource(), copyBuilder::setScanSource);
                    setDefaultOption(optionsHelper, CopyTypeMapping.collationType,
                            e2DefaultOptions.getCollationType(), copyBuilder::setCollateMode);
                    setDefaultOption(optionsHelper, CopyTypeMapping.contentOrientation,
                            e2DefaultOptions.getContentOrientation(), copyBuilder::setOrientation);
                    setDefaultOption(optionsHelper, CopyTypeMapping.contentType,
                            e2DefaultOptions.getContentType(), copyBuilder::setTextGraphicsOptimization);
                    setDefaultOption(optionsHelper, CopyTypeMapping.imagePreviewMode,
                            e2DefaultOptions.getImagePreviewMode(), copyBuilder::setCopyPreview);
                    setDefaultOption(optionsHelper, CopyTypeMapping.outputDuplexBinding,
                            e2DefaultOptions.getOutputDuplexBinding(), copyBuilder::setPrintDuplex);
                    setDefaultOption(optionsHelper, CopyTypeMapping.outputMediaDestination,
                            e2DefaultOptions.getOutputMediaDestination(), copyBuilder::setOutputBin);
                    setDefaultOption(optionsHelper, CopyTypeMapping.outputMediaSize,
                            e2DefaultOptions.getOutputMediaSize(), copyBuilder::setPrintSize);
                    setDefaultOption(optionsHelper, CopyTypeMapping.outputMediaSource,
                            e2DefaultOptions.getOutputMediaSource(), copyBuilder::setPaperSource);
                    setDefaultOption(optionsHelper, CopyTypeMapping.outputMediaType,
                            e2DefaultOptions.getOutputMediaType(), copyBuilder::setPaperType);
                    setDefaultOption(optionsHelper, CopyTypeMapping.pagesPerSheet,
                            e2DefaultOptions.getPagesPerSheet(), copyBuilder::setNumberUpMode);
                    setDefaultOption(optionsHelper, CopyTypeMapping.scaleSelection,
                            e2DefaultOptions.getScaleSelection(), copyBuilder::setScaleMode);
                    setDefaultOption(optionsHelper, CopyTypeMapping.scanCaptureMode,
                            e2DefaultOptions.getScanCaptureMode(), copyBuilder::setCaptureMode);
                    setDefaultOption(optionsHelper, CopyTypeMapping.scanProgressMode,
                            e2DefaultOptions.getScanProgressMode(), copyBuilder::setProgressDialogMode);

                    setDefaultScanDuplexOption(optionsHelper, new Pair<>(e2DefaultOptions.getOriginalPlexMode(),
                            e2DefaultOptions.getMediaBindingFormat()), copyBuilder::setScanDuplex);

                    //TODO : add other default copy options ..
                    /*
                    Margins eraseBackMargins = new Margins(0, 0, 0, 0); //Not exist in E2
                    Margins eraseFrontMargins = new Margins(0, 0, 0, 0); //Not exist in E2

                    Shifts imageShiftFront = new Shifts(0, 0); //Not exist in E2
                    Shifts imageShiftBack = new Shifts(0, 0); //Not exist in E2

                            .setScanDuplex(scanDuplex)
                            .setBackgroundCleanup(CopyAttributes.BackgroundCleanup.DEFAULT) //WP(enum), E2(Unsigned32)
                            .setContrastAdjustment(CopyAttributes.ContrastAdjustment.DEFAULT) //Not exist in E2
                            .setCopies(1)  //Not exist in E2, 0보다 커야하는데 defaultOptions.getCopies()가 0으로 넘어와 문제가 발생함.
                            .setDarknessAdjustment(CopyAttributes.DarknessAdjustment.DEFAULT) //Not exist in E2
                            .setJobAssemblyMode(CopyAttributes.JobAssemblyMode.DEFAULT) //Not exist in E2
                            .setOrientation(CopyAttributes.Orientation.DEFAULT) //Not exist in E2
                            .setScanCustomLength(Integer.parseInt(defaultOptions.getYOffset().toString())) //WP(int), E2
                            (Unsigned32)
                            .setScanCustomWidth(Integer.parseInt(defaultOptions.getXOffset().toString())) //WP(int), E2
                            (Unsigned32)
                            .setPrintCustomLength(Integer.parseInt(defaultOptions.getOutputCanvasYExtent().toString()
                            )) //WP
                            (int), E2(Unsigned32)
                            .setPrintCustomWidth(Integer.parseInt(defaultOptions.getOutputCanvasXExtent().toString())
                            ) //WP
                            (int), E2(Unsigned32)
                            .setSharpnessAdjustment(CopyAttributes.SharpnessAdjustment.DEFAULT) //WP
                            (SharpnessAdjustment), E2
                            (Unsigned32)
                            .setEraseMarginUnit(CopyAttributes.EraseMarginUnit.DEFAULT) //Not exist in E2
                            .setEraseFrontMargin(eraseFrontMargins) //Not exist in E2
                            .setEraseBackMargin(eraseBackMargins) //Not exist in E2
                            .setImageShiftReduceToFit(CopyAttributes.ImageShiftReduceToFit.DEFAULT) //Not exist in E2
                            .setImageShiftUnits(CopyAttributes.ImageShiftUnits.DEFAULT) //Not exist in E2
                            .setImageShiftFront(imageShiftFront) //Not exist in E2
                            .setImageShiftBack(imageShiftBack) //Not exist in E2
                            .setBookletBordersEachPage(CopyAttributes.BookletBordersEachPage.DEFAULT) //Not exist in E2
                            .setBookletFinishingOption(CopyAttributes.BookletFinishingOption.DEFAULT) //Not exist in E2
                            .setBookletFormat(CopyAttributes.BookletFormat.DEFAULT) //Not exist in E2
                            .setStapleOption(CopyAttributes.StapleOption.NONE) //Not exist in E2
                            .setPunchMode(CopyAttributes.PunchMode.NONE) //Not exist in E2
                            .setFoldMode(CopyAttributes.FoldMode.NONE) //Not exist in E2
                            .setWatermarkDarkness(1) //Not exist in E2
                            .setWatermarkText("") //Not exist in E2
                            .setWatermarkRotate45(CopyAttributes.WatermarkRotate45.DEFAULT) //Not exist in E2
                            .setWatermarkType(CopyAttributes.WatermarkType.NONE) //Not exist in E2
                            .setWatermarktextSize(10) //Not exist in E2
                            .setWatermarkTransparency(50) //Not exist in E2
                            .setWatermarkBackgroundColor("") //Not exist in E2
                            .setWatermarkFont("") //Not exist in E2
                            .setWatermarkTextColor("") //Not exist in E2
                            .setWatermarkOnlyFirstPage(CopyAttributes.WatermarkOnlyFirstPage.DEFAULT) //Not exist in E2
                            .setWatermarkMessageType(CopyAttributes.WatermarkMessageType.NONE) //Not exist in E2
                            .setWatermarkBackgroundPattern(CopyAttributes.WatermarkBackgroundPattern.SCROLL) //Not
                            exist in E2
                            .setStampOption(CopyAttributes.StampPosition.TOP_LEFT, getStampOption()); //Not exist in E2
                     */

                    CopyAttributesCapsCreator.Builder builder = createCopyAttributeCapsBuilder(profile, clientVersion);
                    CopyAttributesCaps copyAttributesCaps = new CopyAttributesCaps(builder.build());
                    CopyAttributes attrs = copyBuilder.build(copyAttributesCaps);

                    return JsonParser.getInstance().toJson(attrs);
                } else {
                    SLog.i(TAG, "CopyDeviceAdapter.getDefaults: null DefaultOptions from device service !!");
                }
            } else {
                SLog.i(TAG, "CopyDeviceAdapter.getDefaults: null copy profile from device service !!");
            }
        } catch (BoundDeviceException e) {
            throw new SdkConnectionErrorException("Device is not connected");
        } catch (Exception e) {
            //TODO : define behavior for exception
            SLog.i(TAG, "CopyDeviceAdapter.getDefaults: Unexpected Exception:" + e);
        }
        throw new SdkServiceErrorException("Failed to retrieve copy attributes from the device");
    }

    // ==================================================================
    //         Protected Methods
    // ==================================================================
    protected static StampOption getStampOption( /**com.hp.oxpdlib.options.StampOption stampOption**/) {
        StampOption linkStampOption = null;
//        if (stampOption != null) {
//            StampType linkStampType = getStampType(stampOption.type,StampType.NONE);
//            StampPolicyType linkStampPolicyType = getStampPolicyType(stampOption.policyType, StampPolicyType.NONE);
//            StampFormat linkStampFormat = getStampFormat(stampOption.format);
//            linkStampOption = new StampOption(linkStampFormat,linkStampPolicyType,stampOption.text,linkStampType);
//        } else {
        StampType linkStampType = StampType.NONE;
        StampPolicyType linkStampPolicyType = StampPolicyType.NONE;
        StampFormat linkStampFormat = getStampFormat();
        linkStampOption = new StampOption(linkStampFormat, linkStampPolicyType, "", linkStampType);
//        }
        return linkStampOption;
    }

    protected static StampFormat getStampFormat( /**com.hp.oxpdlib.options.StampFormat stampFormat**/) {
        StampFormat linkStampFormat = null;
//        if(stampFormat != null) {
//            linkStampFormat = new StampFormat(stampFormat.font, stampFormat.textSize, stampFormat.textColor,
//            stampFormat
//            .whiteBackground, stampFormat
//            .startingPage);
//        } else {
        linkStampFormat = new StampFormat("", 10, "", false, 1);
//        }
        return linkStampFormat;
    }

    // ==================================================================
    //         Private Methods
    // ==================================================================
    private static <E, W> void setDefaultOption(OptionProfileHelper<CopyOptions> optionProfileHelper,
                                                CopyTypeMapping option, E optionValue,
                                                SetCopyAttributeBuilder<W> setFunc) {
        try {
            // check option availability.
            if (!optionProfileHelper.getOptionDefinition(option.name()).getIsAvailable()) {
                SLog.d(TAG, "setDefaultOption : optionName = " + option + " is not available");
                return;
            }

            W workpathOption = (W) option.convertEtoW(optionValue, true);
            if (workpathOption != null) {
                setFunc.set(workpathOption);
            }

        } catch (Exception e) {
            SLog.e(TAG, "setDefaultOption: Error setting option '" + option.name() + "': " + e.getMessage(), e);
            throw new RuntimeException("Error setting default scan option '" + option.name() + "'.", e);
        }
    }

    private static void setDefaultScanDuplexOption(OptionProfileHelper<CopyOptions> optionProfileHelper,
                                                   Pair<PlexMode, BindingFormat> optionValue,
                                                   SetCopyAttributeBuilder<CopyAttributes.Duplex> setFunc) {
        try {
            // check option availability.
            if (!optionProfileHelper.getOptionDefinition("originalPlexMode").getIsAvailable()) {
                SLog.d(TAG, "setDefaultDuplexOption : optionName = plexMode is not available");
                return;
            }
            if (!optionProfileHelper.getOptionDefinition("mediaBindingFormat").getIsAvailable()) {
                SLog.d(TAG, "setDefaultDuplexOption : optionName = mediaBindingFormat is not available");
                return;
            }
            CopyAttributes.Duplex workpathOption = CopyTypeMapping.originalPlexMode.convertEtoW(optionValue);
            if (workpathOption != null) {
                setFunc.set(workpathOption);
            }
        } catch (Exception e) {
            SLog.e(TAG, "setDefaultDuplexOption: Error setting duplex option: " + e.getMessage(), e);
            throw new RuntimeException("Error setting duplex option.", e);
        }
    }

    @FunctionalInterface
    private interface SetCopyAttributeBuilder<T> {
        CopyAttributes.CopyAttributesBuilder set(T t);
    }
}
