/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.device.services.converter;

import com.hp.ext.types.protocol.Signed64;
import com.hp.ext.types.protocol.Unsigned32;

import java.util.List;

/**
 * RangeTypeConverter interface : data type converter from Device E2 Range Rule types to Workpath API Enum types
 *
 * @param <W> Workpath Option Enum Type
 */
public interface IRangeTypeConverter<W> extends ITypeConverter<Unsigned32, W> {
    void setRange(Signed64 lower, Signed64 upper, Unsigned32 step);

    /**
     * @return all available options except DEFAULT.
     */
    List<W> getAllWorkpathEnumsExceptDefault();
}
