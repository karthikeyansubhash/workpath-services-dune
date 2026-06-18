package com.hp.jetadvantage.link.services.joblet.jobintentservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Tests for concurrent/sequential scan job submission scenarios (5 jobs).
 * Validates requirements:
 * - Req 2: Exclusive single-scan — sequential mode returns existing SM (no new SM created)
 * - Req 3: Concurrent mode accepts multiple jobs up to pool limit
 * - Req 4: Default behavior = single-scan (sequential)
 * - Req 5: Pre-1.6.3 backward compatibility (sequential)
 *
 * Current behavior for sequential mode (allowConcurrentJobs=false):
 *   - First acquire → creates new SM
 *   - Subsequent acquires while SM active → returns the SAME existing SM (reuse)
 *   - The caller (onStartCommand) sends MSG_START to the reused SM
 */
@RunWith(MockitoJUnitRunner.Silent.class)
public class StateMachinePoolConcurrencyUnitTest {

    @Mock BaseJobIntentService mockService;
    @Mock BaseJobIntentServiceStateMachine mockSM1;
    @Mock BaseJobIntentServiceStateMachine mockSM2;
    @Mock BaseJobIntentServiceStateMachine mockSM3;
    @Mock BaseJobIntentServiceStateMachine mockSM4;
    @Mock BaseJobIntentServiceStateMachine mockSM5;
    @Mock BaseJobIntentServiceStateMachine mockSM6;

    private StateMachinePool pool;
    private static final int POOL_SIZE = 5;

    @Before
    public void setUp() {
        pool = new StateMachinePool(mockService, POOL_SIZE);
    }

    // ==================== Req 3: Concurrent 5 Jobs All Succeed ====================

    @Test
    public void GivenConcurrentMode_When5JobsSubmitted_ThenAll5AreAccepted() {
        when(mockService.createStateMachine(any(), any()))
                .thenReturn(mockSM1, mockSM2, mockSM3, mockSM4, mockSM5);

        // When: submit 5 jobs concurrently
        BaseJobIntentServiceStateMachine sm1 = pool.acquire(true);
        BaseJobIntentServiceStateMachine sm2 = pool.acquire(true);
        BaseJobIntentServiceStateMachine sm3 = pool.acquire(true);
        BaseJobIntentServiceStateMachine sm4 = pool.acquire(true);
        BaseJobIntentServiceStateMachine sm5 = pool.acquire(true);

        // Then: all 5 are accepted with distinct SMs
        assertNotNull(sm1);
        assertNotNull(sm2);
        assertNotNull(sm3);
        assertNotNull(sm4);
        assertNotNull(sm5);
        assertEquals(5, pool.getStats().activeCount);
    }

    @Test
    public void GivenConcurrentModePoolFull_When6thJobSubmitted_ThenRejected() {
        when(mockService.createStateMachine(any(), any()))
                .thenReturn(mockSM1, mockSM2, mockSM3, mockSM4, mockSM5);

        pool.acquire(true);
        pool.acquire(true);
        pool.acquire(true);
        pool.acquire(true);
        pool.acquire(true);

        // When: 6th job submitted (pool full)
        BaseJobIntentServiceStateMachine result = pool.acquire(true);

        // Then: rejected (null → reportBusyToApp will be called by caller)
        assertNull(result);
        assertEquals(5, pool.getStats().activeCount);
    }

    @Test
    public void GivenConcurrentMode_When5JobsComplete_ThenPoolIsEmpty() {
        when(mockService.createStateMachine(any(), any()))
                .thenReturn(mockSM1, mockSM2, mockSM3, mockSM4, mockSM5);

        pool.acquire(true);
        pool.acquire(true);
        pool.acquire(true);
        pool.acquire(true);
        pool.acquire(true);

        // When: all 5 complete
        pool.onStateMachineCompleted(mockSM1);
        pool.onStateMachineCompleted(mockSM2);
        pool.onStateMachineCompleted(mockSM3);
        pool.onStateMachineCompleted(mockSM4);
        boolean lastEmpty = pool.onStateMachineCompleted(mockSM5);

        // Then: pool empty, last returns true
        assertTrue(lastEmpty);
        assertEquals(0, pool.getStats().activeCount);
    }

    @Test
    public void GivenConcurrentMode_WhenJobsCompleteInArbitraryOrder_ThenCountDecrementsCorrectly() {
        when(mockService.createStateMachine(any(), any()))
                .thenReturn(mockSM1, mockSM2, mockSM3, mockSM4, mockSM5);

        pool.acquire(true);
        pool.acquire(true);
        pool.acquire(true);
        pool.acquire(true);
        pool.acquire(true);

        // When: jobs complete in arbitrary order
        pool.onStateMachineCompleted(mockSM3);
        assertEquals(4, pool.getStats().activeCount);

        pool.onStateMachineCompleted(mockSM1);
        assertEquals(3, pool.getStats().activeCount);

        pool.onStateMachineCompleted(mockSM5);
        assertEquals(2, pool.getStats().activeCount);

        pool.onStateMachineCompleted(mockSM2);
        assertEquals(1, pool.getStats().activeCount);

        boolean lastEmpty = pool.onStateMachineCompleted(mockSM4);
        assertTrue(lastEmpty);
        assertEquals(0, pool.getStats().activeCount);
    }

    // ==================== Req 2: Sequential Mode - Reuses Existing SM ====================

    @Test
    public void GivenSequentialMode_When5JobsSubmitted_ThenAllReturnSameSM() {
        // Given: sequential mode, first acquire creates SM
        when(mockService.createStateMachine(any(), any())).thenReturn(mockSM1);

        // When: submit 5 jobs in sequential mode
        BaseJobIntentServiceStateMachine sm1 = pool.acquire(false);
        BaseJobIntentServiceStateMachine sm2 = pool.acquire(false);
        BaseJobIntentServiceStateMachine sm3 = pool.acquire(false);
        BaseJobIntentServiceStateMachine sm4 = pool.acquire(false);
        BaseJobIntentServiceStateMachine sm5 = pool.acquire(false);

        // Then: all return the SAME existing SM (reuse, not reject)
        assertNotNull(sm1);
        assertSame(sm1, sm2);
        assertSame(sm1, sm3);
        assertSame(sm1, sm4);
        assertSame(sm1, sm5);
        // Only 1 SM in pool (no new ones created)
        assertEquals(1, pool.getStats().activeCount);
    }

    @Test
    public void GivenSequentialMode_WhenFirstCompletes_ThenNextCreatesNewSM() {
        when(mockService.createStateMachine(any(), any()))
                .thenReturn(mockSM1, mockSM2);

        BaseJobIntentServiceStateMachine sm1 = pool.acquire(false);
        assertNotNull(sm1);
        // Reuse while active
        assertSame(sm1, pool.acquire(false));

        // When: first completes
        pool.onStateMachineCompleted(sm1);

        // Then: next acquire creates a fresh SM
        BaseJobIntentServiceStateMachine sm2 = pool.acquire(false);
        assertNotNull(sm2);
        assertEquals(mockSM2, sm2);
        assertEquals(1, pool.getStats().activeCount);
    }

    @Test
    public void GivenSequentialMode_When5JobsProcessedSerially_ThenAllSucceedOneAtATime() {
        when(mockService.createStateMachine(any(), any()))
                .thenReturn(mockSM1, mockSM2, mockSM3, mockSM4, mockSM5);

        // When: submit → complete → submit → complete... (serial)
        for (int i = 0; i < 5; i++) {
            BaseJobIntentServiceStateMachine sm = pool.acquire(false);
            assertNotNull("Job " + (i + 1) + " should be accepted", sm);
            assertEquals(1, pool.getStats().activeCount);
            pool.onStateMachineCompleted(sm);
            assertEquals(0, pool.getStats().activeCount);
        }
    }

    // ==================== Pool Recovery After Full ====================

    @Test
    public void GivenConcurrentModePoolFull_WhenSomeComplete_ThenNewJobsAccepted() {
        when(mockService.createStateMachine(any(), any()))
                .thenReturn(mockSM1, mockSM2, mockSM3, mockSM4, mockSM5);

        pool.acquire(true);
        pool.acquire(true);
        pool.acquire(true);
        pool.acquire(true);
        pool.acquire(true);
        // Pool full
        assertNull(pool.acquire(true));

        // When: 2 jobs complete, freeing 2 slots
        pool.onStateMachineCompleted(mockSM1);
        pool.onStateMachineCompleted(mockSM3);
        assertEquals(3, pool.getStats().activeCount);

        // Then: new job can be accepted
        when(mockService.createStateMachine(any(), any())).thenReturn(mockSM6);
        BaseJobIntentServiceStateMachine newSm = pool.acquire(true);
        assertNotNull(newSm);
        assertEquals(4, pool.getStats().activeCount);
    }

    // ==================== Mixed Mode: concurrent + sequential interleaved ====================

    @Test
    public void GivenConcurrentJobsRunning_WhenSequentialJobSubmitted_ThenReusesExistingSM() {
        // Given: 3 concurrent jobs are running
        when(mockService.createStateMachine(any(), any()))
                .thenReturn(mockSM1, mockSM2, mockSM3);

        BaseJobIntentServiceStateMachine sm1 = pool.acquire(true);
        BaseJobIntentServiceStateMachine sm2 = pool.acquire(true);
        BaseJobIntentServiceStateMachine sm3 = pool.acquire(true);
        assertEquals(3, pool.getStats().activeCount);

        // When: a sequential job (allowConcurrent=false) is submitted
        BaseJobIntentServiceStateMachine smSeq = pool.acquire(false);

        // Then: returns one of the existing SMs (reuse, no new SM created)
        assertNotNull(smSeq);
        assertTrue(smSeq == sm1 || smSeq == sm2 || smSeq == sm3);
        assertEquals(3, pool.getStats().activeCount); // count unchanged
    }

    @Test
    public void GivenSequentialJobRunning_WhenConcurrentJobSubmitted_ThenCreatesNewSM() {
        // Given: 1 sequential job is running
        when(mockService.createStateMachine(any(), any()))
                .thenReturn(mockSM1, mockSM2, mockSM3, mockSM4);

        BaseJobIntentServiceStateMachine smSeq = pool.acquire(false);
        assertNotNull(smSeq);
        assertEquals(1, pool.getStats().activeCount);

        // When: concurrent jobs (allowConcurrent=true) are submitted
        BaseJobIntentServiceStateMachine smC1 = pool.acquire(true);
        BaseJobIntentServiceStateMachine smC2 = pool.acquire(true);
        BaseJobIntentServiceStateMachine smC3 = pool.acquire(true);

        // Then: new SMs are created for each concurrent request
        assertNotNull(smC1);
        assertNotNull(smC2);
        assertNotNull(smC3);
        assertEquals(4, pool.getStats().activeCount);
    }

    @Test
    public void GivenMixedJobs_WhenAllComplete_ThenPoolEmptiesCorrectly() {
        // Given: mix of concurrent and sequential submissions
        when(mockService.createStateMachine(any(), any()))
                .thenReturn(mockSM1, mockSM2, mockSM3);

        // 1st: sequential (creates SM1)
        BaseJobIntentServiceStateMachine sm1 = pool.acquire(false);
        assertEquals(1, pool.getStats().activeCount);

        // 2nd: concurrent (creates SM2)
        BaseJobIntentServiceStateMachine sm2 = pool.acquire(true);
        assertEquals(2, pool.getStats().activeCount);

        // 3rd: sequential again → reuses existing (pool not empty)
        BaseJobIntentServiceStateMachine sm3 = pool.acquire(false);
        assertTrue(sm3 == sm1 || sm3 == sm2); // reuses one of existing
        assertEquals(2, pool.getStats().activeCount); // no new SM

        // 4th: concurrent (creates SM3)
        BaseJobIntentServiceStateMachine sm4 = pool.acquire(true);
        assertNotNull(sm4);
        assertEquals(3, pool.getStats().activeCount);

        // When: all distinct SMs complete
        pool.onStateMachineCompleted(sm1);
        pool.onStateMachineCompleted(sm2);
        boolean lastEmpty = pool.onStateMachineCompleted(sm4);

        // Then: pool is empty
        assertTrue(lastEmpty);
        assertEquals(0, pool.getStats().activeCount);
    }
}
