// Copyright 2025 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.workpath.api.authorization;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

/**
 * The SignInMethod class represents a sign-in method used for authentication.
 * This class includes the sign-in method ID (UUID), name, and localized name.
 *
 * @since API 9
 */
public class SignInMethod implements Parcelable {
    protected String id;
    protected String name;
    protected LocalizedString localizedName;

    public SignInMethod() {
        super();
    }

    /**
     * @hide The client should not need to know about the parcelable methods
     */
    @SuppressLint("RestrictedApi")
    private SignInMethod(final Parcel in) {
        id = in.readString();
        name = in.readString();
        localizedName = in.readParcelable(LocalizedString.class.getClassLoader());
    }

    /**
     * @hide The client should not need to know about the parcelable methods
     */
    public static final Parcelable.Creator<SignInMethod> CREATOR = new Parcelable.Creator<SignInMethod>() {

        @Override
        public SignInMethod createFromParcel(final Parcel in) {
            return new SignInMethod(in);
        }

        @Override
        public SignInMethod[] newArray(final int size) {
            return new SignInMethod[size];
        }
    };

    /**
     * @hide The client should not need to know about the parcelable methods
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * @hide The client should not need to know about the parcelable methods
     */
    @Override
    public void writeToParcel(final Parcel parcel, final int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeParcelable(localizedName, i);
    }

    /**
     * Returns the ID of the sign-in method.
     *
     * @return The sign-in method ID.
     * @since API 9
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the name of the sign-in method.
     *
     * @return The sign-in method name.
     * @since API 9
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the localized name of the sign-in method.
     *
     * @return The localized name.
     * @since API 9
     */
    public LocalizedString getLocalizedName() {
        return localizedName;
    }

    /**
     * @hide
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @hide
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @hide
     */
    public void setLocalizedName(LocalizedString localizedName) {
        this.localizedName = localizedName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SignInMethod)) return false;
        SignInMethod that = (SignInMethod) o;
        return Objects.equals(getId(), that.getId()) &&
                Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }

    @Override
    public String toString() {
        return "SignInMethod{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", localizedName=" + localizedName +
                '}';
    }
}
