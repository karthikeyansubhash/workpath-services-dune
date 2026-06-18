/**
 * (C) Copyright 2022 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.ext.clients.application;

import com.hp.ext.clients.ReadableResource;
import com.hp.ext.service.application.CurrentContext;

public interface CurrentContextResource extends ReadableResource<CurrentContext> {
    BeepOperationResourceFacade beep();
    ExecOperationResourceFacade exec();
    ExitOperationResourceFacade exit();
    ResetInactivityTimerOperationResourceFacade resetInactivityTimer();
    RuntimeChromeResourceFacade runtimeChrome();
    StartIntentResourceFacade startIntent();
}
