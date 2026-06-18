/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.device.services.converter;

import android.util.Log;

import com.hp.ext.types.protocol.Signed64;
import com.hp.ext.types.protocol.Unsigned32;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * RangeTypeConverter is an abstract class that implements the IRangeTypeConverter interface.
 * It provides methods to convert between E2 (Unsigned32) types and a Workpath API Type, using a range of values.
 * setRange() method must be called to set E2 valid boundary range before converting.
 *
 * @param <W> the type of the value - Workpath API Type
 */
public class RangeTypeConverter<W> implements IRangeTypeConverter<W> {
    private final W defaultEnumValue;
    private final Class<W> enumClass;
    private final String TAG;
    protected int workpathOptionRange = 1;
    private Long e2LowerBoundary = 0L;
    private Long e2UpperBoundary = 0L;
    private Long e2StepRange = 1L;
    private W minWorkpathEnum;
    private W maxWorkpathEnum;
    private Boolean rangeInitialized = false;

    /**
     * Constructor for RangeTypeConverter with min and max workpath enums.
     *
     * @param wRange           the range of the Workpath option
     * @param enumClass        the class of the Workpath enum
     * @param defaultEnumValue the default Workpath enum value
     * @param wMin             the minimum Workpath enum value
     * @param wMax             the maximum Workpath enum value
     */
    public RangeTypeConverter(int wRange, Class<W> enumClass, W defaultEnumValue, W wMin, W wMax) {
        this.workpathOptionRange = wRange;
        this.enumClass = enumClass;
        this.defaultEnumValue = defaultEnumValue;
        this.minWorkpathEnum = wMin;
        this.maxWorkpathEnum = wMax;
        this.TAG = "ScanRTC(" + enumClass.getName() + ")";
    }

    protected List<W> getWorkpathTypeList() {
        return new ArrayList<>(Arrays.asList(enumClass.getEnumConstants()));
    }

    protected W getDefaultEnumValue() {
        return defaultEnumValue;
    }

    @Override
    public W convertEtoW(Unsigned32 key) {
        if (!rangeInitialized) {
            //Please call setRange() method to set the range before converting
            throw new RuntimeException("Boundary is not configured in the RangeTypeConverter(" + enumClass.getName() + ")");
        }
        if (key.getValue() < e2LowerBoundary || key.getValue() > e2UpperBoundary) {
            throw new RuntimeException("Out of extensibility value range.");
        }
        if (e2UpperBoundary <= e2LowerBoundary) {
            throw new RuntimeException("Invalid boundary range in the RangeTypeConverter(" + enumClass.getName() + ") - lower: "
                    + e2LowerBoundary + ", upper: " + e2UpperBoundary);
        }
        double result =
                (double) (key.getValue() - e2LowerBoundary) * (workpathOptionRange - 1) / (e2UpperBoundary - e2LowerBoundary);
        int a = Math.toIntExact(Math.round(result));
        return getEnumValue(a);
    }

    @Override
    public W convertEtoW(Unsigned32 value, boolean defaultIfNotFound) {
        W converted = null;
        try {
            converted = convertEtoW(value);
        } catch (RuntimeException e) {
            Log.e(TAG, e.getMessage());
        }
        if (converted == null && defaultIfNotFound) {
            converted = getDefaultEnumValue();
        }
        return converted;
    }

    @Override
    public Unsigned32 convertWtoE(W value) {
        if (!rangeInitialized) {
            throw new RuntimeException("Boundary is not configured in the RangeTypeConverter(" + enumClass.getName() + ")");
        }

        if (value == null || workpathOptionRange <= 0) {
            String errorMessage =
                    "Argument is null or invalid range (" + workpathOptionRange + ") in the RangeTypeConverter(" + enumClass.getName() + ")";
            Log.e(TAG, errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        long enumIndex = getEnumIndex(value);
        long result = e2LowerBoundary;

        if (workpathOptionRange == 1) {
            result = (enumIndex == 0) ? e2LowerBoundary : e2UpperBoundary;
        } else {
            result = Math.round((double) (e2UpperBoundary - e2LowerBoundary) / (workpathOptionRange - 1) * enumIndex);
            //Round the result to the nearest multiple of E2 StepRange
            result = Math.round((double) result / e2StepRange) * e2StepRange + e2LowerBoundary;
        }
        return new Unsigned32(result);
    }

    @Override
    public Unsigned32 convertWtoE(W value, Unsigned32 defaultValue) {
        Unsigned32 converted = null;
        try {
            converted = convertWtoE(value);
        } catch (RuntimeException e) {
            Log.e(TAG, e.getMessage());
        }
        if (converted == null) {
            converted = defaultValue;
        }
        return converted;
    }

    @Override
    public Class<Unsigned32> getE2Type() {
        return Unsigned32.class;
    }

    /**
     * Set E2 boundary range for the converter.
     *
     * @param lower lower boundary of E2 values
     * @param upper upper boundary of E2 values
     * @param step  step range of E2 values
     */
    @Override
    public void setRange(Signed64 lower, Signed64 upper, Unsigned32 step) {
        if (upper.getValue() <= lower.getValue()) {
            Log.e(TAG,
                    "setRange() : The range is invalid in the RangeTypeConverter(" + enumClass.getName() + ") - lower: " + lower.getValue() + ", upper: " + upper.getValue());
            throw new IllegalArgumentException("The range is invalid. The upper boundary must be greater than the lower " +
                    "boundary");
        }
        this.e2LowerBoundary = lower.getValue();
        this.e2UpperBoundary = upper.getValue();
        this.e2StepRange = step.getValue();
        this.rangeInitialized = true;
    }

    @Override
    public List<W> getAllWorkpathEnumsExceptDefault() {
        List<W> ret = getWorkpathTypeList();
        ret.remove(getDefaultEnumValue());
        return ret;
    }

    protected W getEnumValue(int value) {
        W[] enumConstants = enumClass.getEnumConstants();
        int index = minWorkpathEnum instanceof Enum ? ((Enum<?>) minWorkpathEnum).ordinal() : 0;
        int targetIndex = index + value;
        if (targetIndex >= 0 && targetIndex < enumConstants.length) {
            return enumConstants[targetIndex];
        }
        return null;
    }

    /**
     * get Enum Index starting from the lowest level enum value except DEFAULT,
     * (assuming the original enum starts with DEFAULT always and excluding the DEFAULT enum in this index count)
     *
     * @param wValue Workpath API Enum value
     * @return
     */
    protected int getEnumIndex(W wValue) {
        return (wValue != null && wValue instanceof Enum) ? ((Enum<?>) wValue).ordinal() - 1 : 0; // assuming the enum starts
        // with DEFAULT(0)
    }
}
