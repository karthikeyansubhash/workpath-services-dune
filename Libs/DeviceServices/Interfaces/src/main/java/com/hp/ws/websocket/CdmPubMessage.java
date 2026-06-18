/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ws.websocket;

import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.pubsub.DeviceReported;

public class CdmPubMessage {
    @SerializedName("cdmPubMessage")
    CdmPubMessageDetails cdmPubMessage;

    public CdmPubMessageDetails getCdmPubMessage() {
        return cdmPubMessage;
    }

    public class CdmPubMessageDetails {
        @SerializedName("message")
        DeviceReported message;
        @SerializedName("subscriptionId")
        String subscriptionId;

        public String getSubscriptionId() {
            return subscriptionId;
        }

        public DeviceReported getMessage() {
            return message;
        }
    }


}
