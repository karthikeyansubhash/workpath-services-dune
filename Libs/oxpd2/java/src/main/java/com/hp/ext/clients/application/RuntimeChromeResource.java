/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.application;

import com.hp.ext.clients.ModifiableResource;
import com.hp.ext.clients.ReadableResource;
import com.hp.ext.clients.ReplaceableResource;
import com.hp.ext.service.application.RuntimeChrome;
import com.hp.ext.service.application.RuntimeChrome_Modify;
import com.hp.ext.service.application.RuntimeChrome_Replace;

public interface RuntimeChromeResource extends ReadableResource<RuntimeChrome>, ModifiableResource<RuntimeChrome, RuntimeChrome_Modify>, ReplaceableResource<RuntimeChrome, RuntimeChrome_Replace> {

}
