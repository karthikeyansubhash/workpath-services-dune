// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.printlet.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.hp.jetadvantage.link.services.printlet.model.ipp.IppConstants;
import com.hp.jetadvantage.link.services.printlet.model.ipp.IppIntegerAttribute;
import com.hp.jetadvantage.link.services.printlet.model.ipp.IppResponse;
import com.hp.jetadvantage.link.services.printlet.model.ipp.IppStringAttribute;

import com.hp.jetadvantage.link.services.printlet.model.ipp.IppAttribute;

/**
 * OXPd Job ID container
 */
@SuppressWarnings("WeakerAccess")
public class PrintJobID implements Parcelable {

    /** IPP job ID */
    private final int mJobID;

    /** Print Job UUID */
    private final String mJobUUID;

    /**
     * Return the IPP job id
     * @return
     *              job id assigned by IPP
     */
    public int getJobID() {
        return mJobID;
    }

    /**
     * Return the IPP job uuid
     * @return
     *              job uuid assigned by IPP
     */
    public String getJobUUID() {
        return mJobUUID;
    }

    /**
     * Print Job ID constructor
     * @param response
     *              Ipp response that contains job ID
     */
    public PrintJobID(IppResponse response) {
        int id = 0;
        IppAttribute attribute = response.findAttribute(IppConstants.IppTag.IPP_TAG_JOB, IppConstants.IppTag.IPP_TAG_INTEGER, IppConstants.IPP_ATTRIBUTE_NAME__JOB_ID);
        if (attribute instanceof IppIntegerAttribute) {
            id = ((IppIntegerAttribute)attribute).get(0);
        }
        this.mJobID = id;

        String uuid = null;
        attribute = response.findAttribute(IppConstants.IppTag.IPP_TAG_JOB, IppConstants.IppTag.IPP_TAG_URI, IppConstants.IPP_ATTRIBUTE_NAME__JOB_UUID);
        if (attribute != null && attribute instanceof IppStringAttribute) {
            uuid = ((IppStringAttribute) attribute).get(0);
            if (uuid.startsWith("urn:uuid:")) {
                uuid = uuid.substring("urn:uuid:".length());
            }
        }
        this.mJobUUID = uuid;
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
        dest.writeInt(mJobID);
        dest.writeString(mJobUUID);
    }

    /**
     * Constructor when rebuilding from {@link Parcel}
     * @param in Parcel from which the object should be reconstructed
     */
    private PrintJobID(Parcel in) {
        mJobID = in.readInt();
        mJobUUID = in.readString();
    }

    /**
     * PrintJobID creator
     */
    public static final Parcelable.Creator<PrintJobID> CREATOR =
            new Parcelable.Creator<PrintJobID>() {

                /**
                 * Create a new instance of the Parcelable class, instantiating it from the given Parcel whose data had previously been written by {@link PrintJobID#writeToParcel(Parcel, int)}
                 * @param in
                 *              The Parcel to read the object's data from.
                 * @return
                 *              Returns a new instance of the Parcelable class.
                 */
                @Override
                public PrintJobID createFromParcel(Parcel in) {
                    return new PrintJobID(in);
                }

                /**
                 * Create a new array of the Parcelable class.
                 * @param size
                 *              Size of the array
                 * @return
                 *              Returns an array of the Parcelable class, with every entry initialized to null.
                 */
                @Override
                public PrintJobID[] newArray(int size) {
                    return new PrintJobID[size];
                }
            };

}
