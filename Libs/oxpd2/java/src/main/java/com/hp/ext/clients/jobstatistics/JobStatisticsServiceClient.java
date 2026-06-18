/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.jobstatistics;

public interface JobStatisticsServiceClient {
    CapabilitiesResourceFacade capabilities();
    JobStatisticsAgentsResourceFacade jobStatisticsAgents();
}
