// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.options;

public class ForceSet
    extends OptionRule
{

    public String optionValue;

    /**
     * Default no-arg constructor
     * 
     */
    public ForceSet() {
        super();
    }

    /**
     * Fully-initialising value constructor
     * 
     * @param optionValue
     *     the String
     */
    public ForceSet(final Condition condition, final String optionValue) {
        super(condition);
        this.optionValue = optionValue;
    }

    /**
     * String representation of ForceSet
     * 
     */
    @Override
    public String toString() {
        return new StringBuilder().append("[").append("condition=").append(((condition == null)?"null":condition.toString())).append(", ").append("optionValue=").append(optionValue).append("]").toString();
    }

}
