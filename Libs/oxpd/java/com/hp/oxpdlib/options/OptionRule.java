// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.options;

import android.os.Parcel;
import android.os.Parcelable;

public class OptionRule implements Parcelable {
    public Condition condition;

    /**
     * Default no-arg constructor
     * 
     */
    public OptionRule() {
        super();
    }

    /**
     * Fully-initialising value constructor
     * 
     * @param condition
     *     the Condition
     */

    public OptionRule(final Condition condition) {
        this.condition = condition;
    }

    private OptionRule(Parcel in) {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OptionRule> CREATOR = new Creator<OptionRule>() {
        @Override
        public OptionRule createFromParcel(Parcel in) {
            return new OptionRule(in);
        }

        @Override
        public OptionRule[] newArray(int size) {
            return new OptionRule[size];
        }
    };

    /**
     * String representation of OptionRule
     * 
     */

    @Override
    public String toString() {
        return new StringBuilder().append("[").append("condition=").append(((condition == null)?"null":condition.toString())).append("]").toString();
    }
}
