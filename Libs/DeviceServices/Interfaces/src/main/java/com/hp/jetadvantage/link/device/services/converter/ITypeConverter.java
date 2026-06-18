/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.device.services.converter;

/**
 * TypeConverter interface : data type converter from Device E2 types to Workpath API types
 *
 * @param <E> Extensibility Option Type
 * @param <W> Workpath Option Type
 */
public interface ITypeConverter<E, W> {
    /**
     * Convert E2 type to Workpath API type
     * @param value E2 type value to be converted
     * @return converted Workpath API type value
     */
    W convertEtoW(E value);

    /**
     * Convert E2 type to Workpath API type with default value
     * @param value E2 type value to be converted
     * @param defaultIfNotFound default value if not found
     * @return converted Workpath API type value
     */
    W convertEtoW(E value, boolean defaultIfNotFound);

    /**
     * Convert Workpath API type to E2 type
     * @param value Workpath API type value to be converted
     * @return converted E2 type value
     */
    E convertWtoE(W value);

    /**
     * Convert Workpath API type to E2 type with default value
     * @param value Workpath API type value to be converted
     * @param defaultValue default value if not found
     * @return converted E2 type value
     */
    E convertWtoE(W value, E defaultValue);

    /**
     * Get E2 type
     * @return E2 type
     */
    Class<E> getE2Type();
}
