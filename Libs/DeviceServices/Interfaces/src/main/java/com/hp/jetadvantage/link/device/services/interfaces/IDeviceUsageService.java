package com.hp.jetadvantage.link.device.services.interfaces;

import com.hp.ext.service.deviceUsage.LifetimeCounters;

public interface IDeviceUsageService {
    public boolean isSupported();
    public LifetimeCounters getLifetimeCounters(String packageName);
}
