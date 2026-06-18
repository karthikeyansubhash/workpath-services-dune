package com.hp.jetadvantage.link.services.printlet.service;

import com.hp.jetadvantage.link.api.printer.PrintAttributesReader;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentServiceStateMachine;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.JobCompletedState;

public class PrintJobCompletedState extends JobCompletedState {
    protected PrintJobCompletedState() {
        super();
        TAG = TAG + "/Print";
    }

    @Override
    protected void processCompletedJob(BaseJobIntentServiceStateMachine stateMachine) {
        // Todo: should be revisited.
        PrintAttributesReader printAttributes = stateMachine.getJobAttributesReader(PrintAttributesReader.class);
    }
}
