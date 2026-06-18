/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */

package com.hp.jetadvantage.link.device.services.interfaces;

import com.hp.jetadvantage.link.device.services.types.EmailSettingsData;


/**
 * Device Email Interface to get email settings data : Email Services
 */
public interface IDeviceEmailService {
    public boolean isOnline();
    public EmailSettingsData getEmailSettings();
}
