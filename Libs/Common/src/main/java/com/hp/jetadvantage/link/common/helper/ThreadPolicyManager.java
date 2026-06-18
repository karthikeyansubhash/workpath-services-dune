package com.hp.jetadvantage.link.common.helper;

import android.os.StrictMode;

/**
 * Manages Android StrictMode ThreadPolicy to temporarily allow network operations.
 * Implements AutoCloseable for automatic policy restoration.
 *
 * Usage:
 * try (ThreadPolicyManager manager = new ThreadPolicyManager()) {
 * // Network operations permitted here
 * } // Policy automatically restored
 */
public class ThreadPolicyManager implements AutoCloseable {
    private StrictMode.ThreadPolicy originalPolicy;

    public ThreadPolicyManager() {
        enableNetworkPolicy();
    }

    public boolean isPolicyEnabled() {
        return this.originalPolicy != null;
    }

    @Override
    public void close() {
        restoreNetworkPolicy();
    }

    private void enableNetworkPolicy() {
        try {
            this.originalPolicy = StrictMode.getThreadPolicy();
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitNetwork().build());
        } catch (Exception e) {
            this.originalPolicy = null;
        }
    }

    private void restoreNetworkPolicy() {
        if (this.originalPolicy != null) {
            StrictMode.setThreadPolicy(originalPolicy);
            this.originalPolicy = null;
        }
    }
}
