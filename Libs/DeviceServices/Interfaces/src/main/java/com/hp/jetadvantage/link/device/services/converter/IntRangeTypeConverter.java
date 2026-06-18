/**
 * (C) Copyright 2025 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.device.services.converter;

import android.util.Log;

import com.hp.ext.types.protocol.Signed16;
import com.hp.ext.types.protocol.Signed32;
import com.hp.ext.types.protocol.Signed64;
import com.hp.ext.types.protocol.Unsigned16;
import com.hp.ext.types.protocol.Unsigned32;
import com.hp.ext.types.protocol.Unsigned64;

public class IntRangeTypeConverter<E> implements INumericRangeTypeConverter<E, Integer> {
    private static final String TAG = "[WS]DSI/IntRange";
    private final Class<E> eClass;

    public IntRangeTypeConverter(Class<E> eClass) {
        this.eClass = eClass;
    }

    @Override
    public Integer convertEtoW(E value) {
        if (value instanceof Signed64 signed64Value) {
            return signed64Value.getValue().intValue();
        } else if (value instanceof Unsigned64 unsigned64Value) {
            return unsigned64Value.getValue().intValue();
        } else if (value instanceof Unsigned32 unsigned32Value) {
            return unsigned32Value.getValue().intValue();
        } else if (value instanceof Signed32 signed32Value) {
            return signed32Value.getValue().intValue();
        } else if (value instanceof Signed16 signed16Value) {
            return signed16Value.getValue().intValue();
        } else if (value instanceof Unsigned16 unsigned16Value) {
            return unsigned16Value.getValue().intValue();
        } else {
            String typeGun = ((com.hp.ext.types.common.E2Type) value).getTypeGUN();
            Log.e(TAG, "convertEtoW: Unsupported E2 Numeric type for converting to Integer: " + typeGun);
            throw new IllegalArgumentException("Unsupported E2 Numeric type for converting to Integer: " + typeGun);
        }
    }

    @Override
    public Integer convertEtoW(E value, boolean defaultIfNotFound) {
        return convertEtoW(value);
    }

    @Override
    public E convertWtoE(Integer value) {
        E converted = null;
        try {
            if (eClass == Signed64.class) {
                converted = (E) new Signed64(value.longValue());
            } else if (eClass == Unsigned64.class) {
                converted = (E) new Unsigned64(value.longValue());
            } else if (eClass == Signed32.class) {
                converted = (E) new Signed32(value);
            } else if (eClass == Unsigned32.class) {
                converted = (E) new Unsigned32(value.longValue());
            } else if (eClass == Signed16.class) {
                converted = (E) new Signed16(value.shortValue());
            } else if (eClass == Unsigned16.class) {
                converted = (E) new Unsigned16(value);
            } else {
                Log.e(TAG, "convertWtoE: Not supported type " + eClass.getName());
            }
            return converted;
        } catch (Exception e) {
            Log.e(TAG, "convertWtoE: Error creating new instance of " + eClass.getName(), e);
            return null;
        }
    }

    @Override
    public E convertWtoE(Integer value, E defaultValue) {
        E converted = convertWtoE(value);
        return converted != null ? converted : defaultValue;
    }

    @Override
    public Class<E> getE2Type() {
        return eClass;
    }
}
