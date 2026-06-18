package com.hp.jetadvantage.link.device.services.standard;

import android.text.TextUtils;
import android.util.Log;

import com.hp.jetadvantage.link.device.services.interfaces.IDeviceAttestationService;
import com.hp.jetadvantage.link.device.services.standard.common.Constants;
import com.hp.ws.e2workpathInterop.BootMode;
import com.hp.ws.e2workpathInterop.DeviceInfoResponse;
import com.hp.jetadvantage.link.device.services.standard.common.DeviceManagementService;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceService;
import com.hp.jetadvantage.link.device.services.standard.common.StandardJsonParser;


/**
 * Standard implementation of Device Attestation Service
 * Handles attestation token requests via HTTPS
 * Uses CDM endpoint: POST /cdm/e2WorkpathInterop/v1/appAttestation
 */
public class StandardDeviceAttestationService extends StandardDeviceService implements IDeviceAttestationService {

    private static final String TAG = Constants.TAG + "/Attestation";

    // No specific E2 service GUN for attestation as it uses external HP API
    public static final String SERVICE_TYPE = "attestation";

    private static final String CDM_APP_ATTESTATION_URL = "/cdm/e2WorkpathInterop/v1/appAttestation";

    // -------------------------------------------------------------------------
    // Inner classes for JSON serialization
    // -------------------------------------------------------------------------

    private static class AppAttestationRequest {
        InputData inputData;

        AppAttestationRequest(InputData inputData) {
            this.inputData = inputData;
        }
    }

    private static class InputData {
        String responseType;
        String clientId;
        String appId;
        String appPackageName;
        String appVersion;
        String appSignature;
        String state;
        String nonce;
    }

    private static class AppAttestationResponse {
        String printerMessage;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public StandardDeviceAttestationService() {
        super(SERVICE_TYPE);
    }

    public StandardDeviceAttestationService(DeviceManagementService deviceManagementService) {
        super(deviceManagementService);
    }

    // -------------------------------------------------------------------------
    // IDeviceAttestationService
    // -------------------------------------------------------------------------

    @Override
    public boolean isSupported() {
        // Attestation service doesn't have specific E2 API capability check
        // It's available if the device supports general connectivity
        try {
            // Simple check - if we can get device IP, service is available
            String deviceIp = getDeviceIPAddress();
            return !TextUtils.isEmpty(deviceIp);
        } catch (Exception e) {
            Log.e(TAG, "isSupported : Exception", e);
            return false;
        }
    }

    private static final String CDM_DEVICE_INFO_URL = "/cdm/e2WorkpathInterop/v1/deviceInfo";

    @Override
    public IDeviceAttestationService.DeviceInfoResult getDeviceInfo() {
        try {
            CdmCall call = () -> getCDMClient().sendGetRequest(CDM_DEVICE_INFO_URL);
            DeviceInfoResponse response = perform(call, DeviceInfoResponse.class);
            if (response == null) {
                Log.w(TAG, "getDeviceInfo: response is null");
                return null;
            }
            boolean isSealed = response.bootMode != null && Boolean.TRUE.equals(response.bootMode.isSealed);
            boolean isSecure = response.bootMode != null && Boolean.TRUE.equals(response.bootMode.isSecure);
            Log.d(TAG, "getDeviceInfo: isSealed=" + isSealed + ", isSecure=" + isSecure + ", cloudStack=" + response.cloudStack);
            return new IDeviceAttestationService.DeviceInfoResult(isSealed, isSecure, response.cloudStack);
        } catch (Exception e) {
            Log.e(TAG, "getDeviceInfo: failed", e);
            return null;
        }
    }

    /**
     * Requests a device-side attestation response (printerMessage in JWT format)
     * by posting to the CDM AppAttestation resource.
     *
     * The returned printerMessage is a JWT signed by the printer's TPM private key
     * and must be forwarded to the Attestation Service for validation.
     *
     * @param packageName  app package name (appPackageName)
     * @param appId        unique identifier of the application
     * @param appVersion   version of the application
     * @param appSignature signature of the application
     * @param clientId     client identifier issued by the authorization server
     * @param state        opaque value used by the client to maintain state between the request and callback
     * @param nonce        string value used to associate a client session with an ID token, and to mitigate replay attacks
     * @return printerMessage JWT string
     */
    @Override
    public String getDeviceAttestationResponse(String packageName, String appId, String appVersion,
                                               String appSignature, String clientId,
                                               String state, String nonce) {
        Log.d(TAG, "getDeviceAttestationResponse : packageName=" + packageName
                + ", appId=" + appId + ", appVersion=" + appVersion + ", clientId=" + clientId
                + ", state=" + state + ", nonce=" + nonce);

        // Build inputData
        InputData inputData = new InputData();
        inputData.responseType = "code";
        inputData.clientId = clientId;
        inputData.appId = appId;
        inputData.appPackageName = packageName;
        inputData.appVersion = appVersion;
        inputData.appSignature = appSignature;
        inputData.state = state;
        inputData.nonce = nonce;

        String requestJson = StandardJsonParser.INSTANCE.toJson(new AppAttestationRequest(inputData));
        Log.d(TAG, "getDeviceAttestationResponse : requestJson=" + requestJson);

        CdmCall call = () -> getCDMClient().sendPostRequest(CDM_APP_ATTESTATION_URL, requestJson);
        AppAttestationResponse response = perform(call, AppAttestationResponse.class);

        if (response == null || TextUtils.isEmpty(response.printerMessage)) {
            Log.e(TAG, "getDeviceAttestationResponse : printerMessage is empty or null");
            throw new RuntimeException("getDeviceAttestationResponse failed: printerMessage is empty");
        }

        Log.d(TAG, "getDeviceAttestationResponse : success");
        return response.printerMessage;
    }
}

