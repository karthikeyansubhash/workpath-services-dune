/**
 * (C) Copyright 2025 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.services.scanlet.adapter;

import static com.hp.jetadvantage.link.services.scanlet.adapter.ScanOptionProfileAdapter.getPossibleWorkpathOptions;

import androidx.annotation.NonNull;

import com.hp.ext.service.scanJob.Profile;
import com.hp.ext.service.scanJob.ScanOptions;
import com.hp.ext.service.scanJob.ScanTicketHelper;
import com.hp.ext.types.optionProfile.OptionRule;
import com.hp.ext.types.optionProfile.optionProfileHelper.OptionProfileHelper;
import com.hp.jetadvantage.link.api.scanner.FileOptionsAttributes;
import com.hp.jetadvantage.link.api.scanner.FileOptionsAttributes.PdfCompressionMode;
import com.hp.jetadvantage.link.api.scanner.FileOptionsAttributes.TiffCompressionMode;
import com.hp.jetadvantage.link.api.scanner.FileOptionsAttributesCaps;
import com.hp.jetadvantage.link.api.scanner.FileOptionsAttributesCapsCreator;
import com.hp.jetadvantage.link.api.scanner.FileOptionsAttributesCapsCreator.Builder;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes;
import com.hp.jetadvantage.link.api.scanner.Scanlet;
import com.hp.jetadvantage.link.common.utils.JsonParser;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.device.services.exceptions.BoundDeviceException;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceScanJobService;
import com.hp.jetadvantage.link.services.common.exception.SdkConnectionErrorException;
import com.hp.jetadvantage.link.services.common.exception.SdkException;
import com.hp.jetadvantage.link.services.common.exception.SdkInvalidParamException;
import com.hp.jetadvantage.link.services.common.exception.SdkServiceErrorException;

import java.util.List;

public class ScanFileOptionAdapter {
    private static final String TAG = Scanlet.TAG + "/FOAdap";

    /**
     * Retrieves scan file capabilities (profile) from the connected device.
     *
     * @param packageName    the package name
     * @param deviceService  the connected device service
     * @param colorMode      the color mode
     * @param documentFormat the document format
     * @return JSON string of scan attributes
     * @throws SdkException if device is not connected or retrieval fails
     */
    public static String getFileOptions(String packageName, IDeviceScanJobService deviceService,
                                        ScanAttributes.ColorMode colorMode,
                                        ScanAttributes.DocumentFormat documentFormat) throws SdkException {
        if (colorMode == null) {
            throw new SdkInvalidParamException("ColorMode parameter must be provided");
        }
        if (documentFormat == null) {
            throw new SdkInvalidParamException("DocumentFormat parameter must be provided");
        }

        try {
            Profile scanDeviceProfile = deviceService.getProfile(packageName);
            if (scanDeviceProfile == null) {
                SLog.i(TAG, "ScanDeviceAdapter.getFileOptions: null scan profile !!");
                throw new SdkServiceErrorException("Failed to retrieve scan capabilities from the device");
            }

            FileOptionsAttributesCapsCreator.Builder capsCreatorBuilder =
                    createFileOptionAttributeBuilder(scanDeviceProfile, colorMode, documentFormat);
            FileOptionsAttributesCaps scanAttributesCaps = new FileOptionsAttributesCaps(capsCreatorBuilder.build());
            return JsonParser.getInstance().toJson(scanAttributesCaps);
        } catch (BoundDeviceException e) {
            throw new SdkConnectionErrorException("Device is not connected, " + e.getMessage());
        } catch (RuntimeException e) {
            SLog.i(TAG, "ScanDeviceAdapter.getFileOptions: RuntimeException = " + e);
            throw new SdkServiceErrorException("Failed to retrieve scan capabilities from the device, " + e.getMessage());
        }
    }

    private static Builder createFileOptionAttributeBuilder(@NonNull Profile scanProfile,
                                                            ScanAttributes.ColorMode colorMode,
                                                            ScanAttributes.DocumentFormat documentFormat) {
        Builder builder = new FileOptionsAttributesCapsCreator.Builder();
        OptionProfileHelper<ScanOptions> optionProfileHelper;

        try {
            ScanTicketHelper scanHelper = new ScanTicketHelper(scanProfile.getBase(), scanProfile.getHttp());
            optionProfileHelper = scanHelper.getHttpScanOptionsProfileHelper();
            if (optionProfileHelper == null) {
                return builder;
            }
        } catch (Exception e) {
            SLog.i(TAG, "createFileOptionAttributeBuilder: failed to create ScanTicketHelper, e=" + e);
            return builder;
        }

        List<FileOptionsAttributes.PdfCompressionMode> pdfCompressionModes =
                getPossiblePdfCompressionModes(optionProfileHelper, documentFormat);
        if (pdfCompressionModes != null) {
            pdfCompressionModes.forEach(builder::addPdfCompressionMode);
        }

        List<FileOptionsAttributes.TiffCompressionMode> tiffCompressionModes =
                getPossibleTiffCompressionModes(optionProfileHelper, colorMode, documentFormat);
        if (tiffCompressionModes != null) {
            tiffCompressionModes.forEach(builder::addTiffCompressionMode);
        }

        builder.setPdfEncryptionPasswordSupported(isPdfEncryptionSupported(optionProfileHelper, documentFormat));

        return builder;
    }

    private static boolean isPdfEncryptionSupported(OptionProfileHelper<ScanOptions> optionProfileHelper,
                                                    ScanAttributes.DocumentFormat documentFormat) {
        if (!optionProfileHelper.getOptionDefinition(ScanTypeMapping.outputFileEncryption.name()).getIsAvailable()) {
            SLog.d(TAG, "isPdfEncryptionSupported: optionName = outputFileEncryption is not available");
            return false;
        }

        ScanOptions scanOptions = new ScanOptions();
        scanOptions.setOutputFileFormat(ScanTypeMapping.outputFileFormat.convertWtoE(documentFormat));

        return !evaluateOptionDisableRule(optionProfileHelper, ScanTypeMapping.outputFileEncryption.name(),
                scanOptions);
    }

    private static List<PdfCompressionMode> getPossiblePdfCompressionModes(OptionProfileHelper<ScanOptions> optionProfileHelper,
                                                                           ScanAttributes.DocumentFormat documentFormat) {
        List<PdfCompressionMode> pdfCompressionModes = getPossibleWorkpathOptions(optionProfileHelper,
                ScanTypeMapping.outputFileCompression);

        ScanOptions scanOptions = new ScanOptions();
        scanOptions.setOutputFileFormat(ScanTypeMapping.outputFileFormat.convertWtoE(documentFormat));

        if (evaluateOptionDisableRule(optionProfileHelper, ScanTypeMapping.outputFileCompression.name(), scanOptions)) {
            return null;
        }
        return pdfCompressionModes;
    }

    private static List<TiffCompressionMode> getPossibleTiffCompressionModes(OptionProfileHelper<ScanOptions> optionProfileHelper,
                                                                             ScanAttributes.ColorMode colorMode,
                                                                             ScanAttributes.DocumentFormat documentFormat) {
        ScanTypeMapping option = ScanAttributes.ColorMode.MONO.equals(colorMode) ? ScanTypeMapping.monoCompression :
                ScanTypeMapping.colorAndGrayCompression;

        List<TiffCompressionMode> tiffCompressionModes =
                getPossibleWorkpathOptions(optionProfileHelper, option);

        ScanOptions scanOptions = new ScanOptions();
        scanOptions.setColorMode(ScanTypeMapping.colorMode.convertWtoE(colorMode));
        scanOptions.setOutputFileFormat(ScanTypeMapping.outputFileFormat.convertWtoE(documentFormat));

        if (evaluateOptionDisableRule(optionProfileHelper, option.name(), scanOptions)) {
            return null;
        }
        return tiffCompressionModes;
    }

    /**
     * Evaluates if the specified option is disabled based on the provided options instance.
     *
     * @param optionProfileHelper the option profile helper
     * @param optionName          the name of the option
     * @param optionsInstance     the scan options instance
     * @return true if the option is disabled, false otherwise
     */
    private static boolean evaluateOptionDisableRule(OptionProfileHelper<ScanOptions> optionProfileHelper,
                                                     String optionName, ScanOptions optionsInstance) {
        boolean isDisabled = false;
        List<OptionRule> definitionRules = optionProfileHelper.getOptionDefinition(optionName).getRules();

        for (OptionRule rule : definitionRules) {
            if (rule.isDisable()) {
                try {
                    isDisabled = optionProfileHelper.evaluateCondition(rule.getDisable().getCondition(),
                            optionsInstance);
                    if (isDisabled) {
                        break;
                    }
                } catch (Exception e) {
                    SLog.e(TAG, "Error evaluating disable rule for option: " + optionName, e);
                }
            }
        }
        return isDisabled;
    }
}
