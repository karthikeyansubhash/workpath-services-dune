// Copyright 2025 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.workpath.api.authorization;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Objects;

/**
 * The Permission class represents an object used to grant users permission
 * This class includes the permission ID (UUID), name, and localized name.
 *
 * @since API 9
 */
public class Permission implements Parcelable {

    protected String id;
    protected String name;
    protected LocalizedString localizedName;

    public Permission() {
    }

    /**
     * @hide The client should not need to know about the parcelable methods
     */
    @SuppressLint("RestrictedApi")
    private Permission(final Parcel in) {
        id = in.readString();
        name = in.readString();
        localizedName = in.readParcelable(LocalizedString.class.getClassLoader());

    }

    /**
     * @hide The client should not need to know about the parcelable methods
     */
    public static final Parcelable.Creator<Permission> CREATOR = new Parcelable.Creator<Permission>() {

        @Override
        public Permission createFromParcel(final Parcel in) {
            return new Permission(in);
        }

        @Override
        public Permission[] newArray(final int size) {
            return new Permission[size];
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
     * Returns the ID of the permission.
     *
     * @return The permission ID.
     * @since API 9
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the name of the permission.
     *
     * @return The permission name.
     * @since API 9
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the localized name of the permission.
     *
     * @return The localized name.
     * @since API 9
     */
    public LocalizedString getLocalizedName() {
        return localizedName;
    }

    /**
     * @hide for internal use
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @hide for internal use
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @hide for internal use
     */
    public void setLocalizedName(LocalizedString localizedName) {
        this.localizedName = localizedName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Permission)) return false;
        Permission that = (Permission) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }

    @Override
    public String toString() {
        return "Permission{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", localizedName=" + localizedName +
                '}';
    }
}