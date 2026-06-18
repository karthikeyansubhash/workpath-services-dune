// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.sdd.servicediscovery.snmp;

import android.os.Bundle;
import android.text.TextUtils;

import com.hp.sdd.servicediscovery.NetworkDevice;
import com.hp.sdd.servicediscovery.ServiceParser;
import com.hp.sdd.servicediscovery.mdns.MDnsUtils;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.List;

public final class SnmpServiceParser implements ServiceParser {

    private static final String FIELD_SEPARATOR = ";";
    private static final String VALUE_SEPERATOR = ":";

    /** SNMP MIB system name, e.g. "HPD11201" */
    public final String mHostName;

    /** IEEE 1284 Device ID */
    public final String mDeviceId;

    public final InetAddress mAddress;

    public final Bundle mAttributes = new Bundle();

    public final int mPort;

    @SuppressWarnings({"unused", "UnusedAssignment"})
    public static List<SnmpServiceParser> parse(DatagramPacket pkt) throws ProtocolException {
        InetAddress address;
        address = pkt.getAddress();
        SnmpParser parser = new SnmpParser(pkt);
        int parserStatus = parser.getErrorStatus();
        if (parserStatus != 0) {
            throw new ProtocolException("SNMP error: " + parserStatus);
        }
        SnmpParser.OID oid;
        // coverity[FB.DLS_DEAD_LOCAL_STORE]
        oid = parser.getOID();
        String hostname = parser.getString();
        if (TextUtils.isEmpty(hostname)) {
            throw new ProtocolException("sysName is empty");
        }
        // coverity[FB.DLS_DEAD_LOCAL_STORE]
        oid = parser.getOID();
        String deviceID  = parser.getString();
        if (deviceID.length() == 0) {
            throw new ProtocolException("mDeviceId is empty");
        }
        parser.finish();

        Bundle attrs = new Bundle();
        if (!TextUtils.isEmpty(deviceID)) {
            String[] attributes = deviceID.split(FIELD_SEPARATOR);
            for(String attribute : attributes) {
                String[] attributeParts = attribute.split(VALUE_SEPERATOR);
                if (attributeParts.length == 2) {
                    attrs.putString(attributeParts[0], attributeParts[1]);
                }
            }
        }

        List<SnmpServiceParser> snmpServiceParsers = new ArrayList<SnmpServiceParser>();

        boolean addIPP = false;

        String value = attrs.getString(SnmpUtils.ATTRIBUTE__PDL);
        if (!TextUtils.isEmpty(value)
                && (value.contains("AppleRaster")
                || value.contains("PCLM")
                || value.contains("PWGRaster"))) {
            addIPP = true;
        }

        if (!addIPP) {
            value = attrs.getString("IPP-HTTP");
            addIPP = TextUtils.equals(value, "T");
        }
        if (!addIPP) {
            value = attrs.getString("IPP-E");
            addIPP = !TextUtils.isEmpty(value);
        }

        if (addIPP) {
            snmpServiceParsers.add(new SnmpServiceParser(deviceID, address, hostname, attrs, 631));
        }

        snmpServiceParsers.add(new SnmpServiceParser(deviceID, address, hostname, attrs, 9100));

        return snmpServiceParsers;
    }

    private SnmpServiceParser(String deviceID,
                              InetAddress address,
                              String hostname,
                              Bundle attributes,
                              int port) {
        mDeviceId = deviceID;
        mAddress = address;
        mHostName = hostname;
        mAttributes.putAll(attributes);
        mAttributes.putString(MDnsUtils.ATTRIBUTE__DEV_ID, mDeviceId);
        mPort = port;
    }

    @Override
    public String toString() {
        return String.valueOf(mDeviceId);
    }

    @Override
    public NetworkDevice.DiscoveryMethod getDiscoveryMethod() {
        return NetworkDevice.DiscoveryMethod.SNMP_DISCOVERY;
    }

    @Override
    public int getPort() {
        return mPort;
    }

    @Override
    public String getDeviceIdentifier() {
        return getHostname();
    }

    @Override
    public String getHostname() {
        return mHostName;
    }

    @Override
    public InetAddress getAddress() {
        return mAddress;
    }

    @Override
    public String getModel() {
        return mAttributes.getString(SnmpUtils.ATTRIBUTE__MODEL);
    }

    @Override
    public String getServiceName() {
        return SnmpUtils.SNMP_SERVICE_NAME;
    }

    @Override
    public String getAttribute(String attr) throws Exception {
        return mAttributes.getString(attr);
    }

    @Override
    public Bundle getAllAttributes() {
        return mAttributes;
    }
}
