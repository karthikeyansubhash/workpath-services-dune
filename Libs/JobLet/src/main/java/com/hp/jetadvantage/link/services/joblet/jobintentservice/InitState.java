package com.hp.jetadvantage.link.services.joblet.jobintentservice;

public class InitState extends BaseJobIntentServiceState {
    public InitState() {
        super(InitState.class.getSimpleName());
        TAG = TAG + "/Init";
        this.possibleNextStates.add(CreatingJobState.class.getSimpleName());
    }

    @Override
    protected void onProcess(BaseJobIntentServiceStateMachine stateMachine) {
        //do nothing
    }
}