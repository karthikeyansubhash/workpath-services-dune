/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.scanjob;

import com.hp.ext.clients.ReadableResource;
import com.hp.ext.service.scanJob.ScanJobAgent;

public interface ScanJobAgentResource extends ReadableResource<ScanJobAgent>{
    public ScanJobsResourceFacade scanJobs();

    /**
     * Gets the resource for local scans.
     *
     * @return the ScanJobsResourceFacade for local scans
     */
    public ScanJobsResourceFacade localScans();
}
