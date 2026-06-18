// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Container for SOAP faults
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class SOAPFault implements Parcelable {
    /** Fault code reported */
    public final SOAPFaultCode mFaultCode;
    /** List for fault reasons reported */
    public final List<String> mReasons;

    /**
     * Constructor
     * @param faultCode Reported fault code
     * @param reasons Reported fault reasons
     */
    private SOAPFault(SOAPFaultCode faultCode, List<String> reasons) {
        mFaultCode = faultCode;
        mReasons = Collections.unmodifiableList(reasons);
    }

    /**
     * Constructor
     * @param in Parcel from which to reconstruct object
     */
    private SOAPFault(Parcel in) {
        mFaultCode = in.readParcelable(SOAPFaultCode.class.getClassLoader());
        mReasons = Collections.unmodifiableList(in.createStringArrayList());
    }

    /**
     * Create a SOAP fault from a Java Exception
     * @param error Java Exception
     * @return SOAP error representation of Exception
     */
    public static SOAPFault fromException(Exception error) {
        if (error == null) return null;
        List<String> reasons = new ArrayList<String>();
        reasons.add(error.getMessage());
        return new SOAPFault(SOAPFaultCode.fromException(error), reasons);
    }

    /**
     * Returns a hash code value for the object
     * @return Integer hash value for this object.
     */
    @Override
    public int hashCode() {
        int result = 1;
        final int prime = 31;
        result = result * prime + mFaultCode.hashCode();
        result = result * prime + mReasons.hashCode();
        return result;
    }

    /**
     * Indicates whether some other object is "equal" to this one.
     * @return true if equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SOAPFault)) return false;
        SOAPFault other = (SOAPFault)obj;
        return (mFaultCode.equals(other.mFaultCode) && mReasons.equals(other.mReasons));
    }

    /**
     * Returns a string representation of this object.
     * @return a string representation of this object.
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(mFaultCode.toString());
        builder.append(":\"");
        for(int index = 0; index < mReasons.size();) {
            builder.append(mReasons.get(index));
            if (++index < mReasons.size()) builder.append('\n');
        }
        builder.append("\"");
        return builder.toString();
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
        dest.writeParcelable(mFaultCode, flags);
        dest.writeStringList(mReasons);
    }

    /**
     * {@link SOAPFault} parcelable creator
     */
    public static final Creator<SOAPFault> CREATOR = new Creator<SOAPFault>() {

        /**
         * Create a new instance of the Parcelable class, instantiating it from the given Parcel whose data had previously been written by {@link SOAPFault#writeToParcel(Parcel, int)}
         * @param in
         *              The Parcel to read the object's data from.
         * @return
         *              Returns a new instance of the Parcelable class.
         */
        @Override
        public SOAPFault createFromParcel(Parcel in) {
            return new SOAPFault(in);
        }

        /**
         * Create a new array of the Parcelable class.
         * @param size
         *              Size of the array
         * @return
         *              Returns an array of the Parcelable class, with every entry initialized to null.
         */
        @Override
        public SOAPFault[] newArray(int size) {
            return new SOAPFault[size];
        }
    };

    /**
     * Builder for constructing a SOAP fault
     */
    public static final class Builder {

        /** Fault code */
        private SOAPFaultCode mFaultCode = null;
        /** Fault reasons */
        private List<String> mReasons = new ArrayList<String>();

        /**
         * Constructor
         */
        public Builder() {
        }

        /**
         * Add a fault reasons
         * @param reason Fault reasons
         * @return This builder
         */
        public Builder addReason(String reason) {
            mReasons.add(reason);
            return this;
        }

        /**
         * Set the fault code
         * @param code Fault code
         * @return This builder
         */
        public Builder setFaultCode(SOAPFaultCode code) {
            mFaultCode = code;
            return this;
        }

        /**
         * Create the {@link SOAPFault} object
         * @return SOAP fault instance
         * @throws IllegalArgumentException if no code or reasons have been provided
         */
        public SOAPFault build() throws IllegalArgumentException {
            if (mFaultCode == null) throw new IllegalArgumentException("no code provided");
            if (mReasons.isEmpty()) throw new IllegalArgumentException("no reasons provided");
            return new SOAPFault(mFaultCode, mReasons);
        }
    }
}
