/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.printjob;

public interface PrintJobServiceClient {
    CapabilitiesResourceFacade capabilities();
    PrintJobAgentsResourceFacade printJobAgents();
}
