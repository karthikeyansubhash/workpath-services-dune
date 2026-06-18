package com.hp.ext.types.optionProfile.optionProfileHelper;

import com.hp.ext.types.optionProfile.OptionRule;

public class OptionRuleDisabledNotification extends OptionRuleNotification {
    
    /**
     * The default constructor
     * 
     * @param optionName   The option name to which the notification
     *                     applies.
     * @param enforcedRule The OptionRule that triggered the notification
     */
    public OptionRuleDisabledNotification(String optionName, OptionRule enforcedRule) {
        super(optionName, OptionRuleNotificationType.OptionDisabled, enforcedRule);
    }
}
