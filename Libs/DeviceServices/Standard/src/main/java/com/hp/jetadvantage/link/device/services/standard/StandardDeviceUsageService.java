package com.hp.jetadvantage.link.device.services.standard;

import com.hp.ext.clients.deviceusage.DeviceUsageServiceClientImpl;
import com.hp.ext.service.deviceUsage.DeviceUsageAgentRegistrationRecord;
import com.hp.ext.service.deviceUsage.DeviceUsageAgents;
import com.hp.ext.service.deviceUsage.LifetimeCounters;
import com.hp.ext.service.deviceUsage.Capabilities;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceUsageService;
import com.hp.jetadvantage.link.device.services.standard.common.DeviceManagementService;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceService;

public class StandardDeviceUsageService extends StandardDeviceService implements IDeviceUsageService {

    private static final String TYPE_GUN = "com.hp.ext.service.deviceUsage.version.1";

    public StandardDeviceUsageService() {
        super(new DeviceUsageAgentRegistrationRecord().getTypeGUN());
    }

    public StandardDeviceUsageService(DeviceManagementService deviceManagementService) {
        super(deviceManagementService);
    }

    public boolean isSupported() {
        E2call<Capabilities> e2call = () -> {
            DeviceUsageServiceClientImpl deviceUsageServiceClient = new DeviceUsageServiceClientImpl(getHttpClient(), getDeviceIPAddress(), getDiscoveryTree());
            return deviceUsageServiceClient.capabilities().getAsync().get();
        };
        Capabilities capabilities = perform(e2call);
        return capabilities != null && TYPE_GUN.equals(capabilities.getServiceGun());
    }
    @Override
    public LifetimeCounters getLifetimeCounters(String packageName) {
        E2callUniParam<LifetimeCounters, String> call = (String id) -> {
            DeviceUsageServiceClientImpl deviceUsageServiceClient = new DeviceUsageServiceClientImpl(getHttpClient(), getDeviceIPAddress(), getDiscoveryTree());
            return deviceUsageServiceClient.deviceUsageAgents().getMember(id).lifetimeCounters().getAsync(getSolutionToken(packageName)).get();
        };
        return perform(call, getAgentId(packageName));
    }

    // for testing purposes
    public DeviceUsageAgents getDeviceUsageAgent(String packageName) {
        E2call<DeviceUsageAgents> e2call = () -> {
            DeviceUsageServiceClientImpl deviceUsageServiceClient = new DeviceUsageServiceClientImpl(getHttpClient(), getDeviceIPAddress(), getDiscoveryTree());
            return deviceUsageServiceClient.deviceUsageAgents().getAsync(getSolutionToken(packageName)).get();
        };
        return perform(e2call);
    }
}
