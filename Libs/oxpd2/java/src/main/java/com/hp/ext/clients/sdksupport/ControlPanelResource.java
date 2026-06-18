package com.hp.ext.clients.sdksupport;

import com.hp.ext.clients.ReadableResource;
import com.hp.ext.service.sdkSupport.ControlPanel;

public interface ControlPanelResource extends ReadableResource<ControlPanel> {
    CaptureScreenResourceFacade captureScreen();
    PressKeyResourceFacade pressKey();
    PressScreenResourceFacade pressScreen();
}
