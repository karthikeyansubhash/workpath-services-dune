/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.services.joblet.jobintentservice;

import com.hp.jetadvantage.link.common.utils.SLog;

public class EndState extends BaseJobIntentServiceState {

    public EndState() {
        super(EndState.class.getSimpleName());
        TAG = TAG + "/End";
    }

    @Override
    protected void onProcess(BaseJobIntentServiceStateMachine stateMachine) {
        SLog.d(TAG, "Finish processing for rid " + stateMachine.getJobRid());
        stateMachine.releaseWakeLock();

        stateMachine.removeCallbacksAndMessages(null);
        stateMachine.stop();
    }

    @Override
    protected void setNextState(BaseJobIntentServiceState nextState) {
        SLog.e(TAG, "EndState cannot have next state");
    }
}
