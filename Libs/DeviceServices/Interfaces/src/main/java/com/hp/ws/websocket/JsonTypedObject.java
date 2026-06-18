package com.hp.ws.websocket;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

public class JsonTypedObject {
    @SerializedName("typeGUN")
    private String typeGUN;
    @SerializedName("value")
    private JsonObject value;

    public JsonTypedObject(String typeGun) {
        this.typeGUN = typeGun;
    }

    public JsonTypedObject(String typeGun, JsonObject value) {
        this.typeGUN = typeGun;
        this.value = value;
    }

    public String getTypeGUN() {
        return typeGUN;
    }

    public void setTypeGUN(String value) {
        typeGUN = value;
    }

    public JsonObject getValue() {
        return value;
    }

    public void setValue(JsonObject value) {
        this.value = value;
    }
}
