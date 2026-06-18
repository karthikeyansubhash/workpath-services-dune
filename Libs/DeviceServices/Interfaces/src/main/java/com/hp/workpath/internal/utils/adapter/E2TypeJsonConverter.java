package com.hp.workpath.internal.utils.adapter;

import android.util.Log;

import com.google.gson.JsonObject;
import com.hp.ext.types.common.E2Type;
import com.hp.ws.websocket.JsonTypedObject;

import org.jetbrains.annotations.NotNull;

/**
 * Utility class for converting E2Type objects to various JSON representations.
 * <p>
 * This class provides three conversion methods:
 * <ul>
 *   <li>{@link #toJsonString(E2Type)} - Converts to JSON string using Jackson</li>
 *   <li>{@link #toJsonObject(E2Type)} - Converts to Gson JsonObject</li>
 *   <li>{@link #toJsonTypedObject(E2Type)} - Converts to JsonTypedObject with type metadata</li>
 * </ul>
 * <p>
 * All methods return null if the conversion fails, with errors logged to the Android log.
 *
 * @see E2Type
 * @see E2TypeMapper
 * @see GsonAdapter
 */
public class E2TypeJsonConverter {
    private static final String TAG = "[WS]DSI/E2Type";

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private E2TypeJsonConverter() {
        throw new AssertionError("Utility class should not be instantiated");
    }

    /**
     * Converts an E2 Type (Jackson-annotated) object to JSON string.
     * <p>
     * This method uses Jackson's ObjectMapper to serialize the E2Type object.
     * If the input object is null or serialization fails, null is returned.
     *
     * @param e2Object The E2 Type object to serialize (must not be null)
     * @param <T>      The type extending E2Type
     * @return JSON string representation, or null if serialization fails or input is null
     */
    public static <T extends E2Type> String toJsonString(@NotNull T e2Object) {
        if (e2Object == null) {
            Log.w(TAG, "toJsonString: Received null e2Object, returning null");
            return null;
        }

        try {
            Log.d(TAG, "toJsonString: Serializing E2Type object of class: " + e2Object.getClass().getName());

            // Suppress unchecked cast warning - safe because e2Object is of type T
            @SuppressWarnings("unchecked")
            Class<T> clazz = (Class<T>) e2Object.getClass();
            E2TypeMapper<T> mapper = new E2TypeMapper<>(clazz);

            String jsonString = mapper.writeValueAsString(e2Object);
            return jsonString;
        } catch (Exception e) {
            Log.e(TAG, "toJsonString: Failed to serialize E2 Type object of class "
                    + e2Object.getClass().getName() + ": " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Converts an E2 Type object (Jackson-annotated) to a Gson JsonObject.
     * <p>
     * This method performs a direct conversion by first mapping the E2Type object
     * to a {@code java.util.Map} using Jackson, and then converting the map to a
     * Gson {@code JsonObject}. This is more efficient than serializing to a string
     * and parsing back.
     *
     * @param e2Object The E2 Type object to serialize (must not be null)
     * @param <T>      The type extending E2Type
     * @return Gson JsonObject representation, or null if conversion fails or input is null
     */
    public static <T extends E2Type> JsonObject toJsonObject(@NotNull T e2Object) {
        if (e2Object == null) {
            Log.w(TAG, "toJsonObject: Received null e2Object, returning null");
            return null;
        }

        try {
            Log.d(TAG, "toJsonObject: Converting E2Type object of class: " + e2Object.getClass().getName());

            // Convert using Jackson to JSON string, then parse with Gson
            @SuppressWarnings("unchecked")
            Class<T> clazz = (Class<T>) e2Object.getClass();
            E2TypeMapper<T> mapper = new E2TypeMapper<>(clazz);
            String jsonString = mapper.writeValueAsString(e2Object);

            // Parse the JSON string with Gson
            JsonObject jsonObject = GsonAdapter.INSTANCE.fromJson(jsonString, JsonObject.class);

            Log.d(TAG, "toJsonObject: Successfully created JsonObject");
            return jsonObject;
        } catch (Exception e) {
            Log.e(TAG, "toJsonObject: Failed to convert E2 Type object of class "
                    + e2Object.getClass().getName() + " to JsonObject: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Converts an E2 Type object to a JsonTypedObject with type metadata.
     * <p>
     * This method creates a JsonTypedObject that includes both the JSON representation
     * and the type GUN (Global Unique Name) from the E2Type object. This is useful
     * for WebSocket communications where type information needs to be preserved.
     *
     * @param e2Object The E2 Type object to serialize (must not be null)
     * @param <T>      The type extending E2Type
     * @return JsonTypedObject with type metadata and JSON content, or null if serialization fails or input is null
     * @see JsonTypedObject
     */
    public static <T extends E2Type> JsonTypedObject toJsonTypedObject(@NotNull T e2Object) {
        if (e2Object == null) {
            Log.w(TAG, "toJsonTypedObject: Received null e2Object, returning null");
            return null;
        }

        try {
            String typeGUN = e2Object.getTypeGUN();
            Log.d(TAG, "toJsonTypedObject: Converting E2Type object of class: "
                    + e2Object.getClass().getName() + " with typeGUN: " + typeGUN);

            JsonObject jsonObject = toJsonObject(e2Object);
            if (jsonObject == null) {
                Log.w(TAG, "toJsonTypedObject: toJsonObject returned null, cannot create JsonTypedObject");
                return null;
            }

            JsonTypedObject jsonTypedObject = new JsonTypedObject(typeGUN);
            jsonTypedObject.setValue(jsonObject);
            return jsonTypedObject;
        } catch (Exception e) {
            Log.e(TAG, "toJsonTypedObject: Failed to convert E2 Type object of class "
                    + e2Object.getClass().getName() + " to JsonTypedObject: " + e.getMessage(), e);
            return null;
        }
    }

    /**
     * Converts a JsonTypedObject back to an E2 Type object.
     * <p>
     * This method performs the reverse operation of {@link #toJsonTypedObject(E2Type)}.
     * It extracts the JSON content from the JsonTypedObject and deserializes it
     * into an instance of the specified E2Type class using Jackson.
     *
     * @param jsonTypedObject The JsonTypedObject to deserialize (must not be null)
     * @param clazz           The class of the E2Type to deserialize into (must not be null)
     * @param <T>             The type extending E2Type
     * @return Deserialized E2Type object, or null if deserialization fails or input is null
     */
    public static <T extends E2Type> T fromJsonTypedObject(@NotNull JsonTypedObject jsonTypedObject, @NotNull Class<T> clazz) {
        if (jsonTypedObject == null) {
            Log.w(TAG, "fromJsonTypedObject: Received null jsonTypedObject, returning null");
            return null;
        }

        if (clazz == null) {
            Log.w(TAG, "fromJsonTypedObject: Received null clazz, returning null");
            return null;
        }

        try {
            String typeGUN = jsonTypedObject.getTypeGUN();
            Log.d(TAG, "fromJsonTypedObject: Converting JsonTypedObject with typeGUN: "
                    + typeGUN + " to class: " + clazz.getName());

            JsonObject jsonObject = jsonTypedObject.getValue();
            if (jsonObject == null) {
                Log.w(TAG, "fromJsonTypedObject: JsonTypedObject has null value, returning null");
                return null;
            }

            // Convert JsonObject to JSON string
            String jsonString = GsonAdapter.INSTANCE.toJson(jsonObject);

            // Deserialize using E2TypeMapper
            E2TypeMapper<T> mapper = new E2TypeMapper<>(clazz);
            T e2Object = mapper.deserialize(jsonString);

            Log.d(TAG, "fromJsonTypedObject: Successfully deserialized to " + clazz.getName());
            return e2Object;
        } catch (Exception e) {
            Log.e(TAG, "fromJsonTypedObject: Failed to convert JsonTypedObject to E2Type class "
                    + clazz.getName() + ": " + e.getMessage(), e);
            return null;
        }
    }
}
