/**
 * (C) Copyright 2023 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.copy;

import com.hp.ext.clients.ReadableResource;
import com.hp.ext.service.copy.CopyJob;

public interface CopyJobResource extends ReadableResource<CopyJob>{
    public CancelCopyJobOperationResourceFacade cancel();
}
