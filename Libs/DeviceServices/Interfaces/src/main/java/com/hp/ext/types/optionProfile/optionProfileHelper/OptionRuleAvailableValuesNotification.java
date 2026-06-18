package com.hp.ext.types.optionProfile.optionProfileHelper;

import java.util.ArrayList;
import java.util.List;

import com.hp.ext.types.optionProfile.OptionRule;

public class OptionRuleAvailableValuesNotification extends OptionRuleNotification {
    List<String> _optionAvailableValues = null;

    /// <summary>
    /// The list of valid values for the option
    /// </summary>

    /**
     * The list of valid values for the option
     */
    public List<String> getOptionAvailableValues() {
        return _optionAvailableValues;
    }

    public void setOptionAvailableValues(List<String> value) {
        this._optionAvailableValues = value;
    }

    /**
     * Check to see if the provided optionsInstance is without conflicts
     * 
     * @param optionName            The option name to which the notification
     *                              applies.
     * @param optionAvailableValues The list of available values for the option
     * @param enforcedRule          The OptionRule that triggered the notification
     */
    public OptionRuleAvailableValuesNotification(String optionName, List<String> optionAvailableValues,
            OptionRule enforcedRule) {
        super(optionName, OptionRuleNotificationType.OptionAvailableValues, enforcedRule);
        _optionAvailableValues = optionAvailableValues;
    }

    /**
     * Check to see if the provided optionsInstance is without conflicts
     * 
     * @param classT The class to which the values should be coerced
     * @return Returns the typed-list of valid values
     */
    public <T> List<T> getOptionAvailableValues(Class<T> classT) throws OptionProfileHelperException {
        List<T> result = new ArrayList<T>();

        if (null != _optionAvailableValues) {
            for (String optionValue : _optionAvailableValues) {
                try {
                    if (true == classT.isEnum()) {
                        for (T candidate : classT.getEnumConstants()) {
                            if (candidate.toString().equals(optionValue)) {
                                result.add(candidate);
                            }
                        }
                    } else if (true == OptionTypeHelper.isE2EnumerationType(classT)) {
                        result.add(OptionTypeHelper.parseE2Enumeration(optionValue, classT));
                    } else if (optionValue.equalsIgnoreCase("true") || optionValue.equalsIgnoreCase("false")) {
                        result.add((T) Boolean.valueOf(optionValue));
                    } else if ("java.lang.String".equals(classT.getName())) {
                        result.add((T) optionValue);
                    } else {
                        throw new OptionProfileHelperException("GetOptionAvailableValues - unnavailable type");
                    }
                } catch (Exception e) {
                    throw new OptionProfileHelperException("GetOptionAvailableValues", e);
                }
            }
        }

        return result;
    }
}
