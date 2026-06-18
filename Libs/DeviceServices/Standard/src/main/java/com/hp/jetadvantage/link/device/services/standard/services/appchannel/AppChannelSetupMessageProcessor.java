package com.hp.jetadvantage.link.device.services.standard.services.appchannel;

import android.util.Log;

import com.hp.jetadvantage.link.device.services.standard.common.Constants;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceService;
import com.hp.ws.websocket.AppChannelMessage.ChannelMessage;
import com.hp.ws.websocket.AppChannelSetup;

import java.util.List;

public class AppChannelSetupMessageProcessor extends AppChannelMessageProcessor {
    private static final String TAG = Constants.TAG + "/AppChn/Setup";

    public AppChannelSetupMessageProcessor(AppChannelContext ctx) {
        super(ctx);
        if (queueEnabled) {
            AppChannelCallbackRegistry.addSetupCallbackRegistrationListener(
                    (e2ServiceGun, callback) -> deliverQueuedMessagesToCallback(e2ServiceGun, callback)
            );
        }
    }

    @Override
    protected boolean canProcessMessage(ChannelMessage message) {
        if(message == null || message.getMessage() == null) {
            Log.e(TAG, "canProcessMessage: Invalid message or message content is null");
            return false;
        }
        return message.getMessage().hasSetup();
    }

    @Override
    protected void processMessage(String channelId, ChannelMessage message) {
        Log.d(TAG, "processMessage : [" + channelId + "]");
        boolean added = ctx.getAppChannelRegistry().addChannel(channelId, message.getMessage().getSetup());
        if (added) {
            AppChannelSetup channel = ctx.getAppChannelRegistry().getChannel(channelId);
            String e2ServiceGun = channel.getDetails().getE2ServiceGun();
            List<StandardDeviceService.ISetupCallback> callbacks =
                    AppChannelCallbackRegistry.getSetupCallback(e2ServiceGun);
            if (callbacks != null && !callbacks.isEmpty()) {
                for (StandardDeviceService.ISetupCallback cb : callbacks) {
                    cb.onSetupNotification(channel.getPackageId(), channel);
                }
            }
        } else {
            Log.e(TAG, "processMessage() Error : failed to add channel channelId=" + channelId);
            ctx.sendChannelError(channelId);
        }
    }

    protected void deliverQueuedMessagesToCallback(String e2ServiceGun, StandardDeviceService.ISetupCallback callback) {
        try {
            List<AppChannelSetup> channels = ctx.getAppChannelRegistry().getChannels();
            if (channels == null || channels.isEmpty()) {
                return;
            }
            for (AppChannelSetup channelSetup : channels) {
                if (channelSetup == null) continue;
                if (!channelSetup.getDetails().hasPayloadDetails()) continue;

                String payloadE2 = channelSetup.getDetails().getPayloadDetails().getE2ServiceGun();
                if (e2ServiceGun.equals(payloadE2)) {
                    Log.d(TAG, "deliverQueuedMessagesToCallback: notifying for e2ServiceGun: " + e2ServiceGun);
                    try {
                        callback.onSetupNotification(channelSetup.getPackageId(), channelSetup);
                    } catch (Exception cbEx) {
                        Log.e(TAG, "Error in setup callback: " + cbEx.getMessage(), cbEx);
                    }
                    break;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "deliverQueuedMessagesToCallback: Exception: " + e.getMessage(), e);
        }
    }
}
