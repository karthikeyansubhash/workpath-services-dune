// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.common.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;

import com.hp.jetadvantage.link.common.utils.SLog;

/**
 * Thread-safe WakeLock wrapper with manual reference counting for concurrent job processing.
 *
 * <p>Uses {@code setReferenceCounted(false)} on the underlying Android WakeLock to avoid
 * the double-release problem that occurs when {@code acquire(timeout)} + explicit
 * {@code release()} are used together with {@code setReferenceCounted(true)}.
 *
 * <p>Reference counting is managed manually via {@code sRefCount}. Each
 * {@link #acquire(Context)} increments the count; each {@link #release()} decrements it.
 * The underlying Android WakeLock is acquired when count goes from 0→1 and released
 * when count returns to 0.
 *
 * <p>A single 60-minute safety timeout is managed via a {@link Handler}. The timer starts
 * when the first reference is acquired and is cancelled when the last reference is released.
 * If a job hangs and never calls release(), the timer fires and drains all references.
 * Because the timer fires only once (not per-reference), there is no phantom double-release.
 *
 * <p>All public methods are {@code synchronized} on the class lock.
 */
public class WakeLock {

    private static final String TAG = "SDK_WAKE_LOCK";
    private static final long SAFETY_TIMEOUT_MS = 60 * 60 * 1000L;

    @SuppressLint("InlinedApi")
    private static final int FLAGS = PowerManager.SCREEN_BRIGHT_WAKE_LOCK
            | PowerManager.ACQUIRE_CAUSES_WAKEUP
            | PowerManager.ON_AFTER_RELEASE;

    private static PowerManager.WakeLock sLock;
    private static int sRefCount = 0;

    private static Handler sSafetyHandler;
    private static Runnable sSafetyRunnable;

    private WakeLock() { /* utility class */ }

    /**
     * Acquires the wake lock and increments the manual reference count.
     * The underlying Android WakeLock is acquired (without timeout) only on the first
     * reference. The 60-minute safety timeout is (re)started at that point.
     */
    @SuppressLint("InvalidWakeLockTag")
    public static synchronized void acquire(Context context, long timeoutMs, int flags, String tag) {
        ensureCreated(context, flags, tag);
        sRefCount++;
        if (sRefCount == 1) {
            safeRun(() -> sLock.acquire());
        }
        // Reset timer on every acquire so the deadline is always 60min from the *last* job start
        scheduleSafetyTimeout();
        SLog.d(TAG, "acquire: refCount=" + sRefCount);
    }

    public synchronized static void acquire(Context context, long timeoutMs) {
        acquire(context, timeoutMs, FLAGS, TAG);
    }

    @SuppressLint("InvalidWakeLockTag")
    public synchronized static void acquire(Context context) {
        acquire(context, SAFETY_TIMEOUT_MS, FLAGS, TAG);
    }

    /**
     * Releases one reference. When all references are released, the underlying lock is freed.
     */
    public static synchronized void release() {
        if (sLock == null || sRefCount <= 0) {
            SLog.w(TAG, "release: already fully released (refCount=" + sRefCount + ")");
            return;
        }
        sRefCount--;
        SLog.d(TAG, "release: refCount=" + sRefCount);
        if (sRefCount == 0) {
            cancelSafetyTimeout();
            safeRun(sLock::release);
            sLock = null;
        }
    }

    /**
     * Drains all outstanding references. Called during service shutdown as a safety net.
     * Only releases the number of references tracked by this pool's count to avoid
     * affecting other services sharing the same process.
     *
     * @param count the number of references to release (from the calling pool)
     */
    public static synchronized void release(int count) {
        for (int i = 0; i < count; i++) {
            release();
        }
    }

    /**
     * Drains all outstanding references. Called during service shutdown as a safety net.
     */
    public static synchronized void releaseAll() {
        if (sLock == null) return;
        SLog.d(TAG, "releaseAll: draining refCount=" + sRefCount);
        sRefCount = 0;
        cancelSafetyTimeout();
        safeRun(sLock::release);
        sLock = null;
    }

    /**
     * Returns the current reference count (for testing/debugging).
     */
    public static synchronized int getRefCount() {
        return sRefCount;
    }

    // ── internals ──────────────────────────────────────────────

    /**
     * Schedules a single safety timeout Runnable. There is only ever one active timer
     * regardless of how many references are held, so no phantom double-release can occur.
     */
    private static void scheduleSafetyTimeout() {
        if (sSafetyHandler == null) {
            sSafetyHandler = new Handler(Looper.getMainLooper());
        }
        cancelSafetyTimeout();
        sSafetyRunnable = () -> {
            synchronized (WakeLock.class) {
                SLog.w(TAG, "Safety timeout fired! Force-releasing all WakeLock refs (refCount=" + sRefCount + ")");
                releaseAll();
            }
        };
        sSafetyHandler.postDelayed(sSafetyRunnable, SAFETY_TIMEOUT_MS);
    }

    private static void cancelSafetyTimeout() {
        if (sSafetyHandler != null && sSafetyRunnable != null) {
            sSafetyHandler.removeCallbacks(sSafetyRunnable);
            sSafetyRunnable = null;
        }
    }

    @SuppressLint("InvalidWakeLockTag")
    private static void ensureCreated(Context context, int flags, String tag) {
        if (sLock == null) {
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            sLock = pm.newWakeLock(flags, tag);
            sLock.setReferenceCounted(false);
            sRefCount = 0;
        }
    }

    /**
     * Runs an operation that may throw on certain vendor ROMs. Returns false if it threw.
     */
    private static boolean safeRun(Runnable action) {
        try {
            action.run();
            return true;
        } catch (RuntimeException e) {
            SLog.w(TAG, "WakeLock operation failed: " + e.getMessage());
            return false;
        }
    }
}
