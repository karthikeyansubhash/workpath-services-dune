/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.authentication;

import com.hp.ext.clients.ReadableResource;
import com.hp.ext.service.authentication.AuthenticationAgent;

public interface AuthenticationAgentResource extends ReadableResource<AuthenticationAgent>{
    LoginOperationResource login();
}
