// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.sdd.servicediscovery.mdns;

@SuppressWarnings({"serial", "unused"})
class BonjourException extends DnsException {

    public BonjourException(String detailString) {
        super(detailString);
    }

    public BonjourException(String detailMessage, Throwable cause) {
        super(detailMessage, cause);
    }
}
