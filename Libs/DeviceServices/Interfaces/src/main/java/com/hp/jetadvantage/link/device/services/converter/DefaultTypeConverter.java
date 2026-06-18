/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.device.services.converter;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * DefaultTypeConverter is a generic class that implements the ITypeConverter interface.
 * It provides methods to convert between two types, E(E2) type and W(Workpath API) type, using a HashMap for the conversion.
 * The class also supports default values for type W and allows reverse lookup from W to E.
 *
 * @param <E> the type of the key - E2Type class
 * @param <W> the type of the value - Workpath API class
 */
public class DefaultTypeConverter<E, W> implements ITypeConverter<E, W> {
    protected final HashMap<E, W> mapEtoW = new HashMap<>();
    private final Class<E> eClass;
    private final W defaultEnumValue;

    public DefaultTypeConverter(Class<E> eClass, W defaultEnumValue) {
        this.eClass = eClass;
        this.defaultEnumValue = defaultEnumValue;
    }

    private <K, V> Optional<K> getKeyByValue(Map<K, V> map, V value) {

        return map.entrySet().stream()
                .filter(entry -> entry.getValue().equals(value))
                .map(Map.Entry::getKey)
                .findFirst();
    }

    protected W getDefaultEnumValue() {
        return defaultEnumValue;
    }

    @Override
    public W convertEtoW(E key) {
        return Optional.ofNullable(key)
                .map(mapEtoW::get)
                .orElse(null);
    }

    @Override
    public W convertEtoW(E value, boolean defaultIfNotFound) {
        return Optional.ofNullable(convertEtoW(value))
                .orElse(defaultIfNotFound ? getDefaultEnumValue() : null);
    }

    @Override
    public E convertWtoE(W value) {
        return value != null ? getKeyByValue(mapEtoW, value).orElse(null) : null;
    }

    @Override
    public E convertWtoE(W value, E defaultValue) {
        E convertedValue = convertWtoE(value);
        return convertedValue != null ? convertedValue : defaultValue;
    }

    @Override
    public Class<E> getE2Type() {
        return eClass;
    }
}
