/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.usbaccessories;

import com.hp.ext.clients.ReadableResource;
import com.hp.ext.service.usbAccessories.Hid;

public interface HidResource extends ReadableResource<Hid> {
    OpenHidOperationResourceFacade openHidOperation();
}
