/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.device.services.standard.services.appchannel;

import android.os.RemoteException;
import android.util.Log;

import com.google.gson.JsonSyntaxException;
import com.hp.jetadvantage.link.common.utils.StringUtility;
import com.hp.jetadvantage.link.device.services.standard.common.Constants;
import com.hp.jetadvantage.link.device.services.standard.common.StandardJsonParser;
import com.hp.jetadvantage.link.device.services.standard.services.StandardWebsocketCallbackService;
import com.hp.jetadvantage.link.system.IWebsocketCallback;
import com.hp.ws.websocket.AppChannelMessage;
import com.hp.ws.websocket.AppChannelMessage.ChannelMessage;
import com.hp.ws.websocket.AppChannelSetup;

import java.util.List;
import java.util.Optional;

/**
 * AppChannelMessageHandler Class : This class is used for handling incoming app channel messages
 * App Channel Message is for event notifications from a Dune E2 service to an Workpath App.
 * App Channel Message consists of setup, notification message delivery (Payload, Service, Stream), and teardown stages.
 * This handler class receives a channelMessage json data from the websocket service (System apk), and handles it.
 */
public class AppChannelMessageHandler {
    public static final String MESSAGE_TYPE = "channelMessage";
    protected static final int ERR_NONE = 0;
    protected static final int ERR_INVALID_MESSAGE = 1;
    protected static final int ERR_UNKNOWN_MESSAGE = 2;
    protected static final int ERR_JSON_SYNTAX = 3;
    protected static final int ERR_PROCESS_EXCEPTION = 4;
    private static final String TAG = Constants.TAG + "/AppChn/Handler";
    private final StandardWebsocketCallbackService websocketService;
    private final AppChannelContext channelContext;
    private final List<AppChannelMessageProcessor> processors;
    private int onReceiveProcessLastError = ERR_NONE;

    public final IWebsocketCallback callback = new IWebsocketCallback.Stub() {
        @Override
        public void onMessageReceived(int what, String data) throws RemoteException {
            onReceived(what, data);
        }
    };

    public AppChannelMessageHandler(StandardWebsocketCallbackService websocketCallbackService) {
        this.websocketService = websocketCallbackService;
        this.channelContext = new AppChannelContext(websocketService);
        processors = List.of(
                new AppChannelSetupMessageProcessor(channelContext),
                new AppChannelPayloadMessageProcessor(channelContext),
                new AppChannelServiceMessageProcessor(channelContext),
                new AppChannelStreamMessageProcessor(channelContext),
                new AppChannelTeardownMessageProcessor(channelContext)
        );
    }

    /**
     * onReceived - invoked when app channel message is coming from a Dune E2 service
     *
     * @param what : not used
     * @param data : app channel message json data
     */
    public void onReceived(int what, String data) {
        Log.d(TAG, "onReceived: ENTER - " + data);
        onReceiveProcessLastError = ERR_NONE;
        if (StringUtility.isEmpty(data)) {
            Log.e(TAG, "onReceived: invalid message - data is null or empty");
            onReceiveProcessLastError = ERR_INVALID_MESSAGE;
            return;
        }
        try {
            final AppChannelMessage callbackMsg = StandardJsonParser.INSTANCE.fromJson(data, AppChannelMessage.class);
            final ChannelMessage msg = callbackMsg.getChannelMessage();
            if (msg == null) {
                Log.e(TAG, "onReceived: invalid message - channelMessage is null : " + data);
                onReceiveProcessLastError = ERR_INVALID_MESSAGE;
                return;
            }
            final String channelId = msg.getChannelId();
            if (StringUtility.isEmpty(channelId)) {
                Log.e(TAG, "onReceived: invalid message - channelId is null");
                onReceiveProcessLastError = ERR_INVALID_MESSAGE;
                return;
            }

            Optional<AppChannelMessageProcessor> processor = processors.stream()
                    .filter(p -> p.canProcessMessage(msg))
                    .findFirst();

            if (processor.isPresent()) {
                processor.get().processMessage(channelId, msg);
            } else {
                Log.e(TAG, "onReceived: unknown message : " + data);
                onReceiveProcessLastError = ERR_UNKNOWN_MESSAGE;
            }
        } catch (JsonSyntaxException e) {
            onReceiveProcessLastError = ERR_JSON_SYNTAX;
            Log.e(TAG, e.getMessage(), e);
        } catch (Exception e) {
            onReceiveProcessLastError = ERR_PROCESS_EXCEPTION;
            Log.e(TAG, e.getMessage(), e);
        }
        Log.d(TAG, "onReceived: EXIT (" + onReceiveProcessLastError + ")");
    }

    protected AppChannelSetup getChannel(String channelId) {
        return channelContext.getAppChannelRegistry().getChannel(channelId);
    }

    protected int getOnReceiveProcessLastError() {
        return onReceiveProcessLastError;
    }

    protected void clearChannelMap() {
        channelContext.getAppChannelRegistry().clearChannels();
    }
}
