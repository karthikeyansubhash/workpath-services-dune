package com.hp.ext.types.optionProfile.optionProfileHelper;

public enum OptionRuleConflictType {

    /**
     * An unknown conflict
     */
    UnknownConflict,

    /**
     * Identifies a Range conflict
     */
    Range,

    /**
     * Identifies a StringLength conflict
     */
    StringLength,

    /**
     * Identifies a RegularExpression conflict
     */
    RegularExpression,

    /**
     * Identifies a ValidValue conflict
     */
    ValidValue
}
