/**
 * (C) Copyright 2023 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.sdksupport;

public interface SdkSupportServiceClient {
    EchoResourceFacade echo();
    FunctionsResourceFacade functions();
    SimulatedHidDevicesResourceFacade simulatedHidDevices();
    ControlPanelResourceFacade controlPanel();
}
