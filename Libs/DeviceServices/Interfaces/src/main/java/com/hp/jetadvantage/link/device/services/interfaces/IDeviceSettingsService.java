/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */

package com.hp.jetadvantage.link.device.services.interfaces;

import com.hp.ws.cdm.network.PrintServices;

import java.util.List;

/**
 * Device Settings Interface : Print Services
 */
public interface IDeviceSettingsService {
    public PrintServices getPrintServices();

    public boolean disableNetworkPrintServices();

    public boolean enableNetworkPrintServices();
}
