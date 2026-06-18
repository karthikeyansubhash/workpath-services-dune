/**
 * (C) Copyright 2025 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.copy;

import com.hp.ext.clients.ReadableResource;
import com.hp.ext.service.copy.StoredJob;

public interface StoredJobResource extends ReadableResource<StoredJob>{
    public ReleaseStoredJobOperationResourceFacade release();
    public RemoveStoredJobOperationResourceFacade remove();
}
