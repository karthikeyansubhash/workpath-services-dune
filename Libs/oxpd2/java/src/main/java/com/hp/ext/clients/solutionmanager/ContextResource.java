/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.solutionmanager;

import com.hp.ext.clients.ModifiableResource;
import com.hp.ext.clients.ReadableResource;
import com.hp.ext.clients.ReplaceableResource;
import com.hp.ext.service.solutionManager.Context;
import com.hp.ext.service.solutionManager.Context_Modify;
import com.hp.ext.service.solutionManager.Context_Replace;

public interface ContextResource extends ReadableResource<Context>, ModifiableResource<Context, Context_Modify>, ReplaceableResource<Context, Context_Replace> {

}
