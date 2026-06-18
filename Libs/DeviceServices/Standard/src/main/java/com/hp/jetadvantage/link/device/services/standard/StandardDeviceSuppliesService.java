package com.hp.jetadvantage.link.device.services.standard;

import com.hp.ext.clients.device.DeviceServiceClientImpl;
import com.hp.ext.clients.supplies.SuppliesServiceClientImpl;
import com.hp.ext.service.device.Identity;
import com.hp.ext.service.supplies.Capabilities;
import com.hp.ext.service.supplies.SuppliesAgentRegistrationRecord;
import com.hp.ext.service.supplies.SuppliesAgents;
import com.hp.ext.service.supplies.SuppliesInfo;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceSuppliesService;
import com.hp.jetadvantage.link.device.services.standard.common.DeviceManagementService;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceService;

public class StandardDeviceSuppliesService extends StandardDeviceService implements IDeviceSuppliesService {

    private static final String TYPE_GUN = "com.hp.ext.service.supplies.version.1";

    public StandardDeviceSuppliesService() {
        super(new SuppliesAgentRegistrationRecord().getTypeGUN());
    }

    public StandardDeviceSuppliesService(DeviceManagementService deviceManagementService) {
        super(deviceManagementService);
    }

    @Override
    public boolean isSupported() {
        E2call<Capabilities> e2call = () -> {
            SuppliesServiceClientImpl suppliesServiceClient = new SuppliesServiceClientImpl(getHttpClient(), getDeviceIPAddress(), getDiscoveryTree());
            return suppliesServiceClient.capabilities().getAsync().get();
        };
        Capabilities capabilities = perform(e2call);
        return capabilities != null && TYPE_GUN.equals(capabilities.getServiceGun());
    }

    @Override
    public Identity getIdentity() {
        E2call<Identity> e2call = () -> {
            DeviceServiceClientImpl deviceServiceClient = new DeviceServiceClientImpl(getHttpClient(), getDeviceIPAddress(), getDiscoveryTree());
            return deviceServiceClient.identity().getAsync().get();
        };
        return perform(e2call);
    }

    @Override
    public SuppliesInfo getSuppliesInfo(String packageName) {
        E2callUniParam<SuppliesInfo, String> call = (String id) -> {
            SuppliesServiceClientImpl suppliesServiceClient = new SuppliesServiceClientImpl(getHttpClient(), getDeviceIPAddress(), getDiscoveryTree());
            return suppliesServiceClient.suppliesAgents().getMember(id).suppliesInfo().getAsync(getSolutionToken(packageName)).get();
        };
        return perform(call, getAgentId(packageName));
    }

    // for testing purposes
    public SuppliesAgents getSuppliesAgent(String packageName) {
        E2call<SuppliesAgents> e2call = () -> {
            SuppliesServiceClientImpl suppliesServiceClient = new SuppliesServiceClientImpl(getHttpClient(), getDeviceIPAddress(), getDiscoveryTree());
            return suppliesServiceClient.suppliesAgents().getAsync(getSolutionToken(packageName)).get();
        };
        return perform(e2call);
    }
}
