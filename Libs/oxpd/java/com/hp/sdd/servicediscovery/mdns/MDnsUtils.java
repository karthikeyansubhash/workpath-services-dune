// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.sdd.servicediscovery.mdns;

import android.os.Bundle;
import android.text.TextUtils;

import com.hp.sdd.servicediscovery.NetworkDevice;

import java.util.List;
import java.util.Locale;

@SuppressWarnings("unused")
public class MDnsUtils {
    public static final String ATTRIBUTE__TY = "ty";
    public static final String ATTRIBUTE__PRODUCT = "product";
    public static final String ATTRIBUTE__USB_MFG = "usb_MFG";
    public static final String ATTRIBUTE__MFG = "mfg";
    public static final String ATTRIBUTE__USB_MDL = "usb_MDL";
    public static final String ATTRIBUTE__MDL = "mdl";
    public static final String ATTRIBUTE__PDL = "pdl";
    public static final String ATTRIBUTE__NOTE = "note";
    public static final String ATTRIBUTE__PRIORITY = "priority";
    public static final String ATTRIBUTE__RESOURCE_PATH = "rp";
    public static final String ATTRIBUTE__AIR = "air";
    public static final String ATTRIBUTE__AIR_NONE = "none";
    public static final String ATTRIBUTE__AIR_USER_PASS = "username,password";
    public static final String ATTRIBUTE__AIR_CERT = "certificate";
    public static final String ATTRIBUTE__AIR_NEGOTIATE = "negotiate";
    public static final String ATTRIBUTE__SCAN = "Scan";
    public static final String ATTRIBUTE__COLOR = "Color";
    public static final String ATTRIBUTE__DUPLEX = "Duplex";
    public static final String ATTRIBUTE__ICON_URLS = "printer-icons-url";
    public static final String ATTRIBUTE__MAC_ADDRESS = "mac";
    public static final String ATTRIBUTE__UUID = "UUID";

    public static final String ATTRIBUTE_VALUE__TRUE = "T";
    public static final String ATTRIBUTE_VALUE__FALSE = "F";
    public static final String ATTRIBUTE__DEV_ID = "SNMP-DEVID";
    public static final String ATTRIBUTE_VALUE__PRIORITY_DEFAULT = "666";
    public static final String ATTRIBUTE__DIRECTED_DISCOVERED = "directed-discovered-printer";

    public static boolean isVendorPrinter(NetworkDevice networkDevice, String[] vendorValues) {
        boolean isVendorPrinter = false;

        List<NetworkDevice> allInstances = networkDevice.getAllDiscoveryInstances();

        for (NetworkDevice instance : allInstances) {
            Bundle attributes = instance.getTxtAttributes();
            String product = attributes.getString(ATTRIBUTE__PRODUCT);
            String ty = attributes.getString(ATTRIBUTE__TY);
            String usbMfg = attributes.getString(ATTRIBUTE__USB_MFG);
            String mfg = attributes.getString(ATTRIBUTE__MFG);
            isVendorPrinter = containsVendor(product, vendorValues) || containsVendor(ty, vendorValues) || containsVendor(usbMfg, vendorValues) || containsVendor(mfg, vendorValues);
            if (isVendorPrinter) {
                break;
            }
        }
        return isVendorPrinter;
    }

    public static String getVendor(NetworkDevice networkDevice) {
        String vendor = null;
        List<NetworkDevice> allInstances = networkDevice.getAllDiscoveryInstances();

        for (NetworkDevice instance : allInstances) {
            Bundle attributes = instance.getTxtAttributes();
            vendor = attributes.getString(ATTRIBUTE__MFG);
            if (!TextUtils.isEmpty(vendor)) break;
            vendor = attributes.getString(ATTRIBUTE__USB_MFG);
            if (!TextUtils.isEmpty(vendor)) break;
            vendor = null;
        }
        return vendor;
    }

    private static boolean containsVendor(String container, String[] vendorValues) {
        if ((container == null) || (vendorValues == null)) return false;
        for (String value : vendorValues) {
            if (containsString(container, value)
                || containsString(container.toLowerCase(Locale.US), value.toLowerCase(Locale.US))
                || containsString(container.toUpperCase(Locale.US), value.toUpperCase(Locale.US)))
                return true;
        }
        return false;
    }

    private static boolean containsString(String container, String contained) {
        return (container != null) && (contained != null) && (container.equalsIgnoreCase(contained) || container.contains(contained + " "));
    }
}
