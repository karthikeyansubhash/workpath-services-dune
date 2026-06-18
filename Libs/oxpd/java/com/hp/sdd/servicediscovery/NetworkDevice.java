// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.sdd.servicediscovery;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import com.hp.sdd.servicediscovery.mdns.BonjourParser;
import com.hp.sdd.servicediscovery.mdns.BonjourServiceParser;
import com.hp.sdd.servicediscovery.mdns.MDnsUtils;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * There is no public constructor. Instances are either returned by the printer
 * discovery provided by the print system, read from parcels or loaded from
 * shared preferences.
 */
@SuppressWarnings("unused")
public final class NetworkDevice {

    public enum DiscoveryMethod {
        MDNS_DISCOVERY,
        SNMP_DISCOVERY,
        DNSSD_DISCOVERY,
        DIRECTED_DISCOVERY,
        OTHER_DISCOVERY,
    }

    private static final String TAG = "NetworkDiscovery";

    public static final String KEY_INET_ADDRESS = "device-address";
    public static final String KEY_MODEL = "model";
    public static final String KEY_HOSTNAME = "hostname";
    public static final String KEY_BONJOUR_NAME = "bonjour-name";
    public static final String KEY_BONJOUR_DOMAIN_NAME = "bonjour-domain-name";
    public static final String KEY_BONJOUR_SERVICE = "bonjourService";
    public static final String KEY_DISCOVERY_METHOD = "discovery-method";
    public static final String KEY_PORT = "port";
    public static final String KEY_DEVICE_IDENTIFIER = "device-identifier";
    public static final String KEY_BONJOUR_DATA = "bonjour-data";
    public static final String KEY_OTHER_INSTANCES = "other-device-instances";
    public static final String KEY_EXTRA_ATTRIBUTES = "extra-attributes";

    /*
     * Keep these declarations ordered alphabetically by field hostname. This helps
     * to keep readFromParcel and writeToParcel up-to-date.
     *
     * The fields have package-level visibility because they must be
     * accessible by the "friend" class EPrintersDatabase.
     */
    private final InetAddress inetAddress;
    private final String model;
    private final String hostname;

    private final String bonjourName;
    private final String bonjourDomainName;
    private final DiscoveryMethod discoveryMethod;
    private final String mBonjourService;
    private final int mPort;
    private final String mDeviceIdentifier;

    private Bundle bonjourData = new Bundle();
    private Bundle extraAttrs = new Bundle();

    private final List<NetworkDevice> mOtherInstances = new ArrayList<NetworkDevice>();

    public NetworkDevice(ServiceParser parser) throws IllegalArgumentException {
        InetAddress inetAddress = parser.getAddress();
        if (inetAddress == null) {
            throw new IllegalArgumentException("inetAddress can not be null");
        }
        this.inetAddress = parser.getAddress();
        this.model = parser.getModel();
        this.hostname = parser.getHostname();
        this.mPort = parser.getPort();

        if (parser instanceof BonjourServiceParser) {
            BonjourServiceParser bonjourParser = (BonjourServiceParser) parser;
            this.bonjourName = bonjourParser.getBonjourName();
            this.bonjourDomainName = bonjourParser.getHostname();
            this.mBonjourService = bonjourParser.getBonjourServiceName();
        } else {
            this.bonjourName = null;
            this.bonjourDomainName = null;
            this.mBonjourService = parser.getServiceName();
        }
        if (TextUtils.isEmpty(bonjourName)) {
            this.mDeviceIdentifier = parser.getDeviceIdentifier();
        } else {
            this.mDeviceIdentifier = bonjourName + "/" + parser.getDeviceIdentifier();
        }

        bonjourData.putAll(parser.getAllAttributes());

        discoveryMethod = parser.getDiscoveryMethod();
    }

    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putByteArray(KEY_INET_ADDRESS, this.inetAddress.getAddress());


        bundle.putString(KEY_MODEL, this.model);
        bundle.putString(KEY_HOSTNAME, this.hostname);
        bundle.putInt(KEY_PORT, this.mPort);
        bundle.putString(KEY_DEVICE_IDENTIFIER, this.mDeviceIdentifier);
        bundle.putString(KEY_BONJOUR_NAME, this.bonjourName);
        bundle.putString(KEY_BONJOUR_DOMAIN_NAME, this.bonjourDomainName);
        bundle.putString(KEY_BONJOUR_SERVICE, this.mBonjourService);
        bundle.putInt(KEY_DISCOVERY_METHOD, this.discoveryMethod.ordinal());
        bundle.putParcelable(KEY_BONJOUR_DATA, this.bonjourData);
        bundle.putParcelable(KEY_EXTRA_ATTRIBUTES, this.extraAttrs);
        ArrayList<Bundle> otherBundles = new ArrayList<Bundle>(this.mOtherInstances.size());
        for(NetworkDevice other : this.mOtherInstances) {
            otherBundles.add(other.toBundle());
        }
        bundle.putParcelableArrayList(KEY_OTHER_INSTANCES, otherBundles);
        return bundle;
    }

    private NetworkDevice(Bundle bundle) throws UnknownHostException {
        byte[] addr = bundle.getByteArray(KEY_INET_ADDRESS);
        this.inetAddress = (addr != null) ? InetAddress.getByAddress(addr) : null;
        this.model = bundle.getString(KEY_MODEL);
        this.mPort = bundle.getInt(KEY_PORT);
        this.hostname = bundle.getString(KEY_HOSTNAME);
        this.mDeviceIdentifier = bundle.getString(KEY_DEVICE_IDENTIFIER);
        this.bonjourName = bundle.getString(KEY_BONJOUR_NAME);
        this.bonjourDomainName = bundle.getString(KEY_BONJOUR_DOMAIN_NAME);
        this.mBonjourService = bundle.getString(KEY_BONJOUR_SERVICE);
        this.discoveryMethod = DiscoveryMethod.values()[bundle.getInt(KEY_DISCOVERY_METHOD)];
        this.bonjourData = bundle.getParcelable(KEY_BONJOUR_DATA);
        this.extraAttrs = bundle.getParcelable(KEY_EXTRA_ATTRIBUTES);
        ArrayList<Bundle> otherInstances = bundle.getParcelableArrayList(KEY_OTHER_INSTANCES);
        if (otherInstances != null) {
            for (Bundle other : otherInstances) {
                mOtherInstances.add(new NetworkDevice(other));
            }
        }
    }

    public static NetworkDevice fromBundle(Bundle bundle) {
        try {
            return (bundle != null) ? new NetworkDevice(bundle) : null;
        } catch (UnknownHostException e) {
            return null;
        }
    }

    public String deviceInfo() {
        return "ip: " + this.inetAddress + " model: " + this.model + " hostname: " + this.hostname
                + " bonjourName: " + this.bonjourName + " bonjourDomainName: " + this.bonjourDomainName;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        do {
            // coverity[UNREACHABLE]
            if (!(obj instanceof NetworkDevice)) continue;
            NetworkDevice other = (NetworkDevice) obj;
            // coverity[UNREACHABLE]
            if (!this.getInetAddress().equals(other.getInetAddress())) continue;
            // coverity[UNREACHABLE]
            if (!TextUtils.equals(this.bonjourDomainName, other.bonjourDomainName)) continue;
            // coverity[UNREACHABLE]
            if (!TextUtils.equals(this.mBonjourService, other.mBonjourService)) continue;
            // coverity[UNREACHABLE]
            if (!TextUtils.equals(this.mDeviceIdentifier, other.mDeviceIdentifier)) continue;
            return true;
        } while (false);
        return false;
    }

    public String getDeviceIdentifier() {
        return mDeviceIdentifier;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hashCode = 1;
        hashCode = hashCode * prime + this.getInetAddress().hashCode();
        hashCode = hashCode * prime + ((bonjourDomainName != null) ? bonjourDomainName.hashCode() : 0);
        hashCode = hashCode * prime + ((mBonjourService != null) ? mBonjourService.hashCode() : 0);
        hashCode = hashCode * prime + ((mDeviceIdentifier != null) ? mDeviceIdentifier.hashCode() : 0);
        return hashCode;
    }

    public List<NetworkDevice> getAllDiscoveryInstances() {
        List<NetworkDevice> instances = new ArrayList<NetworkDevice>(mOtherInstances.size() + 1);
        instances.add(this);
        instances.addAll(mOtherInstances);
        return instances;
    }

    public void addDiscoveryInstance(NetworkDevice device) {
        if ((device != null)
                && !equals(device) && !mOtherInstances.contains(device)) {
            normalizeAttribute(device, MDnsUtils.ATTRIBUTE__MFG);
            normalizeAttribute(device, MDnsUtils.ATTRIBUTE__USB_MFG);
            normalizeAttribute(device, MDnsUtils.ATTRIBUTE__UUID);
            normalizeAttribute(device, MDnsUtils.ATTRIBUTE__USB_MDL);
            normalizeAttribute(device, MDnsUtils.ATTRIBUTE__MDL);
            normalizeAttribute(device, MDnsUtils.ATTRIBUTE__TY);
            mOtherInstances.add(device);
            device.extraAttrs.putAll(extraAttrs);
        }
    }

    private void normalizeAttribute(NetworkDevice other, String attribute) {
        if (discoveryMethod == DiscoveryMethod.MDNS_DISCOVERY && other.discoveryMethod == DiscoveryMethod.MDNS_DISCOVERY) {

            // get the value
            String valueA = bonjourData.getString(attribute);
            String valueB = other.bonjourData.getString(attribute);

            if ((valueA == null) && (valueB != null)) {
                // update our value that of other instances
                bonjourData.putString(attribute, valueB);
                for (NetworkDevice device : mOtherInstances) {
                    if (device.discoveryMethod == DiscoveryMethod.MDNS_DISCOVERY) {
                        device.bonjourData.putString(attribute, valueB);
                    }
                }
            } else if ((valueA != null) && (valueB == null)) {
                // update new entry
                other.bonjourData.putString(attribute, valueA);
            }
        }
    }

    public String getBonjourService() {
        return this.mBonjourService;
    }

    public DiscoveryMethod getDiscoveryMethod() {
        return discoveryMethod;
    }

    /**
     * @return the InetAddress
     */
    public InetAddress getInetAddress() {
        return this.inetAddress;
    }

    /**
     * Returns the numeric representation of the printer's IP address (such as "127.0.0.1").
     */
    public String getHostAddress() {
        return this.inetAddress.getHostAddress();
    }

    /**
     * @return the printer model hostname, e.g. "HP Officejet 6500 E709n"
     */
    public String getModel() {
        // do we have a value?
        if (!TextUtils.isEmpty(this.model)) {
            // we do, return it
            return this.model;
        }
        // look through other entries
        for(NetworkDevice device : mOtherInstances) {
            // does entry have value?
            if (!TextUtils.isEmpty(device.model)) {
                // yes it does, return it
                return device.model;
            }
        }
        // screw it, return null
        return null;
    }

    /**
     * @return the hostname of the printer in the network
     */
    public String getHostname() {
        return this.hostname;
    }

    /**
     * @return the bonjour hostname of the printer in the network
     */
    public String getBonjourName() {
        return this.bonjourName;
    }

    public int getPort() {
        return mPort;
    }

    /**
     * @return the bonjour domain hostname of the printer in the network
     */
    public String getBonjourDomainName() {
        return this.bonjourDomainName;
    }

    public Bundle getTxtAttributes() {
        Bundle attrs = new Bundle(bonjourData);
        Bundle addedAttrs = new Bundle(extraAttrs);
        for(String key : attrs.keySet()) {
            addedAttrs.remove(key);
        }
        attrs.putAll(addedAttrs);
        return attrs;
    }

    public Bundle getTxtAttributes(String serviceName) {
        List<NetworkDevice> instances = getAllDiscoveryInstances();
        for (NetworkDevice instance : instances) {
            if (TextUtils.equals(serviceName, instance.getBonjourService()))
                return instance.getTxtAttributes();
        }
        return new Bundle();
    }

    public void addAttributes(Bundle attrs) {
        if (attrs != null) {
            List<NetworkDevice> instances = getAllDiscoveryInstances();
            for(NetworkDevice instance : instances) {
                instance.extraAttrs.putAll(attrs);
            }
        }
    }
}
