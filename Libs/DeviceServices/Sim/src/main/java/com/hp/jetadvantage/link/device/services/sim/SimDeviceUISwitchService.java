package com.hp.jetadvantage.link.device.services.sim;

import com.hp.jetadvantage.link.device.services.interfaces.IDeviceUISwitchService;

public class SimDeviceUISwitchService implements IDeviceUISwitchService {
    @Override
    public void switchToDevice() {

    }

    @Override
    public boolean launchAppFromDeviceHome(String packageName) {
        return false;
    }

    @Override
    public boolean closeAppFromDeviceHome(String packageName) {
        return false;
    }
}
