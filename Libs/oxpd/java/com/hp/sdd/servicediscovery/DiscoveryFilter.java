// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.sdd.servicediscovery;

public interface DiscoveryFilter {
    boolean meetsFilterCriteria(NetworkDevice device);
}
