package com.hp.workpath.internal.utils.adapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;

import java.lang.reflect.Type;

/**
 * JsonDeserializer class
 * @param <T>
 *
 * Caution : JsonDeserializer is used with Gson @JsonAdapter annotations to deserialize the JSON object.
 * Therefore, it should be excluded from obfuscation in ProGuard.
 */
public class JsonDeserializer<T> implements com.google.gson.JsonDeserializer<T> {
    public JsonDeserializer() {
    }

    @Override
    public T deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext context)
            throws JsonParseException {
        if (jsonElement.isJsonPrimitive()) {
            final JsonPrimitive jsonPrimitive = jsonElement.getAsJsonPrimitive();
            if (jsonPrimitive.isString() && jsonPrimitive.getAsString().isEmpty()) {
                return null;
            }
        }
        return context.deserialize(jsonElement, type);
    }
}
