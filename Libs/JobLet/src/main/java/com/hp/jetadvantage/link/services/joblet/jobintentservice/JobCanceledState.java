/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.services.joblet.jobintentservice;

import com.hp.jetadvantage.link.services.joblet.service.JobletService;

public class JobCanceledState extends BaseJobIntentServiceState {
    public JobCanceledState() {
        super(JobCanceledState.class.getSimpleName());
        TAG = TAG + "/Cancel";
        this.possibleNextStates.add(EndState.class.getSimpleName());
    }

    @Override
    protected void onProcess(BaseJobIntentServiceStateMachine stateMachine) {
        stateMachine.updateCanceledJobState();
        stateMachine.sendLocalJobDataToJobLet(JobletService.Event.TL_EV_JOB_CANCELED);
        this.nextState = new EndState();
    }
}
