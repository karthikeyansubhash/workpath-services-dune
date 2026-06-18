package com.hp.jetadvantage.link.device.services.interfaces;

import com.hp.ext.service.device.Identity;
import com.hp.ext.service.supplies.SuppliesInfo;

public interface IDeviceSuppliesService {
    public boolean isSupported();
    public Identity getIdentity();
    public SuppliesInfo getSuppliesInfo(String packageName);
}
