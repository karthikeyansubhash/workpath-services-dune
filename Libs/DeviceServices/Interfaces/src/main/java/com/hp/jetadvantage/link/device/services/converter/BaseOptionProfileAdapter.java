/**
 * (C) Copyright 2025 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.device.services.converter;

import android.util.Log;

import androidx.annotation.NonNull;

import com.hp.ext.types.optionProfile.RangeRule;
import com.hp.ext.types.optionProfile.optionProfileHelper.OptionProfileHelper;
import com.hp.ext.types.optionProfile.optionProfileHelper.OptionProfileHelperException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public abstract class BaseOptionProfileAdapter {
    private static final String TAG = "[WS]DSI/BOPAdapter";

    /**
     * Get possible Workpath API type options for the given option.
     *
     * @param optionProfileHelper E2 Job OptionProfileHelper instance
     * @param option              Option map enum
     * @param <TOptions>          E2 Job Options Type (ex. ScanOptions, CopyOptions)
     * @param <TMap>              Enum type that implements ITypeMapping
     * @param <E>                 E2 type
     * @param <W>                 Workpath API type
     * @return List of possible Workpath API options, or null if not available
     */
    public static <TOptions, TMap extends Enum & ITypeMapping, E, W> List<W> getPossibleWorkpathOptions(OptionProfileHelper<TOptions> optionProfileHelper, TMap option) {
        String e2OptionName = option.name();
        try {
            if (!isOptionAvailable(optionProfileHelper, e2OptionName)) {
                Log.w(TAG, "getPossibleWorkpathOptions: optionName = " + e2OptionName + " is not available");
                return null;
            }

            ITypeConverter<E, W> converter = option.getConverter();
            if (converter == null) {
                Log.e(TAG, "getPossibleWorkpathOptions: cannot find converter for optionName = " + option.name());
                return null;
            }

            switch (option.getCategory()) {
                case ENUM_RANGE:
                    return convertToRangeTypeWorkpathOptionList(optionProfileHelper, e2OptionName,
                            (IRangeTypeConverter<W>) converter);
                case DEFAULT:
                case CUSTOM:
                    return convertToWorkpathOptionList(optionProfileHelper, e2OptionName, converter);
                default:
                    Log.e(TAG, "getPossibleWorkpathOptions (" + e2OptionName + "): Unknown category = " + option.getCategory());
                    return null;
            }
        } catch (OptionProfileHelperException e) {
            Log.e(TAG, "getPossibleWorkpathOptions (" + e2OptionName + "): OptionProfileHelperException = " + e.getMessage());
            return null;
        } catch (Exception e) {
            Log.e(TAG, "getPossibleWorkpathOptions (" + e2OptionName + "): Exception[ " + e.getClass() + "]" + e.getMessage(), e);
            return null;
        }
    }

    protected static <TOptions, TMap extends Enum & ITypeMapping, W, R> void addPossibleOptions(OptionProfileHelper<TOptions> optionProfileHelper, TMap option, AddToBuilder<W, R> addFunc) {
        List<W> workpathOptions = getPossibleWorkpathOptions(optionProfileHelper, option);
        if (workpathOptions != null && !workpathOptions.isEmpty()) {
            workpathOptions.forEach(addFunc::add);
        }
    }

    protected static <TOptions, TMap extends Enum & ITypeMapping, W, R> void setNumericRangeOptions(OptionProfileHelper<TOptions> optionProfileHelper, TMap option, SetToBuilder<W, R> setFunc) {
        try {
            if (isOptionAvailable(optionProfileHelper, option.name())) {
                switch (option.getCategory()) {
                    case INTEGER_RANGE:
                        RangeRule rangeRule = optionProfileHelper.getCurrentRange(option.name(), null);
                        setFunc.set(option.convertEtoW(rangeRule.getLowerBoundary()),
                                option.convertEtoW(rangeRule.getUpperBoundary()));
                        break;
                    case FLOAT_RANGE:
                        Log.e(TAG, "setNumericRangeOptions: FLOAT_RANGE not supported yet for optionName = " + option.name());
                        break;
                    default:
                        Log.e(TAG,
                                "setNumericRangeOptions: optionName = " + option.name() + " is not a numeric range type. " +
                                        "It's a" + " " + option.getCategory().name());
                        break;
                }
            } else {
                Log.i(TAG, "setNumericRangeOptions: optionName = " + option.name() + " is not available in E2, skipping");
            }
        } catch (Exception e) {
            Log.e(TAG, "setNumericRangeOptions: Exception for handling " + option.name() + " e = " + e.getMessage());
        }
    }

    protected static <TOptions> boolean isOptionAvailable(@NonNull OptionProfileHelper<TOptions> optionProfileHelper,
                                                          String e2OptionName) {
        try {
            return optionProfileHelper.getOptionDefinition(e2OptionName).getIsAvailable();
        } catch (Exception e) {
            Log.e(TAG, "isOptionAvailable: e = " + e);
            return false;
        }
    }

    protected static <TOptions, W> List<W> convertToRangeTypeWorkpathOptionList(@NonNull OptionProfileHelper<TOptions> optionProfileHelper, String e2OptionName, IRangeTypeConverter<W> converter) throws Exception {
        List<W> workpathOptions = new ArrayList<>();
        RangeRule rangeRule = optionProfileHelper.getCurrentRange(e2OptionName, null);
        converter.setRange(rangeRule.getLowerBoundary(), rangeRule.getUpperBoundary(), rangeRule.getStep());
        workpathOptions.addAll(converter.getAllWorkpathEnumsExceptDefault());
        return workpathOptions;
    }

    protected static <TOptions, E, W> List<W> convertToWorkpathOptionList(@NonNull OptionProfileHelper<TOptions> optionProfileHelper, String e2OptionName, ITypeConverter<E, W> converter) throws OptionProfileHelperException, IllegalAccessException {
        List<W> workpathOptions = new ArrayList<>();
        List<E> e2Options = optionProfileHelper.getPossibleValues(e2OptionName, converter.getE2Type());

        if (e2Options == null) {
            e2Options = getOptionsFromFields(optionProfileHelper, e2OptionName, converter.getE2Type());
        }
        if (e2Options == null || e2Options.isEmpty()) {
            Log.e(TAG, "convertToWorkpathOptionList: e2Options is null or empty");
            return workpathOptions;
        }

        for (E e2Option : e2Options) {
            W workpathOption = converter.convertEtoW(e2Option);
            if (workpathOption != null) {
                workpathOptions.add(workpathOption);
            }
        }
        return workpathOptions;
    }

    protected static <TOptions, E> List<E> getOptionsFromFields(@NonNull OptionProfileHelper<TOptions> optionProfileHelper,
                                                                String e2OptionName, Class<E> e2Type) throws IllegalAccessException {
        List<E> extensibilityOptions = new ArrayList<>();
        Field[] fields = ((Field) optionProfileHelper.getOptionPropertyInfo(e2OptionName)).getType().getFields();
        if (fields == null || fields.length == 0) {
            return extensibilityOptions;
        }
        for (Field field : fields) {
            if (field.getType().equals(e2Type)) {
                extensibilityOptions.add((E) field.get(null));
            }
        }
        return extensibilityOptions;
    }

    @FunctionalInterface
    public interface AddToBuilder<T, R> {
        R add(T t);
    }

    @FunctionalInterface
    public interface SetToBuilder<T, R> {
        R set(T lowerBound, T upperBound);
    }
}
