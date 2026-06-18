/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */

package com.hp.ext.clients.solutionmanager;

import com.hp.ext.clients.ModifiableResource;
import com.hp.ext.clients.ReadableResource;
import com.hp.ext.service.solutionManager.Configuration;
import com.hp.ext.service.solutionManager.Configuration_Modify;

public interface ConfigurationResource extends ReadableResource<Configuration>, ModifiableResource<Configuration, Configuration_Modify> {
    DataResourceFacade dataResource();
}
