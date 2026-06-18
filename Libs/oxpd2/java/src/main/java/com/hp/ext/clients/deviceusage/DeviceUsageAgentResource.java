/**
 * (C) Copyright 2023 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.deviceusage;

import com.hp.ext.clients.ReadableResource;
import com.hp.ext.service.deviceUsage.DeviceUsageAgent;

public interface DeviceUsageAgentResource extends ReadableResource<DeviceUsageAgent>{
    public LifetimeCountersResourceFacade lifetimeCounters();
}
