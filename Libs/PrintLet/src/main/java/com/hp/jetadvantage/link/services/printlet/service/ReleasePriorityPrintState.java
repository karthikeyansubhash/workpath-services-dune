package com.hp.jetadvantage.link.services.printlet.service;

import android.content.Intent;

import com.hp.jetadvantage.link.api.printer.intent.PrintRequestIntent;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentServiceState;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentServiceStateMachine;

public class ReleasePriorityPrintState extends PriorityPrintState {
    protected ReleasePriorityPrintState() {
        super(ReleasePriorityPrintState.class.getSimpleName());
        TAG = TAG + "/ReleasePriorityPrint";
    }

    @Override
    protected BaseJobIntentServiceState action(BaseJobIntentServiceStateMachine stateMachine) {
        PrintJobIntentServiceStateMachine sm = (PrintJobIntentServiceStateMachine) stateMachine;
        PrintRequestIntent.IntentParams reqParams = getReqParams(stateMachine);
        String tmpUiContextID = sm.getPrintJobService().getUiContextToken(reqParams.getPackageName());
        SLog.i(TAG, "Priority print released : " + tmpUiContextID);
        return null;
    }

    @Override
    protected void onProcess(BaseJobIntentServiceStateMachine stateMachine) {
        setNextState(new PrintJobCompletedState());
    }
}
