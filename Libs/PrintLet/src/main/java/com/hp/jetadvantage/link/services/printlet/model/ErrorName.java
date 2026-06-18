// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.printlet.model;

/**
 * Enumeration of Error Name.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum ErrorName {
    /** The library has not yet been set up. */
    NotSetup,
    /** The version of the OXPd:Print service required by this library is not available on this device. */
    ServiceNotFound,
    /** An error occurred when attempting the CORS/Ajax request. */
    AjaxError,
    /** The request contained invalid input. */
    BadRequest,
    /** An unexpected condition prevented the device from fulfilling the request. */
    ServerError,
    /** A temporary error indicating that the device is too busy processing jobs and/or other requests. The App should try the unmodified request again at some later point in time with an expectation that the temporary busy condition will have been cleared. */
    Busy,
    /** The request requires user authentication. */
    NotAuthenticated,
    /** The requester is not authorized to perform the request. */
    NotAuthorized,
    /** This status code is used when the request is for a specific resource that can't be found. For example, a request to get job attributes for a job that never existed or no longer exists. */
    NotFound,
    /** This status code is used when the request is for something that cannot happen. For example, a request to cancel a job that has already been canceled or aborted by the system. */
    NotPossible,
    /** The device is refusing to service the request because one or more of the supplied options is longer than the maximum length supported for that option. */
    ParamTooLong,
    /** The scheme of the supplied document Uri is not supported by the device. */
    UnsupportedUri,
    /** The supplied documentUri is not reachable by the device. */
    UnreachableUri,
    /** Unknown error. */
    Unknown
}
