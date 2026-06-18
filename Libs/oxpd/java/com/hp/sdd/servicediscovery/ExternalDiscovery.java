// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.sdd.servicediscovery;

import android.database.DataSetObservable;

import java.net.DatagramPacket;
import java.net.UnknownHostException;
import java.util.List;

public abstract class ExternalDiscovery extends DataSetObservable implements IDiscovery {

    @Override
    public abstract void clear();

    @Override
    public DatagramPacket[] createQueryPackets() throws UnknownHostException {
        return new DatagramPacket[0];
    }

    @Override
    public List<ServiceParser> parseResponse(DatagramPacket reply) {
        return null;
    }

    @Override
    public abstract int getPort();

    @Override
    public boolean isFallback() {
        return false;
    }

    public abstract void start();
    public abstract void stop();

    public abstract List<NetworkDevice> getDevicesFound();
}
