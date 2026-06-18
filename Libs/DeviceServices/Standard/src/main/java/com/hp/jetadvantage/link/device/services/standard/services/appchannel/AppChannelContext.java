package com.hp.jetadvantage.link.device.services.standard.services.appchannel;

import android.os.RemoteException;
import android.util.Log;

import com.hp.jetadvantage.link.device.services.standard.common.Constants;
import com.hp.jetadvantage.link.device.services.standard.common.StandardJsonParser;
import com.hp.jetadvantage.link.device.services.standard.services.StandardWebsocketCallbackService;
import com.hp.ws.websocket.AppChannelReturnMessage;

public class AppChannelContext {
    private static final String TAG = Constants.TAG + "/AppChn/Ctx";
    private final StandardWebsocketCallbackService websocketService;
    private final AppChannelRegistry registry;

    public AppChannelContext(StandardWebsocketCallbackService websocketService) {
        this.websocketService = websocketService;
        this.registry = new AppChannelRegistry(websocketService);
    }

    protected AppChannelRegistry getAppChannelRegistry() {
        return registry;
    }

    protected void sendChannelError(String channelId) {
        Log.e(TAG, "sendChannelError() channelId=" + channelId);
        AppChannelReturnMessage returnMsg = new AppChannelReturnMessage(channelId);
        returnMsg.getChannelMessage().getMessage().setError();
        send(returnMsg);
    }

    protected void send(AppChannelReturnMessage msg) {
        try {
            websocketService.sendMessage(0, StandardJsonParser.INSTANCE.toJson(msg));
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to send message: " + e.getMessage(), e);
        }
    }
}
