// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.sdd.jabberwocky.json;

import android.os.Bundle;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * JSON<->Bundle conversion utilities
 */
@SuppressWarnings({"unused", "unchecked", "WeakerAccess"})
public class JSONConverter {

    // VARIABLES
    /** "NULL" object representation */
    private static final Object NULL = new Object() {
        @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
        @Override public boolean equals(Object o) {
            return o == this || o == null; // API specifies this broken equals implementation
        }
        @Override public String toString() {
            return "null";
        }
    };

    // METHODS
    /**
     * Convert a bundle to a JSON string
     * @param bundle
     *              Bundle containing JSON mappings
     * @return
     *              JSON string representation of the provided bundle
     */
    public static String bundleToJsonString(Bundle bundle) {
        JSONObject obj = bundleToJsonObject(bundle);
        return (obj != null) ? obj.toString() : null;
    }

    /**
     * Convert a bundle to a JSON object
     * @param bundle
     *              Bundle containing JSON mappings
     * @return
     *              JSON object representation of the provided bundle
     */
    public static JSONObject bundleToJsonObject(Bundle bundle) {
        if (bundle == null) return null;
        JSONObject jsonObject = new JSONObject();
        for(String key : bundle.keySet()) {
            try {
                jsonObject.put(key, wrap(bundle.get(key)));
            } catch(JSONException ignored) {}
        }
        return jsonObject;
    }

    /**
     * Convert a JSON string representation to a bundle
     * @param json
     *              String representation of a JSON object
     * @return
     *              Bundle containing all the JSON mappings
     */
    public static Bundle jsonStringToBundle(String json) {
        if (TextUtils.isEmpty(json)) return null;
        JSONObject jsonObj;
        try {
            jsonObj = new JSONObject(json);
        } catch(JSONException ignored) {
            return null;
        }
        return jsonObjectToBundle(jsonObj);
    }

    /**
     * Convert a JSON object to a bundle
     * @param jsonObject
     *              JSON object
     * @return
     *              Bundle containing all the JSON mappings
     */
    public static Bundle jsonObjectToBundle(JSONObject jsonObject) {
        Bundle bundle = new Bundle();
        Iterator<String> keys = jsonObject.keys();
        while(keys.hasNext()) {
            String key = keys.next();
            Object obj = jsonObject.opt(key);
            if (obj == null) continue;
            if (obj instanceof Boolean) {
                bundle.putBoolean(key, (Boolean)obj);
            } else if (obj instanceof Integer) {
                bundle.putInt(key, (Integer)obj);
            } else if (obj instanceof String) {
                bundle.putString(key, (String)obj);
            } else if (obj instanceof Long) {
                bundle.putLong(key, (Long)obj);
            } else if (obj instanceof Double) {
                bundle.putDouble(key, (Double)obj);
            } else if (obj instanceof JSONObject) {
                bundle.putBundle(key, jsonObjectToBundle((JSONObject)obj));
            } else if (obj instanceof JSONArray) {
                JSONArray array = (JSONArray)obj;
                ArrayList<Object> list = new ArrayList<Object>();
                for(int i = 0; i < array.length(); i++) {
                    obj = array.opt(i);
                    if (obj instanceof JSONObject) {
                        obj = jsonObjectToBundle((JSONObject)obj);
                    }
                    if (!(obj instanceof JSONArray))
                        list.add(obj);
                }
                if (!list.isEmpty()) {
                    obj = list.get(0);
                    if (obj instanceof Bundle) {
                        ArrayList castList = new ArrayList<Bundle>();
                        for(Object listItem : list) {
                            castList.add((Bundle)listItem);
                        }
                        //noinspection unchecked
                        bundle.putParcelableArrayList(key, castList);
                    } else if (obj instanceof String) {
                        ArrayList castList = new ArrayList<String>();
                        for(Object listItem : list) {
                            castList.add((String)listItem);
                        }
                        //noinspection unchecked
                        bundle.putStringArrayList(key, castList);
                    } else if (obj instanceof Integer) {
                        ArrayList castList = new ArrayList<Integer>();
                        for(Object listItem : list) {
                            castList.add((Integer)listItem);
                        }
                        //noinspection unchecked
                        bundle.putIntegerArrayList(key, castList);
                    }
                }
            }
        }
        return bundle;
    }

    /**
     * Wrap the provided object as JSON element
     * @param obj
     *              Object to package as a JSON element
     * @return
     *              JSON
     */
    private static Object wrap(Object obj) {
        if (obj == null) {
            return NULL;
        }
        if (obj instanceof JSONArray || obj instanceof JSONObject) {
            return obj;
        }
        if (obj.equals(NULL)) {
            return obj;
        }
        try {
            if (obj instanceof Collection) {
                return new JSONArray((Collection) obj);
            } else if (obj.getClass().isArray()) {
                JSONArray array = new JSONArray();
                final int length = Array.getLength(obj);
                for (int i = 0; i < length; ++i) {
                    array.put(wrap(Array.get(obj, i)));
                }
                return array;
            }
            if (obj instanceof Map) {
                return new JSONObject((Map) obj);
            }
            if (obj instanceof Boolean ||
                    obj instanceof Byte ||
                    obj instanceof Character ||
                    obj instanceof Double ||
                    obj instanceof Float ||
                    obj instanceof Integer ||
                    obj instanceof Long ||
                    obj instanceof Short ||
                    obj instanceof String) {
                return obj;
            }
            if (obj.getClass().getPackage().getName().startsWith("java.")) {
                return obj.toString();
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    /**
     * Private constructor
     */
    private JSONConverter() {
    }
}
