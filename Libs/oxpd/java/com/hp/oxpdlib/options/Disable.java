// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.options;

public class Disable
    extends OptionRule
{
    /**
     * Default no-arg constructor
     * 
     */
    public Disable() {
        super();
    }

    /**
     * Fully-initialising value constructor
     * 
     */
    public Disable(final Condition condition) {
        super(condition);
    }

    /**
     * String representation of Disable
     * 
     */
    @Override
    public String toString() {
        return new StringBuilder().append("[").append("condition=").append(((condition == null)?"null":condition.toString())).append("]").toString();
    }

}
