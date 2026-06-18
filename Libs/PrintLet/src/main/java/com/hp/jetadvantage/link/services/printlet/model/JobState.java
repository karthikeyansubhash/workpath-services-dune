// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.printlet.model;

/**
 * Enumeration of print job states.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum JobState {
    
    /** The job has been aborted by the system. */
    Aborted(8),
    /** The job is a candidate to start processing, but is not yet processing. */
    Pending(3),
    /** The job is not a candidate for processing for any number of reasons but will return to the
     * pending state as soon as the reasons are no longer present. */
    PendingHeld(4),
    /** The job is processing. */
    Processing(5),
    /** The job has stopped while processing for any number of reasons and will return to the
     * processing state as soon as the reasons are no longer present. */
    ProcessingStopped(6),
    /** The job has completed successfully (or with warnings or errors) after processing. */
    Completed(9),
    /** The job has been canceled. */
    Canceled(7),
    /** The job state is unknown. */
    Unknown(0);
    // Purposefully omitting the other job states defined by IPP

    /**
     * IPP value associated with enum
     */
    final int mValue;

    /**
     * JobState constructor
     * @param value
     *              IPP value associated with enum
     */
    JobState(int value) {
        mValue = value;
    }

    /**
     * Convert ipp value to JobState
     * @param value
     *              IPP value string
     * @return
     *              Matching JobState enum or Unknown if no match is found
     */
    static JobState fromAttributeValue(int value) {
        for(JobState enumValue : values()) {
            if (enumValue.mValue == value) {
                return enumValue;
            }
        }
        return Unknown;
    }

}
