/**
 * (C) Copyright 2023 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.copy;

import com.hp.ext.clients.ReadableResource;
import com.hp.ext.service.copy.CopyAgent;

public interface CopyAgentResource extends ReadableResource<CopyAgent>{
    public CopyJobsResourceFacade copyJobs();
    public StoredJobsResourceFacade storedJobs();
}
