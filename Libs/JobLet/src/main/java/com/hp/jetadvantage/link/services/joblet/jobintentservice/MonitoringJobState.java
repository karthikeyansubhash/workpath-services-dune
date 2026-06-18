/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.services.joblet.jobintentservice;

import com.hp.ext.types.job.JobDoneStatus;
import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.api.job.JobletAttributes;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.services.joblet.service.JobletService;

public abstract class MonitoringJobState extends BaseJobIntentServiceState {
    protected final String jobId;

    protected MonitoringJobState(String jobId) {
        super(MonitoringJobState.class.getSimpleName());
        TAG = TAG + "/Mon";
        this.nextState = null;
        this.jobId = jobId;
        this.possibleNextStates.add(JobCanceledState.class.getSimpleName());
        this.possibleNextStates.add(JobCompletedState.class.getSimpleName());
        this.possibleNextStates.add(JobFailedState.class.getSimpleName());
    }

    @Override
    protected void onEnter(BaseJobIntentServiceStateMachine stateMachine) {
        SLog.d(TAG, "onEnter() ENTER - JobId[" + jobId + "]");
        super.onEnter(stateMachine);

        //starting the monitoring process on the JobletService
        JobletAttributes taskAttributes = new JobletAttributes.Builder().setShowUi(false).setExtras(stateMachine.getExtraJobBundle()).build();
        JobletService.startMonitoring(stateMachine.getContext(), jobId, null, taskAttributes);

        //TODO : TL_EV_JOB_PROGRESS is the right event here ?
        stateMachine.sendLocalJobDataToJobLet(JobletService.Event.TL_EV_JOB_PROGRESS);
        SLog.d(TAG, "onEnter() EXIT - JobId[" + jobId + "]");
    }

    @Override
    protected void onExit(BaseJobIntentServiceStateMachine stateMachine) {
        SLog.d(TAG, "onExit() ENTER - JobId[" + jobId + "]");
        super.onExit(stateMachine);
        unregisterNotificationCallback();
        SLog.d(TAG, "onExit() EXIT - JobId[" + jobId + "]");
    }

    @Override
    protected void onProcess(BaseJobIntentServiceStateMachine stateMachine) {
        SLog.d(TAG, "onProcess() ENTER - JobId[" + jobId + "]");
        registerNotificationCallback(stateMachine);
        SLog.d(TAG, "onProcess() EXIT - JobId[" + jobId + "]");
    }

    /**
     * register target job notification callback to monitor job status
     */
    protected abstract void registerNotificationCallback(BaseJobIntentServiceStateMachine stateMachine);

    /**
     * unregister target job notification callback
     */
    protected abstract void unregisterNotificationCallback();

    protected void processJobDoneStatus(BaseJobIntentServiceStateMachine stateMachine, JobDoneStatus jobDoneStatus) {
        SLog.d(TAG, "processJobDoneStatus : ENTER - JobId[" + jobId + "]");
        if (stateMachine == null || jobDoneStatus == null) {
            SLog.e(TAG, "processJobDoneStatus : Invalid argument (stateMachine == null) = " + (stateMachine == null) + ", (jobDoneStatus == null) = " + (jobDoneStatus == null));
            return;
        }

        SLog.d(TAG, "processJobDoneStatus : [" + jobId + "] JobDoneStatus = " + jobDoneStatus.getValue());
        if (JobDoneStatus.JdsCanceled.getValue().equals(jobDoneStatus.getValue())) {
            requestToTransitState(stateMachine, BaseJobIntentServiceStateMachine.MSG_REPORT_CANCELLED, Result.RESULT_FAIL, null, null);
        } else if (JobDoneStatus.JdsFailed.getValue().equals(jobDoneStatus.getValue())) {
            requestToTransitState(stateMachine, BaseJobIntentServiceStateMachine.MSG_REPORT_FAIL, Result.RESULT_FAIL, Result.ErrorCode.JOB_FAILURE, null);
        } else if (JobDoneStatus.JdsPartiallySucceeded.getValue().equals(jobDoneStatus.getValue())) {
            //TODO : revisit later for the JdsPartiallySucceeded case
            requestToTransitState(stateMachine, BaseJobIntentServiceStateMachine.MSG_REPORT_COMPLETED, Result.RESULT_OK, null, null);
        } else if (JobDoneStatus.JdsSucceeded.getValue().equals(jobDoneStatus.getValue())) {
            requestToTransitState(stateMachine, BaseJobIntentServiceStateMachine.MSG_REPORT_COMPLETED, Result.RESULT_OK, null, null);
        } else if (JobDoneStatus.JdsActive.getValue().equals(jobDoneStatus.getValue())) {
            stateMachine.sendLocalJobDataToJobLet(JobletService.Event.TL_EV_JOB_PROGRESS);
        }
        SLog.d(TAG, "processJobDoneStatus : EXIT - JobId[" + jobId + "]");
    }

    protected void requestToTransitState(BaseJobIntentServiceStateMachine handler, final int what, final int result, Result.ErrorCode errorCode, String cause) {
        SLog.d(TAG, "requestToTransitState : ENTER - JobId[" + jobId + "] MSG:" + what + ", result:" + result);
        if (handler != null) {
            int errorCodeOrdinal = errorCode != null ? errorCode.ordinal() : -1;

            SLog.d(TAG, "requestToTransitState : [" + jobId + "] what = " + what + ", result = " + result + ", errorCodeOrdinal = " + errorCodeOrdinal + ", cause = " + cause);
            if (!handler.sendMessage(handler.obtainMessage(what, result, errorCodeOrdinal, cause))) {
                SLog.w(TAG, "Failed to send message, try just handle!");
                handler.handleMessage(handler.obtainMessage(what, result, errorCodeOrdinal, cause));
            }
        }
        SLog.d(TAG, "requestToTransitState : ENTER - JobId[" + jobId + "]");
    }
}
