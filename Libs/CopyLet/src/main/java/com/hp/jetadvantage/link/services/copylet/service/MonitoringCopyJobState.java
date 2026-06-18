/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.services.copylet.service;

import android.os.Bundle;

import com.hp.ext.service.copy.CopyJobIdentifier;
import com.hp.ext.service.copy.CopyJobNotificationContent;
import com.hp.ext.service.copy.CopyJobStatus;
import com.hp.ext.service.copy.CopyNotification;
import com.hp.ext.types.job.JobActivityEvent;
import com.hp.jetadvantage.link.api.ILetObserver;
import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.api.job.CopyJobData;
import com.hp.jetadvantage.link.api.job.CopyJobState;
import com.hp.jetadvantage.link.api.job.JobInfo;
import com.hp.jetadvantage.link.api.job.Joblet;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceCopyJobService;
import com.hp.jetadvantage.link.device.services.interfaces.IE2PayloadCallback;
import com.hp.jetadvantage.link.device.services.standard.StandardDeviceCopyJobService;
import com.hp.jetadvantage.link.services.joblet.adapter.TypeConverter;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentServiceStateMachine;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.MonitoringJobState;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * The MonitoringCopyJobState class.
 * This class is responsible for registering a notification callback to receive copy job notifications from the device.
 * When a notification is received, it updates the job status in the job bundle and the job data accordingly.
 * When a job is done, it moves to the next state according to the job done status.
 */
public class MonitoringCopyJobState extends MonitoringJobState {
    protected IDeviceCopyJobService copyJobService;
    IE2PayloadCallback<CopyNotification> copyNotificationCallback;
    CopyJobState.ActivityState currentJobActivityState;

    protected MonitoringCopyJobState(String jobId) {
        super(jobId);
        TAG = TAG + "/Copy";
        copyJobService = new StandardDeviceCopyJobService();
    }

    // ==================================================================
    //      Override Methods from MonitoringJobState
    // ==================================================================

    @Override
    protected void registerNotificationCallback(BaseJobIntentServiceStateMachine stateMachine) {
        copyNotificationCallback = new IE2PayloadCallback<CopyNotification>() {
            @Override
            public void onReceiveNotification(String appPackageId, CopyNotification notification) {
                if (appPackageId == null || notification == null) {
                    return;
                }
                CopyJobNotificationContent copyJobNotificationContent = notification.getJobNotification();
                if (!notification.isJobNotification() || copyJobNotificationContent == null) {
                    return;
                }

                CopyJobIdentifier notifiedCopyJobId = copyJobNotificationContent.getCopyJobId();
                try {
                    if (jobId == null || notifiedCopyJobId == null
                            || !UUID.fromString(jobId).equals(notifiedCopyJobId.getValue())) {
                        SLog.d(TAG, "registerNotificationCallback : jobId mismatch, expected="
                                + jobId + ", notified=" + notifiedCopyJobId);
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

                CopyJobStatus copyJobStatus = copyJobNotificationContent.getCopyJobStatus();
                if (copyJobStatus != null) {
                    updateJobStatus(stateMachine.getJobBundle(), copyJobStatus);
                    processJobDoneStatus(stateMachine, copyJobStatus.getJobDoneStatus());
                }
            }
        };
        copyJobService.registerNotificationCallback(copyNotificationCallback);
    }

    @Override
    protected void unregisterNotificationCallback() {
        copyJobService.unRegisterNotificationCallback();
    }

    // ==================================================================
    //      Protected/Private Instance Methods
    // ==================================================================

    private int convertUnsigned32ToInt(com.hp.ext.types.protocol.Unsigned32 value) {
        if (value == null) {
            return 0;
        }

        try {
            return Math.toIntExact(value.getValue());
        } catch (ArithmeticException e) {
            SLog.e(TAG, "convertUnsigned32ToInt : Exception [" + e.getMessage() + "]");
            return 0;
        }
    }

    protected CopyJobState getCopyJobState(CopyJobState.State state, CopyJobStatus copyJobStatus) {
        CopyJobState jobState = new CopyJobState(state);
        setJobStateFromActivity(copyJobStatus.getScanningActivity(), jobState::setScanningState);
        //Dune E2 does not provide processing state, so setting it to printing state
        setJobStateFromActivity(copyJobStatus.getPrintingActivity(), jobState::setProcessingState);
        setJobStateFromActivity(copyJobStatus.getPrintingActivity(), jobState::setPrintingState);
        setJobStateFromActivity(copyJobStatus.getCancelingActivity(), jobState::setCancelingState);
        setJobStateFromActivity(copyJobStatus.getJobActivity(), this::setJobActivity);
        return jobState;
    }

    private void setJobStateFromActivity(List<JobActivityEvent> jobActivityEventList, Consumer<CopyJobState.ActivityState> stateSetter) {
        if (jobActivityEventList != null && !jobActivityEventList.isEmpty()) {
            //get the last job activity event from JobActivityEvent List and set the state
            stateSetter.accept(TypeConverter.convertJobActivityState(jobActivityEventList.get(jobActivityEventList.size() - 1).getActivity()));
        }
    }

    private void setJobActivity(CopyJobState.ActivityState jobActivityState) {
        currentJobActivityState = jobActivityState;
    }

    protected void updateJobStatus(Bundle jobBundle, CopyJobStatus copyJobStatus) {
        if (jobBundle == null || copyJobStatus == null) {
            return;
        }

        int totalImagesScanned = convertUnsigned32ToInt(copyJobStatus.getTotalImagesScanned());
        int totalSheetsPrinted = convertUnsigned32ToInt(copyJobStatus.getTotalSheetsPrinted());

        jobBundle.putInt(Joblet.Keys.KEY_SCAN_IMAGE_COUNT, totalImagesScanned);
        jobBundle.putInt(Joblet.Keys.KEY_DST_TOTAL, totalSheetsPrinted);

        JobInfo jobInfo = jobBundle.getParcelable(ILetObserver.Keys.KEY_JOB_INFO);
        if (jobInfo == null || jobInfo.getJobData() == null) {
            return;
        }

        CopyJobData copyJobData = jobInfo.getJobData();
        copyJobData.setImagesScanned(totalImagesScanned);
        copyJobData.setSheetsPrinted(totalSheetsPrinted);

        CopyJobState jobState = getCopyJobState(CopyJobState.State.ACTIVE, copyJobStatus);
        copyJobData.setJobState(jobState);

        SLog.d(TAG, "Job[" + jobId + "] JobDoneStatus: " + copyJobStatus.getJobDoneStatus().getValue());
        SLog.d(TAG, "Job[" + jobId + "] JobDoneStatusDetail: " + copyJobStatus.getJobDoneStatusDetail());
        SLog.d(TAG, "Job[" + jobId + "] JobActivityState: " + currentJobActivityState);
    }

}
