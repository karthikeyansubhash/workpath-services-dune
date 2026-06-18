package com.hp.jetadvantage.link.device.services.sim;

import android.util.Log;

import com.hp.jetadvantage.link.device.services.interfaces.IDeviceAttestationService;

/**
 * Simulation implementation of Device Attestation Service
 * Returns mock data for testing without actual HTTP calls
 */
public class SimDeviceAttestationService implements IDeviceAttestationService {

    private static final String TAG = "SimDeviceAttestationService";

    private boolean isSupported = true;
    private boolean shouldFail = false;

    // Simulated device info defaults: non-production (Development mode), staging cloud stack
    private boolean simIsSealed = false;
    private boolean simIsSecure = false;
    private String simCloudStack = "staging";

    /**
     * Default constructor - service is supported by default
     */
    public SimDeviceAttestationService() {
        Log.d(TAG, "SimDeviceAttestationService created");
    }

    @Override
    public boolean isUiContextAvailable(String packageName) {
        return true; // Always available in simulation
    }

    /**
     * Set whether the service should be reported as supported
     * @param supported true if supported
     */
    public void setSupported(boolean supported) {
        this.isSupported = supported;
        Log.d(TAG, "Set isSupported to: " + supported);
    }

    /**
     * Set whether the service should fail (for error testing)
     * @param shouldFail true to simulate failures
     */
    public void setShouldFail(boolean shouldFail) {
        this.shouldFail = shouldFail;
        Log.d(TAG, "Set shouldFail to: " + shouldFail);
    }

    @Override
    public boolean isSupported() {
        Log.d(TAG, "isSupported() called, returning: " + isSupported);
        return isSupported;
    }

    @Override
    public IDeviceAttestationService.DeviceInfoResult getDeviceInfo() {
        Log.d(TAG, "getDeviceInfo() called in sim: isSealed=" + simIsSealed
                + ", isSecure=" + simIsSecure + ", cloudStack=" + simCloudStack);
        return new IDeviceAttestationService.DeviceInfoResult(simIsSealed, simIsSecure, simCloudStack);
    }

    /**
     * Configure simulated device info for testing.
     * @param isSealed  simulated bootMode.isSealed value
     * @param isSecure  simulated bootMode.isSecure value
     * @param cloudStack simulated cloudStack value ("pie", "staging", "production")
     */
    public void setSimDeviceInfo(boolean isSealed, boolean isSecure, String cloudStack) {
        this.simIsSealed = isSealed;
        this.simIsSecure = isSecure;
        this.simCloudStack = cloudStack;
        Log.d(TAG, "setSimDeviceInfo: isSealed=" + isSealed + ", isSecure=" + isSecure + ", cloudStack=" + cloudStack);
    }

    @Override
    public String getDeviceAttestationResponse(String packageName, String appId, String appVersion,
                                               String appSignature, String clientId,
                                               String state, String nonce) {
        Log.d(TAG, "getDeviceAttestationResponse() called");
        Log.d(TAG, "  packageName: " + packageName);
        Log.d(TAG, "  appId: " + appId);
        Log.d(TAG, "  appVersion: " + appVersion);
        Log.d(TAG, "  appSignature: " + appSignature);
        Log.d(TAG, "  clientId: " + clientId);
        Log.d(TAG, "  state: " + state);
        Log.d(TAG, "  nonce: " + nonce);

        if (shouldFail) {
            Log.e(TAG, "Simulating failure as requested");
            throw new RuntimeException("Simulated attestation service failure");
        }

        // Sim path: never calls device internal APIs.
        // Return mock printerMessage (JWT-like string) for testing.
        String mockPrinterMessage = "mock_printer_message_code=mock_attestation_code_" + appId + "&state=" + state;
        Log.d(TAG, "Returning mock printerMessage: " + mockPrinterMessage);
        return mockPrinterMessage;
    }
}

