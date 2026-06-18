package com.hp.jetadvantage.link.device.services.standard.services.appchannel;

import android.util.Log;

import com.hp.ws.websocket.AppChannelMessage.ChannelMessage;

public class AppChannelStreamMessageProcessor extends AppChannelMessageProcessor {

    AppChannelStreamMessageProcessor(AppChannelContext ctx) {
        super(ctx);
    }

    @Override
    protected boolean canProcessMessage(ChannelMessage message) {
        return message.getMessage().hasStream();
    }

    @Override
    protected void processMessage(String channelId, ChannelMessage message) {
        Log.e(TAG, "Stream message processing is not implemented yet ");
    }
}
