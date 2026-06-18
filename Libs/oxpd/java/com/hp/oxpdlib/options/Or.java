// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.options;

import java.util.List;

public class Or
    extends ConditionOperator
{

    public List<ConditionOperator> equalsOrNotEqualsOrLessThan;

    /**
     * Default no-arg constructor
     * 
     */
    public Or() {
        super();
    }

    /**
     * Fully-initialising value constructor
     * 
     * @param equalsOrNotEqualsOrLessThan
     *     the List<ConditionOperator>
     */
    public Or(final List<ConditionOperator> equalsOrNotEqualsOrLessThan) {
        super();
        this.equalsOrNotEqualsOrLessThan = equalsOrNotEqualsOrLessThan;
    }

     /**
     * String representation of Or
     * 
     */
    @Override
    public String toString() {
        return new StringBuilder().append(((equalsOrNotEqualsOrLessThan == null)?"null":(("{"+ equalsOrNotEqualsOrLessThan.toString().substring(1, equalsOrNotEqualsOrLessThan.toString().lastIndexOf("]")))+"}"))).toString();
    }

}
