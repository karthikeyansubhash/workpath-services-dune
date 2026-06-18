package com.hp.jetadvantage.link.device.services.standard.services.appchannel;

import android.util.Log;

import com.hp.jetadvantage.link.common.utils.StringUtility;
import com.hp.jetadvantage.link.device.services.standard.common.Constants;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceService;
import com.hp.ws.websocket.AppChannelMessage.ChannelMessage;
import com.hp.ws.websocket.AppChannelSetup;
import com.hp.ws.websocket.JsonTypedObject;

import java.util.List;

public class AppChannelPayloadMessageProcessor extends AppChannelMessageProcessor {
    protected static final String TAG = Constants.TAG + "/AppChn/Payload";
    protected AppChannelMessageQueue msgQueue;

    AppChannelPayloadMessageProcessor(AppChannelContext ctx) {
        super(ctx);

        if (queueEnabled) {
            msgQueue = new AppChannelMessageQueue();
            AppChannelCallbackRegistry.addPayloadCallbackRegistrationListener(
                    (e2ServiceGun, callback) -> deliverQueuedMessagesToCallback(e2ServiceGun, callback));
        }
    }

    @Override
    protected boolean canProcessMessage(ChannelMessage message) {
        if(message == null || message.getMessage() == null) {
            Log.e(TAG, "canProcessMessage: Invalid message or message content is null");
            return false;
        }
        return message.getMessage().hasPayload();
    }

    @Override
    protected void processMessage(String channelId, ChannelMessage message) {
        Log.d(TAG, "processMessage:[" + channelId + "]");
        String appPackageId = ctx.getAppChannelRegistry().getPackageId(channelId);
        AppChannelSetup channel = ctx.getAppChannelRegistry().getChannel(channelId);
        if (channel == null) {
            Log.d(TAG, "processMessage:[" + channelId + "] unknown channel but try to go ahead without channel setup");
        }

        JsonTypedObject value = message.getMessage().getPayload().getValue();
        String e2ServiceGun = extractE2ServiceGun(channel, value);
        if (StringUtility.isEmpty(e2ServiceGun)) {
            Log.e(TAG, "processMessage:[" + channelId + "]can't find valid e2ServiceGun, ignore message");
            return;
        }

        List<StandardDeviceService.IPayloadCallback> callbacks =
                AppChannelCallbackRegistry.getPayloadCallback(e2ServiceGun);

        if (callbacks != null && !callbacks.isEmpty()) {
            for (StandardDeviceService.IPayloadCallback cb : callbacks) {
                cb.onReceiveNotification(appPackageId, value);
            }
        } else if (queueEnabled) {
            Log.d(TAG, String.format(
                    "processMessage:[%s] no callback for %s, queuing", channelId, e2ServiceGun));
            msgQueue.addMessage(e2ServiceGun, message);
        } else {
            Log.d(TAG, String.format(
                    "processMessage:[%s] no callback for %s, drop", channelId, e2ServiceGun));
        }
    }

    protected void deliverQueuedMessagesToCallback(String e2ServiceGun,
                                                   StandardDeviceService.IPayloadCallback callback) {
        if (StringUtility.isEmpty(e2ServiceGun) || callback == null) {
            Log.e(TAG, "deliverQueuedMessagesToCallback: invalid parameters");
            return;
        }
        if (msgQueue == null) {
            Log.e(TAG, "deliverQueuedMessagesToCallback: message queue is uninitialized");
            return;
        }
        try {
            while (!msgQueue.isEmpty(e2ServiceGun)) {
                ChannelMessage cm = msgQueue.pollMessage(e2ServiceGun);
                if (cm == null) break;

                String pkg = ctx.getAppChannelRegistry().getPackageId(cm.getChannelId());
                if (StringUtility.isEmpty(pkg)) {
                    Log.e(TAG, "deliverQueuedMessagesToCallback: packageId is null for channelId: "
                            + cm.getChannelId());
                    continue;
                }

                Log.d(TAG, "deliverQueuedMessagesToCallback: e2ServiceGun: " + e2ServiceGun);
                try {
                    callback.onReceiveNotification(pkg, cm.getMessage().getPayload().getValue());
                } catch (Exception cbEx) {
                    Log.e(TAG, "Error in payload callback", cbEx);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "deliverQueuedMessagesToCallback: Exception: " + e.getMessage(), e);
        }
    }
}
