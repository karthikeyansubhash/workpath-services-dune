/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.services.copylet.adapter;

import android.text.TextUtils;

import androidx.core.util.Pair;

import com.hp.ext.service.scanJob.DestinationOptions;
import com.hp.ext.service.scanJob.JobStorageOptions;
import com.hp.ext.service.scanJob.JobStorageOptions_FolderName_Binding;
import com.hp.ext.service.scanJob.JobStorageOptions_FolderName_Value;
import com.hp.ext.service.scanJob.JobStorageOptions_JobName_Binding;
import com.hp.ext.service.scanJob.JobStorageOptions_JobName_Value;
import com.hp.ext.service.scanJob.JobStorageOptions_JobPasswordType_Binding;
import com.hp.ext.service.scanJob.JobStorageOptions_JobPasswordType_Value;
import com.hp.ext.service.scanJob.JobStorageOptions_JobPassword_Binding;
import com.hp.ext.service.scanJob.JobStorageOptions_JobPassword_Value;
import com.hp.ext.service.scanJob.ScanJob;
import com.hp.ext.service.scanJob.ScanJob_Create;
import com.hp.ext.service.scanJob.ScanOptions;
import com.hp.ext.service.scanJob.ScanOptions_FileName_Binding;
import com.hp.ext.service.scanJob.ScanOptions_FileName_Value;
import com.hp.ext.service.copy.DefaultOptions;
import com.hp.ext.service.scanJob.ScanTicket;
import com.hp.ext.types.imaging.BindingFormat;
import com.hp.ext.types.imaging.PlexMode;
import com.hp.ext.types.job.StoredJobPasswordType;
import com.hp.jetadvantage.link.api.copier.CopyAttributesReader;
import com.hp.jetadvantage.link.api.copier.Copylet;
import com.hp.jetadvantage.link.api.copier.JobCredentialsAttributes;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceCopyJobService;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceScanJobService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Converts CopyAttributes to a ScanTicket with JobStorage destination,
 * and creates a ScanJob via IDeviceScanJobService for Stored Copy Job.
 */
public class StoredCopyJobAdapter {
    private static final String TAG = Copylet.TAG + "/SCJAdap";

    /**
     * Creates a ScanJob for storage using CopyAttributes.
     * Maps scan-side CopyAttributes fields to ScanOptions and sets
     * the destination to JobStorage with job name, folder, and optional credentials.
     *
     * @param scanJobService    the device scan job service
     * @param copyJobService    the device copy job service (for DefaultOptions)
     * @param packageName       the calling app's package name
     * @param copyAttributes    the copy attributes from the app
     * @return the scan job ID string, or null if creation failed
     */
    public static String createScanJobForStorage(IDeviceScanJobService scanJobService,
                                                 IDeviceCopyJobService copyJobService,
                                                 String packageName,
                                                 CopyAttributesReader copyAttributes) {
        SLog.d(TAG, "createScanJobForStorage : START, packageName=" + packageName);
        if (scanJobService == null || copyAttributes == null) {
            SLog.d(TAG, "createScanJobForStorage : service or attributes is null");
            return null;
        }

        DefaultOptions defaultOptions = copyJobService != null ? copyJobService.getDefaultOptions(packageName) : null;
        ScanTicket scanTicket = buildScanTicket(copyAttributes, defaultOptions);
        SLog.d(TAG, "createScanJobForStorage : ScanTicket built");

        ScanJob_Create scanJobCreate = new ScanJob_Create();
        scanJobCreate.setScanTicket(scanTicket);

        SLog.d(TAG, "createScanJobForStorage : Calling E2 createScanJob");
        ScanJob scanJob = scanJobService.createScanJob(packageName, scanJobCreate);
        if (scanJob != null && scanJob.getScanJobId() != null
                && scanJob.getScanJobId().getValue() != null) {
            String jobId = scanJob.getScanJobId().getValue().toString();
            SLog.d(TAG, "createScanJobForStorage : ScanJob created, jobId=" + jobId);
            return jobId;
        }

        SLog.d(TAG, "createScanJobForStorage : scanJob or scanJobId is null");
        return null;
    }

    // ==================================================================
    //         Private Methods
    // ==================================================================

    private static ScanTicket buildScanTicket(CopyAttributesReader copyAttributes, DefaultOptions defaultOptions) {
        ScanTicket scanTicket = new ScanTicket();
        scanTicket.setScanOptions(buildScanOptions(copyAttributes, defaultOptions));
        scanTicket.setDestinationOptions(buildJobStorageDestination(copyAttributes));
        return scanTicket;
    }

    private static ScanOptions buildScanOptions(CopyAttributesReader copyAttributes, DefaultOptions defaultOptions) {
        ScanOptions scanOptions = new ScanOptions();
        SLog.d(TAG, "buildScanOptions : Building scan options from CopyAttributes");

        // Color Mode
        scanOptions.setColorMode(CopyTypeMapping.colorMode.convertWtoE(
                copyAttributes.getColorMode(),
                defaultOptions != null ? defaultOptions.getColorMode() : null));

        // Content Orientation
        scanOptions.setContentOrientation(CopyTypeMapping.contentOrientation.convertWtoE(
                copyAttributes.getOrientation(),
                defaultOptions != null ? defaultOptions.getContentOrientation() : null));

        // Content Type (text/graphics optimization)
        scanOptions.setContentType(CopyTypeMapping.contentType.convertWtoE(
                copyAttributes.getTextGraphicsOptimization(),
                defaultOptions != null ? defaultOptions.getContentType() : null));

        // Image Preview Mode
        scanOptions.setImagePreviewMode(CopyTypeMapping.imagePreviewMode.convertWtoE(
                copyAttributes.getCopyPreview(),
                defaultOptions != null ? defaultOptions.getImagePreviewMode() : null));

        // Media Size (scan size)
        scanOptions.setMediaSize(CopyTypeMapping.originalMediaSize.convertWtoE(
                copyAttributes.getScanSize(),
                defaultOptions != null ? defaultOptions.getOriginalMediaSize() : null));

        // Media Source (scan source)
        scanOptions.setMediaSource(CopyTypeMapping.originalMediaSource.convertWtoE(
                copyAttributes.getScanSource(),
                defaultOptions != null ? defaultOptions.getOriginalMediaSource() : null));

        // Plex Mode (scan duplex)
        Pair<PlexMode, BindingFormat> plexModePair =
                CopyTypeMapping.originalPlexMode.convertWtoE(
                        copyAttributes.getScanDuplex(),
                        defaultOptions != null
                                ? new Pair<>(defaultOptions.getOriginalPlexMode(), defaultOptions.getMediaBindingFormat())
                                : new Pair<>(null, null));
        if (plexModePair != null && plexModePair.first != null) {
            scanOptions.setPlexMode(plexModePair.first);
            if (plexModePair.second != null) {
                scanOptions.setMediaBindingFormat(plexModePair.second);
            }
        }

        // Scan Capture Mode
        scanOptions.setScanCaptureMode(CopyTypeMapping.scanCaptureMode.convertWtoE(
                copyAttributes.getCaptureMode(),
                defaultOptions != null ? defaultOptions.getScanCaptureMode() : null));

        // Scan Progress Mode
        scanOptions.setScanProgressMode(CopyTypeMapping.scanProgressMode.convertWtoE(
                copyAttributes.getProgressDialogMode(),
                defaultOptions != null ? defaultOptions.getScanProgressMode() : null));

        // File Name (required field — use storeJobName or timestamp)
        String fileName = !TextUtils.isEmpty(copyAttributes.getStoreJobName())
                ? copyAttributes.getStoreJobName() : getDateFileName();
        ScanOptions_FileName_Binding fileNameBinding = new ScanOptions_FileName_Binding();
        ScanOptions_FileName_Value fileNameValue = new ScanOptions_FileName_Value();
        fileNameValue.setExplicitValue(fileName);
        fileNameBinding.setExplicit(fileNameValue);
        scanOptions.setFileName(fileNameBinding);

        return scanOptions;
    }

    private static DestinationOptions buildJobStorageDestination(CopyAttributesReader copyAttributes) {
        DestinationOptions destinationOptions = new DestinationOptions();
        JobStorageOptions jobStorage = new JobStorageOptions();

        // Job Name
        String jobName = !TextUtils.isEmpty(copyAttributes.getStoreJobName())
                ? copyAttributes.getStoreJobName() : getDateFileName();
        SLog.d(TAG, "buildJobStorageDestination : jobName=" + jobName);
        JobStorageOptions_JobName_Binding nameBinding = new JobStorageOptions_JobName_Binding();
        JobStorageOptions_JobName_Value nameValue = new JobStorageOptions_JobName_Value();
        nameValue.setExplicitValue(jobName);
        nameBinding.setExplicit(nameValue);
        jobStorage.setJobName(nameBinding);

        // Folder Name
        String folderName = copyAttributes.getStoreJobFolderName();
        if (!TextUtils.isEmpty(folderName)) {
            JobStorageOptions_FolderName_Binding folderBinding = new JobStorageOptions_FolderName_Binding();
            JobStorageOptions_FolderName_Value folderValue = new JobStorageOptions_FolderName_Value();
            folderValue.setExplicitValue(folderName);
            folderBinding.setExplicit(folderValue);
            jobStorage.setFolderName(folderBinding);
        }

        // Password and Password Type (mandatory to have at least a type like SjptNone)
        StoredJobPasswordType e2PwdType = StoredJobPasswordType.SjptNone;
        JobCredentialsAttributes credentials = copyAttributes.getStoredJobCredentialsAttributes();
        if (credentials != null) {
            String password = credentials.getStoreJobPassword();
            SLog.d(TAG, "buildJobStorageDestination : credentials present, password=" + (password != null ? "[set]" : "null")
                    + ", passwordType=" + credentials.getStoreJobPasswordType());
            if (!TextUtils.isEmpty(password)) {
                JobStorageOptions_JobPassword_Binding pwdBinding = new JobStorageOptions_JobPassword_Binding();
                JobStorageOptions_JobPassword_Value pwdValue = new JobStorageOptions_JobPassword_Value();
                pwdValue.setExplicitValue(password);
                pwdBinding.setExplicit(pwdValue);
                jobStorage.setJobPassword(pwdBinding);
            }

            StoredJobPasswordType converted = convertPasswordType(credentials.getStoreJobPasswordType());
            if (converted != null) {
                e2PwdType = converted;
            }
        }

        JobStorageOptions_JobPasswordType_Binding pwdTypeBinding = new JobStorageOptions_JobPasswordType_Binding();
        JobStorageOptions_JobPasswordType_Value pwdTypeValue = new JobStorageOptions_JobPasswordType_Value();
        pwdTypeValue.setExplicitValue(e2PwdType);
        pwdTypeBinding.setExplicit(pwdTypeValue);
        jobStorage.setJobPasswordType(pwdTypeBinding);

        destinationOptions.setJobStorage(jobStorage);
        return destinationOptions;
    }

    private static StoredJobPasswordType convertPasswordType(JobCredentialsAttributes.PasswordType passwordType) {
        if (passwordType == null) {
            SLog.d(TAG, "convertPasswordType : null → null");
            return null;
        }
        switch (passwordType) {
            case NONE:
                return StoredJobPasswordType.SjptNone;
            case NUMERIC:
                return StoredJobPasswordType.SjptNumericPIN;
            case ALPHA_NUMERIC:
                return StoredJobPasswordType.SjptAlphanumericPIN;
            default:
                return null;
        }
    }

    private static String getDateFileName() {
        final Date time = Calendar.getInstance().getTime();
        final String pattern = "yyyyMMddHHmmssSS";
        final SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.US);
        return sdf.format(time);
    }
}
