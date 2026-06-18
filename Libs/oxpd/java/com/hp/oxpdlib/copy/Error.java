// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.copy;

import android.os.Parcel;
import android.os.Parcelable;

import com.hp.oxpdlib.SOAPFault;

/** OXPd Scan exceptions */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Error extends Exception implements Parcelable {
    /** Error name */
    public final ErrorName name;
    /** A string describing the reason for the error */
    public final String message;
    /** SOAP fault that triggered the error */
    public final SOAPFault soapFault;

    /** Constructor used by the library to construct an Error object.
     * @param name
     *              Error name
     * @param message
     *              A string describing the reason for the error
     */
    public Error(ErrorName name, String message) {
        this(name, message, null);
    }

    /** Constructor used by the library to construct an Error object from a SOAP fault
     * @param name
     *              Error name
     * @param soapFault
     *              SOAP fault triggering exception
     */
    Error(ErrorName name, SOAPFault soapFault) {
        this(name, soapFault.toString(), soapFault);
    }

    /** Constructor used by the library to construct an Error object from a SOAP fault
     * @param name
     *              Error name
     * @param message
     *              A string describing the reason for the error
     * @param soapFault
     *              SOAP fault triggering exception
     */
    private Error(ErrorName name, String message, SOAPFault soapFault) {
        super(message);
        this.name = name;
        this.message = message;
        this.soapFault = soapFault;
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
        dest.writeString(this.message);
        dest.writeInt(this.name.ordinal());
        dest.writeParcelable(soapFault, flags);
    }

    /**
     * Constructor when rebuilding from {@link Parcel}
     * @param in Parcel from which the object should be reconstructed
     */
    private Error(Parcel in) {
        super(in.readString());
        this.name = ErrorName.values()[in.readInt()];
        this.message = getMessage();
        this.soapFault = in.readParcelable(SOAPFault.class.getClassLoader());
    }

    /**
     * Error creator
     */
    public static final Parcelable.Creator<Error> CREATOR =
            new Parcelable.Creator<Error>() {

                /**
                 * Create a new instance of the Parcelable class, instantiating it from the given Parcel whose data had previously been written by {@link Error#writeToParcel(Parcel, int)}
                 * @param in
                 *              The Parcel to read the object's data from.
                 * @return
                 *              Returns a new instance of the Parcelable class.
                 */
                @Override
                public Error createFromParcel(Parcel in) {
                    return new Error(in);
                }

                /**
                 * Create a new array of the Parcelable class.
                 * @param size
                 *              Size of the array
                 * @return
                 *              Returns an array of the Parcelable class, with every entry initialized to null.
                 */
                @Override
                public Error[] newArray(int size) {
                    return new Error[size];
                }
            };
}
