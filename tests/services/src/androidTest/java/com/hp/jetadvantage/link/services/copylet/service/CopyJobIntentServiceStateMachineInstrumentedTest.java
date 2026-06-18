package com.hp.jetadvantage.link.services.copylet.service;

import static com.hp.jetadvantage.link.services.copylet.service.CopyJobIntentService.PARAMS_TYPE_COPY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.Looper;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.hp.jetadvantage.link.BaseInstrumentedTest;
import com.hp.jetadvantage.link.api.copier.intent.CopyToRequestIntent;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentServiceState;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.EndState;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.InitState;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

@RunWith(AndroidJUnit4.class)
public class CopyJobIntentServiceStateMachineInstrumentedTest extends BaseInstrumentedTest {
    IntentService mockIntentService;
    String testPackageName = "copylet.test.package";
    String testRid = UUID.randomUUID().toString();
    Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

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
    public void GivenCopyJobIntentServiceStateMachine_WhenMsgStartReceived_ThenTransitToCreatingJobState() {

        HandlerThread mHandlerThread = new HandlerThread("Test:" + getClass().getSimpleName(), android.os.Process.THREAD_PRIORITY_LESS_FAVORABLE);
        mHandlerThread.start();

        CopyJobIntentServiceStateMachine sm = new CopyJobIntentServiceStateMachine(mockIntentService, mHandlerThread.getLooper(), true);

        // InitState is the default state
        assertTrue(sm.getCurrentState() instanceof InitState);

        sendStartMsg(sm, getEmptyExtraTestIntent(testRid));
        sleepMs(100);

        assertTrue(sm.isRunning());
        // CreatingJobState is the next state
        assertTrue(sm.getCurrentState() instanceof CreatingCopyJobState);

        mHandlerThread.quit();
    }

    /**
     * StateMachine : InitState - (MSG_START) -> CreatingJobState -> (MSG_START) -> Report Busy
     */
    @Test
    public void GivenCopyJobIntentServiceStateMachine_WhenMsgStartReceivedDuringRunning_ThenReportBusyToApp() {

        HandlerThread mHandlerThread = new HandlerThread("Test:" + getClass().getSimpleName(), android.os.Process.THREAD_PRIORITY_LESS_FAVORABLE);
        mHandlerThread.start();
        CopyJobIntentServiceStateMachine sm = (new CopyJobIntentServiceStateMachine(mockIntentService, mHandlerThread.getLooper(), true));

        //start state machine by sending MSG_START message
        sendStartMsg(sm, getCopyTestIntent(testRid));
        sleepMs(500);

        assertTrue(sm.isRunning());
        assertEquals(0, sm.getBusyReportCounter());

        // sending again MSG_START message
        sendStartMsg(sm, getCopyTestIntent(testRid));
        sleepMs(500);

        //Busy report is expected
        assertEquals(1, sm.getBusyReportCounter());

        mHandlerThread.quit();
    }

    /**
     * StateMachine : InitState - (MSG_START) -> CreatingJobState -> (Empty Extra Intent) -> EndState
     */

    @Test
    public void GivenCopyJobIntentServiceStateMachine_WhenJobCreationFailedWithEmptyExtraIntent_ThenTransitToEnd() {
        HandlerThread mHandlerThread = new HandlerThread("Test:" + getClass().getSimpleName(), android.os.Process.THREAD_PRIORITY_LESS_FAVORABLE);
        mHandlerThread.start();
        CopyJobIntentServiceStateMachineInstrumentedTest.TestCopyJobIntentServiceStateMachine sm = new TestCopyJobIntentServiceStateMachine(mockIntentService, mHandlerThread.getLooper(), true);

        //start state machine by sending MSG_START message (with Empty Extra Intent)
        sendStartMsg(sm, getEmptyExtraTestIntent(testRid));
        sleepMs(100);

        assertTrue(sm.getCurrentState() instanceof CreatingCopyJobState);
        assertTrue(sm.getCurrentState().getNextState() instanceof EndState);

        sm.transitionTo(sm.getCurrentState().getNextState());

        assertFalse(sm.isRunning());
        mHandlerThread.quit();
    }

    private Intent getEmptyExtraTestIntent(String rid) {
        return CopyJobIntentService.createCopyIntent(context, null, PARAMS_TYPE_COPY, null);
    }

    private Intent getCopyTestIntent(String rid) {
        final CopyToRequestIntent copyIntent = new CopyToRequestIntent();
        final CopyToRequestIntent.IntentParams params = new CopyToRequestIntent.IntentParams(null, null, rid, testPackageName, null, null, null, Sdk.VERSION.LEVEL);
        copyIntent.putIntentParams(params);
        copyIntent.setPackage(Sdk.SERVICES_PACKAGE);
        Bundle bundle = copyIntent.getExtras();
        return CopyJobIntentService.createCopyIntent(context, bundle, PARAMS_TYPE_COPY, null);
    }

    public static class TestCopyJobIntentServiceStateMachine extends CopyJobIntentServiceStateMachine {
        TestCopyJobIntentServiceStateMachine(IntentService service, Looper looper, boolean stopFollowingStateTransition) {
            super(service, looper, stopFollowingStateTransition);
        }

        public void transitionTo(BaseJobIntentServiceState state) {
            super.transitionTo(state);
        }
    }
}
