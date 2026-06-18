package com.hp.jetadvantage.link.services.joblet.jobintentservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.hp.jetadvantage.link.BaseInstrumentedTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented tests for StateMachinePool concurrent/sequential behavior.
 * Runs on real device with real HandlerThread and Looper.
 *
 * Validates:
 * - Req 3: Concurrent mode creates separate SM per job with real threads
 * - Req 2: Sequential mode reuses existing SM (single SM handles all messages)
 */
@RunWith(AndroidJUnit4.class)
public class StateMachinePoolConcurrencyInstrumentedTest extends BaseInstrumentedTest {

    private static final int POOL_SIZE = 5;
    private IntentService mockIntentService;
    private StateMachinePool pool;

    @Before
    public void SetUp() {
        super.SetUp();

        mockIntentService = new IntentService("TestService") {
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

        // Create a testable BaseJobIntentService-like wrapper to provide createStateMachine
        pool = new StateMachinePool(mockIntentService, POOL_SIZE) {
            // Pool internally calls ((BaseJobIntentService) service).createStateMachine()
            // Since our mockIntentService is not a BaseJobIntentService, we override acquire behavior
        };
    }

    @After
    public void tearDown() {
        if (pool != null) {
            pool.shutdown();
        }
    }

    /**
     * Req 3: allowMultipleScan=true → 5 concurrent jobs create 5 separate StateMachines
     * with real HandlerThreads running on the device.
     */
    @Test
    public void GivenConcurrentMode_When5JobsSubmitted_ThenAll5RunOnSeparateThreads() {
        // Use a concrete BaseJobIntentService to properly test pool.acquire()
        TestBaseJobIntentService testService = new TestBaseJobIntentService();
        StateMachinePool testPool = new StateMachinePool(testService, POOL_SIZE);

        try {
            // When: acquire 5 SMs in concurrent mode
            BaseJobIntentServiceStateMachine sm1 = testPool.acquire(true);
            BaseJobIntentServiceStateMachine sm2 = testPool.acquire(true);
            BaseJobIntentServiceStateMachine sm3 = testPool.acquire(true);
            BaseJobIntentServiceStateMachine sm4 = testPool.acquire(true);
            BaseJobIntentServiceStateMachine sm5 = testPool.acquire(true);

            // Then: all 5 are non-null and distinct
            assertNotNull(sm1);
            assertNotNull(sm2);
            assertNotNull(sm3);
            assertNotNull(sm4);
            assertNotNull(sm5);
            assertEquals(5, testPool.getStats().activeCount);

            // Each SM has its own Looper (running on separate HandlerThread)
            Looper l1 = sm1.getLooper();
            Looper l2 = sm2.getLooper();
            Looper l3 = sm3.getLooper();
            assertNotNull(l1);
            assertNotNull(l2);
            assertNotNull(l3);
            assertTrue("Each SM should run on its own thread", l1 != l2);
            assertTrue("Each SM should run on its own thread", l2 != l3);

            // 6th job should be rejected (pool full)
            BaseJobIntentServiceStateMachine sm6 = testPool.acquire(true);
            assertNull(sm6);

            // Complete all and verify pool empties
            testPool.onStateMachineCompleted(sm1);
            testPool.onStateMachineCompleted(sm2);
            testPool.onStateMachineCompleted(sm3);
            testPool.onStateMachineCompleted(sm4);
            boolean lastEmpty = testPool.onStateMachineCompleted(sm5);
            assertTrue(lastEmpty);
            assertEquals(0, testPool.getStats().activeCount);
        } finally {
            testPool.shutdown();
        }
    }

    /**
     * Req 2: allowMultipleScan=false → sequential mode returns the same SM for all requests.
     * Only 1 SM exists in the pool. New messages go to the same Handler queue.
     */
    @Test
    public void GivenSequentialMode_When5JobsSubmitted_ThenAllReturnSameSMOnSameThread() {
        TestBaseJobIntentService testService = new TestBaseJobIntentService();
        StateMachinePool testPool = new StateMachinePool(testService, POOL_SIZE);

        try {
            // When: acquire 5 times in sequential mode
            BaseJobIntentServiceStateMachine sm1 = testPool.acquire(false);
            BaseJobIntentServiceStateMachine sm2 = testPool.acquire(false);
            BaseJobIntentServiceStateMachine sm3 = testPool.acquire(false);
            BaseJobIntentServiceStateMachine sm4 = testPool.acquire(false);
            BaseJobIntentServiceStateMachine sm5 = testPool.acquire(false);

            // Then: all return the same SM instance (reuse)
            assertNotNull(sm1);
            assertSame(sm1, sm2);
            assertSame(sm1, sm3);
            assertSame(sm1, sm4);
            assertSame(sm1, sm5);

            // Only 1 SM in pool
            assertEquals(1, testPool.getStats().activeCount);

            // All share the same Looper/thread
            assertSame(sm1.getLooper(), sm2.getLooper());

            // After SM completes, pool is empty
            boolean isEmpty = testPool.onStateMachineCompleted(sm1);
            assertTrue(isEmpty);
            assertEquals(0, testPool.getStats().activeCount);

            // Next acquire creates a NEW SM (fresh thread)
            BaseJobIntentServiceStateMachine smNext = testPool.acquire(false);
            assertNotNull(smNext);
            assertTrue("New SM should be a different instance", sm1 != smNext);
            assertEquals(1, testPool.getStats().activeCount);
        } finally {
            testPool.shutdown();
        }
    }

    // ==================== Test Helpers ====================

    /**
     * Minimal BaseJobIntentService subclass for testing pool.acquire() which calls
     * ((BaseJobIntentService) service).createStateMachine(service, looper)
     */
    private class TestBaseJobIntentService extends BaseJobIntentService {
        TestBaseJobIntentService() {
            super("TestBaseJobIntentService");
        }

        @Override
        protected BaseJobIntentServiceStateMachine createStateMachine(IntentService service, Looper looper) {
            return new TestStateMachine(mockIntentService, looper);
        }

        @Override
        protected boolean cancelJob(Intent intent) {
            return false;
        }

        @Override
        protected String getJobCancelAction() {
            return "TEST_CANCEL";
        }

        @Override
        protected void reportBusyToApp(Intent intent) {
            // no-op for test
        }
    }

    /**
     * Minimal StateMachine that does nothing — just occupies a pool slot.
     */
    private static class TestStateMachine extends BaseJobIntentServiceStateMachine {
        TestStateMachine(IntentService service, Looper looper) {
            super(service, looper, "TestSM");
        }

        @Override
        public void handleMessage(android.os.Message msg) {
            // no-op: we don't process messages in this test
        }

        @Override
        protected CreatingJobState createCreatingJobState(Intent intent) {
            // no-op: we don't process messages in this test
            return null;
        }

        @Override
        protected JobCompletedState createJobCompletedState() {
            // no-op: we don't process messages in this test
            return null;
        }

        @Override
        protected void updateCanceledJobState() {
            // no-op: we don't process messages in this test
        }

        @Override
        protected void updateFailedJobState() {
            // no-op: we don't process messages in this test
        }

        @Override
        protected void updateCompletedJobState() {
            // no-op: we don't process messages in this test
        }

        @Override
        protected void reportBusyToApp(Intent intent) {
            // no-op for test
        }
    }
}
