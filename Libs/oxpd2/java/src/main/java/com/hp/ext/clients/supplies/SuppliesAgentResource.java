/**
 * (C) Copyright 2023 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.supplies;

import com.hp.ext.clients.ReadableResource;
import com.hp.ext.service.supplies.SuppliesAgent;

public interface SuppliesAgentResource extends ReadableResource<SuppliesAgent>{
    public SuppliesConfigurationResourceFacade suppliesConfiguration();
    public SuppliesInfoResourceFacade suppliesInfo();
    public SuppliesUsageResourceFacade suppliesUsage();
}
