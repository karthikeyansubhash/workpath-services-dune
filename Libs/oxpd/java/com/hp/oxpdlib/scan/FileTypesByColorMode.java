// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.scan;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Container for file types supported by a particular color mode
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class FileTypesByColorMode implements Parcelable {
    /** A supported color mode */
    public final ColorMode colorMode;
    /** List of file types supported in this color mode . */
    public final List<FileType> fileTypes;

    /**
     * Constructor used by the library to construct FileTypesByColorMode objects
     * @param colorMode
     *              Supported color mode
     * @param fileTypes
     *              The file types supported with this color mode
     */
    FileTypesByColorMode(ColorMode colorMode, List<FileType> fileTypes) {
        this.colorMode = colorMode;
        if (fileTypes == null) {
            fileTypes = Collections.emptyList();
        }
        this.fileTypes = Collections.unmodifiableList(fileTypes);
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
        dest.writeInt(this.colorMode.ordinal());
        dest.writeInt(this.fileTypes.size());
        for(Enum enumEntry : this.fileTypes) {
            dest.writeInt(enumEntry.ordinal());
        }
    }

    /**
     * Constructor when rebuilding from {@link Parcel}
     * @param in Parcel from which the object should be reconstructed
     */
    private FileTypesByColorMode(Parcel in) {
        this.colorMode = ColorMode.values()[in.readInt()];
        List<FileType> fileTypes = new ArrayList<FileType>();
        for(int length = in.readInt(); length > 0; length--) {
            fileTypes.add(FileType.values()[in.readInt()]);
        }
        this.fileTypes = Collections.unmodifiableList(fileTypes);
    }

    /**
     * FileTypesByColorMode creator
     */
    public static final Parcelable.Creator<FileTypesByColorMode> CREATOR =
            new Parcelable.Creator<FileTypesByColorMode>() {

                /**
                 * Create a new instance of the Parcelable class, instantiating it from the given Parcel whose data had previously been written by {@link FileTypesByColorMode#writeToParcel(Parcel, int)}
                 * @param in
                 *              The Parcel to read the object's data from.
                 * @return
                 *              Returns a new instance of the Parcelable class.
                 */
                @Override
                public FileTypesByColorMode createFromParcel(Parcel in) {
                    return new FileTypesByColorMode(in);
                }

                /**
                 * Create a new array of the Parcelable class.
                 * @param size
                 *              Size of the array
                 * @return
                 *              Returns an array of the Parcelable class, with every entry initialized to null.
                 */
                @Override
                public FileTypesByColorMode[] newArray(int size) {
                    return new FileTypesByColorMode[size];
                }
            };
}
