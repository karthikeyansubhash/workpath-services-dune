package com.hp.workpath.internal.utils.adapter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.util.concurrent.CompletionException;

public class E2TypeMapper<T> extends com.fasterxml.jackson.databind.ObjectMapper {
    private Class<T> tClass;

    public E2TypeMapper(Class<T> tClass) {
        this.tClass = tClass;
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    /**
     * Deserializes the given JSON string into an object of E2Type T.
     */
    public T deserialize(String content) {
        try {
            if (content != null && !content.isEmpty()) {
                T value = this.readValue(content, tClass);
                return value;
            }
            return null;
        } catch (IOException ioe) {
            throw new CompletionException(ioe);
        }
    }
}
