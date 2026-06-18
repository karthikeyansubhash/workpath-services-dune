package com.hp.jetadvantage.link.services.joblet.jobintentservice;

import static com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentServiceStateMachine.MSG_REPORT_CANCELLED;
import static com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentServiceStateMachine.MSG_REPORT_COMPLETED;
import static com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentServiceStateMachine.MSG_REPORT_FAIL;
import static com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentServiceStateMachine.MSG_START;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.app.IntentService;
import android.content.Intent;
import android.os.Looper;
import android.os.Message;

import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.services.common.ssp.AbstractReporter;
import com.hp.jetadvantage.link.services.joblet.service.JobletService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class BaseJobIntentServiceStateMachineUnitTest {
    @Mock
    IntentService mockIntentService;
    @Mock
    Looper mockLooper;
    @Mock
    Intent mockIntent;
    @Mock
    InitState mockInitState;
    @Mock
    CreatingJobState mockCreatingJobState;
    @Mock
    MonitoringJobState mockMonitoringJobState;
    @Mock
    JobCompletedState mockJobCompletedState;
    @Mock
    AbstractReporter mockReporter;
    @Mock
    Message mockMessage;


    private TestJobIntentServiceStateMachine testJobIntentServiceStateMachine;

    @Before
    public void setUp() {
        testJobIntentServiceStateMachine = new TestJobIntentServiceStateMachine(mockIntentService, mockLooper, "jobTypeName", mockReporter, mockInitState);
    }

    @Test
    public void GivenBaseJobIntentServiceStateMachine_WhenStateMachineCreated_ThenInitState() {
        assertTrue(testJobIntentServiceStateMachine.getCurrentState() instanceof InitState);
    }

    /**
     * State Transition : InitState -> CreatingJobState -> MonitoringJobState -> JobCompletedState -> EndState -> stop
     */
    @Test
    public void GivenBaseJobIntentServiceStateMachine_WhenHandleMessageCalled_ThenTransitionToNextState() {
        when(mockInitState.isValidTransition(any())).thenReturn(true);
        when(mockCreatingJobState.getNextState()).thenReturn(mockMonitoringJobState);
        when(mockCreatingJobState.isValidTransition(any())).thenReturn(true);
        when(mockCreatingJobState.getNextState()).thenReturn(mockMonitoringJobState);
        when(mockMonitoringJobState.getNextState()).thenReturn(null);

        assertFalse(testJobIntentServiceStateMachine.isRunning());

        // InitState -> CreatingJobState -> MonitoringJobState
        mockMessage.what = MSG_START;
        mockMessage.obj = mockIntent;
        testJobIntentServiceStateMachine.handleMessage(mockMessage);

        assertTrue(testJobIntentServiceStateMachine.isRunning());
        assertTrue(testJobIntentServiceStateMachine.getCurrentState() instanceof MonitoringJobState);

        // MonitoringJobState -> JobCompletedState
        when(mockMonitoringJobState.isValidTransition(any())).thenReturn(true);
        when(mockJobCompletedState.getNextState()).thenReturn(null);
        mockMessage.what = MSG_REPORT_COMPLETED;
        testJobIntentServiceStateMachine.handleMessage(mockMessage);
        assertTrue(testJobIntentServiceStateMachine.getCurrentState() instanceof JobCompletedState);

        // JobCompletedState -> EndState -> stop
        when(mockJobCompletedState.isValidTransition(any())).thenReturn(true);
        testJobIntentServiceStateMachine.transitionTo(new EndState());

        assertFalse(testJobIntentServiceStateMachine.isRunning());
    }

    /**
     * State Transition : InitState -> CreatingJobState -> MonitoringJobState -> JobCanceledState -> EndState -> stop
     */
    @Test
    public void GivenBaseJobIntentServiceStateMachine_WhenHandleMessageCalled_ThenTransitionToNextState2() {
        TestJobIntentServiceStateMachine spySm = Mockito.spy(testJobIntentServiceStateMachine);
        when(mockInitState.isValidTransition(any())).thenReturn(true);
        when(mockCreatingJobState.getNextState()).thenReturn(mockMonitoringJobState);
        when(mockCreatingJobState.isValidTransition(any())).thenReturn(true);
        when(mockCreatingJobState.getNextState()).thenReturn(mockMonitoringJobState);
        when(mockMonitoringJobState.getNextState()).thenReturn(null);

        assertFalse(spySm.isRunning());

        // InitState -> CreatingJobState -> MonitoringJobState
        mockMessage.what = MSG_START;
        mockMessage.obj = mockIntent;
        spySm.handleMessage(mockMessage);

        assertTrue(spySm.isRunning());
        assertTrue(spySm.getCurrentState() instanceof MonitoringJobState);

        // MonitoringJobState -> (MSG_REPORT_CANCELLED) -> JobCanceledState -> EndState -> stop
        when(mockMonitoringJobState.isValidTransition(any())).thenReturn(true);
        mockMessage.what = MSG_REPORT_CANCELLED;
        spySm.handleMessage(mockMessage);

        assertFalse(spySm.isRunning());
        verify(spySm, Mockito.times(1)).sendLocalJobDataToJobLet(JobletService.Event.TL_EV_JOB_CANCELED);
        verify(spySm, Mockito.times(1)).stop();
    }

    /**
     * State Transition : InitState -> CreatingJobState -> MonitoringJobState -> JobFailedState -> EndState -> stop
     */
    @Test
    public void GivenBaseJobIntentServiceStateMachine_WhenHandleMessageCalled_ThenTransitionToNextState3() {
        TestJobIntentServiceStateMachine spySm = Mockito.spy(testJobIntentServiceStateMachine);
        when(mockInitState.isValidTransition(any())).thenReturn(true);
        when(mockCreatingJobState.getNextState()).thenReturn(mockMonitoringJobState);
        when(mockCreatingJobState.isValidTransition(any())).thenReturn(true);
        when(mockMonitoringJobState.getNextState()).thenReturn(null);

        InOrder inOrderCreatingJobState = Mockito.inOrder(mockCreatingJobState);
        InOrder inOrderMonitoringJobState = Mockito.inOrder(mockMonitoringJobState);


        assertFalse(spySm.isRunning());

        // triggering state change : InitState -> (MSG_START) -> CreatingJobState -> MonitoringJobState
        mockMessage.what = MSG_START;
        mockMessage.obj = mockIntent;
        spySm.handleMessage(mockMessage);

        //verify state transition : CreatingJobState -> MonitoringJobState
        inOrderCreatingJobState.verify(mockCreatingJobState, Mockito.times(1)).onEnter(spySm);
        inOrderCreatingJobState.verify(mockCreatingJobState, Mockito.times(1)).onProcess(spySm);
        inOrderCreatingJobState.verify(mockCreatingJobState, Mockito.times(1)).onExit(spySm);

        inOrderMonitoringJobState.verify(mockMonitoringJobState, Mockito.times(1)).onEnter(spySm);
        inOrderMonitoringJobState.verify(mockMonitoringJobState, Mockito.times(1)).onProcess(spySm);

        assertTrue(spySm.isRunning());
        assertTrue(spySm.getCurrentState() instanceof MonitoringJobState);

        // triggering state transition : MonitoringJobState -> (MSG_REPORT_FAIL) -> JobFailedState -> EndState -> stop
        when(mockMonitoringJobState.isValidTransition(any())).thenReturn(true);
        mockMessage.what = MSG_REPORT_FAIL;
        mockMessage.arg1 = 1;
        mockMessage.arg2 = Result.ErrorCode.JOB_FAILURE.ordinal();
        mockMessage.obj = "cause";
        spySm.handleMessage(mockMessage);

        inOrderMonitoringJobState.verify(mockMonitoringJobState, Mockito.times(1)).onExit(spySm);
        verify(spySm, Mockito.times(1)).sendLocalJobDataToJobLet(JobletService.Event.TL_EV_JOB_FAILED);
        verify(spySm, Mockito.times(1)).stop();
        assertFalse(spySm.isRunning());
    }

    /**
     * State Transition : InitState -> CreatingJobState -> ReportErrorSTate -> EndState -> stop
     */
    @Test
    public void GivenBaseJobIntentServiceStateMachine_WhenHandleMessageCalled_ThenTransitionToNextState4() {
        TestJobIntentServiceStateMachine spySm = Mockito.spy(testJobIntentServiceStateMachine);
        when(mockInitState.isValidTransition(any())).thenReturn(true);
        when(mockCreatingJobState.getNextState()).thenReturn(new ReportErrorState(1, Result.ErrorCode.INVALID_PARAM, "cause"));
        when(mockCreatingJobState.isValidTransition(any())).thenReturn(true);
        when(spySm.getJobRid()).thenReturn("jobRid");

        assertFalse(spySm.isRunning());

        // InitState -> CreatingJobState -> ReportErrorSTate -> EndState -> stop
        mockMessage.what = MSG_START;
        mockMessage.obj = mockIntent;
        spySm.handleMessage(mockMessage);

        verify(mockCreatingJobState, Mockito.times(1)).onProcess(spySm);
        verify(mockReporter, Mockito.times(1)).fail(any(), eq("jobRid"), eq(1), eq(Result.ErrorCode.INVALID_PARAM), eq("cause"));
        verify(spySm, Mockito.times(1)).stop();
        assertFalse(spySm.isRunning());
    }

    @Test
    public void GivenBaseJobIntentServiceStateMachine_WhenHandleMessageCalledWithInvalidMsg_ThenIgnore() {
        TestJobIntentServiceStateMachine spySm = Mockito.spy(testJobIntentServiceStateMachine);

        mockMessage.what = 1;
        spySm.handleMessage(mockMessage);
        verify(spySm, Mockito.never()).transitionTo(any());

        mockMessage.what = 2;
        spySm.handleMessage(mockMessage);
        verify(spySm, Mockito.never()).transitionTo(any());

        mockMessage.what = 3;
        spySm.handleMessage(mockMessage);
        verify(spySm, Mockito.never()).transitionTo(any());

        mockMessage.what = 4;
        spySm.handleMessage(mockMessage);
        verify(spySm, Mockito.never()).transitionTo(any());

        mockMessage.what = 10;
        spySm.handleMessage(mockMessage);
        verify(spySm, Mockito.never()).transitionTo(any());
    }

    /**
     * State Transition : MSG_REPORT_FAIL -> JobFailedState
     */
    @Test
    public void GivenBaseJobIntentServiceStateMachine_WhenHandleMessageCalledWithMsgReportFail_ThenCallsTransitionTo() {
        TestJobIntentServiceStateMachine spySm = Mockito.spy(testJobIntentServiceStateMachine);

        mockMessage.what = MSG_REPORT_FAIL;
        mockMessage.arg1 = 1;
        mockMessage.arg2 = 2;
        spySm.handleMessage(mockMessage);
        verify(spySm, Mockito.times(1)).transitionTo(any(JobFailedState.class));
    }

    /**
     * State Transition : MSG_REPORT_CANCELLED -> JobCanceledState
     */
    @Test
    public void GivenBaseJobIntentServiceStateMachine_WhenHandleMessageCalledWithMsgReportCancelled_ThenCallsTransitionTo() {
        TestJobIntentServiceStateMachine spySm = Mockito.spy(testJobIntentServiceStateMachine);

        mockMessage.what = MSG_REPORT_CANCELLED;
        spySm.handleMessage(mockMessage);
        verify(spySm, Mockito.times(1)).transitionTo(any(JobCanceledState.class));
    }

    /**
     * State Transition : MSG_REPORT_COMPLETED -> JobCompletedState
     */
    @Test
    public void GivenBaseJobIntentServiceStateMachine_WhenHandleMessageCalledWithMsgReportCompleted_ThenCallsTransitionTo() {
        TestJobIntentServiceStateMachine spySm = Mockito.spy(testJobIntentServiceStateMachine);

        mockMessage.what = MSG_REPORT_COMPLETED;
        spySm.handleMessage(mockMessage);
        verify(spySm, Mockito.times(1)).transitionTo(any(JobCompletedState.class));
    }


    public class TestJobIntentServiceStateMachine extends BaseJobIntentServiceStateMachine {
        public TestJobIntentServiceStateMachine(final IntentService service, final Looper looper, final String jobTypeName, final AbstractReporter reporter, final BaseJobIntentServiceState initState) {
            super(service, looper, jobTypeName, reporter, initState);
        }

        @Override
        protected CreatingJobState createCreatingJobState(Intent intent) {
            return mockCreatingJobState;
        }

        @Override
        protected JobCompletedState createJobCompletedState() {
            return mockJobCompletedState;
        }

        @Override
        protected void updateCanceledJobState() {

        }

        @Override
        protected void updateFailedJobState() {

        }

        @Override
        protected void updateCompletedJobState() {

        }

        @Override
        protected void reportBusyToApp(Intent intent) {
            reportBusyToApp("rid","targetPAckage");
        }
    }
}
