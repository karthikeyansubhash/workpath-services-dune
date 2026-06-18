// Copyright 2025 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.workpath.api.util;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

import androidx.annotation.RequiresApi;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * The EventResultReceiver class is a custom ResultReceiver that handles asynchronous event results.
 * This class uses CompletableFuture to manage the response and provides a method to retrieve the response with a timeout.
 *
 * @hide for internal use
 * @since API 9
 */
@RequiresApi(api = Build.VERSION_CODES.N)
public class EventResultReceiver extends ResultReceiver {
    private final BlockingQueue<String> resultQueue = new ArrayBlockingQueue<>(10);

    /**
     * Constructs an EventResultReceiver with the specified handler.
     *
     * @hide for internal use
     * @param handler The handler to manage the result.
     * @since API 9
     */
    public EventResultReceiver(Handler handler) {
        super(handler);
    }

    /**
     * Called when a result is received.
     *
     * @hide for internal use
     * @param resultCode The result code.
     * @param resultData The result data.
     * @since API 9
     */
    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        String result = resultData.getString("response");
        resultQueue.offer(result);
    }

    /**
     * Retrieves the response with the specified timeout.
     *
     * @hide for internal use
     * @param timeout The timeout duration.
     * @param unit The time unit for the timeout.
     * @return The response string.
     * @throws Exception if the response is not received within the timeout.
     * @since API 9
     */
    public String getResponse(long timeout, TimeUnit unit) throws Exception {
        String result = resultQueue.poll(timeout, unit);
        if (result == null) {
            throw new TimeoutException("Timed out waiting for the result.");
        }
        return result;
    }
}