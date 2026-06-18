// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.options;

import java.util.List;

public class And
    extends ConditionOperator
{
    public List<ConditionOperator> equalsOrNotEqualsOrLessThan;

    /**
     * Default no-arg constructor
     * 
     */
    public And() {
        super();
    }

    /**
     * Fully-initialising value constructor
     * 
     * @param equalsOrNotEqualsOrLessThan
     *     the List<ConditionOperator>
     */
    public And(final List<ConditionOperator> equalsOrNotEqualsOrLessThan) {
        super();
        this.equalsOrNotEqualsOrLessThan = equalsOrNotEqualsOrLessThan;
    }

    /**
     * String representation of And
     * 
     */
    @Override
    public String toString() {
        return new StringBuilder().append(((equalsOrNotEqualsOrLessThan == null)?"null":(("{"+ equalsOrNotEqualsOrLessThan.toString().substring(1, equalsOrNotEqualsOrLessThan.toString().lastIndexOf("]")))+"}"))).toString();
    }

}
