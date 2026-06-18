/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.authentication;

/**
 * The Facade Interface for the Extensibility Authentication Service Client
 */
public interface AuthenticationServiceClient {
    CapabilitiesResourceFacade capabilities();
    AuthenticationAccessPointsResourceFacade authenticationAccessPoints();
    AuthenticationAgentsResourceFacade authenticationAgents();
    SessionResourceFacade session();
}
