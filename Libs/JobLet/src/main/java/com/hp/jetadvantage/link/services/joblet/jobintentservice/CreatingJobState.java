/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.services.joblet.jobintentservice;

import android.content.Intent;

import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.common.utils.SLog;

public abstract class CreatingJobState extends BaseJobIntentServiceState {
    protected final Intent jobIntent;

    protected CreatingJobState(Intent jobIntent) {
        super(CreatingJobState.class.getSimpleName());
        TAG = TAG + "/Cre";
        this.jobIntent = jobIntent;
        this.possibleNextStates.add(MonitoringJobState.class.getSimpleName());
        this.possibleNextStates.add(ReportErrorState.class.getSimpleName());
        this.possibleNextStates.add(EndState.class.getSimpleName());
    }

    @Override
    protected void onEnter(BaseJobIntentServiceStateMachine stateMachine) {
        super.onEnter(stateMachine);
        stateMachine.acquireWakeLock();
    }

    @Override
    protected void onProcess(BaseJobIntentServiceStateMachine stateMachine) {
        BaseJobIntentServiceState nextState;
        if (jobIntent == null) {
            SLog.e(TAG, "jobIntent is null, move to EndState");
            // finish service (we don't know target package - so nowhere to report
            nextState = new EndState();
        } else {
            nextState = initializeJob(jobIntent, stateMachine);
        }

        if (nextState == null) {
            nextState = new ReportErrorState(Result.RESULT_FAIL, Result.ErrorCode.UNKNOWN, "Unknown error");
            SLog.e(TAG, "Unexpected : createJob returned null");
        } else if (!isValidTransition(nextState)) {
            nextState = new ReportErrorState(Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, "Invalid state transition");
            SLog.e(TAG, "Unexpected : createJob returned invalid next state");
        }
        setNextState(nextState);
    }

    /**
     * Initialize and create a job with the given intent
     *
     * @param intent
     * @param stateMachine
     * @return the next state to transition to after creating the job
     * return MonitoringJobState if the job is created successfully
     * otherwise return ReportErrorState with error code and cause
     * if null is returned, the state machine will transition to the ReportErrorState
     */
    abstract protected BaseJobIntentServiceState initializeJob(final Intent intent, BaseJobIntentServiceStateMachine stateMachine);
}
