/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.usbaccessories;

public interface UsbAccessoriesServiceClient {
    CapabilitiesResourceFacade capabilities();
    UsbAccessoriesAgentsResourceFacade usbAccessoriesAgents();
    AccessoriesResourceFacade accessories();
}
