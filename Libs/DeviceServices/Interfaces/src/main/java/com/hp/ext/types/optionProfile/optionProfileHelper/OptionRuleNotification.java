package com.hp.ext.types.optionProfile.optionProfileHelper;

import com.hp.ext.types.optionProfile.OptionRule;

/// <summary>
/// The base class for all OptionRuleNotifications
/// </summary>
public class OptionRuleNotification {

    protected String _optionName = "";

    /**
     * The option name to which the notification applies.
     */
    public String getOptionName() {
        return _optionName;
    }

    public void setOptionName(String value) {
        this._optionName = value;
    }

    protected OptionRuleNotificationType _notificationType = OptionRuleNotificationType.UnknownNotification;

    /**
     * The type of notification
     */
    public OptionRuleNotificationType getNotificationType() {
        return _notificationType;
    }

    public void setNotificationType(OptionRuleNotificationType value) {
        this._notificationType = value;
    }

    protected OptionRule _enforcedRule = null;

    /**
     * The OptionRule that triggered the notification
     */
    public OptionRule getEnforcedRule() {
        return _enforcedRule;
    }

    public void setEnforcedRule(OptionRule value) {
        this._enforcedRule = value;
    }

    /**
     * Get The default constructor
     * 
     * @param optionName       The option name to which the notification
     *                         applies.
     * @param notificationType The type of notification
     * @param enforcedRule     The OptionRule that triggered the notification
     */
    protected OptionRuleNotification(String optionName, OptionRuleNotificationType notificationType,
            OptionRule enforcedRule) {
        _optionName = optionName;
        _notificationType = notificationType;
        _enforcedRule = enforcedRule;
    }
}
