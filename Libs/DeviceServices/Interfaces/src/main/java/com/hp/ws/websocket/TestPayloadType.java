package com.hp.ws.websocket;

import com.google.gson.annotations.SerializedName;

public class TestPayloadType {
    @SerializedName("message")
    String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}