/**
 * (C) Copyright 2023 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.copy;

/**
 * The Facade Interface for the Extensibility Copy Service Client
 */
public interface CopyServiceClient {
    CapabilitiesResourceFacade capabilities();
    CopyAgentsResourceFacade copyAgents();
    DefaultOptionsResourceFacade defaultOptions();
    ProfileResourceFacade profile();
}
