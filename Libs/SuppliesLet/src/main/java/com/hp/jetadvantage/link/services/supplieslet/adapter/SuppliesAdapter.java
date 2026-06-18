package com.hp.jetadvantage.link.services.supplieslet.adapter;

import com.hp.ext.service.device.Identity;
import com.hp.ext.service.supplies.SuppliesInfo;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceSuppliesService;
import com.hp.workpath.api.supplies.Supplieslet;

public class SuppliesAdapter {

    private static final String TAG = Supplieslet.TAG + "/A";

    public static boolean isSupported(IDeviceSuppliesService deviceSuppliesService) {
        return deviceSuppliesService.isSupported();
    }

    public static Identity getIdentity(IDeviceSuppliesService deviceSuppliesService) {
        return deviceSuppliesService.getIdentity();
    }

    public static SuppliesInfo getSuppliesInfo(IDeviceSuppliesService deviceSuppliesService, String packageName) {
        return deviceSuppliesService.getSuppliesInfo(packageName);
    }
}
