/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.services.joblet.jobintentservice;

import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.common.utils.SLog;

public class ReportErrorState extends BaseJobIntentServiceState {
    private final String mCause;
    private final Result.ErrorCode mErrorCode;
    private final int mResult;


    public ReportErrorState(final int result, Result.ErrorCode errorCode, String cause) {
        super(ReportErrorState.class.getSimpleName());
        TAG = TAG + "/Err";
        this.mResult = result;
        this.mErrorCode = errorCode;
        this.mCause = cause;
        this.possibleNextStates.add(EndState.class.getSimpleName());
    }

    @Override
    protected void onProcess(BaseJobIntentServiceStateMachine stateMachine) {
        SLog.d(TAG, "Report ERROR for " + stateMachine.getJobRid());

        stateMachine.getReporterToApp().fail(stateMachine.getContext(), stateMachine.getJobRid(), mResult, mErrorCode, mCause);
        setNextState(new EndState());
    }

    public int getResult() {
        return mResult;
    }

    public Result.ErrorCode getErrorCode() {
        return mErrorCode;
    }
}
