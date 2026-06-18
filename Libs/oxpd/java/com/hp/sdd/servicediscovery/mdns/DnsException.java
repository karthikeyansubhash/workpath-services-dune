// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.sdd.servicediscovery.mdns;

@SuppressWarnings("serial")
public class DnsException extends Exception {

    public DnsException(String detailMessage) {
        super(detailMessage);
    }

    public DnsException(String detailMessage, Throwable cause) {
        super(detailMessage, cause);
    }
}
