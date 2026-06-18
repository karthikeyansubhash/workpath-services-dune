package com.hp.jetadvantage.link.services.joblet.jobintentservice;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Intent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CreateJobStateUnitTest {
    @Mock
    private BaseJobIntentServiceStateMachine mockStateMachine;
    @Mock
    private Intent mockIntent;
    private CreatingJobState creatingJobState;
    private MonitoringJobState monitoringJobState;
    private JobCompletedState jobCompletedState;

    @Before
    public void setUp() {
        creatingJobState = new CreatingJobState(mockIntent) {
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
     * Valid Next States for CreatingJobState: MonitoringJobState, ReportErrorState, EndState
     */
    @Test
    public void GivenCreatingJobState_WhenIsValidTransitionCalled_ThenOnlyPossibleNextStatesShouldReturnTrue() {
        creatingJobState = new CreatingJobState(mockIntent) {
            @Override
            protected BaseJobIntentServiceState initializeJob(Intent intent, BaseJobIntentServiceStateMachine stateMachine) {
                return null;
            }
        };

        //check valid next states
        assertTrue(creatingJobState.isValidTransition(monitoringJobState));
        assertTrue(creatingJobState.isValidTransition(new ReportErrorState(0, null, null)));
        assertTrue(creatingJobState.isValidTransition(new EndState()));

        //check invalid next states
        assertFalse(creatingJobState.isValidTransition(creatingJobState));
        assertFalse(creatingJobState.isValidTransition(new InitState()));
        assertFalse(creatingJobState.isValidTransition(new JobCanceledState()));
        assertFalse(creatingJobState.isValidTransition(jobCompletedState));
        assertFalse(creatingJobState.isValidTransition(new JobFailedState(1, null, null)));
    }

    @Test
    public void GivenCreatingJobState_WhenOnEnterCalled_ThenCallsAcquireWakeLock() {
        creatingJobState.onEnter(mockStateMachine);
        Mockito.verify(mockStateMachine).acquireWakeLock();
    }

    @Test
    public void GivenCreatingJobState_WhenOnProcessCalled_ThenCallsCreateJob() {
        CreatingJobState creatingJobStateSpy = Mockito.spy(creatingJobState);
        creatingJobStateSpy.onProcess(mockStateMachine);

        //verify createJob is called
        Mockito.verify(creatingJobStateSpy).initializeJob(mockIntent, mockStateMachine);

        //verify next state is set to ReportErrorState if createJob returns null
        assertTrue(creatingJobStateSpy.getNextState() instanceof ReportErrorState);
    }

    @Test
    public void GivenCreatingJobState_WhenOnProcessCalled_ThenSetMonitoringJobStateAsNextState() {
        CreatingJobState creatingJobState2 = new CreatingJobState(mockIntent) {
            @Override
            protected BaseJobIntentServiceState initializeJob(Intent intent, BaseJobIntentServiceStateMachine stateMachine) {
                return monitoringJobState;
            }
        };
        creatingJobState2.onProcess(mockStateMachine);

        //verify next state is set to MonitoringJobState if createJob returns MonitoringJobState
        assertTrue(creatingJobState2.getNextState() instanceof MonitoringJobState);
    }

    @Test
    public void GivenCreatingJobState_WhenCreateJobReturnsInvalidNextState_ThenSetReportErrorStateAsNextState() {
        CreatingJobState creatingJobState2 = new CreatingJobState(mockIntent) {
            @Override
            protected BaseJobIntentServiceState initializeJob(Intent intent, BaseJobIntentServiceStateMachine stateMachine) {
                // createJob returns invalid next state : JobFailedState cannot be a next state of the CreateJobState
                return new JobFailedState(1, null, null);
            }
        };
        creatingJobState2.onProcess(mockStateMachine);

        //verify next state is set to MonitoringJobState if createJob returns MonitoringJobState
        assertTrue(creatingJobState2.getNextState() instanceof ReportErrorState);
    }
}
