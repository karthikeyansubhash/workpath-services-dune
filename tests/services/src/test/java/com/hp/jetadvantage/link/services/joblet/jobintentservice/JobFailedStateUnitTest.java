package com.hp.jetadvantage.link.services.joblet.jobintentservice;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Intent;

import com.hp.jetadvantage.link.services.joblet.service.JobletService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class JobFailedStateUnitTest {
    @Mock
    private BaseJobIntentServiceStateMachine mockStateMachine;
    private JobFailedState jobFailedState;
    private CreatingJobState creatingJobState;
    private MonitoringJobState monitoringJobState;
    private JobCompletedState jobCompletedState;

    @Before
    public void setUp() {
        jobFailedState = new JobFailedState(1, null, null);
        creatingJobState = new CreatingJobState(null) {
            @Override
            protected BaseJobIntentServiceState initializeJob(Intent intent, BaseJobIntentServiceStateMachine stateMachine) {
                return null;
            }
        };
        monitoringJobState = new MonitoringJobState("jobId") {
            @Override
            protected void registerNotificationCallback(BaseJobIntentServiceStateMachine stateMachine) {
            }

            @Override
            protected void unregisterNotificationCallback() {
            }
        };
        jobCompletedState = new JobCompletedState() {
            @Override
            protected void processCompletedJob(BaseJobIntentServiceStateMachine stateMachine) {

            }
        };
    }

    /**
     * Test isValidTransition method
     * Valid Next States for JobFailedState: EndState
     */
    @Test
    public void GivenJobFailedState_WhenIsValidTransitionCalled_ThenOnlyPossibleNextStatesShouldReturnTrue() {

        //check valid next states
        assertTrue(jobFailedState.isValidTransition(new EndState()));

        //check invalid next states
        assertFalse(jobFailedState.isValidTransition(creatingJobState));
        assertFalse(jobFailedState.isValidTransition(monitoringJobState));
        assertFalse(jobFailedState.isValidTransition(new InitState()));
        assertFalse(jobFailedState.isValidTransition(new JobCanceledState()));
        assertFalse(jobFailedState.isValidTransition(jobCompletedState));
        assertFalse(jobFailedState.isValidTransition(new JobFailedState(2, null, null)));
    }

    @Test
    public void GivenJobFailedState_WhenOnProcessCalled_ThenNextStateShouldBeEndState() {
        jobFailedState.onProcess(mockStateMachine);

        Mockito.verify(mockStateMachine, Mockito.times(1)).updateFailedJobState();
        Mockito.verify(mockStateMachine, Mockito.times(1)).sendLocalJobDataToJobLet(JobletService.Event.TL_EV_JOB_FAILED);
        assertTrue(jobFailedState.getNextState() instanceof EndState);
    }
}
