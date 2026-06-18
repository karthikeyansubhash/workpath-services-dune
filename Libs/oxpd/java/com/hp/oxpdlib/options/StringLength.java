// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.options;

public class StringLength
    extends OptionRule
{

    public long minimumLength;
    public long maximumLength;

    /**
     * Default no-arg constructor
     * 
     */
    public StringLength() {
        super();
    }

    /**
     * Fully-initialising value constructor
     * 
     * @param minimumLength
     *     the long
     * @param maximumLength
     *     the long
     */
    public StringLength(final Condition condition, final long minimumLength, final long maximumLength) {
        super(condition);
        this.minimumLength = minimumLength;
        this.maximumLength = maximumLength;
    }

    /**
     * String representation of StringLength
     * 
     */
    @Override
    public String toString() {
        return new StringBuilder().append("[").append("condition=").append(((condition == null)?"null":condition.toString())).append(", ").append("minimumLength=").append(minimumLength).append(", ").append("maximumLength=").append(maximumLength).append("]").toString();
    }

}
