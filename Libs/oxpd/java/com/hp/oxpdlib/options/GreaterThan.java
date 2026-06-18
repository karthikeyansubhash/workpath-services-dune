// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.options;

import java.math.BigDecimal;

public class GreaterThan
    extends ConditionOperator
{

    public String optionName;
    public BigDecimal optionValue;

    /**
     * Default no-arg constructor
     * 
     */
    public GreaterThan() {
        super();
    }

    /**
     * Fully-initialising value constructor
     * 
     * @param optionValue
     *     the BigDecimal
     * @param optionName
     *     the String
     */
    public GreaterThan(final String optionName, final BigDecimal optionValue) {
        super();
        this.optionName = optionName;
        this.optionValue = optionValue;
    }

    /**
     * String representation of GreaterThan
     * 
     */
    @Override
    public String toString() {
        return new StringBuilder().append("[").append("optionName=").append(optionName).append(", ").append("optionValue=").append(((optionValue == null)?"null":optionValue.toString())).append("]").toString();
    }

}
