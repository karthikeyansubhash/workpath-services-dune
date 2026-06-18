/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.device.services.standard.services;

import android.os.RemoteException;
import android.util.Log;

import com.google.gson.JsonSyntaxException;
import com.hp.jetadvantage.link.common.utils.SLog;
import com.hp.jetadvantage.link.device.services.interfaces.ICdmCallback;
import com.hp.jetadvantage.link.device.services.standard.StandardDeviceSubscriptionService;
import com.hp.jetadvantage.link.device.services.standard.common.Constants;
import com.hp.jetadvantage.link.device.services.standard.common.StandardJsonParser;
import com.hp.jetadvantage.link.system.IWebsocketCallback;
import com.hp.ws.websocket.CdmPubMessage;

import java.util.Collection;

public class CdmPubMessageHandler {
    /**
     * CDM pub/sub redirected message :
     * The E2Workpath Interop wraps the original CDM publication reports with "cdmPubMessage" and forward the message thru websocket
     * if the subscription callbackUri is http://localhost/cdm/e2WorkpathInterop/v1/cdmPubData
     * {
     *     "cdmPubMessage": {
     *         "message": {
     *             "continuingReports": "false",
     *             "reports": [{
     *                     "data": {...},
     *                     "eTag": "1000001",
     *                     "gSeqNum": 1,
     *                     "gun": "com.hp.cdm.service.clock.version.1.resource.configuration",
     *                     "path": "/cdm/clock/v1/configuration",
     *                     "updateType": "delta"
     *             }],
     *             "subscriptionId": "1223579568",
     *             "version": "2.3.0"
     *         },
     *         "subscriptionId": "1223579568"
     *     }
     * }
     */
    public static final String MESSAGE_TYPE = "cdmPubMessage";
    private static final String TAG = Constants.TAG + "WS/PubMsg";

    public IWebsocketCallback callback = new IWebsocketCallback.Stub() {
        @Override
        public void onMessageReceived(int what, String data) throws RemoteException {
            onReceived(what, data);
        }
    };

    public void onReceived(int what, String data) {
        Log.d(TAG, "onReceived");

        try {
            CdmPubMessage callbackMsg = StandardJsonParser.INSTANCE.fromJson(data, CdmPubMessage.class);
            String subscriptionId = callbackMsg.getCdmPubMessage().getSubscriptionId();
            Log.i(TAG, "onReceived : subscriptionId = " + subscriptionId);

            if (!callbackMsg.getCdmPubMessage().getMessage().getReports().isEmpty()) {
                Collection<ICdmCallback> callbacks = StandardDeviceSubscriptionService.getCallbacks();
                callbacks.forEach(callback -> {
                    if (callback != null)
                        callback.onChangeEvent(callbackMsg.getCdmPubMessage().getMessage().getReports());
                });
            }
        } catch (JsonSyntaxException e) {
            SLog.e(TAG, e.getMessage(), e);
        } catch (Exception e) {
            SLog.e(TAG, e.getMessage(), e);
        }
    }
}
