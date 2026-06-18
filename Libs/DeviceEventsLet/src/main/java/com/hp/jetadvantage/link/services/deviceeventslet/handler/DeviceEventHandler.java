package com.hp.jetadvantage.link.services.deviceeventslet.handler;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.device.services.standard.common.Constants;
import com.hp.jetadvantage.link.device.services.standard.common.StandardJsonParser;
import com.hp.jetadvantage.link.services.common.ssp.SpsConstants;
import com.hp.jetadvantage.link.services.deviceeventslet.model.DeviceEvent;
import com.hp.workpath.api.deviceevents.DeviceEventslet;

import java.lang.ref.WeakReference;

public class DeviceEventHandler {
    private static final String TAG = Constants.TAG + "/Handler";
    private static final String THREAD_NAME = "SendMessageToClientHandlerThread";

    private WeakReference<Context> mContext;

    // Handler and HandlerThread send a message to the client.
    private final Handler mSendMessageToClientHandler;

    public DeviceEventHandler() {
        // Initialize the handler and the thread for sending a message to the client
        HandlerThread mSendMessageToClientHandlerThread = new HandlerThread(THREAD_NAME);
        mSendMessageToClientHandlerThread.start();
        mSendMessageToClientHandler = new SendMessageToClientHandler(mSendMessageToClientHandlerThread.getLooper());
    }

    public DeviceEventHandler(Context context) {
        this();
        mContext = new WeakReference<>(context);
    }

    // For testing adapter
    public DeviceEventHandler(Handler testMessageHandler) {
        mSendMessageToClientHandler = testMessageHandler;
    }

    protected Context getContext() {
        return mContext.get();
    }

    public void sendMessageToClient(DeviceEvent deviceEvent) {
        Message sendMessage = mSendMessageToClientHandler.obtainMessage(SendMessageToClientHandler.MSG_SEND_MESSAGE);
        sendMessage.obj = deviceEvent;
        mSendMessageToClientHandler.sendMessage(sendMessage);
    }

    private void sendMessageToClient(Context context, String data) {
        try {
            SLog.d(TAG, "Device events is occurred, start sending an event");

            final Intent intent = new Intent(DeviceEventslet.DEVICE_EVENTS_CHANGED_ACTION);
            intent.putExtra(DeviceEventslet.Keys.KEY_DEVICE_EVENTS, data);
            context.sendBroadcast(intent, SpsConstants.SDK_ACCESS_DEVICE_EVENTS_PERMISSION);

            SLog.i(TAG, "Send device events");
        } catch (Throwable e) {
            SLog.e(TAG, "Failed to send message to the client with " + e.getMessage());
        }
    }

    class SendMessageToClientHandler extends Handler {
        public static final int MSG_SEND_MESSAGE = 1001;

        public SendMessageToClientHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SEND_MESSAGE:
                    DeviceEvent deviceEvent = (DeviceEvent) msg.obj;
                    if (getContext() != null) {
                        String data = StandardJsonParser.INSTANCE.toJson(deviceEvent);
                        sendMessageToClient(getContext(), data);
                    }
                    break;
            }
        }
    }
}
