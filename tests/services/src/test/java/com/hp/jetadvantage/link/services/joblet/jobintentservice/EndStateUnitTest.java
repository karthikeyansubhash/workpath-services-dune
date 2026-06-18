package com.hp.jetadvantage.link.services.joblet.jobintentservice;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import android.content.Intent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EndStateUnitTest {
    @Mock
    private BaseJobIntentServiceStateMachine mockStateMachine;

    @Mock
    private Intent mockIntent;

    private EndState endState;

    @Before
    public void setUp() {
        endState = new EndState();
    }

    @Test
    public void GivenEndState_WhenIsValidTransitionCalled_ThenReturnFalse() {
        assertFalse(endState.isValidTransition(new InitState()));
        assertFalse(endState.isValidTransition(new JobCompletedState() {
            @Override
            protected void processCompletedJob(BaseJobIntentServiceStateMachine stateMachine) {

            }
        }));
        assertFalse(endState.isValidTransition(new JobCanceledState()));
        assertFalse(endState.isValidTransition(new JobFailedState(0, null, null)));
        assertFalse(endState.isValidTransition(new ReportErrorState(0, null, null)));
    }

    @Test
    public void GivenEndState_WhenOnProcess_ThenReleasesResources() {
        endState.onProcess(mockStateMachine);
        Mockito.verify(mockStateMachine, Mockito.times(1)).releaseWakeLock();
        Mockito.verify(mockStateMachine, Mockito.times(1)).removeCallbacksAndMessages(null);
        Mockito.verify(mockStateMachine, Mockito.times(1)).stop();
    }

    /**
     * EndState cannot have a next state
     */
    @Test
    public void GivenEndState_WhenSetNextState_ThenDoNotSetNextState() {
        endState.setNextState(new InitState());
        assertNull(endState.getNextState());
    }
}
