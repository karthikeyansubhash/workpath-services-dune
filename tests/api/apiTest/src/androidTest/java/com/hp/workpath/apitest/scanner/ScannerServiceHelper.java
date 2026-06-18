package com.hp.workpath.apitest.scanner;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.hp.workpath.api.scanner.Margins;
import com.hp.workpath.api.scanner.NetworkCredentialsAttributes;
import com.hp.workpath.api.scanner.ScanAttributes;
import com.hp.workpath.api.scanner.ScanAttributesCaps;
import com.hp.workpath.api.scanner.ScanAttributesReader;

public class ScannerServiceHelper {
    private static final String TAG = "ScannerServiceHelper";

    public static ScanAttributes createHttpScanAttributes(Uri httpUri, NetworkCredentialsAttributes httpCredentialsAttributes,
                                                          ScanAttributesCaps scanAttributesCapsCaps, ScanAttributes scanDefaultAttributes) {
        try {
            ScanAttributesReader defaultScanAttributesReader = new ScanAttributesReader(scanDefaultAttributes);
            Margins eraseBackMargin = new Margins(defaultScanAttributesReader.getEraseBackLeftMargin(), defaultScanAttributesReader.getEraseBackTopMargin(),
                    defaultScanAttributesReader.getEraseBackRightMargin(), defaultScanAttributesReader.getEraseBackBottomMargin());
            Margins eraseFrontMargin = new Margins(defaultScanAttributesReader.getEraseFrontLeftMargin(),
                    defaultScanAttributesReader.getEraseFrontTopMargin(), defaultScanAttributesReader.getEraseFrontRightMargin(),
                    defaultScanAttributesReader.getEraseFrontBottomMargin());
            ScanAttributes.HttpBuilder httpBuilder = new ScanAttributes.HttpBuilder(httpUri)
                    .setNetworkCredentials(httpCredentialsAttributes)
                    .setConnectTimeout(1)
                    .setAutomaticStraightenMode(defaultScanAttributesReader.getAutomaticStraightenMode())
                    .setAutomaticToneMode(defaultScanAttributesReader.getAutomaticToneMode())
                    .setBackgroundCleanup(defaultScanAttributesReader.getBackgroundCleanup())
                    .setBlankImageRemovalMode(defaultScanAttributesReader.getBlankImageRemovalMode())
                    .setCaptureMode(defaultScanAttributesReader.getCaptureMode())
                    .setColorDropoutMode(defaultScanAttributesReader.getColorDropoutMode())
                    .setColorMode(defaultScanAttributesReader.getColorMode())
                    .setContrastAdjustment(defaultScanAttributesReader.getContrastAdjustment())
                    .setCropMode(defaultScanAttributesReader.getCropMode())
                    .setCustomLength(defaultScanAttributesReader.getCustomLength())
                    .setCustomWidth(defaultScanAttributesReader.getCustomWidth())
                    .setDarknessAdjustment(defaultScanAttributesReader.getDarknessAdjustment())
                    .setDuplex(defaultScanAttributesReader.getPlex())
                    .setDocumentFormat(defaultScanAttributesReader.getDocumentFormat())
                    .setEraseBackMargin(eraseBackMargin)
                    .setEraseFrontMargin(eraseFrontMargin)
                    .setEraseMarginUnit(defaultScanAttributesReader.getEraseMarginUnit())
                    .setFileName(defaultScanAttributesReader.getFileName())
                    //.setFileOptionsAttributes(defaultScanAttributesReader.getFileOptionsAttributes())
                    .setJobAssemblyMode(defaultScanAttributesReader.getJobAssemblyMode())
                    .setMediaSource(defaultScanAttributesReader.getMediaSource())
                    .setMaxPagesPerAttachment(defaultScanAttributesReader.getMaxPagesPerAttachment())
                    .setMediaWeightAdjustment(defaultScanAttributesReader.getMediaWeightAdjustment())
                    .setMisfeedDetectionMode(defaultScanAttributesReader.getMisfeedDetectionMode())
                    .setOrientation(defaultScanAttributesReader.getOrientation())
                    .setOutputQuality(defaultScanAttributesReader.getOutputQuality())
                    .setProgressDialogMode(defaultScanAttributesReader.getProgressDialogMode())
                    .setResolution(defaultScanAttributesReader.getResolution())
                    //.setScanPreview(defaultScanAttributesReader.getScanPreview())
                    .setScanSize(defaultScanAttributesReader.getScanSize())
                    .setSharpnessAdjustment(defaultScanAttributesReader.getSharpnessAdjustment())
                    .setSplitAttachmentByPage(defaultScanAttributesReader.getSplitAttachmentByPage())
                    .setTextPhotoOptimization(defaultScanAttributesReader.getTextPhotoOptimization())
                    .setTransmissionMode(defaultScanAttributesReader.getTransmissionMode());
            return httpBuilder.build(scanAttributesCapsCaps);
        } catch (Exception unexpected) {
            Log.e(TAG, "Failed to create Http scan attributes : ", unexpected);
            return null;
        }
    }

    public static ScanAttributes createNetworkFolderScanAttributes(String smbServer, String smbPath, String username,
                                                                   String password, String domain, ScanAttributesCaps scanAttributesCaps,
                                                                   ScanAttributes scanDefaultAttributes) {
        try {
            String path = "\\\\" + smbServer + "\\" + smbPath;
            Uri netFolderUri = Uri.parse(path);

            NetworkCredentialsAttributes netFolderCredentialsAttributes = null;
            if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                netFolderCredentialsAttributes = new NetworkCredentialsAttributes.Builder()
                        .setUserName(username)
                        .setPassword(password)
                        .setDomain(domain)
                        .build();
            }

            ScanAttributesReader defaultScanAttributesReader = new ScanAttributesReader(scanDefaultAttributes);
            ScanAttributes.NetworkFolderBuilder netFolderBuilder = new ScanAttributes.NetworkFolderBuilder(netFolderUri)
                    .setNetworkCredentials(netFolderCredentialsAttributes)
                    .setConnectTimeout(1)
                    .setTransmissionMode(ScanAttributes.TransmissionMode.JOB);
            return netFolderBuilder.build(scanAttributesCaps);
        } catch (Exception unexpected) {
            Log.e(TAG, "createNetworkFolderScanAttributes : Failed to create NetworkFolder scan attributes : ", unexpected);
            return null;
        }
    }

    public static ScanAttributes createFtpScanAttributes(String ftpServer, String ftpPath, String username,
                                                         String password, ScanAttributesCaps scanAttributesCapsCaps,
                                                         ScanAttributes scanDefaultAttributes) {
        try {
            String path = "ftp://" + ftpServer;
            if (!TextUtils.isEmpty(ftpPath)) {
                path += "/" + ftpPath;
            }
            Uri ftpUri = Uri.parse(path);

            NetworkCredentialsAttributes ftpCredentialsAttributes = null;
            if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
                ftpCredentialsAttributes = new NetworkCredentialsAttributes.Builder()
                        .setUserName(username)
                        .setPassword(password)
                        .build();
            }

            ScanAttributesReader defaultScanAttributesReader = new ScanAttributesReader(scanDefaultAttributes);
            ScanAttributes.FtpBuilder ftpBuilder = new ScanAttributes.FtpBuilder(ftpUri)
                    .setNetworkCredentials(ftpCredentialsAttributes)
                    .setConnectTimeout(1)
                    .setTransmissionMode(ScanAttributes.TransmissionMode.JOB);
            return ftpBuilder.build(scanAttributesCapsCaps);
        } catch (Exception unexpected) {
            Log.e(TAG, "createFtpScanAttributes: Failed to create Ftp scan attributes : ", unexpected);
            return null;
        }
    }

    public static ScanAttributes createMeScanAttributes(ScanAttributesCaps scanAttributesCaps,
                                                        ScanAttributes scanDefaultAttributes) {
        try {
            ScanAttributesReader defaultScanAttributesReader = new ScanAttributesReader(scanDefaultAttributes);
            ScanAttributes.MeBuilder meBuilder = new ScanAttributes.MeBuilder()
                    .setColorMode(defaultScanAttributesReader.getColorMode())
                    .setDocumentFormat(defaultScanAttributesReader.getDocumentFormat())
                    .setDuplex(defaultScanAttributesReader.getPlex())
                    .setScanSize(defaultScanAttributesReader.getScanSize())
                    .setResolution(defaultScanAttributesReader.getResolution())
                    .setOrientation(defaultScanAttributesReader.getOrientation())
                    .setMediaSource(defaultScanAttributesReader.getMediaSource())
                    .setFileName(defaultScanAttributesReader.getFileName())
                    .setTransmissionMode(ScanAttributes.TransmissionMode.JOB);
            return meBuilder.build(scanAttributesCaps);
        } catch (Exception unexpected) {
            Log.e(TAG, "createMeScanAttributes: Failed to create Me scan attributes: ", unexpected);
            return null;
        }
    }
}
