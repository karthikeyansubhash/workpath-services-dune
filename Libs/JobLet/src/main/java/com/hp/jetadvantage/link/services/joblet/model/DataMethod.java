// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.joblet.model;

/**
 * Possible methods for JobDataContentProvider and for ListenerDataContentProvider
 */
public enum DataMethod {
    PUT,
    GET,
    REMOVE,
    REMOVE_ALL,
    CONTAINS,
    SIZE,
    GET_ALL,
    GET_CAUSES_BY_ID,
    GET_IDS,
    PERSISTENT_DATA,
}
