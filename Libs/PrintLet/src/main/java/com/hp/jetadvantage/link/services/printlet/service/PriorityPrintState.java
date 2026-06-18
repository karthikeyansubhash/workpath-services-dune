package com.hp.jetadvantage.link.services.printlet.service;

import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.api.printer.intent.PrintRequestIntent;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentServiceState;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentServiceStateMachine;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.ReportErrorState;

public abstract class PriorityPrintState extends BaseJobIntentServiceState {
    protected PriorityPrintState(String stateName) {
        super(stateName);
        TAG = TAG + "/PriorityPrint";
        this.possibleNextStates.add(ReportErrorState.class.getSimpleName());
    }

    @Override
    protected void onProcess(BaseJobIntentServiceStateMachine stateMachine) {
        BaseJobIntentServiceState nextState = action(stateMachine);
        if (nextState == null) {
            nextState = new ReportErrorState(Result.RESULT_FAIL, Result.ErrorCode.UNKNOWN, "Unknown error");
            SLog.e(TAG, "Unexpected : action() returned null");
        } else if (!isValidTransition(nextState)) {
            nextState = new ReportErrorState(Result.RESULT_FAIL, Result.ErrorCode.SERVICE_ERROR, "Invalid state transition");
            SLog.e(TAG, "Unexpected : action() returned invalid next state");
        }
        setNextState(nextState);
    }

    abstract protected BaseJobIntentServiceState action(BaseJobIntentServiceStateMachine stateMachine);

    protected PrintRequestIntent.IntentParams getReqParams(BaseJobIntentServiceStateMachine stateMachine) {
        PrintJobIntentServiceStateMachine sm = (PrintJobIntentServiceStateMachine) stateMachine;
        return PrintRequestIntent.getIntentParams(sm.getExtraParams());
    }
}
