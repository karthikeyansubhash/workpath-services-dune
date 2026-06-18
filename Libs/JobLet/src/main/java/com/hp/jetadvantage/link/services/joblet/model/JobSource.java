// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.joblet.model;

/**
 * Enum of job sources, which is needed in order to decide proper
 * Joblet UI components behaviour and further determination
 * of jobs executed.
 */
public enum JobSource {
    /**
     * Job is submitted using SDK APIs and so, Joblet UI is applicable for this job.
     * This has highest priority
     */
    SDK,
    /**
     * Job submitted without SDK involved, e.g. using OOB applications.
     * This has medium priority and can be replaced with any other value
     */
    EXTERNAL,
    /**
     * Job submitted from unknown source.
     * This has lowest priority and can be replaced with any other value
     */
    UNKNOWN
}
