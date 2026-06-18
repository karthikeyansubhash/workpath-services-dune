// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.options;

import java.math.BigDecimal;
/**
 * OptionRule that declares a range restriction.
 */
public class Range
    extends OptionRule
{
    public BigDecimal lowerBoundValue;
    public BigDecimal upperBoundValue;

    /**
     * Default no-arg constructor
     * 
     */
    public Range() {
        super();
    }

    /**
     * Fully-initialising value constructor
     * 
     * @param lowerBoundValue
     *     the BigDecimal
     * @param upperBoundValue
     *     the BigDecimal
     */
    public Range(final Condition condition, final BigDecimal lowerBoundValue, final BigDecimal upperBoundValue) {
        super(condition);
        this.lowerBoundValue = lowerBoundValue;
        this.upperBoundValue = upperBoundValue;
    }

    /**
     * String representation of Range
     * 
     */
    @Override
    public String toString() {
        return new StringBuilder().append("[").append("condition=").append(((condition == null)?"null":condition.toString())).append(", ").append("lowerBoundValue=").append(((lowerBoundValue == null)?"null":lowerBoundValue.toString())).append(", ").append("upperBoundValue=").append(((upperBoundValue == null)?"null":upperBoundValue.toString())).append("]").toString();
    }

}
