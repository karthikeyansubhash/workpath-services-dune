// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.options;

import java.util.List;

/**
 * An OptionRule that declares a range of valid values.
 */
public class ValidValues
    extends OptionRule
{
    public List<String> optionValues;

    /**
     * Default no-arg constructor
     * 
     */
    public ValidValues() {
        super();
    }

    /**
     * Fully-initialising value constructor
     * 
     * @param optionValues
     *     the List<String>
     */
    public ValidValues(final Condition condition, final List<String> optionValues) {
        super(condition);
        this.optionValues = optionValues;
    }

    /**
     * String representation of ValidValues
     * 
     */
    @Override
    public String toString() {
        return new StringBuilder().append("[").append("condition=").append(((condition == null)?"null":condition.toString())).append(", ").append(((optionValues == null)?"null":(("{"+ optionValues.toString().substring(1, optionValues.toString().lastIndexOf("]")))+"}"))).toString();
    }

}
