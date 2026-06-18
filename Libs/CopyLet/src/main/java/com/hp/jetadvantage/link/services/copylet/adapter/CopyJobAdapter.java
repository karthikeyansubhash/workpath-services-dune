package com.hp.jetadvantage.link.services.copylet.adapter;

import androidx.core.util.Pair;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hp.ext.service.copy.CopyJob;
import com.hp.ext.service.copy.CopyJobTicket;
import com.hp.ext.service.copy.CopyJob_Create;
import com.hp.ext.service.copy.DefaultOptions;
import com.hp.ext.service.copy.ReleaseStoredJobRequest;
import com.hp.ext.service.copy.RemoveStoredJobRequest;
import com.hp.ext.service.copy.StoredJob;
import com.hp.ext.service.copy.StoredJob_Release;
import com.hp.ext.service.copy.StoredJob_Remove;
import com.hp.ext.service.copy.StoredJobs;
import com.hp.ext.types.imaging.BindingFormat;
import com.hp.ext.types.imaging.PlexMode;
import com.hp.ext.types.job.StoredJobPasswordType;
import com.hp.jetadvantage.link.api.copier.CopyAttributes;
import com.hp.jetadvantage.link.api.copier.CopyAttributesReader;
import com.hp.jetadvantage.link.api.copier.Copylet;
import com.hp.jetadvantage.link.api.copier.JobCredentialsAttributes;
import com.hp.jetadvantage.link.api.copier.StoredJobAttributes;
import com.hp.jetadvantage.link.api.copier.StoredJobInfo;
import com.hp.jetadvantage.link.api.job.CopyJobData;
import com.hp.jetadvantage.link.api.job.JobInfo;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceCopyJobService;
import com.hp.jetadvantage.link.services.common.exception.SdkServiceErrorException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import com.hp.jetadvantage.link.services.copylet.service.StoredCopyJobPreferenceStorage;

public class CopyJobAdapter {
    private static final String TAG = Copylet.TAG + "/JAdap";
    private static final String LOG_COPIES = ", copies=";
    private static final String LOG_TOTAL_PAGES = ", totalPages=";

    public static String createCopyJob(IDeviceCopyJobService copyJobService, String packageName, JobInfo jobInfo,
                                       CopyAttributesReader attributesReader) throws Exception {
        if (copyJobService == null) {
            return null;
        }
        // Get default copy options from E2 service
        DefaultOptions defaultOptions = copyJobService.getDefaultOptions(packageName);
        manipulateJobInfo(jobInfo, defaultOptions);

        // Get copy job ticket from default options
        CopyJobTicket copyJobTicket = CopyJobAdapter.getCopyJobTicket(attributesReader, defaultOptions);

        CopyJob_Create copyJobCreate = new CopyJob_Create();
        copyJobCreate.setTicket(copyJobTicket);

        CopyJob copyJob = copyJobService.createCopyJob(packageName, copyJobCreate);

        if (copyJob != null && copyJob.getCopyJobId() != null) {
            return copyJob.getCopyJobId().getValue().toString();
        }

        return null;
    }

    // ==================================================================
    //         Enumerate Stored Jobs
    // ==================================================================

    /**
     * Converts E2 StoredJobs response to a list of SDK StoredJobInfo.
     * For each member, fetches individual stored job resource and applies
     * saved CopyAttributes from PreferenceStorage for copy option values
     * (colorMode, scanSize, duplex, copies) not available in E2 response.
     *
     * @param storedJobs     E2 StoredJobs response (from enumerateStoredJobs)
     * @param copyJobService Device copy job service for individual GET calls
     * @param packageName    The package name of the calling application
     * @return List of StoredJobInfo for SDK consumption
     */
    public static List<StoredJobInfo> convertToStoredJobInfoList(
            Context context,
            StoredJobs storedJobs,
            IDeviceCopyJobService copyJobService,
            String packageName) {
        ArrayList<StoredJobInfo> result = new ArrayList<>();
        if (storedJobs == null || storedJobs.getMembers() == null) {
            SLog.d(TAG, "convertToStoredJobInfoList : storedJobs or members is null");
            return result;
        }
        SLog.d(TAG, "convertToStoredJobInfoList : Converting " + storedJobs.getMembers().size() + " E2 stored jobs");
        for (StoredJob e2Job : storedJobs.getMembers()) {
            StoredJob detailedJob = fetchStoredJobDetails(copyJobService, packageName, e2Job);
            
            CopyAttributes savedAttributes = null;
            if (context != null && detailedJob != null && detailedJob.getJobId() != null) {
                savedAttributes = StoredCopyJobPreferenceStorage.getCopyAttributes(context, detailedJob.getJobId().toString());
            }

            StoredJobInfo info = convertStoredJob(detailedJob, savedAttributes);
            if (info != null) {
                result.add(info);
            }
        }
        SLog.d(TAG, "convertToStoredJobInfoList : Converted " + result.size() + " stored jobs");
        return result;
    }

    /**
     * Enumerates stored copy jobs from the device and converts them to SDK StoredJobInfo list.
     * This method encapsulates E2 API calls so callers don't need E2 type dependencies.
     *
     * @param context        Android context for PreferenceStorage access
     * @param copyJobService Device copy job service
     * @param packageName    The package name of the calling application
     * @return List of StoredJobInfo for SDK consumption
     */
    public static List<StoredJobInfo> enumerateAndConvertStoredJobs(
            Context context,
            IDeviceCopyJobService copyJobService,
            String packageName) {
        StoredJobs storedJobs = copyJobService.enumerateStoredJobs(packageName);
        return convertToStoredJobInfoList(context, storedJobs, copyJobService, packageName);
    }

    // ==================================================================
    //         Delete Stored Job
    // ==================================================================

    /**
     * Deletes a stored copy job from the device and removes saved preferences.
     * This is a stateless operation: ContentProvider → CopyJobAdapter → DeviceCopyJobService.
     *
     * @param context        Android context for PreferenceStorage access
     * @param copyJobService Device copy job service
     * @param packageName    The package name of the calling application
     * @param storedJobId    The stored job ID to delete
     * @param credentials    Job credentials (nullable, for password-protected jobs)
     * @return StoredJob_Remove result from E2 API
     * @throws SdkServiceErrorException if the E2 delete call fails or returns null
     */
    public static StoredJob_Remove deleteStoredJob(
            Context context,
            IDeviceCopyJobService copyJobService,
            String packageName,
            String storedJobId,
            JobCredentialsAttributes credentials) throws SdkServiceErrorException {
        SLog.d(TAG, "deleteStoredJob : START, jobId=" + storedJobId + ", packageName=" + packageName);

        RemoveStoredJobRequest removeRequest = new RemoveStoredJobRequest();
        String password = extractDeletePassword(credentials);
        if (password != null && !password.isEmpty()) {
            removeRequest.setJobPassword(password);
            SLog.d(TAG, "deleteStoredJob : jobPassword=[set]");
        } else {
            SLog.d(TAG, "deleteStoredJob : No password applied");
        }

        StoredJob_Remove removeResult = copyJobService.deleteStoredJob(packageName, storedJobId, removeRequest);
        SLog.d(TAG, "deleteStoredJob : removeResult=" + (removeResult != null ? "received" : "null"));

        if (removeResult == null) {
            throw new SdkServiceErrorException("Failed to delete stored copy job");
        }

        StoredCopyJobPreferenceStorage.remove(context, storedJobId);
        SLog.d(TAG, "deleteStoredJob : PreferenceStorage removed for jobId=" + storedJobId);
        SLog.i(TAG, "deleteStoredJob : END, success");
        return removeResult;
    }

    private static String extractDeletePassword(JobCredentialsAttributes credentials) {
        if (credentials != null
                && credentials.getStoreJobPasswordType() != null
                && credentials.getStoreJobPasswordType() != JobCredentialsAttributes.PasswordType.NONE) {
            return credentials.getStoreJobPassword();
        }
        return null;
    }

    // ==================================================================
    //         Release Stored Copy Job
    // ==================================================================

    /**
     * Releases a stored copy job and returns the resulting CopyJob ID.
     * Encapsulates E2 type conversions for ReleaseStoredJobRequest, CopyOptions, and StoredJob_Release.
     *
     * @param copyJobService     Device copy job service
     * @param packageName        The package name of the calling application
     * @param storedJobId        The stored job ID to release
     * @param savedCopyAttributes The original CopyAttributes saved during creation (nullable)
     * @param storedJobAttributes The release request attributes from the app
     * @return the released CopyJob ID string
     * @throws SdkServiceErrorException if the release call fails
     */
    public static String releaseStoredJob(
            IDeviceCopyJobService copyJobService,
            String packageName,
            String storedJobId,
            CopyAttributes savedCopyAttributes,
            StoredJobAttributes storedJobAttributes) throws SdkServiceErrorException {
        SLog.d(TAG, "releaseStoredJob : START, jobId=" + storedJobId + ", packageName=" + packageName);

        DefaultOptions defaultOptions = copyJobService.getDefaultOptions(packageName);
        SLog.d(TAG, "releaseStoredJob : defaultOptions=" + (defaultOptions != null ? "found" : "null"));

        ReleaseStoredJobRequest releaseRequest = new ReleaseStoredJobRequest();
        try {
            buildReleaseCopyOptions(releaseRequest, savedCopyAttributes, storedJobAttributes, defaultOptions, storedJobId);
        } catch (IOException e) {
            SLog.e(TAG, "releaseStoredJob : Failed to build CopyOptions: " + e.getMessage(), e);
            throw new SdkServiceErrorException("Failed to build CopyOptions for release");
        }
        applyReleaseJobPassword(releaseRequest, storedJobAttributes);

        SLog.i(TAG, "releaseStoredJob : Calling E2 releaseStoredJob for jobId=" + storedJobId);
        StoredJob_Release releaseResult = copyJobService.releaseStoredJob(packageName, storedJobId, releaseRequest);
        SLog.d(TAG, "releaseStoredJob : releaseResult=" + (releaseResult != null ? "received" : "null")
                + ", copyJob=" + (releaseResult != null && releaseResult.getCopyJob() != null ? "present" : "null"));

        if (releaseResult == null || releaseResult.getCopyJob() == null) {
            SLog.e(TAG, "releaseStoredJob : Failed to release stored copy job");
            throw new SdkServiceErrorException("Failed to release stored copy job");
        }

        CopyJob copyJob = releaseResult.getCopyJob();
        if (copyJob.getCopyJobId() == null) {
            SLog.e(TAG, "releaseStoredJob : Released copy job has no ID");
            throw new SdkServiceErrorException("Released copy job has no ID");
        }

        String copyJobId = copyJob.getCopyJobId().toString();
        SLog.i(TAG, "releaseStoredJob : CopyJob ID = " + copyJobId);
        return copyJobId;
    }

    private static void buildReleaseCopyOptions(ReleaseStoredJobRequest releaseRequest,
            CopyAttributes savedCopyAttributes,
            StoredJobAttributes storedJobAttributes,
            DefaultOptions defaultOptions,
            String storedJobId) throws IOException {
        if (savedCopyAttributes != null && defaultOptions != null) {
            SLog.d(TAG, "buildReleaseCopyOptions : Building CopyOptions from saved CopyAttributes");
            CopyJobTicket ticket = getCopyJobTicketForRelease(
                    savedCopyAttributes, storedJobAttributes.getCopies(), defaultOptions);
            releaseRequest.setCopyOptions(ticket.getCopyOptions());
            SLog.i(TAG, "buildReleaseCopyOptions : CopyOptions set from saved CopyAttributes");
        } else if (defaultOptions != null) {
            SLog.w(TAG, "buildReleaseCopyOptions : No saved CopyAttributes for jobId=" + storedJobId
                    + ", using default options as fallback");
            releaseRequest.setCopyOptions(convertDefaultOptionsToCopyOptions(defaultOptions));
            SLog.i(TAG, "buildReleaseCopyOptions : CopyOptions set from DefaultOptions (fallback)");
        } else {
            SLog.e(TAG, "buildReleaseCopyOptions : Both savedCopyAttributes and defaultOptions are null!");
        }
    }

    private static void applyReleaseJobPassword(ReleaseStoredJobRequest releaseRequest,
            StoredJobAttributes storedJobAttributes) {
        JobCredentialsAttributes credentials = storedJobAttributes.getJobCredentialsAttributes();
        if (credentials != null
                && credentials.getStoreJobPasswordType() != null
                && credentials.getStoreJobPasswordType() != JobCredentialsAttributes.PasswordType.NONE) {
            String password = credentials.getStoreJobPassword();
            releaseRequest.setJobPassword(password != null ? password : "");
            SLog.d(TAG, "applyReleaseJobPassword : jobPassword=[" + (password != null ? "set" : "empty") + "]");
        } else {
            SLog.d(TAG, "applyReleaseJobPassword : No password required");
        }
    }

    /**
     * Fetches individual stored job resource (GET .../storedJobs/{jobId})
     * to obtain full field details not available in the collection enumerate response.
     */
    private static StoredJob fetchStoredJobDetails(
            IDeviceCopyJobService copyJobService,
            String packageName,
            StoredJob summaryJob) {
        if (summaryJob == null || summaryJob.getJobId() == null || copyJobService == null) {
            return summaryJob;
        }
        String jobId = summaryJob.getJobId().toString();
        try {
            StoredJob detailedJob = copyJobService.getStoredJob(packageName, jobId);
            if (detailedJob != null) {
                SLog.d(TAG, "fetchStoredJobDetails : jobId=" + jobId + " fetched successfully");
                return detailedJob;
            }
            SLog.d(TAG, "fetchStoredJobDetails : jobId=" + jobId + " returned null, using summary");
        } catch (Exception e) {
            SLog.e(TAG, "fetchStoredJobDetails : Failed for jobId=" + jobId + ", using summary", e);
        }
        return summaryJob;
    }

    private static StoredJobInfo convertStoredJob(StoredJob e2Job, CopyAttributes savedAttributes) {
        if (e2Job == null) {
            return null;
        }
        String jobId = e2Job.getJobId() != null ? e2Job.getJobId().toString() : "";

        // Log raw E2 fields for diagnosis
        SLog.d(TAG, "convertStoredJob E2 : jobId=" + jobId
                + LOG_TOTAL_PAGES + (e2Job.getTotalPages() != null && e2Job.getTotalPages().getValue() != null
                        ? e2Job.getTotalPages().getValue() : "null"));

        String folderName = nullToEmpty(e2Job.getFolderName());
        String jobName = nullToEmpty(e2Job.getJobName());
        String userName = nullToEmpty(e2Job.getJobUserName());
        String timestamp = nullToEmpty(e2Job.getJobTimestamp());
        JobCredentialsAttributes.PasswordType passwordType = convertPasswordType(e2Job.getJobPasswordType());
        int totalPages = extractTotalPages(e2Job);

        // E2 StoredJob does not return colorMode, copies, scanSize, duplex;
        // use defaults and let saved CopyAttributes override
        int copies = 1;
        CopyAttributes.ColorMode colorMode = CopyAttributes.ColorMode.DEFAULT;
        CopyAttributes.ScanSize scanSize = CopyAttributes.ScanSize.DEFAULT;
        CopyAttributes.Duplex duplex = CopyAttributes.Duplex.DEFAULT;

        if (savedAttributes != null) {
            SLog.d(TAG, "convertStoredJob : Applying saved attributes from PreferenceStorage");
            CopyAttributesReader reader = new CopyAttributesReader(savedAttributes);
            colorMode = resolveSavedColorMode(colorMode, reader);
            scanSize = resolveSavedScanSize(scanSize, reader);
            duplex = resolveSavedDuplex(duplex, reader);
            copies = resolveSavedCopies(copies, reader);
        }

        // Log converted Workpath values
        SLog.d(TAG, "convertStoredJob WP : jobId=" + jobId
                + ", [colorMode=" + colorMode
                + ", scanSize=" + scanSize
                + ", duplex=" + duplex
                + LOG_COPIES + copies
                + LOG_TOTAL_PAGES + totalPages + "]");

        return new StoredJobInfo(
                jobId, folderName, jobName, userName, passwordType, timestamp,
                copies, colorMode, scanSize, duplex, totalPages);
    }

    private static String nullToEmpty(String value) {
        return value != null ? value : "";
    }

    private static int extractTotalPages(StoredJob e2Job) {
        if (e2Job.getTotalPages() != null && e2Job.getTotalPages().getValue() != null) {
            return e2Job.getTotalPages().getValue().intValue();
        }
        return 0;
    }

    private static CopyAttributes.ColorMode resolveSavedColorMode(CopyAttributes.ColorMode e2Value, CopyAttributesReader reader) {
        if (reader.getColorMode() != null && reader.getColorMode() != CopyAttributes.ColorMode.DEFAULT) {
            return reader.getColorMode();
        }
        return e2Value;
    }

    private static CopyAttributes.ScanSize resolveSavedScanSize(CopyAttributes.ScanSize e2Value, CopyAttributesReader reader) {
        if (reader.getScanSize() != null && reader.getScanSize() != CopyAttributes.ScanSize.DEFAULT) {
            return reader.getScanSize();
        }
        return e2Value;
    }

    private static CopyAttributes.Duplex resolveSavedDuplex(CopyAttributes.Duplex e2Value, CopyAttributesReader reader) {
        if (reader.getPrintDuplex() != null) {
            return reader.getPrintDuplex();
        }
        return e2Value;
    }

    private static int resolveSavedCopies(int e2Value, CopyAttributesReader reader) {
        if (reader.getCopies() > 0) {
            return reader.getCopies();
        }
        return e2Value;
    }

    private static JobCredentialsAttributes.PasswordType convertPasswordType(
            StoredJobPasswordType e2PasswordType) {
        if (e2PasswordType == null) {
            return JobCredentialsAttributes.PasswordType.NONE;
        }
        if (StoredJobPasswordType.SjptNumericPIN.equals(e2PasswordType)) {
            return JobCredentialsAttributes.PasswordType.NUMERIC;
        } else if (StoredJobPasswordType.SjptAlphanumericPIN.equals(e2PasswordType)) {
            return JobCredentialsAttributes.PasswordType.ALPHA_NUMERIC;
        }
        return JobCredentialsAttributes.PasswordType.NONE;
    }

    // ==================================================================
    //         Release Stored Copy Job
    // ==================================================================

    /**
     * Builds a CopyJobTicket for the Release operation using the saved CopyAttributes.
     * Reuses the same CopyTypeMapping conversion logic as standard copy.
     *
     * @param savedCopyAttributes The original CopyAttributes saved during Create
     * @param releaseCopies       The copies override from StoredJobAttributes (release request)
     * @param defaultOptions      E2 DefaultOptions for fallback values
     * @return CopyJobTicket for the E2 release API call
     */
    public static CopyJobTicket getCopyJobTicketForRelease(
            CopyAttributes savedCopyAttributes,
            int releaseCopies,
            DefaultOptions defaultOptions) throws IOException {
        SLog.d(TAG, "getCopyJobTicketForRelease : releaseCopies=" + releaseCopies);
        CopyAttributesReader reader = new CopyAttributesReader(savedCopyAttributes);
        CopyJobTicket ticket = getCopyJobTicket(reader, defaultOptions);

        // Override copies from release request if specified (> 0)
        if (releaseCopies > 0 && ticket.getCopyOptions() != null) {
            ticket.getCopyOptions().setCopies(
                    CopyTypeMapping.copies.convertWtoE(releaseCopies, defaultOptions.getCopies()));
            SLog.d(TAG, "getCopyJobTicketForRelease : Overrode copies to " + releaseCopies);
        }
        SLog.d(TAG, "getCopyJobTicketForRelease : CopyOptions=" + (ticket.getCopyOptions() != null ? "built" : "null"));
        return ticket;
    }

    /**
     * Converts DefaultOptions to CopyOptions using JSON serialization.
     * Used as a fallback when saved CopyAttributes are not available for release.
     */
    public static com.hp.ext.service.copy.CopyOptions convertDefaultOptionsToCopyOptions(
            DefaultOptions defaultOptions) throws IOException {
        SLog.d(TAG, "convertDefaultOptionsToCopyOptions : Converting DefaultOptions to CopyOptions via JSON");
        ObjectNode defaultJson = new ObjectMapper().convertValue(defaultOptions, ObjectNode.class);
        defaultJson.remove("$opMeta");
        defaultJson.remove("links");
        com.hp.ext.service.copy.CopyOptions result = new ObjectMapper().readValue(defaultJson.toString(), com.hp.ext.service.copy.CopyOptions.class);
        SLog.d(TAG, "convertDefaultOptionsToCopyOptions : Conversion successful");
        return result;
    }

    // ==================================================================
    //         Private Methods
    // ==================================================================
    static CopyJobTicket getCopyJobTicket(final CopyAttributesReader copyAttributes,
                                                  final DefaultOptions defaultCopyOptions) throws IOException {
        com.hp.ext.service.copy.CopyOptions copyOptions = convertDefaultOptionsToCopyOptions(defaultCopyOptions);

        CopyJobTicket copyJobTicket = new com.hp.ext.service.copy.CopyJobTicket();
        if (copyAttributes == null) {
            return copyJobTicket;
        }

        // Not yet supported
        /*
        copyOptions.storeJobName = !TextUtils.isEmpty(copyAttributes.getStoreJobName()) ?
                copyAttributes.getStoreJobName() : getDateFileName(); // if empty default is timestamp

        copyOptions.storeJobFolderName = !TextUtils.isEmpty(copyAttributes.getStoreJobFolderName()) ?
                copyAttributes.getStoreJobFolderName() : ""; // if empty default is "" (Shown as "Untitled" in Omni
                Print)

        if (copyAttributes.getStoredJobCredentialsAttributes() != null) {
            copyOptions.storeJobPasswordType = LinkToOXPdConverter.convertJobPasswordType(
                    copyAttributes.getStoredJobCredentialsAttributes().getStoreJobPasswordType(), copyOptions
                    .storeJobPasswordType);
            copyOptions.storeJobPassword = copyAttributes.getStoredJobCredentialsAttributes().getStoreJobPassword();
        }

        copyOptions.storeJobDeleteOnPowerCycle = LinkToOXPdConverter.convertRetentionMode(
                copyAttributes.getStoredJobRetentionModeOnPowerCycle(), copyOptions.storeJobDeleteOnPowerCycle);
        copyOptions.storeJobDeleteOnRelease = LinkToOXPdConverter.convertRetentionMode(
                copyAttributes.getStoredJobRetentionModeOnRelease(), copyOptions.storeJobDeleteOnRelease);
*/

        copyOptions.setCopies(CopyTypeMapping.copies.convertWtoE(
                copyAttributes.getCopies(), defaultCopyOptions.getCopies()));
        copyOptions.setColorMode(CopyTypeMapping.colorMode.convertWtoE(copyAttributes.getColorMode(),
                defaultCopyOptions.getColorMode()));
        copyOptions.setContentOrientation(CopyTypeMapping.contentOrientation.convertWtoE(copyAttributes.getOrientation(),
                defaultCopyOptions.getContentOrientation()));
        copyOptions.setOutputMediaSize(CopyTypeMapping.outputMediaSize.convertWtoE(copyAttributes.getPrintSize(),
                defaultCopyOptions.getOutputMediaSize()));
        copyOptions.setOutputMediaSource(CopyTypeMapping.outputMediaSource.convertWtoE(copyAttributes.getPaperSource(),
                defaultCopyOptions.getOutputMediaSource()));
        copyOptions.setScaleSelection(CopyTypeMapping.scaleSelection.convertWtoE(copyAttributes.getScaleMode(),
                defaultCopyOptions.getScaleSelection()));

        Pair<PlexMode, BindingFormat> plexModePair =
                CopyTypeMapping.originalPlexMode.convertWtoE(copyAttributes.getScanDuplex(),
                        new Pair<>(defaultCopyOptions.getOriginalPlexMode(),
                                defaultCopyOptions.getMediaBindingFormat()));
        copyOptions.setOriginalPlexMode(plexModePair.first);
        copyOptions.setOriginalMediaSize(CopyTypeMapping.originalMediaSize.convertWtoE(copyAttributes.getScanSize(),
                defaultCopyOptions.getOriginalMediaSize()));
//        copyOptions.originalMediaSize = LinkToOXPdConverter.convertScanSize(copyAttributes.getScanSize(), copyOptions
//        .originalMediaSize);
//        if (copyOptions.originalMediaSize == MediaSizeId.Custom) {
//            if (copyAttributes.getScanCustomWidth() != null) {
//                copyOptions.originalCustomSizeX = copyAttributes.getScanCustomWidth();
//            }
//
//            if (copyAttributes.getScanCustomLength() != null) {
//                copyOptions.originalCustomSizeY = copyAttributes.getScanCustomLength();
//            }
//        }

        copyOptions.setOriginalMediaSource(CopyTypeMapping.originalMediaSource.convertWtoE(copyAttributes.getScanSource(),
                defaultCopyOptions.getOriginalMediaSource()));
        copyOptions.setImagePreviewMode(CopyTypeMapping.imagePreviewMode.convertWtoE(copyAttributes.getCopyPreview(),
                defaultCopyOptions.getImagePreviewMode()));
        copyOptions.setOutputDuplexBinding(CopyTypeMapping.outputDuplexBinding.convertWtoE(copyAttributes.getPrintDuplex(),
                defaultCopyOptions.getOutputDuplexBinding()));

//        copyOptions.outputMediaSize = LinkToOXPdConverter.convertPrintSize(copyAttributes.getPrintSize(), copyOptions
//        .outputMediaSize);
//        if (copyOptions.outputMediaSize == MediaSizeId.Custom) {
//            if (copyAttributes.getPrintCustomWidth() != null) {
//                copyOptions.outputCustomSizeX = copyAttributes.getPrintCustomWidth();
//            }
//
//            if (copyAttributes.getPrintCustomLength() != null) {
//                copyOptions.outputCustomSizeY = copyAttributes.getPrintCustomLength();
//            }
//        }

        copyOptions.setCollationType(CopyTypeMapping.collationType.convertWtoE(copyAttributes.getCollateMode(),
                defaultCopyOptions.getCollationType()));
        copyOptions.setOutputMediaType(CopyTypeMapping.outputMediaType.convertWtoE(copyAttributes.getPaperType(),
                defaultCopyOptions.getOutputMediaType()));
        copyOptions.setContentType(CopyTypeMapping.contentType.convertWtoE(copyAttributes.getTextGraphicsOptimization(),
                defaultCopyOptions.getContentType()));
        copyOptions.setPagesPerSheet(CopyTypeMapping.pagesPerSheet.convertWtoE(copyAttributes.getNumberUpMode(),
                defaultCopyOptions.getPagesPerSheet()));

        // SDK 1.5 Copy
        copyOptions.setOutputMediaDestination(CopyTypeMapping.outputMediaDestination.convertWtoE(copyAttributes.getOutputBin(),
                defaultCopyOptions.getOutputMediaDestination()));
        copyOptions.setScanProgressMode(CopyTypeMapping.scanProgressMode.convertWtoE(copyAttributes.getProgressDialogMode(),
                defaultCopyOptions.getScanProgressMode()));
        copyOptions.setScanCaptureMode(CopyTypeMapping.scanCaptureMode.convertWtoE(copyAttributes.getCaptureMode(),
                defaultCopyOptions.getScanCaptureMode()));

        // SDK 1.6
        // Not yet supported
        /*

        copyOptions.backgroundCleanup = LinkToOXPdConverter.convertBackgroundCleanup(copyAttributes
        .getBackgroundCleanup(),
        copyOptions.backgroundCleanup);
        copyOptions.contrastAdjustment = LinkToOXPdConverter.convertContrastAdjustment(copyAttributes
        .getContrastAdjustment(),
        copyOptions.contrastAdjustment);
        copyOptions.darknessAdjustment = LinkToOXPdConverter.convertDarknessAdjustment(copyAttributes
        .getDarknessAdjustment(),
        copyOptions.darknessAdjustment);
        copyOptions.outputSides = LinkToOXPdConverter.convertDuplex(copyAttributes.getPrintDuplex(), copyOptions
        .outputSides);
        copyOptions.reduceEnlargeMarginsIncluded = LinkToOXPdConverter.convertScaleMode(copyAttributes.getScaleMode()
        , copyOptions
        .reduceEnlargeMarginsIncluded);
        if (copyAttributes.getScaleMode() == CopyAttributes.ScaleMode.MANUAL) {
            copyOptions.reduceEnlargePercent = copyAttributes.getScalePercent();
        }

        copyOptions.originalSides = LinkToOXPdConverter.convertDuplex(copyAttributes.getScanDuplex(), copyOptions
        .originalSides);
        copyOptions.outputMediaTray = LinkToOXPdConverter.convertPaperSource(copyAttributes.getPaperSource(),
        copyOptions
        .outputMediaTray);
        copyOptions.jobAssembly = LinkToOXPdConverter.convertJobAssemblyMode(copyAttributes.getJobAssemblyMode(),
        copyOptions
        .jobAssembly);
        copyOptions.jobExecutionMode = LinkToOXPdConverter.convertJobExecutionMode(copyAttributes.getJobExecutionMode(),
        copyOptions.jobExecutionMode);
        copyOptions.numberUpDirection = LinkToOXPdConverter.convertNumberUpDirection(copyAttributes
        .getNumberUpDirection(),
        copyOptions.numberUpDirection);


        copyOptions.eraseMarginUnit = LinkToOXPdConverter.convertEraseMarginUnit(copyAttributes.getEraseMarginUnit(),
        copyOptions.eraseMarginUnit);
        if (copyAttributes.getEraseBackBottomMargin() != null) {
            copyOptions.eraseBackBottom = copyAttributes.getEraseBackBottomMargin();
        }
        if (copyAttributes.getEraseBackLeftMargin() != null) {
            copyOptions.eraseBackLeft = copyAttributes.getEraseBackLeftMargin();
        }
        if (copyAttributes.getEraseBackRightMargin() != null) {
            copyOptions.eraseBackRight = copyAttributes.getEraseBackRightMargin();
        }
        if (copyAttributes.getEraseBackTopMargin() != null) {
            copyOptions.eraseBackTop = copyAttributes.getEraseBackTopMargin();
        }
        if (copyAttributes.getEraseFrontBottomMargin() != null) {
            copyOptions.eraseFrontBottom = copyAttributes.getEraseFrontBottomMargin();
        }
        if (copyAttributes.getEraseFrontLeftMargin() != null) {
            copyOptions.eraseFrontLeft = copyAttributes.getEraseFrontLeftMargin();
        }
        if (copyAttributes.getEraseFrontRightMargin() != null) {
            copyOptions.eraseFrontRight = copyAttributes.getEraseFrontRightMargin();
        }
        if (copyAttributes.getEraseFrontTopMargin() != null) {
            copyOptions.eraseFrontTop = copyAttributes.getEraseFrontTopMargin();
        }



        copyOptions.imageShiftReduceToFit = LinkToOXPdConverter.convertImageShiftReduceToFit(copyAttributes
        .getImageShiftReduceToFit(), copyOptions
        .imageShiftReduceToFit);
        copyOptions.imageShiftUnits = LinkToOXPdConverter.convertImageShiftUnits(copyAttributes.getImageShiftUnits(),
        copyOptions.imageShiftUnits);
        if (copyAttributes.getImageShiftXFront() != null) {
            copyOptions.imageShiftXFront = copyAttributes.getImageShiftXFront();
        }
        if (copyAttributes.getImageShiftYFront() != null) {
            copyOptions.imageShiftYFront = copyAttributes.getImageShiftYFront();
        }
        if (copyAttributes.getImageShiftXBack() != null) {
            copyOptions.imageShiftXBack = copyAttributes.getImageShiftXBack();
        }
        if (copyAttributes.getImageShiftYBack() != null) {
            copyOptions.imageShiftYBack = copyAttributes.getImageShiftYBack();
        }
        copyOptions.bookletBordersEachPage = LinkToOXPdConverter.convertBookletBordersEachPage(copyAttributes
        .getBookletBordersEachPage(), copyOptions
        .bookletBordersEachPage);
        copyOptions.bookletFinishingOption = LinkToOXPdConverter.convertBookletFinishingOption(copyAttributes
        .getBookletFinishingOption(), copyOptions
        .bookletFinishingOption);
        copyOptions.bookletFormat = LinkToOXPdConverter.convertBookletFormat(copyAttributes.getBookletFormat(),
        copyOptions
        .bookletFormat);
        //SDK 1.6 Copy
        copyOptions.stapleOption = LinkToOXPdConverter.convertStapleOption(copyAttributes.getStapleOption(), copyOptions
        .stapleOption);
        copyOptions.punchMode = LinkToOXPdConverter.convertPunchMode(copyAttributes.getPunchMode(), copyOptions
        .punchMode);
        copyOptions.foldMode = LinkToOXPdConverter.convertFoldMode(copyAttributes.getFoldMode(),copyOptions.foldMode);

        // SDK 1.6
        copyOptions.stampOptionMap = LinkToOXPdConverter.convertStampOptionMap(copyAttributes.getStampOptionMap());
        // SDK 1.6



        // if empty default is timestamp

        copyOptions.watermarkDarkness = copyAttributes.getWatermarkDarkness();
        copyOptions.watermarkText = !TextUtils.isEmpty(copyAttributes.getWatermarkText()) ?
                copyAttributes.getWatermarkText() : "None"; // if empty default is timestamp
        copyOptions.watermarkRotate45 = LinkToOXPdConverter.convertWatermarkRotate45(copyAttributes
        .getWatermarkRotation45(),
        copyOptions.watermarkRotate45);
        copyOptions.watermarkType = LinkToOXPdConverter.convertWatermarkType(copyAttributes.getWatermarkType(),
        copyOptions
        .watermarkType);
        copyOptions.watermarkTextSize = 40;//copyAttributes.getWatermarkTextSize();
        copyOptions.watermarkTransparency = copyAttributes.getWatermarkTransparency();
        copyOptions.watermarkBackgroundColor = !TextUtils.isEmpty(copyAttributes.getWatermarkBackgroundColor()) ?
                copyAttributes.getWatermarkBackgroundColor() : "";
        copyOptions.watermarkFont = !TextUtils.isEmpty(copyAttributes.getWatermarkFont()) ?
                copyAttributes.getWatermarkFont() : "";
        copyOptions.watermarkTextColor = !TextUtils.isEmpty(copyAttributes.getWatermarkTextColor()) ?
                copyAttributes.getWatermarkTextColor() : "";
        copyOptions.watermarkOnlyFirstPage = LinkToOXPdConverter.convertWatermarkOnlyFirstPage(copyAttributes
        .getWatermarkOnlyFirstPage(), copyOptions
        .watermarkOnlyFirstPage);
        copyOptions.watermarkMessageType = LinkToOXPdConverter.convertWatermarkMessageType(copyAttributes
        .getWatermarkMessageType(),copyOptions
        .watermarkMessageType);
        copyOptions.watermarkBackgroundPattern = LinkToOXPdConverter.convertWatermarkBackgroundPattern(copyAttributes
        .getWatermarkBackgroundPattern(),
        copyOptions.watermarkBackgroundPattern);
*/
        copyJobTicket.setCopyOptions(copyOptions);

        return copyJobTicket;
    }

    //Why ?
    private static void manipulateJobInfo(JobInfo mJobInfo, DefaultOptions defaultOptions) {
        if (mJobInfo == null) {
            return;
        }
        if (((CopyJobData) mJobInfo.getJobData()).getDuplex() == CopyAttributes.Duplex.DEFAULT) {
            if (defaultOptions.getOriginalPlexMode() == PlexMode.PmSimplex) {
                ((CopyJobData) mJobInfo.getJobData()).setDuplex(CopyAttributes.Duplex.NONE);
            } else if (defaultOptions.getOriginalPlexMode() == PlexMode.PmDuplex) {
                if (defaultOptions.getMediaBindingFormat() == BindingFormat.BfFlipLeft) {
                    ((CopyJobData) mJobInfo.getJobData()).setDuplex(CopyAttributes.Duplex.BOOK);
                } else {
                    ((CopyJobData) mJobInfo.getJobData()).setDuplex(CopyAttributes.Duplex.FLIP);
                }
            }
        }
    }
}
