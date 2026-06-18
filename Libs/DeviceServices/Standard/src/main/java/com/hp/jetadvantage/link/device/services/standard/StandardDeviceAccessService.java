package com.hp.jetadvantage.link.device.services.standard;

import android.util.Log;

import com.hp.ext.clients.security.SecurityServiceClient;
import com.hp.ext.clients.security.SecurityServiceClientImpl;
import com.hp.ext.service.security.Capabilities;
import com.hp.ext.service.security.ResolveSecurityExpressionRequest;
import com.hp.ext.service.security.SecurityAgentRegistrationRecord;
import com.hp.ext.service.security.SecurityAgent_ResolveSecurityExpression;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceAccessService;
import com.hp.jetadvantage.link.device.services.standard.common.Constants;
import com.hp.jetadvantage.link.device.services.standard.common.DeviceManagementService;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceService;
import com.hp.ws.cdm.security.AuthenticationAgents;

public class StandardDeviceAccessService extends StandardDeviceService implements IDeviceAccessService {
    public static final String E2SERVICE_SECURITY_CANONICAL_GUN = "com.hp.ext.service.security.version.1";

    private static final String TAG = Constants.TAG + "/Access";

    public StandardDeviceAccessService() {
        super(new SecurityAgentRegistrationRecord().getTypeGUN());
    }

    public StandardDeviceAccessService(DeviceManagementService deviceManagementService) {
        super(deviceManagementService);
    }

    @Override
    public boolean isSupported() {
        E2call<Capabilities> call = () -> {
            SecurityServiceClient client = new SecurityServiceClientImpl(getHttpClient(), getDeviceIPAddress(), getDiscoveryTree());
            return client.capabilities().getAsync().get();
        };

        try {
            Capabilities e2SecurityCap = perform(call);
            String serviceGun = e2SecurityCap.getServiceGun();
            if (serviceGun != null && serviceGun.equalsIgnoreCase(E2SERVICE_SECURITY_CANONICAL_GUN)) {
                return true;
            }
            Log.i(TAG, "isSupported : serviceGun not match :" + serviceGun);
            return false;
        } catch (Exception e) {
            Log.e(TAG, "isSupported : Exception");
            Log.e(TAG, Log.getStackTraceString(e));
            return false;
        }
    }

    @Override
    public SecurityAgent_ResolveSecurityExpression getCurrentPrincipal(String packageName, ResolveSecurityExpressionRequest expression) {
        E2call<SecurityAgent_ResolveSecurityExpression> call = () -> {
            SecurityServiceClient client = new SecurityServiceClientImpl(getHttpClient(), getDeviceIPAddress(), getDiscoveryTree());
            /// Execute the POST operation
            return client.securityAgents().getMember(getAgentId(packageName)).resolveSecurityExpression().executeAsync(getSolutionToken(packageName), expression).get();
        };
        return perform(call);
    }

    @Override
    public AuthenticationAgents getAuthenticationAgents() {
        CdmCall call = () -> getCDMClient().sendGetRequest(CDMUrl.AUTHENTICATION_AGENTS);
        return perform(call, AuthenticationAgents.class);
    }

    public static final class CDMUrl {
        public static final String AUTHENTICATION_AGENTS = "/cdm/security/v1/authenticationAgents";
    }
}
