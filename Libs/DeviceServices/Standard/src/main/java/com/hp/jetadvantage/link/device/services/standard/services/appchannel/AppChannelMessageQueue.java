package com.hp.jetadvantage.link.device.services.standard.services.appchannel;

import android.util.Log;

import com.hp.jetadvantage.link.common.utils.StringUtility;
import com.hp.jetadvantage.link.device.services.standard.common.Constants;
import com.hp.ws.websocket.AppChannelMessage.ChannelMessage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

public class AppChannelMessageQueue {
    private static final String TAG = Constants.TAG + "/AppChn/Q";
    private final ConcurrentHashMap<String, LinkedBlockingQueue<ChannelMessage>> channelMsgQueueMap;

    public AppChannelMessageQueue() {
        channelMsgQueueMap = new ConcurrentHashMap<>();
    }

    protected boolean addMessage(String key, ChannelMessage message) {
        boolean result = false;
        if (key == null || message == null) {
            Log.e(TAG, "addMessage: key or message is null");
            return false;
        }
        LinkedBlockingQueue<ChannelMessage> queue = channelMsgQueueMap.computeIfAbsent(key,
                k -> new LinkedBlockingQueue<>(100));
        try {
            result = queue.offer(message);
            if (!result) {
                Log.e(TAG, "Queue is full for e2ServiceGun: " + key + ", message: " + String.valueOf(message));
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to add message to queue for e2ServiceGun: " + key, e);
        }
        return result;
    }

    protected ChannelMessage pollMessage(String key) {
        if (StringUtility.isEmpty(key)) {
            Log.e(TAG, "pollMessage: input key is null or empty");
            return null;
        }
        LinkedBlockingQueue<ChannelMessage> queue = channelMsgQueueMap.get(key);
        if (queue != null) {
            ChannelMessage message = queue.poll();
            if (queue.isEmpty()) {
                channelMsgQueueMap.remove(key);
            }
            return message;
        }
        return null;
    }

    protected boolean isEmpty(String e2ServiceGun) {
        LinkedBlockingQueue<ChannelMessage> queue = channelMsgQueueMap.get(e2ServiceGun);
        return queue == null || queue.isEmpty();
    }

    protected void clearQueue() {
        for (Map.Entry<String, LinkedBlockingQueue<ChannelMessage>> entry : channelMsgQueueMap.entrySet()) {
            LinkedBlockingQueue<ChannelMessage> queue = entry.getValue();
            if (queue != null) {
                queue.clear();
            }
        }
        channelMsgQueueMap.clear();
        Log.i(TAG, "All queues cleared");
    }
}
