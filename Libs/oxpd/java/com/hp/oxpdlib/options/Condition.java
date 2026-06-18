// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.options;

/**
 * An condition that must be met for the OptionRule to be applicable. *
 */
public class Condition {
    public And and;
    public Or or;
    public Equals equals;
    public NotEquals notEquals;
    public LessThan lessThan;
    public GreaterThan greaterThan;

    /**
     * Default no-arg constructor
     * 
     */

    public Condition() {
        super();
    }

    /**
     * Fully-initialising value constructor
     * 
     * @param or
     *     the Or
     * @param and
     *     the And
     * @param equals
     *     the Equals
     * @param lessThan
     *     the LessThan
     * @param notEquals
     *     the NotEquals
     * @param greaterThan
     *     the GreaterThan
     */

    public Condition(final And and, final Or or, final Equals equals, final NotEquals notEquals, final LessThan lessThan, final GreaterThan greaterThan) {
        this.and = and;
        this.or = or;
        this.equals = equals;
        this.notEquals = notEquals;
        this.lessThan = lessThan;
        this.greaterThan = greaterThan;
    }

    /**
     * String representation of Condition
     * 
     */
    @Override
    public String toString() {
        return new StringBuilder().append("[").append("and=").append(((and == null)?"null":and.toString())).append(", ").append("or=").append(((or == null)?"null":or.toString())).append(", ").append("equals=").append(((equals == null)?"null":equals.toString())).append(", ").append("notEquals=").append(((notEquals == null)?"null":notEquals.toString())).append(", ").append("lessThan=").append(((lessThan == null)?"null":lessThan.toString())).append(", ").append("greaterThan=").append(((greaterThan == null)?"null":greaterThan.toString())).append("]").toString();
    }
}
