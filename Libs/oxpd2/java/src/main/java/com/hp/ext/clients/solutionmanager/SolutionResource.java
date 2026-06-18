/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.solutionmanager;

import com.hp.ext.clients.ReadableResource;
import com.hp.ext.service.solutionManager.Solution;

public interface SolutionResource extends ReadableResource<Solution> {
    CertificateAuthoritiesResourceFacade certificateAuthorities();

    ContextResourceFacade context();

    ReissueInstallCodeOperationResourceFacade reissueInstallCode();

    ConfigurationResourceFacade configuration();

    RuntimeRegistrationsResourceFacade runtimeRegistrations();
}
