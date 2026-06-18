/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.device;

/**
 * The Facade Interface for the Extensibility Device Service Client
 */
public interface DeviceServiceClient {
    CapabilitiesResourceFacade capabilities();
    IdentityResourceFacade identity();
    StatusResourceFacade status();
    ScannerResourceFacade scanner();
    EmailResourceFacade email();
    DeploymentInformationResourceFacade deploymentInformation();
}
