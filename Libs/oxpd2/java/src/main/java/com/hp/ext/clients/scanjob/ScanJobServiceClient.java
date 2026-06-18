/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.scanjob;

public interface ScanJobServiceClient {
    public CapabilitiesResourceFacade capabilities();
    public ScanJobAgentsResourceFacade scanJobAgents();
    public ProfileResourceFacade profile();
    public DefaultOptionsResourceFacade defaultOptions();
}
