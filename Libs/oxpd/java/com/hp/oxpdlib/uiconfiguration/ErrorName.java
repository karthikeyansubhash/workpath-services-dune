// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.uiconfiguration;

/**
 * Enumeration of Error Name.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum ErrorName {
    /** The library has not yet been set up. */
    NotSetup,
    /** The version of the OXPd:UIConfiguration service required by this library is not available on this device. */
    ServiceNotFound,
    /** An error occurred when attempting the CORS/Ajax request. */
    AjaxError,
    /** A specific resource that can't be found. */
    NotFound,
    /** The request contained invalid input. */
    ServerError,
    /** Unsupported version */
    UnsupportedVersion,
    /** The request has invalid authorization data. */
    AuthError,
    /** Unknown error. */
    Unknown
}
