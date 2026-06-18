/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.services.scanlet.service;

import android.os.Bundle;
import android.util.Log;

import com.hp.ext.service.scanJob.ScanJobIdentifier;
import com.hp.ext.service.scanJob.ScanJobNotificationContent;
import com.hp.ext.service.scanJob.ScanJobStatus;
import com.hp.ext.service.scanJob.ScanNotification;
import com.hp.jetadvantage.link.api.ILetObserver;
import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.api.job.JobInfo;
import com.hp.jetadvantage.link.api.job.Joblet;
import com.hp.jetadvantage.link.api.job.ScanJobData;
import com.hp.jetadvantage.link.api.job.ScanJobState;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceScanJobService;
import com.hp.jetadvantage.link.device.services.interfaces.IE2PayloadCallback;
import com.hp.jetadvantage.link.device.services.standard.StandardDeviceScanJobService;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentServiceStateMachine;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.MonitoringJobState;
import com.hp.jetadvantage.link.services.scanlet.adapter.ScanTypeMappingHelper;

import java.util.UUID;

public class MonitoringScanJobState extends MonitoringJobState {
    protected IDeviceScanJobService scanJobService;
    protected IE2PayloadCallback<ScanNotification> scanNotificationCallback;

    protected MonitoringScanJobState(String jobId) {
        super(jobId);
        TAG = TAG + "/Scan";
        scanJobService = new StandardDeviceScanJobService();
    }

    @Override
    protected void registerNotificationCallback(BaseJobIntentServiceStateMachine stateMachine) {
        scanNotificationCallback = new IE2PayloadCallback<ScanNotification>() {
            @Override
            public void onReceiveNotification(String appPackageId, ScanNotification notification) {
                Log.d(TAG, "onReceiveNotification : ENTER - Monitoring JobID[" + jobId + "]");
                if (appPackageId == null || notification == null) {
                    Log.e(TAG, "onReceiveNotification() appPackageId or notification is null (appPackageId=" + appPackageId + ", notification=" + notification + ")");
                    return;
                }
                ScanJobNotificationContent scanJobNotificationContent = notification.getJobNotification();
                if (notification.isJobNotification() && scanJobNotificationContent != null) {
                    ScanJobIdentifier notifiedScanJobId = scanJobNotificationContent.getScanJobId();
                    try {
                        if (jobId == null || notifiedScanJobId == null || !UUID.fromString(jobId).equals(notifiedScanJobId.getValue())) {
                            Log.e(TAG, "onReceiveNotification() jobId is null or not matching, jobId=" + jobId + ", notified ScanJobId: " + notifiedScanJobId);
                            return;
                        }
                    } catch (IllegalArgumentException e) {
                        String cause = "JobId is not a valid UUID, jobId=" + jobId;
                        Log.e(TAG, "onReceiveNotification :  jobId is not a valid UUID, jobId=" + jobId);
                        requestToTransitState(stateMachine, BaseJobIntentServiceStateMachine.MSG_REPORT_FAIL, Result.RESULT_FAIL, Result.ErrorCode.JOB_FAILURE, cause);
                        return;
                    }
                    Log.d(TAG, "onReceiveNotification : Notification JobID[" + jobId + "]");
                    ScanJobStatus scanJobStatus = scanJobNotificationContent.getScanJobStatus();
                    if (scanJobStatus != null) {
                        updateJobStatus(stateMachine.getJobBundle(), scanJobStatus);
                        processJobDoneStatus(stateMachine, scanJobStatus.getJobDoneStatus());
                    }
                }
                Log.d(TAG, "onReceiveNotification : EXIT");
            }
        };
        scanJobService.registerNotificationCallback(scanNotificationCallback);
    }

    @Override
    protected void unregisterNotificationCallback() {
        scanJobService.unRegisterNotificationCallback();
    }

    protected void updateJobStatus(Bundle jobBundle, ScanJobStatus scanJobStatus) {
        if (jobBundle == null || scanJobStatus == null) {
            Log.e(TAG, "updateJobStatus() jobBundle or scanJobStatus is null (" + jobBundle + ", " + scanJobStatus + ")");
            return;
        }
        long totalImagesScanned = scanJobStatus.getTotalImagesScanned() != null ? scanJobStatus.getTotalImagesScanned().getValue() : 0;
        long totalImagesTransmitted = scanJobStatus.getTotalImagesTransmitted() != null ? scanJobStatus.getTotalImagesTransmitted().getValue() : 0;
        long totalImagesProcessed = scanJobStatus.getTotalImagesProcessed() != null ? scanJobStatus.getTotalImagesProcessed().getValue() : 0;

        jobBundle.putInt(Joblet.Keys.KEY_SCAN_IMAGE_COUNT, (int) totalImagesScanned);
        jobBundle.putInt(Joblet.Keys.KEY_DST_TOTAL, (int) totalImagesTransmitted);

        JobInfo jobInfo = jobBundle.getParcelable(ILetObserver.Keys.KEY_JOB_INFO);
        if (jobInfo == null) {
            Log.e(TAG, "updateJobStatus() jobInfo is null");
            return;
        }
        ScanJobData scanJobData = jobInfo.getJobData();
        if (scanJobData == null) {
            Log.e(TAG, "updateJobStatus() scanJobData is null");
            return;
        }
        scanJobData.setImagesScanned((int) totalImagesScanned);
        scanJobData.setImagesProcessed((int) totalImagesProcessed);
        scanJobData.setImagesTransmitted((int) totalImagesTransmitted);
        scanJobData.setJobState(getScanJobState(scanJobStatus));
    }

    /**
     * Converts an E2 ScanJobStatus object to a Workpath ScanJobState object.
     *
     * @param scanJobStatus E2 ScanJobStatus object
     * @return Workpath ScanJobState object
     */
    protected ScanJobState getScanJobState(ScanJobStatus scanJobStatus) {
        ScanJobState jobState = new ScanJobState(ScanJobState.State.PENDING);
        jobState.setScanningState(
                ScanTypeMappingHelper.getJobActivityState(scanJobStatus.getScanningActivity())
        );
        jobState.setProcessingState(
                ScanTypeMappingHelper.getJobActivityState(
                        scanJobStatus.getProcessingActivity(),
                        scanJobStatus.getProcessingRestartCount() != null && scanJobStatus.getProcessingRestartCount().getValue() > 0
                )
        );
        jobState.setTransmittingState(
                ScanTypeMappingHelper.getJobActivityState(
                        scanJobStatus.getTransmittingActivity(),
                        scanJobStatus.getTransmissionRetryCount() != null && scanJobStatus.getTransmissionRetryCount().getValue() > 0
                )
        );
        jobState.setCancelingState(
                ScanTypeMappingHelper.getJobActivityState(scanJobStatus.getCancelingActivity())
        );

        //E2 does not update processing state, manually updates to make it completed when transmission is done
        if (jobState.getTransmittingState() == ScanJobState.ActivityState.COMPLETED) {
            jobState.setProcessingState(ScanJobState.ActivityState.COMPLETED);
        }
        return jobState;
    }
}
