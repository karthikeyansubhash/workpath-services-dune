package com.hp.jetadvantage.link.services.accessorylet.model;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;
import com.hp.jetadvantage.link.api.accessory.hid.HIDAccessoryInfo;

/**
 * AccessoryRegistrationInfo class
 * This class is used to represent the accessory registration information in the registration records of the Workpath
 * package manager.
 * The registration records are stored in the "Param1" column of the PacMan's providers DB table.
 * <p>
 * Ex) {"productId":"272","registrationType":"OWNED","serialNumber":"1","vendorId":"7590"}
 */
@Keep
public class AccessoryRegistrationInfo {
    @SerializedName("productId")
    String productId;

    @SerializedName("registrationType")
    String registrationType;

    @SerializedName("serialNumber")
    String serialNumber;

    @SerializedName("vendorId")
    String vendorId;

    public int getProductId() {
        return Integer.parseInt(productId);
    }

    public int getVendorId() {
        return Integer.parseInt(vendorId);
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public RegistrationType getRegistrationType() {
        return RegistrationType.valueOf(registrationType);
    }

    public boolean isOwned() {
        return RegistrationType.valueOf(registrationType) == RegistrationType.OWNED;
    }

    public boolean isShared() {
        return RegistrationType.valueOf(registrationType) == RegistrationType.SHARED;
    }

    public boolean equals(HIDAccessoryInfo hidAccessoryInfo) {
        boolean result = false;
        if (hidAccessoryInfo == null) {
            return false;
        }

        String currentSerial = this.getSerialNumber();
        if ("NULL".equalsIgnoreCase(currentSerial) || TextUtils.isEmpty(currentSerial)) {
            currentSerial = null;
        }

        if (this.getProductId() == hidAccessoryInfo.getProductId()
                && this.getVendorId() == hidAccessoryInfo.getVendorId()
                && (currentSerial == null ? true : currentSerial.equalsIgnoreCase(hidAccessoryInfo.getSerialNumber()))) {
            result = true;
        } else {
            result = false;
        }
        Log.d("AccessoryRegistrationInfo", String.format("equals: PID[%s:%s],VID[%s:%s],Serial[%s:%s],Type[%s:%s]",
                this.getProductId(), hidAccessoryInfo.getProductId(),
                this.getVendorId(), hidAccessoryInfo.getVendorId(),
                currentSerial, hidAccessoryInfo.getSerialNumber(),
                this.getRegistrationType(), hidAccessoryInfo.getRegistrationType()));
        Log.d("AccessoryRegistrationInfo", "equals: EXIT " + result);
        return result;
    }

    public enum RegistrationType {
        OWNED,
        SHARED
    }
}
