/**
 * (C) Copyright 2023 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.sdksupport;

import com.hp.ext.clients.CreatableResource;
import com.hp.ext.clients.ReadableResource;
import com.hp.ext.service.sdkSupport.SimulatedHidDevice;
import com.hp.ext.service.sdkSupport.SimulatedHidDevice_Create;
import com.hp.ext.service.sdkSupport.SimulatedHidDevices;

public interface SimulatedHidDevicesResource extends ReadableResource<SimulatedHidDevices>, CreatableResource<SimulatedHidDevice, SimulatedHidDevice_Create>{
}
