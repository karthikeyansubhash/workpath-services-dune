// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.sdd.servicediscovery.mdns;

import android.os.Bundle;

import com.hp.sdd.servicediscovery.ServiceParser;

@SuppressWarnings("unused")
public interface BonjourServiceParser extends ServiceParser {

    String getBonjourName();
    String getBonjourServiceName();
    String getAttribute(String key) throws Exception;
    Bundle getAllAttributes();
}
