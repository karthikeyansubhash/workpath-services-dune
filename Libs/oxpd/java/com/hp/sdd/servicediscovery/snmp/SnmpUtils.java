// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.sdd.servicediscovery.snmp;

import android.os.Bundle;
import android.text.TextUtils;

import com.hp.sdd.servicediscovery.NetworkDevice;

@SuppressWarnings("unused")
public class SnmpUtils {
    public static final String ATTRIBUTE__MODEL = "MDL";
    public static final String ATTRIBUTE__PDL = "CMD";
    public static final String ATTRIBUTE__MFG = "MFG";
    public static final String SNMP_SERVICE_NAME = "SNMP-UDP";

    public static boolean isVendorPrinter(NetworkDevice networkDevice, String[] vendorValues) {
        Bundle attrs = networkDevice.getTxtAttributes();
        String mfg = attrs.getString(ATTRIBUTE__MFG);
        return (!TextUtils.isEmpty(mfg) && containsVendor(mfg, vendorValues));
    }

    public static String getVendor(NetworkDevice networkDevice) {
        Bundle attrs = networkDevice.getTxtAttributes(SNMP_SERVICE_NAME);
        return ((attrs != null) ? attrs.getString(ATTRIBUTE__MFG) : null);
    }

    private static boolean containsVendor(String container, String[] vendorValues) {
        for (String value : vendorValues) {
            boolean result = containsString(container, value);
            if (result) return true;
        }
        return false;
    }

    private static boolean containsString(String container, String contained) {
        return container != null && (container.equals(contained) || container.contains(contained + " "));
    }
}
