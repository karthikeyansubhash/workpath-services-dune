/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.device.services.interfaces;

import androidx.annotation.NonNull;

public interface IDeviceSubscriptionService {
    public String Subscribe(@NonNull String[] guns, @NonNull ICdmCallback callback);
}
