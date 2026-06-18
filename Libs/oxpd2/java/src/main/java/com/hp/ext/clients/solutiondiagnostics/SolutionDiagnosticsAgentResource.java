/**
 * (C) Copyright 2023 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.solutiondiagnostics;

import com.hp.ext.clients.ReadableResource;
import com.hp.ext.service.solutionDiagnostics.SolutionDiagnosticsAgent;

public interface SolutionDiagnosticsAgentResource extends ReadableResource<SolutionDiagnosticsAgent> {
    LogResourceFacade log();
}
