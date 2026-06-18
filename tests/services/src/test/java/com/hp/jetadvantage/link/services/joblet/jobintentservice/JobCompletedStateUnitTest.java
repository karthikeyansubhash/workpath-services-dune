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
public class JobCompletedStateUnitTest {
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
     * Valid Next States for JobCompletedState: EndState
     */
    @Test
    public void GivenJobCompletedState_WhenIsValidTransitionCalled_ThenOnlyPossibleNextStatesShouldReturnTrue() {

        //check valid next states
        assertTrue(jobCompletedState.isValidTransition(new EndState()));

        //check invalid next states
        assertFalse(jobCompletedState.isValidTransition(creatingJobState));
        assertFalse(jobCompletedState.isValidTransition(monitoringJobState));
        assertFalse(jobCompletedState.isValidTransition(new InitState()));
        assertFalse(jobCompletedState.isValidTransition(new JobCanceledState()));
        assertFalse(jobCompletedState.isValidTransition(jobCompletedState));
        assertFalse(jobCompletedState.isValidTransition(new JobFailedState(2, null, null)));
    }

    @Test
    public void GivenJobCompletedState_WhenOnProcessCalled_ThenNextStateShouldBeEndState() {
        JobCompletedState jobCompletedStateSpy = Mockito.spy(jobCompletedState);
        jobCompletedStateSpy.onProcess(mockStateMachine);

        Mockito.verify(jobCompletedStateSpy, Mockito.times(1)).processCompletedJob(mockStateMachine);
        Mockito.verify(mockStateMachine, Mockito.times(1)).updateCompletedJobState();
        Mockito.verify(mockStateMachine, Mockito.times(1)).sendLocalJobDataToJobLet(JobletService.Event.TL_EV_JOB_COMPLETED);
        assertTrue(jobCompletedStateSpy.getNextState() instanceof EndState);
    }
}
