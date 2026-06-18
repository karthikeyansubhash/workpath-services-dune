package com.hp.jetadvantage.link.device.services.standard.services.appchannel;

import android.util.Log;

import com.hp.jetadvantage.link.device.services.standard.common.Constants;
import com.hp.ws.websocket.AppChannelMessage.ChannelMessage;
import com.hp.ws.websocket.AppChannelSetup;
import com.hp.ws.websocket.JsonTypedObject;

public abstract class AppChannelMessageProcessor {
    protected static final String TAG = Constants.TAG + "/AppChn/Processor";
    protected AppChannelContext ctx;

    /**
     * queueEnabled : flag to enable or disable the queue for app channel messages.
     * The queue is used to store app channel messages temporarily when callbacks aren’t yet registered.
     */
    protected final boolean queueEnabled = true;

    protected AppChannelMessageProcessor(AppChannelContext ctx) {
        this.ctx = ctx;
    }

    /**
     * Check if the AppChannel message can be processed by this processor.
     *
     * @param message
     * @return
     */
    protected abstract boolean canProcessMessage(ChannelMessage message);

    /**
     * Process the AppChannel message received from the E2Interop websocket.
     *
     * @param channelId
     * @param message
     */
    protected abstract void processMessage(String channelId, ChannelMessage message);

    protected String extractE2ServiceGun(AppChannelSetup channel, JsonTypedObject typedObject) {
        String e2ServiceGun = "";
        if (channel != null) {
            e2ServiceGun = channel.getDetails().getE2ServiceGun();
        } else if (typedObject != null) {
            String typeGun = typedObject.getTypeGUN();
            if (typeGun != null) {
                Log.d(TAG, "extractE2ServiceGun: Channel is null, using typeGUN");
                try {
                    String[] parts = typeGun.split("\\.type\\.");
                    if (parts.length > 0) {
                        e2ServiceGun = parts[0];
                    }
                } catch (Exception e) {
                    Log.e(TAG, "extractE2ServiceGun: failed to parse typeGUN: " + e.getMessage());
                }
            }
        }
        return e2ServiceGun;
    }
}
