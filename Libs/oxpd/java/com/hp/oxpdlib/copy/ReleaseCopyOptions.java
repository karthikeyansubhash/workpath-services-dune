package com.hp.oxpdlib.copy;

import android.os.Parcel;
import android.os.Parcelable;

public class ReleaseCopyOptions implements Parcelable {
    public long copies;
    public String storeJobPassword;

    /**
     * Default empty constructor
     */
    public ReleaseCopyOptions() {
    }

    /**
     * Create a copy of the provided copy options
     * @param defaultCopyOptions
     *              CopyOptions to copy from
     */
    public ReleaseCopyOptions(ReleaseCopyOptions defaultCopyOptions) {
        copies = defaultCopyOptions.copies;
        storeJobPassword = defaultCopyOptions.storeJobPassword;
    }

    ReleaseCopyOptions(Parcel in) {
        copies = in.readLong();
        storeJobPassword = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(copies);
        dest.writeString(this.storeJobPassword);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ReleaseCopyOptions> CREATOR = new Creator<ReleaseCopyOptions>() {
        @Override
        public ReleaseCopyOptions createFromParcel(Parcel in) {
            return new ReleaseCopyOptions(in);
        }

        @Override
        public ReleaseCopyOptions[] newArray(int size) {
            return new ReleaseCopyOptions[size];
        }
    };
}
