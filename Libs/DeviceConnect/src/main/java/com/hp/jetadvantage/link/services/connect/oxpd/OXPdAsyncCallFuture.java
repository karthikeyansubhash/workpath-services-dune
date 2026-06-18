// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.connect.oxpd;

import androidx.annotation.NonNull;
import android.os.Message;

import com.hp.oxpdlib.OXPdDevice;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class OXPdAsyncCallFuture<T> implements Future<Message>, OXPdDevice.RequestCallback {
    private volatile Message result = null;
    private volatile boolean cancelled = false;
    private final CountDownLatch countDownLatch;

    @SuppressWarnings("WeakerAccess")
    public OXPdAsyncCallFuture() {
        countDownLatch = new CountDownLatch(1);
    }

    @Override
    public boolean cancel(boolean b) {
        if (isDone()) {
            return false;
        } else {
            countDownLatch.countDown();
            cancelled = true;
            return !isDone();
        }
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public boolean isDone() {
        return countDownLatch.getCount() == 0;
    }

    @Override
    public Message get() throws InterruptedException, ExecutionException {
        countDownLatch.await();
        return result;
    }

    @Override
    public Message get(final long timeout, @NonNull final TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
        countDownLatch.await(timeout, timeUnit);
        return result;
    }

    @Override
    public void requestResult(final OXPdDevice device, final Message message) {
        result = message;
        countDownLatch.countDown();
    }

    /**
     * This method must be overridden to call an OXPd operation with this instance set as callback
     * Call result() to get result object or exception
     */
    public void execute() throws Exception {
        // by default returns null
        result = null;
        countDownLatch.countDown();
    }

    @SuppressWarnings("unchecked")
    public T getResult() throws Exception {
        execute();
        Message message = get();
        if (message != null) {
            if (message.arg1 == OXPdDevice.REQUEST_RETURN_CODE__OK) {
                return (T) message.obj;
            } else if (message.obj instanceof Exception) {
                throw (Exception) message.obj;
            }
        }
        return null;
    }
}
