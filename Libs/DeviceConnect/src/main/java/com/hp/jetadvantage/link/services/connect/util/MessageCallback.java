// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.connect.util;

public interface MessageCallback {
    void onResponse(int status, String eTag, String data);
}
