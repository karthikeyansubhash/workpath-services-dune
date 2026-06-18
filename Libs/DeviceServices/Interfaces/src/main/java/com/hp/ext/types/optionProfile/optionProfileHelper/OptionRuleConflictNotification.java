package com.hp.ext.types.optionProfile.optionProfileHelper;

import com.hp.ext.types.optionProfile.OptionRule;

public class OptionRuleConflictNotification extends OptionRuleNotification {

    protected OptionRuleConflictType _conflictType = OptionRuleConflictType.UnknownConflict;

    /**
     * The specific type of conflict
     */
    public OptionRuleConflictType getConflictType() {
        return _conflictType;
    }

    public void setConflictType(OptionRuleConflictType value) {
        this._conflictType = value;
    }

    /**
     * Check to see if the provided optionsInstance is without conflicts
     * 
     * @param optionName   The option name to which the notification
     *                     applies.
     * @param conflictType The specific type of conflict
     * @param enforcedRule The OptionRule that triggered the notification
     */
    public OptionRuleConflictNotification(String optionName, OptionRuleConflictType conflictType,
            OptionRule enforcedRule) {
        super(optionName, OptionRuleNotificationType.OptionConflict, enforcedRule);
        _conflictType = conflictType;
    }

}
