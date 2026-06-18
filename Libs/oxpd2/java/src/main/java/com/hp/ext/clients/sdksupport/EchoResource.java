/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.sdksupport;

import com.hp.ext.clients.ModifiableResource;
import com.hp.ext.clients.ReadableResource;
import com.hp.ext.clients.ReplaceableResource;
import com.hp.ext.service.sdkSupport.Echo;
import com.hp.ext.service.sdkSupport.Echo_Modify;
import com.hp.ext.service.sdkSupport.Echo_Replace;

public interface EchoResource extends ReadableResource<Echo>, ModifiableResource<Echo, Echo_Modify>, ReplaceableResource<Echo, Echo_Replace> {
    ExecuteEchoOperationResourceFacade execute();
}
