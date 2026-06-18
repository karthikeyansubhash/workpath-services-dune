// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.joblet.model;

/**
 * Possible SPS job states
 */
public enum JobState {
    TL_ST_UNKNOWN,
    TL_ST_IDLE,
    TL_ST_JOB_PENDING,
    TL_ST_COMPLETE,
    TL_ST_CANCELED,
    TL_ST_PROGRESSING,
    TL_ST_WAITING,
    TL_ST_FAILED
}
