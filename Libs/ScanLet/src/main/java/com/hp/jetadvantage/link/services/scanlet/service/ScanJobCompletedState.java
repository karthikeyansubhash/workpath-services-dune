/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.services.scanlet.service;

import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.core.util.Pair;

import com.hp.ext.service.scanJob.ScanJob;
import com.hp.ext.types.imaging.BindingFormat;
import com.hp.ext.types.imaging.PlexMode;
import com.hp.ext.types.media.MediaSizeId;
import com.hp.jetadvantage.link.api.ILetObserver;
import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.api.job.JobInfo;
import com.hp.jetadvantage.link.api.job.ScanJobData;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes;
import com.hp.jetadvantage.link.api.scanner.ScanAttributesReader;
import com.hp.jetadvantage.link.common.utils.JsonParser;
import com.hp.jetadvantage.link.device.services.converter.ITypeConverter;
import com.hp.jetadvantage.link.device.services.standard.StandardDeviceScanJobService;
import com.hp.jetadvantage.link.device.services.standard.common.PackageManagerHelper;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentServiceStateMachine;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.EndState;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.JobCompletedState;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.JobFailedState;
import com.hp.jetadvantage.link.services.joblet.service.JobletService;
import com.hp.jetadvantage.link.services.scanlet.ScanConstants;
import com.hp.jetadvantage.link.services.scanlet.adapter.ScanTypeMapping;
import com.hp.jetadvantage.link.services.scanlet.model.Metadata;
import com.hp.jetadvantage.link.services.scanlet.model.MetadataFile;
import com.hp.jetadvantage.link.services.scanlet.service.postprocessor.ScanToEmailProcessor;
import com.hp.jetadvantage.link.services.scanlet.service.postprocessor.ScanToMeProcessor;
import com.hp.jetadvantage.link.services.scanlet.service.postprocessor.ScanToUsbProcessor;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class ScanJobCompletedState extends JobCompletedState {

    protected ScanJobCompletedState() {
        super();
        TAG = TAG + "/ScanJobCompleted";
        this.possibleNextStates.add(JobFailedState.class.getSimpleName());
    }

    @Override
    protected void onProcess(BaseJobIntentServiceStateMachine stateMachine) {
        processCompletedJob(stateMachine);
    }

    @Override
    protected void processCompletedJob(BaseJobIntentServiceStateMachine stateMachine) {
        try {
            updateJobInfoFromScanJob(stateMachine);
            ScanAttributesReader scanAttributes = stateMachine.getJobAttributesReader(ScanAttributesReader.class);
            if (ScanConstants.LOCAL_FOLDER_DESTINATIONS.contains(scanAttributes.getDestination())) {
                Metadata metadata = parseMetadata(stateMachine);
                if (scanAttributes.getDestination() == ScanAttributes.Destination.ME) {
                    ScanToMeProcessor meProcessor = new ScanToMeProcessor(stateMachine);
                    meProcessor.process(metadata);
                } else if (scanAttributes.getDestination() == ScanAttributes.Destination.USB) {
                    ScanToUsbProcessor usbProcessor = new ScanToUsbProcessor(stateMachine);
                    usbProcessor.process(metadata);
                } else if (scanAttributes.getDestination() == ScanAttributes.Destination.EMAIL) {
                    ScanToEmailProcessor emailProcessor = new ScanToEmailProcessor(stateMachine);
                    emailProcessor.process(metadata);
                }
            }
            processUpdateCompleteJobState(stateMachine);
        } catch (Exception e) {
            this.nextState = new JobFailedState(Result.RESULT_FAIL, Result.ErrorCode.JOB_FAILURE, e.getMessage());
        }
    }

    private void processUpdateCompleteJobState(BaseJobIntentServiceStateMachine stateMachine) {
        ScanJobIntentServiceStateMachine scanSM = (ScanJobIntentServiceStateMachine) stateMachine;
        scanSM.updateCompletedJobState();
        stateMachine.sendLocalJobDataToJobLet(JobletService.Event.TL_EV_JOB_COMPLETED);
        this.nextState = new EndState();
    }

    private Metadata parseMetadata(BaseJobIntentServiceStateMachine stateMachine) throws Exception {
        JobInfo jobInfo = getJobInfo(stateMachine);
        String solutionUuid = getSolutionUuid(stateMachine);
        if (TextUtils.isEmpty(solutionUuid)) {
            throw new Exception("Solution UUID is null or empty");
        }

        File metadataFile = findMetadataFile(solutionUuid, jobInfo.getJobId());
        String content = new String(Files.readAllBytes(metadataFile.toPath()), StandardCharsets.UTF_8);
        if (TextUtils.isEmpty(content)) throw new IOException("Metadata content is null or empty");
        Metadata metadata = JsonParser.getInstance().fromJson(content, Metadata.class);
        metadata.setSolutionUuid(solutionUuid);

        updateJobInfoFromMetadata(jobInfo, metadata.getMetadataFile());
        return metadata;
    }

    private void updateJobInfoFromMetadata(JobInfo jobInfo, MetadataFile metadata) throws IllegalArgumentException {
        if (metadata != null) {
            // TODO DUNE-291837: Scan metadata: jobName handling
            // TODO: We need to add a function to properly retrieve and return the job name from metadata.
            if (!TextUtils.isEmpty(metadata.getUsername())) {
                jobInfo.setOwner(metadata.getUsername());
            } else {
                jobInfo.setOwner("guest");
            }
        }
    }

    private void updateJobInfoFromScanJob(BaseJobIntentServiceStateMachine stateMachine) throws Exception {
        String packageName = stateMachine.getTargetPackageName();
        JobInfo jobInfo = getJobInfo(stateMachine);
        ScanJob scanJob = new StandardDeviceScanJobService().getScanJob(packageName, stateMachine.getJobId());

        if (scanJob == null) {
            throw new Exception("ScanJob is null");
        }

        PlexMode plexMode = scanJob.getScanTicket().getScanOptions().getPlexMode();
        BindingFormat bindingFormat = scanJob.getScanTicket().getScanOptions().getMediaBindingFormat();

        MediaSizeId mediaSizeId = scanJob.getScanTicket().getScanOptions().getMediaSize();

        ITypeConverter<Pair<PlexMode, BindingFormat>, ScanAttributes.Duplex> duplexConverter =
                ScanTypeMapping.duplex.getConverter();
        ScanAttributes.Duplex duplex = duplexConverter.convertEtoW(new Pair<>(plexMode, bindingFormat));

        ITypeConverter<MediaSizeId, ScanAttributes.ScanSize> scanSizeConverter = ScanTypeMapping.mediaSize.getConverter();
        ScanAttributes.ScanSize scanSize = scanSizeConverter.convertEtoW(mediaSizeId);

        ScanJobData scanJobData = jobInfo.getJobData();
        scanJobData.setScanSize(scanSize);
        scanJobData.setDuplex(duplex);
    }

    private File findMetadataFile(String solutionUuid, String jobId) throws Resources.NotFoundException {
        File[] originalFiles = ScanConstants.getOriginalFiles(solutionUuid, jobId).listFiles();
        if (originalFiles != null) {
            for (File file : originalFiles) {
                if (file.getName().startsWith(ScanConstants.METADATA_PREFIX)) {
                    return file;
                }
            }
        }
        throw new Resources.NotFoundException("Metadata file not found");
    }

    private String getSolutionUuid(BaseJobIntentServiceStateMachine stateMachine) {
        PackageManagerHelper pmHelper = new PackageManagerHelper();
        return pmHelper.getSolutionId(stateMachine.getContext(), stateMachine.getTargetPackageName());
    }

    private JobInfo getJobInfo(BaseJobIntentServiceStateMachine stateMachine) throws Exception {
        Bundle jobBundle = stateMachine.getJobBundle();
        if (jobBundle == null) {
            throw new Exception("JobBundle is null");
        }

        JobInfo jobInfo = jobBundle.getParcelable(ILetObserver.Keys.KEY_JOB_INFO);
        if (jobInfo == null || jobInfo.getJobData() == null) {
            throw new Exception("JobInfo or JobData is null");
        }

        return jobInfo;
    }
}
