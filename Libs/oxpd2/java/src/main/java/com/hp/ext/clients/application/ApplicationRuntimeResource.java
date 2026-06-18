/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.application;

import com.hp.ext.clients.ReadableResource;
import com.hp.ext.service.application.ApplicationRuntime;

public interface ApplicationRuntimeResource extends ReadableResource<ApplicationRuntime> {
    ResetOperationResourceFacade reset();
    CurrentContextResourceFacade currentContext();
}
