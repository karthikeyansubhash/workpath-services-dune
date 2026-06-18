// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.connect.util;

public class OmniResponseException extends Exception {
    private int status;

    public OmniResponseException(int status, String response) {
        super(response);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
