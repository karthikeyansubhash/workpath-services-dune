/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.device.services.standard.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

public enum StandardJsonParser {

    //JVM guarantees thread-safe creation of the singleton enum instance
    INSTANCE;

    private final Gson gson;

    StandardJsonParser() {
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
}

