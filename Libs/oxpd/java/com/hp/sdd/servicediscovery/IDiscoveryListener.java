// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.sdd.servicediscovery;

public interface IDiscoveryListener {
    void onDiscoveryFailed();
    void onDeviceRemoved(NetworkDevice networkDevice);
    void onDeviceFound(NetworkDevice networkDevice);
    void onActiveDiscoveryFinished();
    void onDiscoveryFinished();
}
