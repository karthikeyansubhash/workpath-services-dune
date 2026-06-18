// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.options;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Option definition
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class OptionDefinition implements Parcelable {
    public final OptionName optionName;
    public final boolean isAvailable;

    public List<OptionRule> rangeOrStringLengthOrRegularExpression;

    /**
     * Fully-initialising value constructor
     * 
     * @param isAvailable
     *     the boolean
     * @param optionName
     *     the String
     * @param rangeOrStringLengthOrRegularExpression
     *     the List<OptionRule>
     */
    public OptionDefinition(final OptionName optionName, final boolean isAvailable, final List<OptionRule> rangeOrStringLengthOrRegularExpression) {
        this.optionName = optionName;
        this.isAvailable = isAvailable;
        this.rangeOrStringLengthOrRegularExpression = rangeOrStringLengthOrRegularExpression;
    }

    protected OptionDefinition(Parcel in) {
        optionName = (OptionName) in.readSerializable();
        isAvailable = in.readByte() != 0;

        List<OptionRule> optionRules = new ArrayList<>();
        for(int length = in.readInt(); length > 0; length--) {
            optionRules.add((OptionRule) in.readParcelable(OptionRule.class.getClassLoader()));
        }
        this.rangeOrStringLengthOrRegularExpression = Collections.unmodifiableList(optionRules);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(optionName);
        dest.writeByte((byte) (isAvailable ? 1 : 0));
        dest.writeInt(this.rangeOrStringLengthOrRegularExpression.size());
        for(OptionRule optionRule : this.rangeOrStringLengthOrRegularExpression) {
            dest.writeParcelable(optionRule, 0);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OptionDefinition> CREATOR = new Creator<OptionDefinition>() {
        @Override
        public OptionDefinition createFromParcel(Parcel in) {
            return new OptionDefinition(in);
        }

        @Override
        public OptionDefinition[] newArray(int size) {
            return new OptionDefinition[size];
        }
    };

    public static OptionDefinition fromAttributeValue(String data) {
        return null;
    }
}
