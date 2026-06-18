// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.sdd.servicediscovery;

import android.os.Bundle;

import java.net.InetAddress;

public interface ServiceParser {
    NetworkDevice.DiscoveryMethod getDiscoveryMethod();
    int getPort();
    String getDeviceIdentifier();
    String getHostname();
    InetAddress getAddress();
    String getModel();
    String getServiceName();
    String getAttribute(String key) throws Exception;
    Bundle getAllAttributes();

}
