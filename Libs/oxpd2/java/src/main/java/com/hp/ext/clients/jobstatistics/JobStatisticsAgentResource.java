/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.jobstatistics;

import com.hp.ext.clients.ReadableResource;
import com.hp.ext.service.jobStatistics.JobStatisticsAgent;

public interface JobStatisticsAgentResource extends ReadableResource<JobStatisticsAgent>{
    public JobsResourceFacade jobs();
}
