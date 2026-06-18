/**
 * (C) Copyright 2026 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.services.scanlet.service.postprocessor;

import android.os.Bundle;

import com.hp.ext.types.base.DeleteContent;
import com.hp.jetadvantage.link.api.ILetObserver;
import com.hp.jetadvantage.link.api.job.JobInfo;
import com.hp.jetadvantage.link.api.job.ScanJobData;
import com.hp.jetadvantage.link.api.scanner.ScanAttributesReader;
import com.hp.jetadvantage.link.api.scanner.Scanlet;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.device.services.standard.StandardDeviceScanJobService;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentServiceStateMachine;
import com.hp.jetadvantage.link.services.scanlet.ScanConstants;
import com.hp.jetadvantage.link.services.scanlet.model.Metadata;
import com.hp.jetadvantage.link.services.scanlet.model.MetadataFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Handles post-scan file processing for Local Destination.
 */
public abstract class LocalDestinationProcessor {

    private static final String TAG = Scanlet.TAG + "/LocalScan";

    private final BaseJobIntentServiceStateMachine mStateMachine;

    public LocalDestinationProcessor(BaseJobIntentServiceStateMachine stateMachine) {
        mStateMachine = stateMachine;
    }

    public BaseJobIntentServiceStateMachine getStateMachine() {
        return mStateMachine;
    }

    public ScanAttributesReader getScanAttributesReader() {
        return mStateMachine.getJobAttributesReader(ScanAttributesReader.class);
    }

    /**
     * Processes files for local destination.
     *
     * @throws Exception if processing fails
     */
    public void process(Metadata metadata) throws Exception {
        Bundle jobBundle = getStateMachine().getJobBundle();
        JobInfo jobInfo = (jobBundle != null) ? jobBundle.getParcelable(ILetObserver.Keys.KEY_JOB_INFO) : null;

        if (jobInfo == null || jobInfo.getJobData() == null) {
            throw new Exception("JobInfo or JobData is null");
        }

        File originalFiles = ScanConstants.getOriginalFiles(metadata.getSolutionUuid(), jobInfo.getJobId());
        File outputFiles = createOutputFolder(getOutputPath(jobInfo));

        //usb does not need to grant permission
        List<String> scannedFileList = processFiles(metadata.getMetadataFile(), originalFiles, outputFiles);

        if (!scannedFileList.isEmpty()) {
            ScanJobData scanJobData = jobInfo.getJobData();
            scanJobData.setFileNames(scannedFileList);

            DeleteContent deleted = new StandardDeviceScanJobService().deleteLocalScanJob(getStateMachine().getTargetPackageName(), jobInfo.getJobId());
            SLog.i(TAG, "Processed successfully. deleteLocalScanJob result: " + deleted);
        } else {
            throw new Exception("Failed to process files or no files matched. outputFiles=" + outputFiles.getAbsolutePath());
        }
    }

    protected abstract String getOutputPath(JobInfo jobInfo);

    protected File createOutputFolder(String path) throws IOException {
        File outputFolder = new File(path);
        if (!outputFolder.exists()) {
            outputFolder.mkdirs();
            if (!outputFolder.exists()) {
                throw new IOException("Failed to create output directory: " + outputFolder.getAbsolutePath());
            }
        }
        return outputFolder;
    }

    private List<String> processFiles(MetadataFile metadata, File originalFiles, File outputFolder) throws Exception {
        String extension = metadata.getExtension();
        int scanCount = Integer.parseInt(metadata.getAttachmentCount());

        List<File> scannedFileList = new ArrayList<>();
        File[] files = originalFiles.listFiles();
        String timeStamp = getTimeStamp();
        if (files != null) {
            for (File file : files) {
                if (file.getName().startsWith(ScanConstants.METADATA_PREFIX)) continue;
                if (file.getName().toLowerCase(Locale.US).endsWith(extension.toLowerCase(Locale.US))) {
                    scannedFileList.add(copyScannedFile(file, outputFolder, timeStamp));
                }
            }
        }
        scannedFileList.sort(Comparator.comparing(File::getName));

        if (scannedFileList.size() != scanCount) {
            throw new Exception("File count mismatch. Expected: " + scanCount + ", Found: " + scannedFileList.size());
        }

        return postProcessFiles(scannedFileList);
    }

    private File copyScannedFile(File originalFile, File outputFolder, String timeStamp) throws Exception {
        File newFile = new File(outputFolder.getAbsolutePath(), getFileName(originalFile, timeStamp));
        Files.copy(originalFile.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        return newFile;
    }

    protected  String getTimeStamp() {
        final Date time = Calendar.getInstance().getTime();
        final String pattern = "yyyyMMddHHmmssSS";
        final SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.US);
        return sdf.format(time);
    }

    protected abstract String getFileName(File originalFile, String timeStamp);

    protected abstract List<String> postProcessFiles(List<File> scannedFileList) throws Exception;

}
