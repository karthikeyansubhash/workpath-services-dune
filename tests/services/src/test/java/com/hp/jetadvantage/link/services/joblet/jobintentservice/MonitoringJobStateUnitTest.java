package com.hp.jetadvantage.link.services.joblet.jobintentservice;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.content.Intent;

import com.hp.ext.types.job.JobDoneStatus;
import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.services.joblet.service.JobletService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MonitoringJobStateUnitTest {
    @Mock
    private BaseJobIntentServiceStateMachine mockStateMachine;
    @Mock
    private Context mockContext;
    private CreatingJobState creatingJobState;
    private MonitoringJobState monitoringJobState;
    private JobCompletedState jobCompletedState;

    @Before
    public void setUp() {
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
     * Valid Next States for MonitoringJobState: JobCanceledState, JobCompletedState, JobFailedState
     */
    @Test
    public void GivenJMonitoringJobState_WhenIsValidTransitionCalled_ThenOnlyPossibleNextStatesShouldReturnTrue() {
        //check valid next states
        assertTrue(monitoringJobState.isValidTransition(new JobCanceledState()));
        assertTrue(monitoringJobState.isValidTransition(jobCompletedState));
        assertTrue(monitoringJobState.isValidTransition(new JobFailedState(2, null, null)));

        //check invalid next states
        assertFalse(monitoringJobState.isValidTransition(new EndState()));
        assertFalse(monitoringJobState.isValidTransition(creatingJobState));
        assertFalse(monitoringJobState.isValidTransition(monitoringJobState));
        assertFalse(monitoringJobState.isValidTransition(new InitState()));
        assertFalse(monitoringJobState.isValidTransition(new ReportErrorState(1, null, null)));

    }

    @Test
    public void GivenMonitoringJobState_WhenOnEnterCalled_ThenCallsRegisterNotificationCallback() {
        when(mockStateMachine.getContext()).thenReturn(mockContext);
        monitoringJobState.onEnter(mockStateMachine);

        Mockito.verify(mockContext, Mockito.times(1)).startService(any(Intent.class));
        Mockito.verify(mockStateMachine, Mockito.times(1)).sendLocalJobDataToJobLet(JobletService.Event.TL_EV_JOB_PROGRESS);
    }

    @Test
    public void GivenMonitoringJobState_WhenOnProcessCalled_ThenCallsRegisterNotificationCallback() {
        MonitoringJobState monitoringJobStateSpy = Mockito.spy(monitoringJobState);
        monitoringJobStateSpy.onProcess(mockStateMachine);

        Mockito.verify(monitoringJobStateSpy, Mockito.times(1)).registerNotificationCallback(mockStateMachine);
    }

    @Test
    public void GivenMonitoringJobState_WhenOnExitCalled_ThenCallsUnregisterNotificationCallback() {
        MonitoringJobState monitoringJobStateSpy = Mockito.spy(monitoringJobState);
        monitoringJobStateSpy.onExit(mockStateMachine);

        Mockito.verify(monitoringJobStateSpy, Mockito.times(1)).unregisterNotificationCallback();
    }

    @Test
    public void GivenMonitoringJobState_WhenProcessJobDoneStatusCalledWithJdsCanceled_ThenRequestToTransitState() {
        MonitoringJobState monitoringJobStateSpy = Mockito.spy(monitoringJobState);
        monitoringJobStateSpy.processJobDoneStatus(mockStateMachine, JobDoneStatus.JdsCanceled);

        Mockito.verify(monitoringJobStateSpy, Mockito.times(1)).requestToTransitState(mockStateMachine, BaseJobIntentServiceStateMachine.MSG_REPORT_CANCELLED, Result.RESULT_FAIL, null, null);
    }

    @Test
    public void GivenMonitoringJobState_WhenProcessJobDoneStatusCalledWithJdsFailed_ThenRequestToTransitState() {
        MonitoringJobState monitoringJobStateSpy = Mockito.spy(monitoringJobState);
        monitoringJobStateSpy.processJobDoneStatus(mockStateMachine, JobDoneStatus.JdsFailed);

        Mockito.verify(monitoringJobStateSpy, Mockito.times(1)).requestToTransitState(mockStateMachine, BaseJobIntentServiceStateMachine.MSG_REPORT_FAIL, Result.RESULT_FAIL, Result.ErrorCode.JOB_FAILURE, null);
    }

    @Test
    public void GivenMonitoringJobState_WhenProcessJobDoneStatusCalledWithJdsSucceeded_ThenRequestToTransitState() {
        MonitoringJobState monitoringJobStateSpy = Mockito.spy(monitoringJobState);
        monitoringJobStateSpy.processJobDoneStatus(mockStateMachine, JobDoneStatus.JdsSucceeded);

        Mockito.verify(monitoringJobStateSpy, Mockito.times(1)).requestToTransitState(mockStateMachine, BaseJobIntentServiceStateMachine.MSG_REPORT_COMPLETED, Result.RESULT_OK, null, null);
    }

    @Test
    public void GivenMonitoringJobState_WhenProcessJobDoneStatusCalledWithJdsActive_ThenDoNotCallRequestToTransitState() {
        MonitoringJobState monitoringJobStateSpy = Mockito.spy(monitoringJobState);
        monitoringJobStateSpy.processJobDoneStatus(mockStateMachine, JobDoneStatus.JdsActive);

        Mockito.verify(monitoringJobStateSpy, Mockito.never()).requestToTransitState(any(), anyInt(), anyInt(), any(), any());
        Mockito.verify(mockStateMachine, Mockito.times(1)).sendLocalJobDataToJobLet(JobletService.Event.TL_EV_JOB_PROGRESS);
    }
}
