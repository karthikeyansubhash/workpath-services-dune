/**
 * (C) Copyright 2023 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.security;

import com.hp.ext.clients.ReadableResource;
import com.hp.ext.service.security.SecurityAgent;

public interface SecurityAgentResource extends ReadableResource<SecurityAgent> {
    ResolveSecurityExpressionOperationResourceFacade resolveSecurityExpression();
}
