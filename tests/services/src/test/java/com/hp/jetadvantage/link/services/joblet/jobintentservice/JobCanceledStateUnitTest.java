package com.hp.jetadvantage.link.services.joblet.jobintentservice;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import android.content.Intent;

import com.hp.jetadvantage.link.services.joblet.service.JobletService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class JobCanceledStateUnitTest {
    @Mock
    private BaseJobIntentServiceStateMachine mockStateMachine;
    private JobCanceledState jobCanceledState;
    private CreatingJobState creatingJobState;
    private MonitoringJobState monitoringJobState;
    private JobCompletedState jobCompletedState;

    @Before
    public void setUp() {
        jobCanceledState = new JobCanceledState();
        creatingJobState = new CreatingJobState(null) {
            @Override
            protected BaseJobIntentServiceState initializeJob(Intent intent, BaseJobIntentServiceStateMachine stateMachine) {
                return null;
            }
        };
        monitoringJobState = new MonitoringJobState("jobId") {
            @Override
            protected void registerNotificationCallback(BaseJobIntentServiceStateMachine stateMachine) {}
            @Override
            protected void unregisterNotificationCallback() {}
        };
        jobCompletedState = new JobCompletedState() {
            @Override
            protected void processCompletedJob(BaseJobIntentServiceStateMachine stateMachine) {

            }
        };
    }
    /**
     * Test isValidTransition method
     * Valid Next States for JobCanceledState: EndState
     */
    @Test
    public void GivenJobCanceledState_WhenIsValidTransitionCalled_ThenOnlyPossibleNextStatesShouldReturnTrue() {

        //check valid next states
        assertTrue(jobCanceledState.isValidTransition(new EndState()));

        //check invalid next states
        assertFalse(jobCanceledState.isValidTransition(creatingJobState));
        assertFalse(jobCanceledState.isValidTransition(monitoringJobState));
        assertFalse(jobCanceledState.isValidTransition(new InitState()));
        assertFalse(jobCanceledState.isValidTransition(new JobCanceledState()));
        assertFalse(jobCanceledState.isValidTransition(jobCompletedState));
        assertFalse(jobCanceledState.isValidTransition(new JobFailedState(1, null, null)));
    }

    @Test
    public void GivenJobCanceledState_WhenOnProcessCalled_ThenNextStateShouldBeEndState() {
        jobCanceledState.onProcess(mockStateMachine);

        Mockito.verify(mockStateMachine, Mockito.times(1)).updateCanceledJobState();
        Mockito.verify(mockStateMachine, Mockito.times(1)).sendLocalJobDataToJobLet(JobletService.Event.TL_EV_JOB_CANCELED);
        assertTrue(jobCanceledState.getNextState() instanceof EndState);
    }
}
