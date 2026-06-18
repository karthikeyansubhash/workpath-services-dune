package com.hp.jetadvantage.link.services.joblet.jobintentservice;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.content.Intent;

import com.hp.jetadvantage.link.services.common.ssp.AbstractReporter;
import com.hp.jetadvantage.link.services.joblet.service.JobletService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ReportErrorStateUnitTest {
    @Mock
    private BaseJobIntentServiceStateMachine mockStateMachine;
    @Mock
    private AbstractReporter mockReporter;
    @Mock
    private Context mockContext;
    private JobFailedState jobFailedState;
    private CreatingJobState creatingJobState;
    private MonitoringJobState monitoringJobState;
    private JobCompletedState jobCompletedState;
    private ReportErrorState reportErrorState;

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
    public void GivenReportErrorState_WhenIsValidTransitionCalled_ThenOnlyPossibleNextStatesShouldReturnTrue() {
        reportErrorState = new ReportErrorState(1, null, null);

        //check valid next states
        assertTrue(reportErrorState.isValidTransition(new EndState()));

        //check invalid next states
        assertFalse(reportErrorState.isValidTransition(creatingJobState));
        assertFalse(reportErrorState.isValidTransition(monitoringJobState));
        assertFalse(reportErrorState.isValidTransition(new InitState()));
        assertFalse(reportErrorState.isValidTransition(new JobCanceledState()));
        assertFalse(reportErrorState.isValidTransition(jobCompletedState));
        assertFalse(reportErrorState.isValidTransition(new JobFailedState(2, null, null)));
        assertFalse(reportErrorState.isValidTransition(new ReportErrorState(2, null, null)));
    }

    @Test
    public void GivenReportErrorState_WhenOnProcessCalled_ThenNextStateShouldBeEndState() {
        when(mockStateMachine.getReporterToApp()).thenReturn(mockReporter);
        when(mockStateMachine.getContext()).thenReturn(mockContext);
        when(mockStateMachine.getJobRid()).thenReturn("rid");

        reportErrorState = new ReportErrorState(2, null, "test");
        reportErrorState.onProcess(mockStateMachine);

        Mockito.verify(mockReporter, Mockito.times(1)).fail(mockContext, "rid", 2, null, "test");
        assertTrue(reportErrorState.getNextState() instanceof EndState);
    }
}
