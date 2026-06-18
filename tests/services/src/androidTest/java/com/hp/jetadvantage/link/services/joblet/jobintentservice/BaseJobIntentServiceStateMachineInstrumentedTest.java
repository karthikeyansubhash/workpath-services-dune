package com.hp.jetadvantage.link.services.joblet.jobintentservice;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.hp.jetadvantage.link.BaseInstrumentedTest;
import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.api.scanner.intent.ScanToRequestIntent;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.services.common.ssp.AbstractReporter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

@RunWith(AndroidJUnit4.class)
public class BaseJobIntentServiceStateMachineInstrumentedTest extends BaseInstrumentedTest {
    IntentService mockIntentService;
    String testPackageName = "test.package";
    String testRid = UUID.randomUUID().toString();

    @Before
    public void SetUp() {
        super.SetUp();

        mockIntentService = new IntentService("MockIntentService") {
            @Override
            protected void onHandleIntent(Intent intent) {
            }

            @Override
            public Context getApplicationContext() {
                return InstrumentationRegistry.getInstrumentation().getTargetContext();
            }

            @Override
            public ContentResolver getContentResolver() {
                return InstrumentationRegistry.getInstrumentation().getTargetContext().getContentResolver();
            }
        };
    }

    /**
     * StateMachine : Start -> stop
     */
    @Test
    public void GivenBaseJobIntentServiceStateMachine_WhenStartCalled_ThenIsRunningReturnTrue() {

        HandlerThread mHandlerThread = new HandlerThread("Test:" + getClass().getSimpleName(), android.os.Process.THREAD_PRIORITY_LESS_FAVORABLE);
        mHandlerThread.start();

        TestJobIntentServiceStateMachine sm = new TestJobIntentServiceStateMachine(mockIntentService, mHandlerThread.getLooper(), false);
        assertFalse(sm.isRunning());

        sm.start();
        assertTrue(sm.isRunning());

        sm.stop();
        assertFalse(sm.isRunning());

        mHandlerThread.quit();
    }

    /**
     * StateMachine : InitState -> CreatingJobState
     */
    @Test
    public void GivenBaseJobIntentServiceStateMachine_WhenMsgStartReceived_ThenTransitToCreatingJobState() {

        HandlerThread mHandlerThread = new HandlerThread("Test:" + getClass().getSimpleName(), android.os.Process.THREAD_PRIORITY_LESS_FAVORABLE);
        mHandlerThread.start();

        TestJobIntentServiceStateMachine sm = new TestJobIntentServiceStateMachine(mockIntentService, mHandlerThread.getLooper(), true);

        // InitState is the default state
        assertTrue(sm.getCurrentState() instanceof InitState);

        sendStartMsg(sm);
        sleepMs(100);

        assertTrue(sm.isRunning());
        // CreatingJobState is the next state
        assertTrue(sm.getCurrentState() instanceof CreatingJobState);

        mHandlerThread.quit();
    }

    /**
     * StateMachine : InitState - (MSG_START) -> CreatingJobState -> (MSG_START) -> Report Busy
     */
    @Test
    public void GivenBaseJobIntentServiceStateMachine_WhenMsgStartReceivedDuringRunning_ThenReportBusyToApp() {

        HandlerThread mHandlerThread = new HandlerThread("Test:" + getClass().getSimpleName(), android.os.Process.THREAD_PRIORITY_LESS_FAVORABLE);
        mHandlerThread.start();
        TestJobIntentServiceStateMachine sm = new TestJobIntentServiceStateMachine(mockIntentService, mHandlerThread.getLooper(), true);

        //start state machine by sending MSG_START message
        sendStartMsg(sm);
        sleepMs(100);

        assertTrue(sm.isRunning());
        assertFalse(sm.isBusyReportedToApp);

        // sending again MSG_START message
        sendStartMsg(sm);
        sleepMs(100);

        //Busy report is expected
        assertTrue(sm.isBusyReportedToApp);

        mHandlerThread.quit();
    }

    /**
     * StateMachine : InitState - (MSG_START) -> CreatingJobState -> ReportErrorState -> EndState
     */
    @Test
    public void GivenBaseJobIntentServiceStateMachine_WhenJobCreationFailed_ThenReportErrorToApp() {
        HandlerThread mHandlerThread = new HandlerThread("Test:" + getClass().getSimpleName(), android.os.Process.THREAD_PRIORITY_LESS_FAVORABLE);
        mHandlerThread.start();
        TestJobIntentServiceStateMachine sm = new TestJobIntentServiceStateMachine(mockIntentService, mHandlerThread.getLooper(), false);
        sm.injectCreatingJobState(new CreatingTestJobState(getTestIntent(testRid), new ReportErrorState(Result.RESULT_FAIL, Result.ErrorCode.CONNECTION_ERROR, "Device is not connected")));

        //start state machine by sending MSG_START message
        sendStartMsg(sm);
        sleepMs(100);

        assertFalse(sm.isRunning());
        assertTrue(sm.isErrorReportedToApp);

        mHandlerThread.quit();
    }

    /**
     * StateMachine : InitState - (MSG_START) -> CreatingJobState -> (createJob method does not return proper next state) -> ReportErrorState -> EndState
     */
    @Test
    public void GivenBaseJobIntentServiceStateMachine_WhenJobCreationFailedWithoutNextState_ThenReportErrorToApp() {
        HandlerThread mHandlerThread = new HandlerThread("Test:" + getClass().getSimpleName(), android.os.Process.THREAD_PRIORITY_LESS_FAVORABLE);
        mHandlerThread.start();
        TestJobIntentServiceStateMachine sm = new TestJobIntentServiceStateMachine(mockIntentService, mHandlerThread.getLooper(), false);
        sm.injectCreatingJobState(new CreatingTestJobState(getTestIntent(testRid), null));

        //start state machine by sending MSG_START message
        sendStartMsg(sm);
        waitUntilNotRunning(sm, 10, 100);
        assertFalse(sm.isRunning());
        assertTrue(sm.isErrorReportedToApp);

        mHandlerThread.quit();
    }

    /**
     * StateMachine : InitState - (MSG_START) -> CreatingJobState -> EndState
     */
    @Test
    public void GivenBaseJobIntentServiceStateMachine_WhenJobCreationFailedWithEmptyTargetPackage_ThenTransitToEnd() {
        HandlerThread mHandlerThread = new HandlerThread("Test:" + getClass().getSimpleName(), android.os.Process.THREAD_PRIORITY_LESS_FAVORABLE);
        mHandlerThread.start();
        TestJobIntentServiceStateMachine sm = new TestJobIntentServiceStateMachine(mockIntentService, mHandlerThread.getLooper(), false);
        sm.injectCreatingJobState(new CreatingTestJobState(getTestIntent(testRid), new EndState()));

        //start state machine by sending MSG_START message
        sendStartMsg(sm);
        waitUntilNotRunning(sm, 10, 100);

        assertFalse(sm.isRunning());
        assertFalse(sm.isErrorReportedToApp);
        assertFalse(sm.isBusyReportedToApp);

        mHandlerThread.quit();
    }

    /**
     * StateMachine : InitState - (MSG_START) -> CreatingJobState -> MonitoringJobState
     */
    @Test
    public void GivenBaseJobIntentServiceStateMachine_WhenJobCreationSuccessful_ThenTransitToMonitoringJobState() {
        HandlerThread mHandlerThread = new HandlerThread("Test:" + getClass().getSimpleName(), android.os.Process.THREAD_PRIORITY_LESS_FAVORABLE);
        mHandlerThread.start();
        TestJobIntentServiceStateMachine sm = new TestJobIntentServiceStateMachine(mockIntentService, mHandlerThread.getLooper(), false);

        //start state machine by sending MSG_START message
        sendStartMsg(sm);
        sleepMs(100);

        assertTrue(sm.isRunning());
        assertFalse(sm.isBusyReportedToApp);
        assertTrue(sm.getCurrentState() instanceof MonitoringJobState);

        mHandlerThread.quit();
    }

    /**
     * StateMachine : InitState - (MSG_START) -> CreatingJobState -> MonitoringJobState -> JobCompletedSTate -> EndState
     */
    @Test
    public void GivenBaseJobIntentServiceStateMachine_WhenJobCompletedSuccessful_ThenTransitionsToJobCompletedState() {
        HandlerThread mHandlerThread = new HandlerThread("Test:" + getClass().getSimpleName(), android.os.Process.THREAD_PRIORITY_LESS_FAVORABLE);
        mHandlerThread.start();
        TestJobIntentServiceStateMachine sm = new TestJobIntentServiceStateMachine(mockIntentService, mHandlerThread.getLooper(), true);

        //start state machine by sending MSG_START message
        sendStartMsg(sm);
        sleepMs(100);

        assertTrue(sm.getCurrentState() instanceof CreatingJobState);
        sm.transitionTo(sm.getCurrentState().getNextState());

        assertTrue(sm.isRunning());
        assertFalse(sm.isBusyReportedToApp);
        assertTrue(sm.getCurrentState() instanceof MonitoringJobState);

        MonitoringTestJobState monitoringTestJobState = (MonitoringTestJobState) sm.getCurrentState();
        monitoringTestJobState.requestToTransitState(sm, BaseJobIntentServiceStateMachine.MSG_REPORT_COMPLETED, Result.RESULT_OK, null, null);
        ;
        sleepMs(100);

        assertTrue(sm.getCurrentState() instanceof JobCompletedState);
        sm.transitionTo(sm.getCurrentState().getNextState());

        assertFalse(sm.isRunning());
        mHandlerThread.quit();
    }

    /**
     * StateMachine : InitState - (MSG_START) -> CreatingJobState -> MonitoringJobState -> JobFailedState -> EndState
     */
    @Test
    public void GivenBaseJobIntentServiceStateMachine_WhenJobFailed_ThenTransitionsToJobFailedState() {
        HandlerThread mHandlerThread = new HandlerThread("Test:" + getClass().getSimpleName(), android.os.Process.THREAD_PRIORITY_LESS_FAVORABLE);
        mHandlerThread.start();

        //Set oneStepTransitionOnly to true to verify step by step for each state
        TestJobIntentServiceStateMachine sm = new TestJobIntentServiceStateMachine(mockIntentService, mHandlerThread.getLooper(), true);

        //start state machine by sending MSG_START message
        sendStartMsg(sm);
        sleepMs(100);

        assertTrue(sm.getCurrentState() instanceof CreatingJobState);
        sm.transitionTo(sm.getCurrentState().getNextState());

        assertTrue(sm.isRunning());
        assertFalse(sm.isBusyReportedToApp);
        assertTrue(sm.getCurrentState() instanceof MonitoringJobState);

        MonitoringTestJobState monitoringTestJobState = (MonitoringTestJobState) sm.getCurrentState();
        monitoringTestJobState.requestToTransitState(sm, BaseJobIntentServiceStateMachine.MSG_REPORT_FAIL, Result.RESULT_OK, null, null);
        sleepMs(100);

        assertTrue(sm.getCurrentState() instanceof JobFailedState);
        sm.transitionTo(sm.getCurrentState().getNextState());

        assertFalse(sm.isRunning());
        mHandlerThread.quit();
    }

    /**
     * StateMachine : InitState - (MSG_START) -> CreatingJobState -> MonitoringJobState -> JobCanceledState -> EndState
     */
    @Test
    public void GivenBaseJobIntentServiceStateMachine_WhenJobCanceled_ThenTransitionsToJobCanceledState() {
        HandlerThread mHandlerThread = new HandlerThread("Test:" + getClass().getSimpleName(), android.os.Process.THREAD_PRIORITY_LESS_FAVORABLE);
        mHandlerThread.start();

        //Set oneStepTransitionOnly to true to verify step by step for each state
        TestJobIntentServiceStateMachine sm = new TestJobIntentServiceStateMachine(mockIntentService, mHandlerThread.getLooper(), true);

        //start state machine by sending MSG_START message
        sendStartMsg(sm);
        sleepMs(100);

        assertTrue(sm.getCurrentState() instanceof CreatingJobState);
        sm.transitionTo(sm.getCurrentState().getNextState());

        assertTrue(sm.isRunning());
        assertFalse(sm.isBusyReportedToApp);
        assertTrue(sm.getCurrentState() instanceof MonitoringJobState);

        MonitoringTestJobState monitoringTestJobState = (MonitoringTestJobState) sm.getCurrentState();
        monitoringTestJobState.requestToTransitState(sm, BaseJobIntentServiceStateMachine.MSG_REPORT_CANCELLED, Result.RESULT_OK, null, null);
        sleepMs(100);

        assertTrue(sm.getCurrentState() instanceof JobCanceledState);
        sm.transitionTo(sm.getCurrentState().getNextState());

        assertFalse(sm.isRunning());
        mHandlerThread.quit();
    }

    /**
     * StateMachine : InitState - (MSG_START) -> CreatingJobState -> (X) JobCompletedState
     */
    @Test
    public void GivenBaseJobIntentServiceStateMachine_WhenOutOfOrderJobCompletedStateChangeRequested_ThenTransitToReportErrorState() {
        HandlerThread mHandlerThread = new HandlerThread("Test:" + getClass().getSimpleName(), android.os.Process.THREAD_PRIORITY_LESS_FAVORABLE);
        mHandlerThread.start();
        TestJobIntentServiceStateMachine sm = new TestJobIntentServiceStateMachine(mockIntentService, mHandlerThread.getLooper(), true);

        //Set the next state of the createJob()'s return value to JobCompletedState (invalid state)
        JobCompletedState jobCompletedState = new JobCompletedState() {
            @Override
            protected void processCompletedJob(BaseJobIntentServiceStateMachine stateMachine) {
                //do nothing
            }
        };
        sm.injectCreatingJobState(new CreatingTestJobState(getTestIntent(testRid), jobCompletedState));

        //start state machine by sending MSG_START message
        sendStartMsg(sm);
        sleepMs(100);

        assertTrue(sm.isRunning());
        assertFalse(sm.isBusyReportedToApp);
        assertTrue(sm.getCurrentState() instanceof CreatingTestJobState);
        assertTrue(sm.getCurrentState().getNextState() instanceof ReportErrorState);

        sm.transitionTo(sm.getCurrentState().getNextState());
        assertTrue(sm.getCurrentState() instanceof ReportErrorState);
        mHandlerThread.quit();
    }

    /**
     * StateMachine : InitState - (MSG_START) -> CreatingJobState -> MonitoringJobState -> (X) EndState
     */
    @Test
    public void GivenBaseJobIntentServiceStateMachine_WhenOutOfOrderEndStateChangeRequested_ThenDoNotTransitToTheState() {
        HandlerThread mHandlerThread = new HandlerThread("Test:" + getClass().getSimpleName(), android.os.Process.THREAD_PRIORITY_LESS_FAVORABLE);
        mHandlerThread.start();
        TestJobIntentServiceStateMachine sm = new TestJobIntentServiceStateMachine(mockIntentService, mHandlerThread.getLooper(), false);

        //start state machine by sending MSG_START message
        sendStartMsg(sm);
        sleepMs(100);

        assertTrue(sm.isRunning());
        assertFalse(sm.isBusyReportedToApp);

        // Wait for state transition with timeout
        int counter = 0;
        while (sm.getCurrentState() instanceof CreatingJobState) {
            if (counter > 10) {
                fail("Timeout waiting for transition to MonitoringJobState");
            }
            sleepMs(100); // Poll every 100ms
            counter++;
        }
        assertTrue(sm.getCurrentState() instanceof MonitoringJobState);

        sm.getCurrentState().setNextState(new EndState());
        sm.transitionTo(sm.getCurrentState().getNextState());

        assertTrue(sm.getCurrentState() instanceof MonitoringJobState);

        mHandlerThread.quit();
    }

    /**
     * StateMachine : InitState - (MSG_START) -> CreatingJobState -> MonitoringJobState -> (Invalid message)
     */
    @Test
    public void GivenBaseJobIntentServiceStateMachine_WhenInvalidMsgReceived_ThenIgnoreTheMsg() {
        HandlerThread mHandlerThread = new HandlerThread("Test:" + getClass().getSimpleName(), android.os.Process.THREAD_PRIORITY_LESS_FAVORABLE);
        mHandlerThread.start();

        //Set oneStepTransitionOnly to true to verify step by step for each state
        TestJobIntentServiceStateMachine sm = new TestJobIntentServiceStateMachine(mockIntentService, mHandlerThread.getLooper(), true);

        //start state machine by sending MSG_START message
        sendStartMsg(sm);
        sleepMs(100);

        assertTrue(sm.getCurrentState() instanceof CreatingJobState);
        sm.transitionTo(sm.getCurrentState().getNextState());

        assertTrue(sm.isRunning());
        assertFalse(sm.isBusyReportedToApp);
        assertTrue(sm.getCurrentState() instanceof MonitoringJobState);

        //send invalid message
        MonitoringTestJobState monitoringTestJobState = (MonitoringTestJobState) sm.getCurrentState();
        monitoringTestJobState.requestToTransitState(sm, 1, Result.RESULT_OK, null, null);
        sleepMs(100);

        assertTrue(sm.getCurrentState() instanceof MonitoringJobState);
        assertTrue(sm.isRunning());

        //send invalid message again
        monitoringTestJobState = (MonitoringTestJobState) sm.getCurrentState();
        monitoringTestJobState.requestToTransitState(sm, 10, Result.RESULT_OK, null, null);

        assertTrue(sm.getCurrentState() instanceof MonitoringJobState);
        assertTrue(sm.isRunning());

        mHandlerThread.quit();
    }

    private Intent getTestIntent(String rid) {
        final ScanToRequestIntent intent = new ScanToRequestIntent();
        final ScanToRequestIntent.IntentParams params = new ScanToRequestIntent.IntentParams(null, null, rid, testPackageName, null, rid, null, null, Sdk.VERSION.LEVEL);
        intent.putIntentParams(params);
        intent.setPackage(Sdk.SERVICES_PACKAGE);
        return intent;
    }

    private void sendStartMsg(BaseJobIntentServiceStateMachine sm) {
        Message msg = sm.obtainMessage();
        msg.what = BaseJobIntentServiceStateMachine.MSG_START;
        msg.obj = getTestIntent(testRid);
        sm.sendMessage(msg);
    }

    private void waitUntilNotRunning(BaseJobIntentServiceStateMachine sm, int maxRetries, int sleepMs) {
        int retries = 0;
        do {
            sleepMs(sleepMs);
            retries++;
        } while (sm.isRunning() && retries < maxRetries);
    }

    public class TestJobIntentServiceStateMachine extends BaseJobIntentServiceStateMachine {
        public boolean isBusyReportedToApp = false;
        public boolean isErrorReportedToApp = false;
        public CreatingJobState creatingJobState = null;

        public TestJobIntentServiceStateMachine(IntentService service, Looper looper, boolean oneStepTransitionOnly) {
            super(service, looper, "Test", oneStepTransitionOnly);
        }

        public void injectCreatingJobState(CreatingJobState creatingJobState) {
            this.creatingJobState = creatingJobState;
        }

        @Override
        protected void reportBusyToApp(Intent intent) {
            isBusyReportedToApp = true;
        }

        @Override
        public AbstractReporter getReporterToApp() {
            return new AbstractReporter("", null, "") {
                @Override
                public void fail(Context context, String rid, int result, Result.ErrorCode errorCode, String cause) {
                    isErrorReportedToApp = true;
                }
            };
        }

        @Override
        protected CreatingJobState createCreatingJobState(Intent intent) {
            if (creatingJobState == null) {
                return new CreatingTestJobState(intent, new MonitoringTestJobState("JobId"));
            } else {
                return creatingJobState;
            }
        }

        @Override
        protected JobCompletedState createJobCompletedState() {
            return new JobCompletedState() {
                @Override
                protected void processCompletedJob(BaseJobIntentServiceStateMachine stateMachine) {
                    //do nothing
                }
            };
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
    }

    public class CreatingTestJobState extends CreatingJobState {
        private BaseJobIntentServiceState preDefinedNextState;

        public CreatingTestJobState(Intent jobIntent, BaseJobIntentServiceState preDefinedNextState) {
            super(jobIntent);
            this.preDefinedNextState = preDefinedNextState;
        }

        @Override
        protected BaseJobIntentServiceState initializeJob(final Intent intent, BaseJobIntentServiceStateMachine stateMachine) {
            return preDefinedNextState;
        }
    }

    public class MonitoringTestJobState extends MonitoringJobState {
        public MonitoringTestJobState(String jobId) {
            super(jobId);
        }

        @Override
        public void onEnter(BaseJobIntentServiceStateMachine stateMachine) {
            //make this method empty for testing
        }

        @Override
        protected void registerNotificationCallback(BaseJobIntentServiceStateMachine stateMachine) {

        }

        @Override
        protected void unregisterNotificationCallback() {

        }

    }

    public class TestJobIntentService extends BaseJobIntentService {
        public TestJobIntentService() {
            super(TestJobIntentService.class.getSimpleName());
            TAG = TAG + "/Test";
        }

        @Override
        protected BaseJobIntentServiceStateMachine createStateMachine(IntentService service, Looper looper) {
            return new TestJobIntentServiceStateMachine(service, looper, false);
        }

        @Override
        protected boolean cancelJob(Intent intent) {
            return true;
        }

        @Override
        protected String getJobCancelAction() {
            return "CANCEL";
        }

        @Override
        protected void reportBusyToApp(Intent intent) {

        }
    }
}
