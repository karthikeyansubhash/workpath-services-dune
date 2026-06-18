/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.usbaccessories;

import com.hp.ext.clients.ReadableResource;
import com.hp.ext.service.usbAccessories.OpenHIDAccessory;

public interface OpenHidAccessoryResource extends ReadableResource<OpenHIDAccessory> {
    public ReadReportOperationResourceFacade readReport();
    public WriteReportOperationResourceFacade writeReport();
}
