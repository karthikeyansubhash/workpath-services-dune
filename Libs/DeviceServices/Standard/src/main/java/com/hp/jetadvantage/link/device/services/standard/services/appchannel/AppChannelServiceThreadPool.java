package com.hp.jetadvantage.link.device.services.standard.services.appchannel;

import android.util.Log;

import com.hp.jetadvantage.link.device.services.standard.common.Constants;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Thread pool to handle AppChannelMessage for the service-based callbacks.
 * Each channelId uses a dedicated single-threaded executor to ensure sequential execution.
 */
public class AppChannelServiceThreadPool {
    private static final String TAG = Constants.TAG + "/AppChn/SvcThread";

    private static final ConcurrentHashMap<String, ExecutorService> channelExecutors = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Future<?>> taskMap = new ConcurrentHashMap<>();

    public static synchronized boolean submit(String channelId, String serviceCallId, Runnable task) {
        if (taskMap.containsKey(serviceCallId)) return false;

        // Get or create single-threaded executor for this channelId
        ExecutorService executor = channelExecutors.computeIfAbsent(
                channelId,
                k -> Executors.newSingleThreadExecutor(r -> {
                    Thread t = new Thread(r);
                    t.setName("AppChannel-" + channelId);
                    return t;
                })
        );

        Future<?> future = executor.submit(() -> {
            Log.d(TAG, "[" + channelId + "][" + serviceCallId + "] Start - AppChannel service thread");
            try {
                task.run();
            } catch (Exception e) {
                Log.e(TAG, "[" + channelId + "][" + serviceCallId + "] Exception in AppChannel service thread:" + e.getMessage(), e);
            } finally {
                taskMap.remove(serviceCallId);
                Log.d(TAG, "[" + channelId + "][" + serviceCallId + "] End - AppChannel service thread");
            }
        });

        taskMap.put(serviceCallId, future);
        return true;
    }

    public static boolean isRunning(String serviceCallId) {
        Future<?> future = taskMap.get(serviceCallId);
        return future != null && !future.isDone();
    }

    public static Future<?> getFuture(String serviceCallId) {
        Future<?> future = taskMap.get(serviceCallId);
        return future;
    }

    public static boolean cancel(String serviceCallId) {
        Future<?> future = taskMap.remove(serviceCallId);
        if (future != null && !future.isDone()) {
            return future.cancel(true);
        }
        return false;
    }

    public static void clear() {
        taskMap.clear();
    }

    public static synchronized void shutdownChannel(String channelId) {
        ExecutorService executor = channelExecutors.remove(channelId);
        if (executor != null) {
            executor.shutdown();
        }
    }

    public static synchronized void shutdownAll() {
        channelExecutors.values().forEach(ExecutorService::shutdown);
        channelExecutors.clear();
        taskMap.clear();
    }
}
