// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.sdd.servicediscovery;

import java.net.DatagramPacket;
import java.net.UnknownHostException;
import java.util.List;

public interface IDiscovery {

    void clear();
    DatagramPacket[] createQueryPackets() throws UnknownHostException;
    List<ServiceParser> parseResponse(DatagramPacket reply);
    int getPort();
    boolean isFallback();
}
