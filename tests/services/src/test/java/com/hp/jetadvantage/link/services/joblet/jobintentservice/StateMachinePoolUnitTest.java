package com.hp.jetadvantage.link.services.joblet.jobintentservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
public class StateMachinePoolUnitTest {

    @Mock
    BaseJobIntentService mockService;

    @Mock
    BaseJobIntentServiceStateMachine mockStateMachine1;

    @Mock
    BaseJobIntentServiceStateMachine mockStateMachine2;

    @Mock
    BaseJobIntentServiceStateMachine mockStateMachine3;

    private StateMachinePool stateMachinePool;
    private static final int MAX_POOL_SIZE = 3;

    @Before
    public void setUp() {
        stateMachinePool = new StateMachinePool(mockService, MAX_POOL_SIZE);
    }

    // ==================== Initialization Tests ====================

    @Test
    public void GivenStateMachinePool_WhenPoolCreated_ThenStatsAreZero() {
        // When: pool is created
        StateMachinePool.PoolStats stats = stateMachinePool.getStats();

        // Then: no active StateMachines
        assertEquals(0, stats.activeCount);
        assertEquals(MAX_POOL_SIZE, stats.maxSize);
    }

    // ==================== Sequential Mode Tests ====================

    @Test
    public void GivenStateMachinePool_WhenAcquireInSequentialMode_ThenReturnsNewStateMachine() {
        // Given: mock creates new StateMachine
        when(mockService.createStateMachine(any(), any())).thenReturn(mockStateMachine1);

        // When: acquire in sequential mode
        BaseJobIntentServiceStateMachine result = stateMachinePool.acquire(false);

        // Then: returns new StateMachine
        assertEquals(mockStateMachine1, result);
        assertEquals(1, stateMachinePool.getStats().activeCount);
    }

    @Test
    public void GivenStateMachinePool_WhenAcquireMultipleInSequentialMode_ThenReturnsExistingStateMachine() {
        // Given: first StateMachine acquired
        when(mockService.createStateMachine(any(), any())).thenReturn(mockStateMachine1);
        BaseJobIntentServiceStateMachine first = stateMachinePool.acquire(false);

        // When: try to acquire second in sequential mode
        BaseJobIntentServiceStateMachine result = stateMachinePool.acquire(false);

        // Then: returns the existing active StateMachine
        assertEquals(first, result);
        assertEquals(1, stateMachinePool.getStats().activeCount);
    }

    @Test
    public void GivenStateMachinePool_WhenStateMachineCompletedInSequentialMode_ThenRemovedFromPool() {
        // Given: StateMachine acquired
        when(mockService.createStateMachine(any(), any())).thenReturn(mockStateMachine1);
        BaseJobIntentServiceStateMachine sm = stateMachinePool.acquire(false);

        // When: StateMachine completed
        boolean isEmpty = stateMachinePool.onStateMachineCompleted(sm);

        // Then: removed from pool and pool is empty
        assertTrue(isEmpty);
        assertEquals(0, stateMachinePool.getStats().activeCount);
    }

    // ==================== Concurrent Mode Tests ====================

    @Test
    public void GivenStateMachinePool_WhenAcquireInConcurrentMode_ThenReturnsNewStateMachine() {
        // Given: mock creates new StateMachine
        when(mockService.createStateMachine(any(), any())).thenReturn(mockStateMachine1);

        // When: acquire in concurrent mode
        BaseJobIntentServiceStateMachine result = stateMachinePool.acquire(true);

        // Then: returns new StateMachine
        assertEquals(mockStateMachine1, result);
        assertEquals(1, stateMachinePool.getStats().activeCount);
    }

    @Test
    public void GivenStateMachinePool_WhenAcquireMultipleInConcurrentMode_ThenCreatesMultiple() {
        // Given: mock creates multiple StateMachines
        when(mockService.createStateMachine(any(), any()))
                .thenReturn(mockStateMachine1)
                .thenReturn(mockStateMachine2);

        // When: acquire multiple times
        BaseJobIntentServiceStateMachine sm1 = stateMachinePool.acquire(true);
        BaseJobIntentServiceStateMachine sm2 = stateMachinePool.acquire(true);

        // Then: creates 2 StateMachines
        assertEquals(mockStateMachine1, sm1);
        assertEquals(mockStateMachine2, sm2);
        assertEquals(2, stateMachinePool.getStats().activeCount);
    }

    @Test
    public void GivenStateMachinePool_WhenAcquireExceedsMaxPoolSize_ThenReturnsNull() {
        // Given: pool at max capacity
        when(mockService.createStateMachine(any(), any()))
                .thenReturn(mockStateMachine1)
                .thenReturn(mockStateMachine2)
                .thenReturn(mockStateMachine3);

        stateMachinePool.acquire(true); // 1st
        stateMachinePool.acquire(true); // 2nd
        stateMachinePool.acquire(true); // 3rd (max reached)

        // When: try to acquire when pool is full
        BaseJobIntentServiceStateMachine result = stateMachinePool.acquire(true);

        // Then: returns null
        assertNull(result);
        assertEquals(3, stateMachinePool.getStats().activeCount);
    }

    @Test
    public void GivenStateMachinePool_WhenStateMachineCompleted_ThenRemovedFromPool() {
        // Given: StateMachine acquired
        when(mockService.createStateMachine(any(), any())).thenReturn(mockStateMachine1);
        BaseJobIntentServiceStateMachine sm = stateMachinePool.acquire(true);

        // When: StateMachine completed
        boolean isEmpty = stateMachinePool.onStateMachineCompleted(sm);

        // Then: removed from pool and pool is empty
        assertTrue(isEmpty);
        assertEquals(0, stateMachinePool.getStats().activeCount);
    }

    @Test
    public void GivenStateMachinePool_WhenNonExistentStateMachineCompleted_ThenNoError() {
        // Given: StateMachine not in pool

        // When: try to complete non-existent StateMachine
        boolean isEmpty = stateMachinePool.onStateMachineCompleted(mockStateMachine1);

        // Then: no error, pool reports empty
        assertTrue(isEmpty);
        assertEquals(0, stateMachinePool.getStats().activeCount);
    }

    // ==================== Shutdown Tests ====================

    @Test
    public void GivenStateMachinePool_WhenShutdown_ThenPoolCleared() {
        // Given: pool has active StateMachines
        when(mockService.createStateMachine(any(), any()))
                .thenReturn(mockStateMachine1)
                .thenReturn(mockStateMachine2);
        stateMachinePool.acquire(true);
        stateMachinePool.acquire(true);

        // When: shutdown
        stateMachinePool.shutdown();

        // Then: pool cleared
        assertEquals(0, stateMachinePool.getStats().activeCount);
    }

    @Test
    public void GivenStateMachinePool_WhenShutdownEmptyPool_ThenNoError() {
        // Given: empty pool

        // When: shutdown
        stateMachinePool.shutdown();

        // Then: no error
        assertEquals(0, stateMachinePool.getStats().activeCount);
    }

    // ==================== Statistics Tests ====================

    @Test
    public void GivenStateMachinePool_WhenGetStats_ThenReturnsCorrectStatistics() {
        // Given: pool with active StateMachines
        when(mockService.createStateMachine(any(), any())).thenReturn(mockStateMachine1);
        stateMachinePool.acquire(true);

        // When: get stats
        StateMachinePool.PoolStats stats = stateMachinePool.getStats();

        // Then: correct statistics
        assertEquals(1, stats.activeCount);
        assertEquals(MAX_POOL_SIZE, stats.maxSize);
    }

    // ==================== Edge Cases ====================

    @Test
    public void GivenStateMachinePool_WhenAcquireCompleteRepeatedly_ThenHandlesCorrectly() {
        // When: acquire and complete repeatedly
        when(mockService.createStateMachine(any(), any()))
                .thenReturn(mockStateMachine1)
                .thenReturn(mockStateMachine2)
                .thenReturn(mockStateMachine3);

        for (int i = 0; i < 3; i++) {
            BaseJobIntentServiceStateMachine sm = stateMachinePool.acquire(true);
            assertNotNull(sm);
            assertEquals(1, stateMachinePool.getStats().activeCount);

            boolean isEmpty = stateMachinePool.onStateMachineCompleted(sm);
            assertTrue(isEmpty);
            assertEquals(0, stateMachinePool.getStats().activeCount);
        }

        // Then: pool is still healthy
        assertEquals(0, stateMachinePool.getStats().activeCount);
    }

    @Test
    public void GivenStateMachinePool_WhenCompleteMultipleTimes_ThenHandlesGracefully() {
        // Given: acquired StateMachine
        when(mockService.createStateMachine(any(), any())).thenReturn(mockStateMachine1);
        BaseJobIntentServiceStateMachine sm = stateMachinePool.acquire(true);

        // When: complete multiple times
        boolean isEmpty1 = stateMachinePool.onStateMachineCompleted(sm);
        boolean isEmpty2 = stateMachinePool.onStateMachineCompleted(sm); // Second time
        boolean isEmpty3 = stateMachinePool.onStateMachineCompleted(sm); // Third time

        // Then: no errors, all report empty
        assertTrue(isEmpty1);
        assertTrue(isEmpty2);
        assertTrue(isEmpty3);
        assertEquals(0, stateMachinePool.getStats().activeCount);
    }
}

