package com.hp.jetadvantage.link.services.scanlet.service;

import static com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentServiceStateMachine.EXTRA_PARAMS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
import com.hp.jetadvantage.link.api.scanner.ScanAttributes;
import com.hp.jetadvantage.link.api.scanner.intent.ScanToRequestIntent;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.common.utils.JsonParser;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentServiceState;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentServiceStateMachine;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.EndState;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.InitState;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.ReportErrorState;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

@RunWith(AndroidJUnit4.class)
public class ScanJobIntentServiceStateMachineInstrumentedTest extends BaseInstrumentedTest {
    IntentService mockIntentService;
    String testPackageName = "scanlet.test.package";
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
     * StateMachine : InitState -> CreatingJobState
     */
    @Test
    public void GivenScanJobIntentServiceStateMachine_WhenMsgStartReceived_ThenTransitToCreatingJobState() {

        HandlerThread mHandlerThread = new HandlerThread("Test:" + getClass().getSimpleName(), android.os.Process.THREAD_PRIORITY_LESS_FAVORABLE);
        mHandlerThread.start();

        ScanJobIntentServiceStateMachine sm = new ScanJobIntentServiceStateMachine(mockIntentService, mHandlerThread.getLooper(), true);

        // InitState is the default state
        assertTrue(sm.getCurrentState() instanceof InitState);

        sendStartMsg(sm, getEmptyExtraTestIntent(testRid));
        sleepMs(100);

        assertTrue(sm.isRunning());
        // CreatingJobState is the next state
        assertTrue(sm.getCurrentState() instanceof CreatingScanJobState);

        mHandlerThread.quit();
    }

    /**
     * StateMachine : InitState - (MSG_START) -> CreatingJobState -> (MSG_START) -> Report Busy
     */
    @Test
    public void GivenScanJobIntentServiceStateMachine_WhenMsgStartReceivedDuringRunning_ThenReportBusyToApp() {

        HandlerThread mHandlerThread = new HandlerThread("Test:" + getClass().getSimpleName(), android.os.Process.THREAD_PRIORITY_LESS_FAVORABLE);
        mHandlerThread.start();
        ScanJobIntentServiceStateMachine sm = (new ScanJobIntentServiceStateMachine(mockIntentService, mHandlerThread.getLooper(), true));

        //start state machine by sending MSG_START message
        sendStartMsg(sm, getEmptyExtraTestIntent(testRid));
        sleepMs(100);

        assertTrue(sm.isRunning());
        assertEquals(0, sm.getBusyReportCounter());

        // sending again MSG_START message
        sendStartMsg(sm, getEmptyExtraTestIntent(testRid));
        sleepMs(100);

        //Busy report is expected
        assertEquals(1, sm.getBusyReportCounter());

        mHandlerThread.quit();
    }

    /**
     * StateMachine : InitState - (MSG_START) -> CreatingJobState -> (Empty Extra Intent) -> EndState
     */
    @Test
    public void GivenScanJobIntentServiceStateMachine_WhenJobCreationFailedWithEmptyExtraIntent_ThenTransitionToEnd() {
        HandlerThread mHandlerThread = new HandlerThread("Test:" + getClass().getSimpleName(), android.os.Process.THREAD_PRIORITY_LESS_FAVORABLE);
        mHandlerThread.start();
        TestScanJobIntentServiceStateMachine sm = new TestScanJobIntentServiceStateMachine(mockIntentService, mHandlerThread.getLooper(), true);

        //start state machine by sending MSG_START message (with Empty Extra Intent)
        sendStartMsg(sm, getEmptyExtraTestIntent(testRid));
        sleepMs(100);

        assertTrue(sm.getCurrentState() instanceof CreatingScanJobState);
        assertTrue(sm.getCurrentState().getNextState() instanceof EndState);

        sm.transitionTo(sm.getCurrentState().getNextState());

        assertFalse(sm.isRunning());
        mHandlerThread.quit();
    }

    /**
     * TODO : To test more to initialize a scan job for the instrumented tests, it needs to make mocks such as
     *        SelectedPrinterHelper.get(), which requires content provider for com.hp.jetadvantage.link.authority.lsmcp.oxp,
     *        content provider for com.hp.jetadvantage.link.authority.joblet,
     *        and so on.. later
     */

    /**
     * StateMachine : InitState - (MSG_START) -> CreatingJobState -> () -> ReportErrorState -> EndState
     * TODO:... to be continued
     */
    public void GivenScanJobIntentServiceStateMachine_WhenJobCreationFailedWithoutNextState_ThenReportErrorToApp() {
        HandlerThread mHandlerThread = new HandlerThread("Test:" + getClass().getSimpleName(), android.os.Process.THREAD_PRIORITY_LESS_FAVORABLE);
        mHandlerThread.start();
        TestScanJobIntentServiceStateMachine sm = new TestScanJobIntentServiceStateMachine(mockIntentService, mHandlerThread.getLooper(), true);

        //start state machine by sending MSG_START message
        sendStartMsg(sm, getTestIntent(testRid));
        sleepMs(100);

        assertTrue(sm.getCurrentState() instanceof CreatingScanJobState);
        assertTrue(sm.getCurrentState().getNextState() instanceof ReportErrorState);

        sm.transitionTo(sm.getCurrentState().getNextState());
        assertTrue(sm.getCurrentState() instanceof EndState);

        sm.transitionTo(sm.getCurrentState().getNextState());
        assertFalse(sm.isRunning());
        mHandlerThread.quit();
    }

    private Intent getEmptyExtraTestIntent(String rid) {
        final ScanToRequestIntent intent = new ScanToRequestIntent();
        final ScanToRequestIntent.IntentParams params = new ScanToRequestIntent.IntentParams(null, null, rid, testPackageName, null, rid, null, null, Sdk.VERSION.LEVEL);
        intent.putIntentParams(params);
        intent.setPackage(Sdk.SERVICES_PACKAGE);
        return intent;
    }

    private Intent getTestIntent(String rid) {
        ScanAttributes scanAttributes = JsonParser.getInstance().fromJson("{\"mVersion\":9}", ScanAttributes.class);
        ;
        final ScanToRequestIntent intent = new ScanToRequestIntent();
        final ScanToRequestIntent.IntentParams params = new ScanToRequestIntent.IntentParams(scanAttributes, null, rid, testPackageName, null, rid, null, null, Sdk.VERSION.LEVEL);
        intent.putIntentParams(params);
        intent.setPackage(Sdk.SERVICES_PACKAGE);

        intent.putExtra(EXTRA_PARAMS, intent.getExtras());
        return intent;
    }

    public class TestScanJobIntentServiceStateMachine extends ScanJobIntentServiceStateMachine {
        TestScanJobIntentServiceStateMachine(IntentService service, Looper looper, boolean stopFollowingStateTransition) {
            super(service, looper, stopFollowingStateTransition);
        }

        public void transitionTo(BaseJobIntentServiceState state) {
            super.transitionTo(state);
        }
    }
}
