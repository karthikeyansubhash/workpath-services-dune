/**
 * (C) Copyright 2023 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.solutiondiagnostics;

public interface SolutionDiagnosticsServiceClient {
    CapabilitiesResourceFacade capabilities();
    SolutionDiagnosticsAgentsResourceFacade solutionDiagnosticsAgents();
    
}
