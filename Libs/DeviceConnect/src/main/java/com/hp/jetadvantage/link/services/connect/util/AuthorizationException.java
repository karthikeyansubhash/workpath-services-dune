// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.connect.util;

@SuppressWarnings({"unused", "WeakerAccess"})
public class AuthorizationException extends Exception {
    private static final long serialVersionUID = -5713285945397115821L;

    public AuthorizationException() {
        super();
    }

    public AuthorizationException(String message) {
        super(message);
    }
}
