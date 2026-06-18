/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.device.services.converter;

/**
 * Interface for defining mappings between E2 type and Workpath API type to convert data types.
 * Provides methods to retrieve type information, default values, and mapping configurations.
 * Each component will implement this interface to define its own type mapping enum.
 */
public interface ITypeMapping {
    Category getCategory();

    <E, W> ITypeConverter<E, W> getConverter();

    /**
     * Convert E2 type to Workpath API type.
     *
     * @param value E2 type value
     * @param <E>   E2 type
     * @param <W>   Workpath API type
     * @return converted Workpath API type value
     */
    default <E, W> W convertEtoW(E value) {
        ITypeConverter<E, W> converter = getConverter();
        if (converter != null) {
            return (W) converter.convertEtoW(value);
        }
        return null;
    }

    default <E, W> W convertEtoW(E value, boolean defaultNotFound) {
        ITypeConverter<E, W> converter = getConverter();
        if (converter != null) {
            return (W) converter.convertEtoW(value, defaultNotFound);
        }
        return null;
    }

    /**
     * Convert Workpath API type to E2 type.
     *
     * @param value Workpath API type value
     * @param <E>   E2 type
     * @param <W>   Workpath API type
     * @return converted E2 type value
     */
    default <E, W> E convertWtoE(W value) {
        ITypeConverter<E, W> converter = getConverter();
        if (converter != null) {
            return (E) converter.convertWtoE(value);
        }
        return null;
    }

    /**
     * Convert Workpath API type to E2 type with default value.
     * @param value Workpath API type value
     * @param defaultValue Default value for E2 type
     * @return converted E2 type value, or defaultValue if conversion fails
     * @param <E> E2 type
     * @param <W> Workpath API type
     */
    default <E, W> E convertWtoE(W value, E defaultValue) {
        ITypeConverter<E, W> converter = getConverter();
        if (converter != null) {
            return (E) converter.convertWtoE(value, defaultValue);
        }
        return defaultValue;
    }

    default <E> Class<E> getE2Type() {
        ITypeConverter<E, ?> converter = getConverter();
        if (converter != null) {
            return converter.getE2Type();
        }
        return null;
    }

    /**
     * Enum for defining the category of the type mapping between E2 and Workpath API.
     * DEFAULT - 1:1 type mapping.
     * ENUM_RANGE - Workpath Enum mapping with E2 range values.
     * INTEGER_RANGE - Workpath Integer range mapping with E2 range values.
     * FLOAT_RANGE - Workpath Float range mapping with E2 range values.
     * CUSTOM - Custom type mapping.
     */
    enum Category {
        DEFAULT, ENUM_RANGE, INTEGER_RANGE, FLOAT_RANGE, CUSTOM
    }
}
