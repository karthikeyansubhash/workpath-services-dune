package com.hp.jetadvantage.link.device.services.standard;

import com.hp.ext.service.application.ApplicationAgentRegistrationRecord;
import com.hp.jetadvantage.link.device.services.clients.CDMResponse;
import com.hp.jetadvantage.link.device.services.clients.TestConnector;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceUISwitchService;
import com.hp.jetadvantage.link.device.services.standard.common.Constants;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceService;

import org.json.JSONException;
import org.json.JSONObject;

public class StandardDeviceUISwitchService extends StandardDeviceService implements IDeviceUISwitchService {
    private static final String TAG = Constants.TAG + "/UISwitch";

    public StandardDeviceUISwitchService() {
        super(new ApplicationAgentRegistrationRecord().getTypeGUN());
    }

    @Override
    public void switchToDevice() {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("workpathActionType", "closeDisplay");
            requestBody.put("gatewayType", "application");
            requestBody.put("applicationId", "");
            requestBody.put("solutionId", "");
        } catch (JSONException e) {
            throw new RuntimeException("JSONException: Invalid argument");
        }

        CdmCall call = () -> getCDMClient().sendPutRequest(InteropUrl.WORKPATH_GATEWAY, requestBody.toString());
        perform(call);
    }

    @Override
    public boolean launchAppFromDeviceHome(String packageName) {
        TestConnector testConn = new TestConnector();
        try {

            CDMResponse<String> response = testConn.getUdwClient(getDeviceIPAddress()).
                    initiateAppLaunch(getAgentId(packageName), getSolutionToken(packageName));

            return response != null && response.httpStatusCode == 200;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean closeAppFromDeviceHome(String packageName) {
        TestConnector testConn = new TestConnector();
        try {

            CDMResponse<String> response = testConn.getUdwClient(getDeviceIPAddress()).
                    closeCurrentApp(getUiContextToken(packageName));

            return response != null && response.httpStatusCode == 200;
        } catch (Exception e) {
            return false;
        }
    }

    public static final class InteropUrl {
        public static final String WORKPATH_GATEWAY = "/cdm/e2WorkpathInterop/v1/workpathGateway";
    }
}
