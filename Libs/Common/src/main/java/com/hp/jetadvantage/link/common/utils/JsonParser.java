// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.common.utils;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public class JsonParser {

    private static JsonParser instance = null;
    private final Gson gson;

    // Thread safe Singleton class, It is double locking design pattern.
    public static JsonParser getInstance() {
        if (instance == null) {
            synchronized (JsonParser.class) {
                if (instance == null) {
                    instance = new JsonParser();
                }
            }
        }
        return instance;
    }

    private JsonParser() {
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
