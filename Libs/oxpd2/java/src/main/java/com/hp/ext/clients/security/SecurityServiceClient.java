/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.security;

public interface SecurityServiceClient {
    CapabilitiesResourceFacade capabilities();
    SecurityAgentsResourceFacade securityAgents();
}
