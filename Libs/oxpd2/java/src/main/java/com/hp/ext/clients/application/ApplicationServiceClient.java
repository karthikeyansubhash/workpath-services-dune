/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.application;

public interface ApplicationServiceClient {
    CapabilitiesResourceFacade capabilities();
    ApplicationAccessPointsResourceFacade applicationAccessPoints();
    ApplicationAgentsResourceFacade applicationAgents();
    ApplicationRuntimeResourceFacade applicationRuntime();
    HomescreenResourceFacade homescreen();
    I18nAssetsResourceFacade i18nAssets();
    MessageCenterAgentsResourceFacade messageCenterAgents();
}
