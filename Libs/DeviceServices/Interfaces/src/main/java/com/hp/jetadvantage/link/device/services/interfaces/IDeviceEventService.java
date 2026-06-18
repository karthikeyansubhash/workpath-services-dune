/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */

package com.hp.jetadvantage.link.device.services.interfaces;

import com.hp.ws.cdm.alert.Alerts;

public interface IDeviceEventService {
    public Alerts getDeviceEvents();
    public boolean isSupported();
    public void addCallback(ICdmCallback callback);
}
