/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.solutionmanager;

public interface SolutionManagerServiceClient {
    CapabilitiesResourceFacade capabilities();
    InstallerResourceFacade installer();
    SolutionsResourceFacade solutions();
}
