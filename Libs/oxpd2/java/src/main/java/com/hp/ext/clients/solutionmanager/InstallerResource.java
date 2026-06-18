/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.solutionmanager;

import com.hp.ext.clients.ReadableResource;
import com.hp.ext.service.solutionManager.Installer;

public interface InstallerResource extends ReadableResource<Installer> {

    InstallerOperationsResourceFacade installerOperations();
    InstallSolutionOperationResourceFacade installSolution();
    InstallRemoteOperationResourceFacade installRemote();
    UninstallSolutionOperationResourceFacade uninstallSolution();

}
