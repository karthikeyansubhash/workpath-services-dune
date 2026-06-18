package com.hp.jetadvantage.link.services.attestationlet.adapter;

import android.util.Log;

import com.hp.jetadvantage.link.device.services.interfaces.IDeviceAttestationService;

/**
 * Adapter class to handle attestation service operations
 * Provides a bridge between AttestationLet and DeviceServices
 */
public class AttestationAdapter {
    private static final String TAG = "AttestationAdapter";

    /**
     * Check if attestation service is supported
     *
     * @param attestationService the attestation device service
     * @return true if supported
     */
    public static boolean isSupported(IDeviceAttestationService attestationService) {
        try {
            return attestationService.isSupported();
        } catch (Exception e) {
            Log.e(TAG, "isSupported failed", e);
            return false;
        }
    }

    /**
     * Get device-side attestation response.
     *
     * Provider is responsible for generating state/nonce and exchanging this response with cloud to obtain an app token.
     *
     * @param state opaque value used by the client to maintain state between the request and callback
     * @param nonce string value used to associate a client session with an ID token, and to mitigate replay attacks
     */
    public static String getDeviceAttestationResponse(IDeviceAttestationService attestationService,
                                                      String packageName,
                                                      String appId,
                                                      String appVersion,
                                                      String appSignature,
                                                      String clientId,
                                                      String state,
                                                      String nonce) {
        try {
            Log.d(TAG, "getDeviceAttestationResponse for appId: " + appId + ", clientId: " + clientId
                    + ", state=" + state + ", nonce=" + nonce);

            String result = attestationService.getDeviceAttestationResponse(packageName, appId, appVersion, appSignature, clientId, state, nonce);
            if (result == null) {
                throw new RuntimeException("Device attestation service returned null device response");
            }
            return result;
        } catch (Exception e) {
            Log.e(TAG, "getDeviceAttestationResponse failed", e);
            throw new RuntimeException("Failed to get device attestation response: " + e.getMessage(), e);
        }
    }
}

