// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.sdd.servicediscovery.mdns;

import java.util.Map;

public class DnsService {
    private DnsPacket.Name name;
    private DnsPacket.Name serviceNameSuffix;
    private DnsPacket.Name hostname;
    private byte[][] addresses;
    private int port;
    private Map<String, byte[]> attributes;

    public DnsService(DnsPacket.Name name, DnsPacket.Name serviceNameSuffix, DnsPacket.Name hostname, byte[][] addresses, int port,
                      Map<String, byte[]> attributes) {
        this.name = name;
        this.serviceNameSuffix = serviceNameSuffix;
        this.hostname = hostname;
        this.addresses = addresses;
        this.port = port;
        this.attributes = attributes;
    }

    public DnsPacket.Name getName() {
        return this.name;
    }

    public DnsPacket.Name getNameSuffix() {
        return this.serviceNameSuffix;
    }

    public DnsPacket.Name getHostname() {
        return this.hostname;
    }

    public byte[][] getAddresses() {
        return addresses;
    }

    public int getPort() {
        return port;
    }

    public Map<String, byte[]> getAttributes() {
        return attributes;
    }

    @Override
    public String toString() {
        return this.getName().toString();
    }
}
