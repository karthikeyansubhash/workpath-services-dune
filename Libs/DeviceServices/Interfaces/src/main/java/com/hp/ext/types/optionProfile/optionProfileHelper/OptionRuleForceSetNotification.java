package com.hp.ext.types.optionProfile.optionProfileHelper;

import com.hp.ext.types.optionProfile.ForceSetRule;
import com.hp.ext.types.optionProfile.OptionRule;

public class OptionRuleForceSetNotification extends OptionRuleNotification {
    String _optionValueToSet = null;

    /**
     * The value to which the option should be set.
     */
    public String getOptionValueToSet() {
        return _optionValueToSet;
    }

    public void setOptionValueToSet(String value) {
        this._optionValueToSet = value;
    }

    /**
     * The default constructor
     * 
     * @param optionName       The option name to which the notification
     *                         applies.
     * @param optionValueToSet The value to which the option should be set.
     * @param enforcedRule     The OptionRule that triggered the notification
     */
    public OptionRuleForceSetNotification(String optionName, String optionValueToSet, ForceSetRule enforcedRule) {
        super(optionName, OptionRuleNotificationType.OptionForceSet, new OptionRule(enforcedRule));
        _optionValueToSet = optionValueToSet;
    }

    /**
     * Get the ForceSet value as specified type
     * 
     * @param optionName The option name to which the notification
     *                   applies.
     * @param classT     The class to which the values should be coerced.
     * @return Returns the value converted to specified type
     */
    public <T> T getForceSetValue(Class<T> classT) throws OptionProfileHelperException {

        T result = null;

        try {
            if (true == classT.isEnum()) {
                for (T candidate : classT.getEnumConstants()) {
                    if (candidate.toString().equals(_optionValueToSet)) {
                        result = candidate;
                    }
                }
            } else if (true == OptionTypeHelper.isE2EnumerationType(classT)) {
                result = OptionTypeHelper.parseE2Enumeration(_optionValueToSet, classT);
            } else if (_optionValueToSet.equalsIgnoreCase("true") || _optionValueToSet.equalsIgnoreCase("false")) {
                result = (T) Boolean.valueOf(_optionValueToSet);
            } else if ("java.lang.String".equals(classT.getName())) {
                result = (T) _optionValueToSet;
            } else {
                throw new OptionProfileHelperException("GetForceSetValue - unnavailable type");
            }
        } catch (Exception e) {
            throw new OptionProfileHelperException("GetForceSetValue", e);
        }

        return result;
    }
}
