/**
 * (C) Copyright 2025 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.solutionmanager;

import com.hp.ext.clients.CreatableResource;
import com.hp.ext.clients.ReadableResource;
import com.hp.ext.service.solutionManager.RuntimeRegistrations;
import com.hp.ext.service.solutionManager.RuntimeRegistration;
import com.hp.ext.service.solutionManager.RuntimeRegistration_Create;

public interface RuntimeRegistrationsResource extends ReadableResource<RuntimeRegistrations>, CreatableResource<RuntimeRegistration, RuntimeRegistration_Create>{

}
