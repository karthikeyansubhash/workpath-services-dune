/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.scanjob;

import com.hp.ext.clients.ReadableResource;
import com.hp.ext.service.scanJob.ScanJob;

public interface ScanJobResource extends ReadableResource<ScanJob>{
    public CancelScanJobOperationResourceFacade cancel();
}
