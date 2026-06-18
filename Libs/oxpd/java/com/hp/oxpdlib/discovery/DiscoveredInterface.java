// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.discovery;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Represents a discovered interface.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class DiscoveredInterface implements Parcelable {
    /**
     * The URI of the interface's manifest XML file on this device.
     */
    public final String manifestUri;
    /**
     * The interface's 'well known' name.
     */
    public final String resourceType;
    /**
     * The interface's major revision (compatibility version).
     */
    public final String revision;
    /**
     * The interface's minor revision (functionality version).
     */
    public final String minorRevision;
    
    /**
     * Discovered interface constructor
     * @param manifestUri
     *              The URI of the interface on this device.
     * @param resourceType
     *              The interface's 'well known' name.
     * @param revision
     *              The interface's major revision (compatibility version).
     * @param minorRevision
     *              The interface's minor revision (functionality version).
     */
    DiscoveredInterface(String manifestUri, String resourceType, String revision, String minorRevision)
    {
        this.manifestUri = manifestUri;
        this.resourceType = resourceType;
        this.revision = revision;
        this.minorRevision = minorRevision;
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
        dest.writeString(this.manifestUri);
        dest.writeString(this.resourceType);
        dest.writeString(this.revision);
        dest.writeString(this.minorRevision);
    }

    /**
     * Constructor when rebuilding from {@link Parcel}
     * @param in Parcel from which the object should be reconstructed
     */
    private DiscoveredInterface(Parcel in) {
        this.manifestUri = in.readString();
        this.resourceType = in.readString();
        this.revision = in.readString();
        this.minorRevision = in.readString();
    }

    /**
     * DiscoveredInterface creator
     */
    public static final Parcelable.Creator<DiscoveredInterface> CREATOR =
            new Parcelable.Creator<DiscoveredInterface>() {

                /**
                 * Create a new instance of the Parcelable class, instantiating it from the given Parcel whose data had previously been written by {@link DiscoveredInterface#writeToParcel(Parcel, int)}
                 * @param in
                 *              The Parcel to read the object's data from.
                 * @return
                 *              Returns a new instance of the Parcelable class.
                 */
                @Override
                public DiscoveredInterface createFromParcel(Parcel in) {
                    return new DiscoveredInterface(in);
                }

                /**
                 * Create a new array of the Parcelable class.
                 * @param size
                 *              Size of the array
                 * @return
                 *              Returns an array of the Parcelable class, with every entry initialized to null.
                 */
                @Override
                public DiscoveredInterface[] newArray(int size) {
                    return new DiscoveredInterface[size];
                }
            };
}
