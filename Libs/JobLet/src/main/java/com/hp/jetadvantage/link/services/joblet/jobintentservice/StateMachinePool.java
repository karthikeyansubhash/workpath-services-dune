package com.hp.jetadvantage.link.services.joblet.jobintentservice;

import android.app.IntentService;
import android.os.HandlerThread;

import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.services.common.util.WakeLock;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Simple StateMachine Pool with on-demand creation
 * <p>
 * Design Philosophy:
 * - Create new StateMachine for each job
 * - StateMachine self-destructs after job completion
 * - No reuse - simple and clean lifecycle
 * - Pool only tracks concurrent execution count
 * <p>
 * Lifecycle:
 * 1. acquire() → Create new StateMachine + HandlerThread
 * 2. Job processing → StateMachine handles job
 * 3. EndState → StateMachine.stop() → self-destruct (quitSafely)
 * 4. Pool automatically decrements active count
 * <p>
 * Advantages:
 * - Simple design - no complex state management
 * - Clear lifecycle - each job is independent
 * - No memory leaks - immediate cleanup
 * - Easy debugging - each StateMachine is fresh
 */
public class StateMachinePool {
    private static final String TAG = "SMPool";

    private final int maxPoolSize;
    private IntentService service;

    // Track active StateMachines: StateMachine → HandlerThread
    private final ConcurrentHashMap<BaseJobIntentServiceStateMachine, HandlerThread> activeStateMachines =
            new ConcurrentHashMap<>();

    // StateMachine ID counter (for logging)
    private final AtomicInteger stateMachineCounter = new AtomicInteger(0);

    // Track WakeLock references acquired by this pool to avoid cross-service drain
    private int wakeLockCount = 0;

    /**
     * Constructor
     *
     * @param service     IntentService instance
     * @param maxPoolSize Maximum concurrent jobs
     */
    public StateMachinePool(IntentService service, int maxPoolSize) {
        this.service = service;
        this.maxPoolSize = maxPoolSize;
        SLog.i(TAG, "Pool initialized (max: " + maxPoolSize + ")");
    }

    /**
     * Acquire new StateMachine for a job
     * Creates a fresh StateMachine each time
     *
     * @param allowConcurrentJobs if false, reject if any job is running (sequential mode);
     *                            if true, allow up to maxPoolSize concurrent jobs
     * @return new StateMachine, or null if pool is full
     */
    public synchronized BaseJobIntentServiceStateMachine acquire(boolean allowConcurrentJobs) {
        // Check concurrent mode
        if (!allowConcurrentJobs) {
            if (!activeStateMachines.isEmpty()) {
                // Return the currently running StateMachine for sequential mode
                BaseJobIntentServiceStateMachine existing = activeStateMachines.keySet().iterator().next();
                SLog.d(TAG, "Sequential mode: returning existing active StateMachine");
                return existing;
            }
            // else fallthrough to create a new StateMachine
        }

        // Check pool size limit
        if (activeStateMachines.size() >= maxPoolSize) {
            SLog.w(TAG, "Pool is full (max: " + maxPoolSize + ")");
            return null;
        }

        // Create new StateMachine
        int id = stateMachineCounter.incrementAndGet();
        String threadName = TAG + ":" + service.getClass().getSimpleName() + "-" + id;

        HandlerThread handlerThread = new HandlerThread(
                threadName,
                android.os.Process.THREAD_PRIORITY_LESS_FAVORABLE
        );
        handlerThread.start();

        BaseJobIntentServiceStateMachine stateMachine =
                ((BaseJobIntentService) service).createStateMachine(service, handlerThread.getLooper());

        // Track active StateMachine
        activeStateMachines.put(stateMachine, handlerThread);

        SLog.d(TAG, "Created StateMachine-" + id +
                " (active: " + activeStateMachines.size() + "/" + maxPoolSize +
                ", mode: " + (allowConcurrentJobs ? "concurrent" : "sequential") + ")");

        return stateMachine;
    }

    /**
     * Remove StateMachine from tracking and atomically check if pool is now empty.
     * Called automatically when StateMachine.stop() is invoked.
     * This method combines removal and empty-check in a single synchronized block
     * to prevent race conditions where two threads both see an empty pool.
     *
     * @param stateMachine StateMachine that completed
     * @return true if the pool has no remaining active jobs after removal
     */
    public synchronized boolean onStateMachineCompleted(BaseJobIntentServiceStateMachine stateMachine) {
        HandlerThread handlerThread = activeStateMachines.remove(stateMachine);

        if (handlerThread != null) {
            SLog.d(TAG, "StateMachine completed and removed from pool " +
                    "(active: " + activeStateMachines.size() + "/" + maxPoolSize + ")");
        } else {
            SLog.w(TAG, "StateMachine not found in active pool");
        }
        return activeStateMachines.isEmpty();
    }

    /**
     * Check if pool has any active jobs
     *
     * @return true if there are active jobs
     */
    public synchronized boolean hasActiveJobs() {
        return !activeStateMachines.isEmpty();
    }

    /**
     * Get current pool statistics
     */
    public PoolStats getStats() {
        return new PoolStats(
                activeStateMachines.size(),
                maxPoolSize
        );
    }

    /**
     * Shutdown entire pool (called on Service.onDestroy())
     * Idempotent - safe to call multiple times
     * All active StateMachines will stop themselves via their own stop() method
     */
    public synchronized void shutdown() {
        SLog.i(TAG, "Shutting down pool (active: " + activeStateMachines.size() +
                ", wakeLockCount: " + wakeLockCount + ")");

        // Force quit any remaining active threads
        // StateMachines should have already called stop() in their EndState
        for (HandlerThread thread : activeStateMachines.values()) {
            if (thread != null && thread.isAlive()) {
                SLog.w(TAG, "Force quitting active thread: " + thread.getName());
                thread.quitSafely();
            }
        }

        // Release only this pool's WakeLock references to avoid draining other services
        if (wakeLockCount > 0) {
            try {
                WakeLock.release(wakeLockCount);
            } catch (Throwable t) {
                SLog.w(TAG, "Failed to release wake locks: " + t.getMessage());
            }
            wakeLockCount = 0;
        }

        // Clean up resources
        activeStateMachines.clear();
        service = null;

        SLog.i(TAG, "Pool shutdown complete");
    }

    /**
     * Increment the WakeLock reference count tracked by this pool.
     * Called when a StateMachine acquires a WakeLock.
     */
    public synchronized void incrementWakeLockCount() {
        wakeLockCount++;
    }

    /**
     * Decrement the WakeLock reference count tracked by this pool.
     * Called when a StateMachine releases a WakeLock.
     */
    public synchronized void decrementWakeLockCount() {
        if (wakeLockCount > 0) {
            wakeLockCount--;
        }
    }

    /**
     * Find an active StateMachine by its JobId.
     *
     * @param jobId the job ID to search for
     * @return the matching StateMachine, or null if not found
     */
    public synchronized BaseJobIntentServiceStateMachine findByJobId(String jobId) {
        if (jobId == null) return null;
        for (BaseJobIntentServiceStateMachine sm : activeStateMachines.keySet()) {
            if (jobId.equalsIgnoreCase(sm.getJobId())) {
                return sm;
            }
        }
        return null;
    }

    /**
     * Pool Statistics (for monitoring)
     */
    public static class PoolStats {
        public final int activeCount;    // Jobs being processed
        public final int maxSize;        // Maximum pool size

        PoolStats(int activeCount, int maxSize) {
            this.activeCount = activeCount;
            this.maxSize = maxSize;
        }

        @androidx.annotation.NonNull
        @Override
        public String toString() {
            return String.format(java.util.Locale.US, "Pool[active=%d, max=%d]",
                    activeCount, maxSize);
        }
    }
}
