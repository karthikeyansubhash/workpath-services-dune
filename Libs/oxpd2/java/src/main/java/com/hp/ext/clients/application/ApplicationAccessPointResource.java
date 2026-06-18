/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.application;

import com.hp.ext.clients.ReadableResource;
import com.hp.ext.service.application.ApplicationAccessPoint;

public interface ApplicationAccessPointResource extends ReadableResource<ApplicationAccessPoint> {
    InitiateLaunchOperationResourceFacade initiateLaunch();
}
