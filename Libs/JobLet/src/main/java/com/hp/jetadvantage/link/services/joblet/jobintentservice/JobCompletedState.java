/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.services.joblet.jobintentservice;

import com.hp.jetadvantage.link.services.joblet.service.JobletService;

public abstract class JobCompletedState extends BaseJobIntentServiceState {
    protected JobCompletedState() {
        super(JobCompletedState.class.getSimpleName());
        TAG = TAG + "/Comp";
        this.possibleNextStates.add(EndState.class.getSimpleName());
    }

    @Override
    protected void onProcess(BaseJobIntentServiceStateMachine stateMachine) {
        processCompletedJob(stateMachine);
        stateMachine.updateCompletedJobState();

        stateMachine.sendLocalJobDataToJobLet(JobletService.Event.TL_EV_JOB_COMPLETED);
        this.nextState = new EndState();
    }

    abstract protected void processCompletedJob(BaseJobIntentServiceStateMachine stateMachine);
}
