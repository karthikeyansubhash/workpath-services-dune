/**
 * (C) Copyright 2023 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.sdksupport;

import com.hp.ext.clients.DeletableResource;
import com.hp.ext.clients.ReadableResource;
import com.hp.ext.service.sdkSupport.SimulatedHidDevice;
import com.hp.ext.types.base.DeleteContent;

public interface SimulatedHidDeviceResource extends ReadableResource<SimulatedHidDevice>, DeletableResource<DeleteContent> {
    TapSimulatedHidDeviceOperationResourceFacade tap();
}
