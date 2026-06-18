/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.device.services.standard.services;

import android.os.RemoteException;
import android.util.Log;

import com.google.gson.JsonSyntaxException;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.device.services.standard.common.Constants;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceManagementService;
import com.hp.jetadvantage.link.device.services.standard.common.StandardJsonParser;
import com.hp.jetadvantage.link.system.IWebsocketCallback;
import com.hp.ws.websocket.GatewayMessage;
import com.hp.ws.websocket.WorkpathGatewayData;

public class GatewayMessageHandler {
    /**
     * {
     *     "gatewayMessage": {
     *         "details": {
     *             "workpathGatewayData": {
     *                 "applicationId": "5187d0aa-f86e-4f4e-a970-9595680752b4",
     *                 "gatewayType": "wgdgtApplication",
     *                 "solutionId": "11111111-1111-1111-9999-111111111111",
     *                 "uiContextToken": "eyJhbGciOiAiZGly..",
     *                 "workpathActionType": "wgdatShowDisplay"
     *             }
     *         },
     *         "traceId": 41
     *     }
     * }
     */
    public static final String MESSAGE_TYPE = "gatewayMessage";
    private static final String TAG = Constants.TAG + "/WS/GtwMsg";

    public IWebsocketCallback callback = new IWebsocketCallback.Stub() {
        @Override
        public void onMessageReceived(int what, String data) throws RemoteException {
            onReceived(what, data);
        }
    };

    public void onReceived(int what, String data) {
        Log.d(TAG, "onReceived");

        try {
            GatewayMessage callbackMsg = StandardJsonParser.INSTANCE.fromJson(data, GatewayMessage.class);
            WorkpathGatewayData gatewayData = callbackMsg.getGatewayMessage().getDetails().getWorkpathGatewayData();

            switch (gatewayData.getWorkpathActionType()) {
                case SHOW_DISPLAY:
                case LEGACY_SHOW_DISPLAY:
                    Log.d(TAG, "onReceived : set SolutionId( " + gatewayData.getSolutionId() + " )");
                    StandardDeviceManagementService.getInstance().getUIContextTokenManager().setUIConTextToken(gatewayData.getSolutionId(), gatewayData.getUiContextToken());
                    break;
                case CLOSE_DISPLAY:
                case LEGACY_CLOSE_DISPLAY:
                    Log.d(TAG, "onReceived : clear for " + gatewayData.getSolutionId());
                    StandardDeviceManagementService.getInstance().getUIContextTokenManager().clearUIConTextToken();
                    break;
                default:
                    break;
            }
        } catch (JsonSyntaxException e) {
            SLog.e(TAG, "Invalid data : " + data);
            SLog.e(TAG, e.getMessage(), e);
        } catch (Exception e) {
            SLog.e(TAG, e.getMessage(), e);
        }
    }
}
