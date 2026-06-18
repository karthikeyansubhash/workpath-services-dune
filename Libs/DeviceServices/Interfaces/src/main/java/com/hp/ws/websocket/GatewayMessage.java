package com.hp.ws.websocket;

import com.google.gson.annotations.SerializedName;

public class GatewayMessage {
    @SerializedName("gatewayMessage")
    GatewayMessageDetails gatewayMessage;

    public GatewayMessageDetails getGatewayMessage() {
        return gatewayMessage;
    }

    public static class GatewayMessageDetails {
        @SerializedName("details")
        DetailsData details;

        public DetailsData getDetails() {
            return details;
        }

        public static class DetailsData {
            @SerializedName("workpathGatewayData")
            WorkpathGatewayData workpathGatewayData;

            public WorkpathGatewayData getWorkpathGatewayData() {
                return workpathGatewayData;
            }
        }
    }
}
