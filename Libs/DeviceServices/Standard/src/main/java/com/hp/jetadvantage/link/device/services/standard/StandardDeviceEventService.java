/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */

package com.hp.jetadvantage.link.device.services.standard;

import com.hp.jetadvantage.link.device.services.interfaces.ICdmCallback;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceEventService;
import com.hp.jetadvantage.link.device.services.standard.common.DeviceManagementService;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceService;
import com.hp.ws.cdm.alert.Alerts;
import com.hp.ws.cdm.commonglossary.Constraints;

public class StandardDeviceEventService extends StandardDeviceService implements IDeviceEventService {

    public static final String SUBSCRIPTION_ID = "StandardDeviceEventCallback-001";

    public StandardDeviceEventService() {
        super();
    }

    public StandardDeviceEventService(DeviceManagementService deviceManagementService) {
        super(deviceManagementService);
    }

    @Override
    public Alerts getDeviceEvents() {
        CdmCall call = () -> getCDMClient().sendGetRequest(Url.CDM_ALERTS);
        return perform(call, Alerts.class);
    }

    @Override
    public boolean isSupported() {
        CdmCall call = () -> getCDMClient().sendGetRequest(Url.CDM_DEVICE_CAPABILITIES);
        Constraints response = perform(call, Constraints.class);
        return response != null && response.getValidators() != null;
    }

    @Override
    public void addCallback(ICdmCallback callback) {
        StandardDeviceSubscriptionService.addCallback(SUBSCRIPTION_ID, callback);
    }

    public static final class Url {
        public static final String CDM_DEVICE_CAPABILITIES = "/cdm/alert/v1/capabilities";
        public static final String CDM_ALERTS = "/cdm/alert/v1/alerts";
    }

    public static final class Gun {
        public static final String GUN_ALERTS = "com.hp.cdm.service.alert.version.1.resource.alert";
    }
}
