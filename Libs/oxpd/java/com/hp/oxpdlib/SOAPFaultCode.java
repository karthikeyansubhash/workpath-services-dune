// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.Stack;

/**
 * Container for SOAP fault codes
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class SOAPFaultCode implements Parcelable {
    /** Reported fault code */
    public final String mValue;
    /** Fault sub-code (may be null) */
    public final SOAPFaultCode mSubCode;

    /**
     * Constructor
     * @param value Fault code value
     * @param subcode Fault sub-code (may be null)
     */
    private SOAPFaultCode(String value, SOAPFaultCode subcode) {
        mValue = value;
        mSubCode = subcode;
    }

    /**
     * Constructor
     * @param in Parcel from which to reconstruct object
     */
    private SOAPFaultCode(Parcel in) {
        mValue = in.readString();
        mSubCode = in.readParcelable(SOAPFaultCode.class.getClassLoader());
    }

    /**
     * Returns a hash code value for the object
     * @return Integer hash value for this object.
     */
    @Override
    public int hashCode() {
        int result = 1;
        final int prime = 31;
        result = result * prime + mValue.hashCode();
        result = result * prime + ((mSubCode != null) ? mSubCode.hashCode() : 0);
        return result;
    }

    /**
     * Returns a string representation of this object.
     * @return a string representation of this object.
     */
    @Override
    public String toString() {
        return mValue + ((mSubCode != null) ? "." + mSubCode.toString() : "");
    }

    /**
     * Indicates whether some other object is "equal" to this one.
     * @return true if equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SOAPFaultCode)) return false;
        SOAPFaultCode other = (SOAPFaultCode)obj;
        return TextUtils.equals(mValue, other.mValue) && ((mSubCode == other.mSubCode) || ((mSubCode != null) && mSubCode.equals(other.mSubCode)));
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable instance's marshaled representation.
     * @return
     *              0 for no special objects
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     * @param dest The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written. May be 0 or {@link Parcelable#PARCELABLE_WRITE_RETURN_VALUE}
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mValue);
        dest.writeParcelable(mSubCode, flags);
    }

    /**
     * {@link SOAPFaultCode} parcelable creator
     */
    public static final Creator<SOAPFaultCode> CREATOR = new Creator<SOAPFaultCode>() {
        @Override
        public SOAPFaultCode createFromParcel(Parcel in) {
            return new SOAPFaultCode(in);
        }

        @Override
        public SOAPFaultCode[] newArray(int size) {
            return new SOAPFaultCode[size];
        }
    };

    /**
     * Create a SOAP fault code from a Java Exception
     * @param error Java Exception
     * @return SOAP fault code representation of exception
     */
    public static SOAPFaultCode fromException(Exception error) {
        if (error == null) return null;
        String parts[] = error.getClass().getCanonicalName().split("\\.");
        SOAPFaultCode faultCode = null;
        for(int index = parts.length - 1; parts.length >= 0; index--) {
            faultCode = new SOAPFaultCode(parts[index], faultCode);
        }
        return faultCode;
    }

    /**
     * Builder for constructing a SOAP fault code
     */
    public static final class Builder {

        /** Fault code value */
        private String mValue = null;
        /** Fault sub-code */
        private SOAPFaultCode mSubcode = null;

        /**
         * Subcode value stack
         */
        private Stack<String> mValues = new Stack<String>();

        /**
         * Constructor
         */
        public Builder() {
        }

        /**
         * Start adding a fault sub-code
         * @return This builder
         */
        public Builder startSubcode() {
            mValues.push(mValue);
            mValue = null;
            mSubcode = null;
            return  this;
        }

        /**
         * Finish adding a fault sub-code
         * @return This builder
         * @throws IllegalArgumentException if no fault code value has been set
         * @throws IllegalStateException if not adding a sub-code
         */
        public Builder endSubcode() throws IllegalArgumentException, IllegalStateException {
            checkValue();
            if (mValues.isEmpty()) throw new IllegalStateException("no subcode in progress");
            mSubcode = new SOAPFaultCode(mValue, mSubcode);
            mValue = mValues.pop();
            return this;
        }

        /**
         * Set the fault code value
         * @param value Fault code
         * @return This builder
         */
        public Builder setValue(String value) {
            mValue = value;
            return this;
        }

        /**
         * Build the {@link SOAPFaultCode} object
         * @return {@link SOAPFaultCode} instance
         * @throws IllegalArgumentException if fault code value has not been set
         * @throws IllegalStateException if still in the process of adding a sub-code
         */
        public SOAPFaultCode build() throws IllegalArgumentException, IllegalStateException {
            if (!mValues.isEmpty()) throw new IllegalStateException("subcode not ended");
            checkValue();
            return new SOAPFaultCode(mValue, mSubcode);
        }

        /**
         * Checks to see if fault code value is valid
         * @throws IllegalArgumentException if fault code value has not been set
         */
        private void checkValue() throws IllegalArgumentException {
            if (TextUtils.isEmpty(mValue)) throw new IllegalArgumentException("value not set");
        }
    }
}
