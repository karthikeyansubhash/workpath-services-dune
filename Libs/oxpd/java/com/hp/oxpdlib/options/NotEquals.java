// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.options;

public class NotEquals
    extends ConditionOperator
{

    public String optionName;
    public String optionValue;

    /**
     * Default no-arg constructor
     * 
     */
    public NotEquals() {
        super();
    }

    /**
     * Fully-initialising value constructor
     * 
     * @param optionValue
     *     the String
     * @param optionName
     *     the String
     */
    public NotEquals(final String optionName, final String optionValue) {
        super();
        this.optionName = optionName;
        this.optionValue = optionValue;
    }

    /**
     * String representation of NotEquals
     * 
     */
    @Override
    public String toString() {
        return new StringBuilder().append("[").append("optionName=").append(optionName).append(", ").append("optionValue=").append(optionValue).append("]").toString();
    }

}
