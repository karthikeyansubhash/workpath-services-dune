// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.sdd.servicediscovery;

public class DuplicateAddressResolution {
    public final NetworkDevice mDeviceToAdd;
    public final NetworkDevice mDeviceToRemove;

    public DuplicateAddressResolution(NetworkDevice deviceToAdd, NetworkDevice deviceToRemove) {
        mDeviceToAdd = deviceToAdd;
        mDeviceToRemove = deviceToRemove;
    }
}
