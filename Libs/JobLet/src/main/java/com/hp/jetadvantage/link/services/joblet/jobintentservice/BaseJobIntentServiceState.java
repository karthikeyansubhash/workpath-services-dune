/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.services.joblet.jobintentservice;

import androidx.annotation.NonNull;

import com.hp.jetadvantage.link.common.utils.SLog;

import java.util.ArrayList;
import java.util.List;

/**
 * BaseJobIntentServiceState is an abstract class that represents a state in a state machine for a JobIntentService.
 * This class provides common functionality for managing state transitions and handling state-specific behavior.
 * Specific states should extend this class and implement the abstract handle method.
 * Each state has a name, a list of possible next states, and a reference to the next state in the state machine.
 * The state machine transitions between states based on the current state and the next state defined by the state itself.
 * When a state is entered, the onEnter method is called first,and then onProcess method is called afterward.
 * When a state is exited, the onExit method is called.
 */
public abstract class BaseJobIntentServiceState {
    protected String TAG = "JobISS";
    protected final String STATE_NAME;
    protected List<String> possibleNextStates;
    protected BaseJobIntentServiceState nextState = null;

    protected BaseJobIntentServiceState(String stateName) {
        STATE_NAME = stateName;
        possibleNextStates = new ArrayList<>();
    }

    protected void onEnter(BaseJobIntentServiceStateMachine stateMachine) {
        SLog.d(TAG, "JobIntentServiceState Entering state: " + STATE_NAME);
    }

    protected void onExit(BaseJobIntentServiceStateMachine stateMachine) {
        SLog.d(TAG, "JobIntentServiceState Exiting state: " + STATE_NAME);
    }

    public BaseJobIntentServiceState getNextState() {
        return nextState;
    }

    protected void setNextState(BaseJobIntentServiceState nextState) {
        this.nextState = nextState;
    }

    protected void setPossibleNextStates(List<String> possibleNextStates) {
        this.possibleNextStates = possibleNextStates;
    }

    protected boolean isValidTransition(@NonNull BaseJobIntentServiceState nextState) {
        if(nextState == null) {
            SLog.e(TAG, "JobIntentServiceState isValidTransition: input nextState is null");
            return false;
        }
        return possibleNextStates.contains(nextState.STATE_NAME);
    }

    protected abstract void onProcess(BaseJobIntentServiceStateMachine stateMachine);
}
