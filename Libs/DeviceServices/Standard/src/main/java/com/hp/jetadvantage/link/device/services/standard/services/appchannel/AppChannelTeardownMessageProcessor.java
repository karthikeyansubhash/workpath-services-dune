package com.hp.jetadvantage.link.device.services.standard.services.appchannel;

import com.hp.ws.websocket.AppChannelMessage;

public class AppChannelTeardownMessageProcessor extends AppChannelMessageProcessor {
    AppChannelTeardownMessageProcessor(AppChannelContext ctx) {
        super(ctx);
    }

    @Override
    protected boolean canProcessMessage(AppChannelMessage.ChannelMessage message) {
        return message.getMessage().hasTeardown();
    }

    @Override
    protected void processMessage(String channelId, AppChannelMessage.ChannelMessage message) {
        AppChannelServiceThreadPool.shutdownChannel(channelId);
        ctx.getAppChannelRegistry().removeChannel(channelId);
    }
}
