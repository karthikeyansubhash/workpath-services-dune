// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.sdd.servicediscovery;

import java.util.List;

public interface DuplicateAddressArbitrator {

    DuplicateAddressResolution duplicateResolution(List<NetworkDevice> currentDevices, NetworkDevice newDevice);
}
