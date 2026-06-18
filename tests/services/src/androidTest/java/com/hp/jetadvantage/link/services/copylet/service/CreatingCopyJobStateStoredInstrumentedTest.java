package com.hp.jetadvantage.link.services.copylet.service;

import static com.hp.jetadvantage.link.services.copylet.service.CopyJobIntentService.PARAMS_TYPE_COPY;
import static com.hp.jetadvantage.link.services.copylet.service.CopyJobIntentService.PARAMS_TYPE_RELEASE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.ResultReceiver;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.hp.jetadvantage.link.BaseInstrumentedTest;
import com.hp.jetadvantage.link.api.copier.CopyAttributes;
import com.hp.jetadvantage.link.api.copier.StoredJobAttributes;
import com.hp.jetadvantage.link.api.copier.intent.CopyToRequestIntent;
import com.hp.jetadvantage.link.api.copier.intent.ReleaseRequestIntent;
import com.hp.jetadvantage.link.api.job.CopyJobData;
import com.hp.jetadvantage.link.api.job.JobInfo;
import com.hp.jetadvantage.link.common.Sdk;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.InitState;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

/**
 * Instrumented test for CreatingCopyJobState — Stored Copy Job Intent creation and StateMachine integration.
 * Verifies Intent construction for STORE, RELEASE, and DELETE operations in real Android environment.
 */
@RunWith(AndroidJUnit4.class)
public class CreatingCopyJobStateStoredInstrumentedTest extends BaseInstrumentedTest {
    private IntentService mockIntentService;
    private final String testPackageName = "copylet.test.package";
    private final String testRid = UUID.randomUUID().toString();
    private Context context;

    @Before
    public void setUp() {
        super.SetUp();
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();

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

    // ===== Test 1: Stored Copy Job Intent Creation =====

    @Test
    public void testStoredCopyJobIntentCreation() {
        Bundle extraParams = new Bundle();
        extraParams.putString("reqIDExtra", testRid);
        extraParams.putString("appPackageName", testPackageName);

        Intent intent = CopyJobIntentService.createCopyIntent(
                context, extraParams, PARAMS_TYPE_COPY, null);

        assertNotNull("Intent should not be null", intent);
        assertEquals("PARAMS_TYPE should be COPY",
                PARAMS_TYPE_COPY,
                intent.getStringExtra(CopyJobIntentService.PARAMS_TYPE));

        Bundle retrievedParams = intent.getBundleExtra(CopyJobIntentService.EXTRA_PARAMS);
        assertNotNull("Extra params bundle should not be null", retrievedParams);
    }

    // ===== Test 2: Release Intent Creation =====

    @Test
    public void testReleaseIntentCreation() {
        Bundle extraParams = new Bundle();
        extraParams.putString("reqIDExtra", testRid);
        extraParams.putString("appPackageName", testPackageName);

        Intent intent = CopyJobIntentService.createCopyIntent(
                context, extraParams, PARAMS_TYPE_RELEASE, null);

        assertNotNull("Release intent should not be null", intent);
        assertEquals("PARAMS_TYPE should be RELEASE",
                PARAMS_TYPE_RELEASE,
                intent.getStringExtra(CopyJobIntentService.PARAMS_TYPE));

        Bundle retrievedParams = intent.getBundleExtra(CopyJobIntentService.EXTRA_PARAMS);
        assertNotNull("Extra params bundle should not be null", retrievedParams);
    }

    // ===== Test 3: StateMachine Init with Stored Copy Intent =====

    @Test
    public void testStateMachineInitWithStoredCopyIntent() {
        HandlerThread handlerThread = new HandlerThread(
                "Test:" + getClass().getSimpleName(),
                android.os.Process.THREAD_PRIORITY_LESS_FAVORABLE);
        handlerThread.start();

        CopyJobIntentServiceStateMachine sm = new CopyJobIntentServiceStateMachine(
                mockIntentService, handlerThread.getLooper(), true);

        // Initial state should be InitState
        assertTrue("Initial state should be InitState",
                sm.getCurrentState() instanceof InitState);

        // Create a STORE-mode copy intent
        final CopyToRequestIntent copyIntent = new CopyToRequestIntent();
        final CopyToRequestIntent.IntentParams params = new CopyToRequestIntent.IntentParams(
                null, null, testRid, testPackageName, null, null, null, Sdk.VERSION.LEVEL);
        copyIntent.putIntentParams(params);
        copyIntent.setPackage(Sdk.SERVICES_PACKAGE);
        Bundle bundle = copyIntent.getExtras();
        Intent intent = CopyJobIntentService.createCopyIntent(
                context, bundle, PARAMS_TYPE_COPY, null);

        // Send start message (transitions to CreatingCopyJobState)
        sendStartMsg(sm, intent);
        sleepMs(500);

        assertTrue("StateMachine should be running after start", sm.isRunning());
        assertTrue("State should transition to CreatingCopyJobState",
                sm.getCurrentState() instanceof CreatingCopyJobState);

        handlerThread.quit();
    }

    // ===== Test 6: Create Release CopyJob Bundle Verification =====

    @Test
    public void testCreateReleaseCopyJobBundleStructure() {
        // Verify intent construction for a release operation has correct components
        Bundle extraParams = new Bundle();
        extraParams.putString("reqIDExtra", testRid);
        extraParams.putString("appPackageName", testPackageName);

        Intent releaseIntent = CopyJobIntentService.createCopyIntent(
                context, extraParams, PARAMS_TYPE_RELEASE, null);

        // Verify intent has correct structure
        assertNotNull("Release intent should not be null", releaseIntent);
        String paramsType = releaseIntent.getStringExtra(CopyJobIntentService.PARAMS_TYPE);
        assertEquals("Params type should be RELEASE", PARAMS_TYPE_RELEASE, paramsType);

        Bundle extras = releaseIntent.getBundleExtra(CopyJobIntentService.EXTRA_PARAMS);
        assertNotNull("Extra params should exist", extras);

        // Verify that no ResultReceiver was set for release (unlike delete)
        ResultReceiver receiver = releaseIntent.getParcelableExtra(
                CopyJobIntentService.EXTRA_RESULT_RECEIVER);
        assertTrue("Release intent should not have ResultReceiver", receiver == null);
    }
}
