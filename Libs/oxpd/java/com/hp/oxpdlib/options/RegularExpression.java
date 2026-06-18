// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.options;

public class RegularExpression
    extends OptionRule
{

    public String expression;

    /**
     * Default no-arg constructor
     * 
     */
    public RegularExpression() {
        super();
    }

    /**
     * Fully-initialising value constructor
     * 
     * @param expression
     *     the String
     */
    public RegularExpression(final Condition condition, final String expression) {
        super(condition);
        this.expression = expression;
    }

    /**
     * String representation of RegularExpression
     * 
     */
    @Override
    public String toString() {
        return new StringBuilder().append("[").append("condition=").append(((condition == null)?"null":condition.toString())).append(", ").append("expression=").append(expression).append("]").toString();
    }

}
