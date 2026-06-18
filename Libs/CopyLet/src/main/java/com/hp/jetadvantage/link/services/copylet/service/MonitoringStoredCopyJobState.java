/**
 * (C) Copyright 2026 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.services.copylet.service;

import android.os.Bundle;

import com.hp.ext.service.scanJob.ScanJobIdentifier;
import com.hp.ext.service.scanJob.ScanJobNotificationContent;
import com.hp.ext.service.scanJob.ScanJobStatus;
import com.hp.ext.service.scanJob.ScanNotification;
import com.hp.ext.types.job.JobActivityEvent;
import com.hp.jetadvantage.link.api.ILetObserver;
import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.api.job.CopyJobData;
import com.hp.jetadvantage.link.api.job.CopyJobState;
import com.hp.jetadvantage.link.api.job.JobInfo;
import com.hp.jetadvantage.link.api.job.Joblet;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceScanJobService;
import com.hp.jetadvantage.link.device.services.interfaces.IE2PayloadCallback;
import com.hp.jetadvantage.link.device.services.standard.StandardDeviceScanJobService;
import com.hp.jetadvantage.link.services.joblet.adapter.TypeConverter;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentServiceStateMachine;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.MonitoringJobState;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * Monitors a stored copy job (which is a ScanJob with JobStorage destination).
 * Receives ScanNotification callbacks and maps them to CopyJobState/CopyJobData
 * for app-level compatibility (the app sees this as a COPY job).
 *
 * Only imagesScanned is reported — no sheetsPrinted since this is scan-only.
 */
public class MonitoringStoredCopyJobState extends MonitoringJobState {
    /* package */ IDeviceScanJobService scanJobService;
    private CopyJobState.ActivityState currentJobActivityState;

    protected MonitoringStoredCopyJobState(String jobId) {
        super(jobId);
        TAG = TAG + "/StoredCopy";
        scanJobService = new StandardDeviceScanJobService();
        SLog.d(TAG, "MonitoringStoredCopyJobState : created for jobId=" + jobId);
    }

    // ==================================================================
    //      Override Methods from MonitoringJobState
    // ==================================================================

    @Override
    protected void registerNotificationCallback(BaseJobIntentServiceStateMachine stateMachine) {
        SLog.d(TAG, "registerNotificationCallback : Registering ScanNotification callback");
        IE2PayloadCallback<ScanNotification> scanNotificationCallback = (appPackageId, notification) -> {
            if (appPackageId == null || notification == null) {
                return;
            }
            ScanJobNotificationContent scanJobNotificationContent = notification.getJobNotification();
            if (!notification.isJobNotification() || scanJobNotificationContent == null) {
                return;
            }

            ScanJobIdentifier notifiedScanJobId = scanJobNotificationContent.getScanJobId();
            try {
                if (jobId == null || notifiedScanJobId == null
                        || !UUID.fromString(jobId).equals(notifiedScanJobId.getValue())) {
                    SLog.d(TAG, "registerNotificationCallback : jobId mismatch, expected="
                            + jobId + ", notified=" + notifiedScanJobId);
                    return;
                }
            } catch (IllegalArgumentException e) {
                String cause = "JobId is not a valid UUID, jobId=" + jobId;
                SLog.e(TAG, "registerNotificationCallback : " + cause);
                requestToTransitState(stateMachine,
                        BaseJobIntentServiceStateMachine.MSG_REPORT_FAIL,
                        Result.RESULT_FAIL, Result.ErrorCode.JOB_FAILURE, cause);
                return;
            }

            ScanJobStatus scanJobStatus = scanJobNotificationContent.getScanJobStatus();
            if (scanJobStatus != null) {
                updateJobStatus(stateMachine.getJobBundle(), scanJobStatus);
                processJobDoneStatus(stateMachine, scanJobStatus.getJobDoneStatus());
            }
        };
        scanJobService.registerNotificationCallback(scanNotificationCallback);
    }

    @Override
    protected void unregisterNotificationCallback() {
        SLog.d(TAG, "unregisterNotificationCallback : Unregistering ScanNotification callback");
        scanJobService.unRegisterNotificationCallback();
    }

    // ==================================================================
    //      Private Instance Methods
    // ==================================================================

    private int convertUnsigned32ToInt(com.hp.ext.types.protocol.Unsigned32 value) {
        if (value == null) {
            return 0;
        }
        try {
            return Math.toIntExact(value.getValue());
        } catch (ArithmeticException e) {
            SLog.d(TAG, "convertUnsigned32ToInt : Exception [" + e.getMessage() + "]");
            return 0;
        }
    }

    private CopyJobState getCopyJobState(CopyJobState.State state, ScanJobStatus scanJobStatus) {
        CopyJobState jobState = new CopyJobState(state);
        setJobStateFromActivity(scanJobStatus.getScanningActivity(), jobState::setScanningState);
        setJobStateFromActivity(scanJobStatus.getProcessingActivity(), jobState::setProcessingState);
        // No printing activity for stored copy — transmitting to storage is the final phase
        setJobStateFromActivity(scanJobStatus.getTransmittingActivity(), jobState::setPrintingState);
        setJobStateFromActivity(scanJobStatus.getCancelingActivity(), jobState::setCancelingState);
        setJobStateFromActivity(scanJobStatus.getJobActivity(), this::setJobActivity);
        return jobState;
    }

    private void setJobStateFromActivity(List<JobActivityEvent> jobActivityEventList,
                                         Consumer<CopyJobState.ActivityState> stateSetter) {
        if (jobActivityEventList != null && !jobActivityEventList.isEmpty()) {
            stateSetter.accept(TypeConverter.convertJobActivityState(
                    jobActivityEventList.get(jobActivityEventList.size() - 1).getActivity()));
        }
    }

    private void setJobActivity(CopyJobState.ActivityState jobActivityState) {
        currentJobActivityState = jobActivityState;
    }

    private void updateJobStatus(Bundle jobBundle, ScanJobStatus scanJobStatus) {
        if (jobBundle == null || scanJobStatus == null) {
            return;
        }

        int totalImagesScanned = convertUnsigned32ToInt(scanJobStatus.getTotalImagesScanned());
        SLog.d(TAG, "updateJobStatus : totalImagesScanned=" + totalImagesScanned);

        jobBundle.putInt(Joblet.Keys.KEY_SCAN_IMAGE_COUNT, totalImagesScanned);
        // No sheetsPrinted for stored copy — scan-only phase
        jobBundle.putInt(Joblet.Keys.KEY_DST_TOTAL, 0);

        JobInfo jobInfo = jobBundle.getParcelable(ILetObserver.Keys.KEY_JOB_INFO);
        if (jobInfo == null || jobInfo.getJobData() == null) {
            return;
        }

        CopyJobData copyJobData = jobInfo.getJobData();
        copyJobData.setImagesScanned(totalImagesScanned);
        // No sheetsPrinted update

        CopyJobState jobState = getCopyJobState(CopyJobState.State.ACTIVE, scanJobStatus);
        copyJobData.setJobState(jobState);

        SLog.d(TAG, "Job[" + jobId + "] JobDoneStatus: " + scanJobStatus.getJobDoneStatus().getValue());
        SLog.d(TAG, "Job[" + jobId + "] JobDoneStatusDetail: " + scanJobStatus.getJobDoneStatusDetail());
        SLog.d(TAG, "Job[" + jobId + "] JobActivityState: " + currentJobActivityState);
    }
}
