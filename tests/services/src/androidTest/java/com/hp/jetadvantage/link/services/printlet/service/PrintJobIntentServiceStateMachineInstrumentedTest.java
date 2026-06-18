package com.hp.jetadvantage.link.services.printlet.service;

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
import com.hp.jetadvantage.link.api.printer.intent.PrintRequestIntent;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentService;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentServiceState;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.EndState;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.InitState;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

@RunWith(AndroidJUnit4.class)
public class PrintJobIntentServiceStateMachineInstrumentedTest extends BaseInstrumentedTest {
    IntentService mockIntentService;
    String testPackageName = "printlet.test.package";
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
     * StateMachine : InitState -> CreatingPrintJobState
     */
    @Test
    public void GivenPrintJobIntentServiceStateMachine_WhenMsgStartReceived_ThenTransitToCreatingJobState() {
        HandlerThread mHandlerThread = new HandlerThread("Test:" + getClass().getSimpleName(), android.os.Process.THREAD_PRIORITY_LESS_FAVORABLE);
        mHandlerThread.start();

        PrintJobIntentServiceStateMachine sm = new PrintJobIntentServiceStateMachine(mockIntentService, mHandlerThread.getLooper(), true);

        // InitState is the default state
        assertTrue(sm.getCurrentState() instanceof InitState);

        sendStartMsg(sm, getPrintTestIntent(testRid));
        sleepMs(100);

        assertTrue(sm.isRunning());
        // CreatingPrintJobState is the next state
        assertTrue(sm.getCurrentState() instanceof CreatingPrintJobState);

        mHandlerThread.quit();
    }

    /**
     * StateMachine : InitState - (MSG_START) -> CreatingPrintJobState -> (MSG_START) -> Report Busy
     */
    @Test
    public void GivenPrintJobIntentServiceStateMachine_WhenMsgStartReceivedDuringRunning_ThenReportBusyToApp() {
        HandlerThread mHandlerThread = new HandlerThread("Test:" + getClass().getSimpleName(), android.os.Process.THREAD_PRIORITY_LESS_FAVORABLE);
        mHandlerThread.start();
        PrintJobIntentServiceStateMachine sm = new PrintJobIntentServiceStateMachine(mockIntentService, mHandlerThread.getLooper(), true);

        // start state machine by sending MSG_START message
        sendStartMsg(sm, getPrintTestIntent(testRid));
        sleepMs(500);

        assertTrue(sm.isRunning());
        assertEquals(0, sm.getBusyReportCounter());

        // sending again MSG_START message
        sendStartMsg(sm, getPrintTestIntent(testRid));
        sleepMs(500);

        // Busy report is expected
        assertEquals(1, sm.getBusyReportCounter());

        mHandlerThread.quit();
    }

    /**
     * StateMachine : InitState - (MSG_START with null Intent) -> CreatingPrintJobState -> EndState
     * When jobIntent is null, CreatingJobState.onProcess() returns EndState directly.
     */
    @Test
    public void GivenPrintJobIntentServiceStateMachine_WhenJobCreationFailedWithEmptyExtraIntent_ThenTransitToEnd() {
        HandlerThread mHandlerThread = new HandlerThread("Test:" + getClass().getSimpleName(), android.os.Process.THREAD_PRIORITY_LESS_FAVORABLE);
        mHandlerThread.start();
        TestPrintJobIntentServiceStateMachine sm = new TestPrintJobIntentServiceStateMachine(mockIntentService, mHandlerThread.getLooper(), true);

        // start state machine by sending MSG_START message with null intent
        // null jobIntent causes CreatingJobState.onProcess() to return EndState directly
        sendStartMsg(sm, null);
        sleepMs(100);

        assertTrue(sm.getCurrentState() instanceof CreatingPrintJobState);
        assertTrue(sm.getCurrentState().getNextState() instanceof EndState);

        sm.transitionTo(sm.getCurrentState().getNextState());

        assertFalse(sm.isRunning());
        mHandlerThread.quit();
    }

    private Intent getPrintTestIntent(String rid) {
        final PrintRequestIntent printIntent = new PrintRequestIntent();
        final PrintRequestIntent.IntentParams params = new PrintRequestIntent.IntentParams(
                null, null, rid, testPackageName, null, Sdk.VERSION.LEVEL);
        printIntent.putIntentParams(params);
        printIntent.setPackage(Sdk.SERVICES_PACKAGE);
        Bundle bundle = printIntent.getExtras();
        Intent intent = new Intent(context, PrintJobIntentService.class);
        intent.putExtra(BaseJobIntentService.EXTRA_PARAMS, bundle);
        return intent;
    }

    public static class TestPrintJobIntentServiceStateMachine extends PrintJobIntentServiceStateMachine {
        TestPrintJobIntentServiceStateMachine(IntentService service, Looper looper, boolean stopFollowingStateTransition) {
            super(service, looper, stopFollowingStateTransition);
        }

        public void transitionTo(BaseJobIntentServiceState state) {
            super.transitionTo(state);
        }
    }
}
