package com.hp.workpath.internal.utils.adapter;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;

public enum GsonAdapter {
    //JVM guarantees thread-safe creation of the singleton enum instance
    INSTANCE;

    private final Gson gson;

    GsonAdapter() {
        gson = new Gson();
    }

    public <T> T fromJson(String json, Class<T> ClassOfT) {
        return gson.fromJson(json, ClassOfT);
    }

    public <T> T fromJson(String json, Type TypeOfT) {
        return gson.fromJson(json, TypeOfT);
    }

    public String toJson(Object object) {
        return gson.toJson(object);
    }

    public JsonElement toJsonTree(Object src) {
        return gson.toJsonTree(src);
    }
}
