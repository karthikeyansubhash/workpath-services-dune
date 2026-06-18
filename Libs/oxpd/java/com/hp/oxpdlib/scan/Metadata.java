// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.scan;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * Defines the name and content of a file to be sent to the scan destination in addition to the scanned images.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Metadata implements Parcelable {

    /**
     *Delimiter to use between file names when expanding the $SCAN_FILELIST$ macro (min length=1, max length=128, may be null).
     */
    public final String fileListDelimiter;
    /**
     *Base name for the metadata file (min length=1, max length=128).
     */
    public final String fileNameBase;
    /**
     *Extension for the metadata file, including the '.' if needed (min length=1, max length=128, may be null).
     */
    public final String fileNameExtension;
    /**
     *Format string to be used for the content of the metadata file (min length=1, max length=10240).
     * <p>The format string is a 'free form' string, that may contain OXPd defined macros. When the
     * scan, processing, and transmission of all of the images are complete, the device will expand
     * the macros in the format string and use the result as the content for the metadata file.
     * Through the format string, the web application has complete control over the content of the
     * metadata file.</p>
     */
    public final String formatString;

    /**
     * Constructor used to construct Metadata objects.
     * @param fileListDelimiter
     *              (Optional) Delimiter to use between file names when expanding the $SCAN_FILELIST$ macro (min length=1, max length=128, may be null).
     * @param fileNameBase
     *              Base name for the metadata file (min length=1, max length=128).
     * @param fileNameExtension
     *              (Optional) Extension for the metadata file, including the '.' if needed (min length=1, max length=128, may be null).
     * @param formatString
     *              Format string to be used for the content of the metadata file (min length=1, max length=10240).
     * @throws IllegalArgumentException If fileNameBase or formatString are empty
     */
    public Metadata(String fileListDelimiter, String fileNameBase, String fileNameExtension, String formatString) throws IllegalArgumentException
    {
        if (TextUtils.isEmpty(fileNameBase)) throw new IllegalArgumentException("fileNameBase is null or empty");
        if (TextUtils.isEmpty(formatString)) throw new IllegalArgumentException("formatString is null or empty");
        if (TextUtils.isEmpty(fileListDelimiter)) fileListDelimiter = null;
        if (TextUtils.isEmpty(fileNameExtension)) fileNameExtension = null;
        this.fileListDelimiter = fileListDelimiter;
        this.fileNameBase = fileNameBase;
        this.fileNameExtension = fileNameExtension;
        this.formatString = formatString;
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
        dest.writeString(this.fileListDelimiter);
        dest.writeString(this.fileNameBase);
        dest.writeString(this.fileNameExtension);
        dest.writeString(this.formatString);
    }

    /**
     * Constructor when rebuilding from {@link Parcel}
     * @param in Parcel from which the object should be reconstructed
     */
    private Metadata(Parcel in) {
        this.fileListDelimiter = in.readString();
        this.fileNameBase = in.readString();
        this.fileNameExtension = in.readString();
        this.formatString = in.readString();
    }

    /**
     * Metadata creator
     */
    public static final Parcelable.Creator<Metadata> CREATOR =
            new Parcelable.Creator<Metadata>() {

                /**
                 * Create a new instance of the Parcelable class, instantiating it from the given Parcel whose data had previously been written by {@link Metadata#writeToParcel(Parcel, int)}
                 * @param in
                 *              The Parcel to read the object's data from.
                 * @return
                 *              Returns a new instance of the Parcelable class.
                 */
                @Override
                public Metadata createFromParcel(Parcel in) {
                    return new Metadata(in);
                }

                /**
                 * Create a new array of the Parcelable class.
                 * @param size
                 *              Size of the array
                 * @return
                 *              Returns an array of the Parcelable class, with every entry initialized to null.
                 */
                @Override
                public Metadata[] newArray(int size) {
                    return new Metadata[size];
                }
            };
}
