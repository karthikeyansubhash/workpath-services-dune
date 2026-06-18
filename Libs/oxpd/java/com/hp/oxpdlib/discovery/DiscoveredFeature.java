// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.discovery;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Represents a discovered feature.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class DiscoveredFeature implements Parcelable {
    /**
     * The feature's URI on this device.
     * (e.g. SOAP endpoint). May be null.
     */
    public final String resourceURI;
    /**
     * The feature's 'well known' name.
     */
    public final String resourceType;
    /**
     * The feature's major revision (compatibility version).
     */
    public final String revision;
    /**
     * The feature's minor revision (functionality version).
     */
    public final String minorRevision;

    /**
     * Discovered feature constructor
     * @param resourceURI
     *              The URI of the feature on this device.
     * @param resourceType
     *              The feature's 'well known' name.
     * @param revision
     *              The feature's major revision (compatibility version).
     * @param minorRevision
     *              The feature's minor revision (functionality version).
     */
    DiscoveredFeature(String resourceURI, String resourceType, String revision, String minorRevision)
    {
        this.resourceURI = resourceURI;
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
        dest.writeString(this.resourceURI);
        dest.writeString(this.resourceType);
        dest.writeString(this.revision);
        dest.writeString(this.minorRevision);
    }

    /**
     * Constructor when rebuilding from {@link Parcel}
     * @param in Parcel from which the object should be reconstructed
     */
    private DiscoveredFeature(Parcel in) {
        this.resourceURI = in.readString();
        this.resourceType = in.readString();
        this.revision = in.readString();
        this.minorRevision = in.readString();
    }

    /**
     * DiscoveredFeature creator
     */
    public static final Parcelable.Creator<DiscoveredFeature> CREATOR =
            new Parcelable.Creator<DiscoveredFeature>() {

                /**
                 * Create a new instance of the Parcelable class, instantiating it from the given Parcel whose data had previously been written by {@link DiscoveredFeature#writeToParcel(Parcel, int)}
                 * @param in
                 *              The Parcel to read the object's data from.
                 * @return
                 *              Returns a new instance of the Parcelable class.
                 */
                @Override
                public DiscoveredFeature createFromParcel(Parcel in) {
                    return new DiscoveredFeature(in);
                }

                /**
                 * Create a new array of the Parcelable class.
                 * @param size
                 *              Size of the array
                 * @return
                 *              Returns an array of the Parcelable class, with every entry initialized to null.
                 */
                @Override
                public DiscoveredFeature[] newArray(int size) {
                    return new DiscoveredFeature[size];
                }
            };
}
