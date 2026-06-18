// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.massstorage;

import androidx.annotation.NonNull;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.lang.ref.WeakReference;

public class CustomerDataFile implements Parcelable {
    /**
     * Version of info. Important to maintain to avoid Parcel breakage
     */
    private int mVersion;
    private WeakReference<Context> mContext;
    private MassStorageInfo mStorageInfo;
    private String mPath;
    private String mPackageName;

    public CustomerDataFile(@NonNull final Context context, MassStorageInfo storageInfo, String path) {
        this.mContext = new WeakReference<>(context);
        this.mStorageInfo = storageInfo;
        this.mPath = path;
        this.mPackageName = context.getPackageName();
    }

    protected CustomerDataFile(Parcel in) {
        mPath = in.readString();
        mStorageInfo = (MassStorageInfo) in.readParcelable(this.getClass().getClassLoader());
        mPackageName = in.readString();
    }

    protected void setContext(@NonNull final Context context) {
        this.mContext = new WeakReference<>(context);
        this.mPackageName = context.getPackageName();
    }

    public static final Creator<CustomerDataFile> CREATOR = new Creator<CustomerDataFile>() {
        @Override
        public CustomerDataFile createFromParcel(Parcel in) {
            return new CustomerDataFile(in);
        }

        @Override
        public CustomerDataFile[] newArray(int size) {
            return new CustomerDataFile[size];
        }
    };

    public MassStorageInfo getStorageInfo() {
        return mStorageInfo;
    }

    public String getPath() {
        return mPath;
    }

    public String getPackageName() {
        return mPackageName;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mPath);
        dest.writeParcelable(mStorageInfo, flags);
        dest.writeString(mPackageName);
    }
}
