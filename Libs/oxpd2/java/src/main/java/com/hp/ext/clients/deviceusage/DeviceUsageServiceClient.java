/**
 * (C) Copyright 2023 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.deviceusage;

public interface DeviceUsageServiceClient {
    CapabilitiesResourceFacade capabilities();
    DeviceUsageAgentsResourceFacade deviceUsageAgents();
}
