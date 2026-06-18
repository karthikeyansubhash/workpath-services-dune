package com.hp.ext.clients;

import java.nio.charset.StandardCharsets;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;

/**
 * (C) Copyright 2023 HP Development Company, L.P.
 * All rights reserved.
 */
public class MultipartResponseHelper {

    public static <T> SimpleEntry<T, byte[]> multipartResponse(List<MultipartResponseItem> list, Class<T> type) {
        byte[] data = null;
        String contentPart = null;
        T part = null;
        CustomObjectMapper<T> mapper = new CustomObjectMapper(type);
        for (MultipartResponseItem multipartResponseItem : list) {
            if (multipartResponseItem.getName().equals("content")) {
                contentPart = new String(multipartResponseItem.getContent(), StandardCharsets.UTF_8);
                part = mapper.readValue(contentPart);
            }
            if (multipartResponseItem.getName().equalsIgnoreCase(type.getName())) {
                data = multipartResponseItem.getContent();
            }
        }
        SimpleEntry<T, byte[]> map = new SimpleEntry<T, byte[]>(part, data);
        return map;
    }

}
