// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.options;

public class Equals
    extends ConditionOperator
{
    public String optionName;
    public String optionValue;

    /**
     * Default no-arg constructor
     * 
     */
    public Equals() {
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
    public Equals(final String optionName, final String optionValue) {
        super();
        this.optionName = optionName;
        this.optionValue = optionValue;
    }

    /**
     * String representation of Equals
     * 
     */
    @Override
    public String toString() {
        return new StringBuilder().append("[").append("optionName=").append(optionName).append(", ").append("optionValue=").append(optionValue).append("]").toString();
    }

}
