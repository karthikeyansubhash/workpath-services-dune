// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.scan;

import android.os.Parcel;
import android.os.Parcelable;

/** A ScanTicket defines all of the settings to be used to start a scan job. */
@SuppressWarnings({"unused", "WeakerAccess"})
public class ScanTicket implements Parcelable {

    /**
     * Defines the values to be used for 'basic' options appropriate to the specified TransmissionMode.
     */
    public final BasicOptions basicOptions;
    /**
     * Defines the values to be used for the settings that apply to the fileType and colorMode specified in the basicOptions.
     */
    public final FileOptions fileOptions;
    /**
     * Defines how and where the scan and optional metadata will be sent.
     */
    public final Destination destination;
    /**
     * Defines the metadata to be sent along with the scan data (use null to indicate that no metadata should be sent).
     */
    public final Metadata metadata;
    /**
     * Base name for a scan file(s) (min length=1, max length=128).
     */
    public final String scanFileNameBase;
    /**
     * TransmissionMode to be used for this scan job.
     */
    public final TransmissionMode transmissionMode;

    /**
     * Constructor used to construct ScanTicket objects.
     * @param basicOptions
     *              Defines the values to be used for 'basic' options appropriate to the specified TransmissionMode.
     * @param fileOptions
     *              Defines the values to be used for the settings that apply to the fileType and colorMode specified in the basicOptions.
     * @param destination
     *              Defines how and where the scan and optional metadata will be sent.
     * @param metadata
     *              (Optional) Defines the metadata to be sent along with the scan data (use null to indicate that no metadata should be sent).
     * @param scanFileNameBase
     *              Base name for a scan file(s) (min length=1, max length=128).
     * @param transmissionMode
     *              TransmissionMode to be used for this scan job.
     */
    public ScanTicket(BasicOptions basicOptions,
                      FileOptions fileOptions,
                      Destination destination,
                      Metadata metadata,
                      String scanFileNameBase,
                      TransmissionMode transmissionMode)
    {
        this.basicOptions = basicOptions;
        this.fileOptions = fileOptions;
        this.destination = destination;
        this.metadata = metadata;
        this.scanFileNameBase = scanFileNameBase;
        this.transmissionMode = transmissionMode;
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
        dest.writeParcelable(this.basicOptions, flags);
        dest.writeParcelable(this.fileOptions, flags);
        dest.writeParcelable(this.destination, flags);
        dest.writeParcelable(this.metadata, flags);
        dest.writeString(this.scanFileNameBase);
        dest.writeInt(this.transmissionMode.ordinal());
    }

    /**
     * Constructor when rebuilding from {@link Parcel}
     * @param in Parcel from which the object should be reconstructed
     */
    private ScanTicket(Parcel in) {
        this.basicOptions = in.readParcelable(BasicOptions.class.getClassLoader());
        this.fileOptions = in.readParcelable(FileOptions.class.getClassLoader());
        this.destination = in.readParcelable(Destination.class.getClassLoader());
        this.metadata = in.readParcelable(Metadata.class.getClassLoader());
        this.scanFileNameBase = in.readString();
        this.transmissionMode = TransmissionMode.values()[in.readInt()];
    }

    /**
     * ScanTicket creator
     */
    public static final Parcelable.Creator<ScanTicket> CREATOR =
            new Parcelable.Creator<ScanTicket>() {

                /**
                 * Create a new instance of the Parcelable class, instantiating it from the given Parcel whose data had previously been written by {@link ScanTicket#writeToParcel(Parcel, int)}
                 * @param in
                 *              The Parcel to read the object's data from.
                 * @return
                 *              Returns a new instance of the Parcelable class.
                 */
                @Override
                public ScanTicket createFromParcel(Parcel in) {
                    return new ScanTicket(in);
                }

                /**
                 * Create a new array of the Parcelable class.
                 * @param size
                 *              Size of the array
                 * @return
                 *              Returns an array of the Parcelable class, with every entry initialized to null.
                 */
                @Override
                public ScanTicket[] newArray(int size) {
                    return new ScanTicket[size];
                }
            };
}
