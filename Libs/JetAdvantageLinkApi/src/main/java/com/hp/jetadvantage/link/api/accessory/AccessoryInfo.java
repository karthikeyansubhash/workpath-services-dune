// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.accessory;

import android.os.Parcelable;

/**
 * Container for accessory details.
 *
 * @since API 3
 */
public abstract class AccessoryInfo implements Parcelable {
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
}
