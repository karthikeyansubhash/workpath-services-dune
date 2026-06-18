/**
 * (C) Copyright 2025 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.solutionmanager;

import com.hp.ext.clients.DeletableResource;
import com.hp.ext.clients.ReadableResource;
import com.hp.ext.service.solutionManager.RuntimeRegistration;
import com.hp.ext.types.base.DeleteContent;

public interface RuntimeRegistrationResource
        extends ReadableResource<RuntimeRegistration>, DeletableResource<DeleteContent> {
}
