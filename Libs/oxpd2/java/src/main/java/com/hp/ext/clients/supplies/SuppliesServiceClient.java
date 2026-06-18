/**
 * (C) Copyright 2023 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.supplies;

public interface SuppliesServiceClient {
    CapabilitiesResourceFacade capabilities();
    SuppliesAgentsResourceFacade suppliesAgents();
}
