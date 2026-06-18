// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.workpath.api.accessory;

import android.os.Parcelable;

import com.hp.workpath.api.Convertor;
import com.hp.workpath.api.accessory.hid.HIDAccessoryInfo;

/**
 * Container for accessory details.
 *
 * @since API 3
 */
public abstract class AccessoryInfo implements Parcelable, Convertor {
    /**
     * A collection of the AccessoryClass.
     *
     * @since API 3
     */
    public enum AccessoryClass {
        /**
         * The Attached USB accessory is not supported. Used in cases where the device does not support the class of accessory reported by the accessory.
         *
         * @since API 3
         */
        UNSUPPORTED,

        /**
         * Generic ccid accessory.
         *
         * @since API 3
         */
        CCID,

        /**
         * Generic HID accessory.
         *
         * @since API 3
         */
        HID,

        /**
         * Generic mass storage (file system) accessory.
         *
         * @since API 3
         */
        MASS_STORAGE
    }

    /**
     * Returns accessory class.
     *
     * @since API 3
     */
    public abstract AccessoryClass getAccessoryClass();

    /**
     * Returns accessory registration type.
     *
     * @since API 3
     */
    public abstract RegistrationType getRegistrationType();

    /**
     * Returns accessory details specific to accessory class.
     *
     * @since API 3
     */
    public <T extends AccessoryInfo> T getDetails() {
        return (T) this;
    }

    public AccessoryInfo() {
    }

    /**
     * @hide trivial
     */
    public static final ConvertorCreator<AccessoryInfo> CREATOR_OBJ = new ConvertorCreator<AccessoryInfo>() {
        @Override
        public AccessoryInfo createFromObject(Object object) {
            if (object instanceof com.hp.jetadvantage.link.api.accessory.hid.HIDAccessoryInfo) {
                return HIDAccessoryInfo.CREATOR_OBJ.createFromObject(object);
            }
            return null;
        }
    };
}
