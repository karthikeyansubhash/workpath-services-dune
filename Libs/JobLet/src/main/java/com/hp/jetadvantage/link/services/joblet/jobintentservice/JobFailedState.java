/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.services.joblet.jobintentservice;

import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.services.joblet.service.JobletService;

public class JobFailedState extends BaseJobIntentServiceState {
    private final int mResult;
    private final Result.ErrorCode mErrorCode;
    private final String mCause;

    public JobFailedState(final int result, Result.ErrorCode errorCode, String cause) {
        super(JobFailedState.class.getSimpleName());
        TAG = TAG + "/Fail";
        this.mResult = result;
        this.mErrorCode = errorCode;
        this.mCause = cause;
        this.possibleNextStates.add(EndState.class.getSimpleName());
    }

    @Override
    protected void onProcess(BaseJobIntentServiceStateMachine stateMachine) {
        stateMachine.updateFailedJobState();

        Result.pack(stateMachine.getJobBundle(), mResult, mErrorCode, mCause);
        stateMachine.sendLocalJobDataToJobLet(JobletService.Event.TL_EV_JOB_FAILED);
        this.nextState = new EndState();
    }
}
