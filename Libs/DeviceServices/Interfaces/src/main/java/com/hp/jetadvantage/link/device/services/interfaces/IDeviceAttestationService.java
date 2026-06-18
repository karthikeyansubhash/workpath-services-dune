package com.hp.jetadvantage.link.device.services.interfaces;

/**
 * Interface for Device Attestation Service
 * Provides methods to interact with attestation related E2 APIs
 */
public interface IDeviceAttestationService extends IAbstractDeviceService {

    /**
     * Check if attestation service is supported on the device
     *
     * @return true if attestation service is supported
     */
    boolean isSupported();

    /**
     * Get device info containing bootMode (isSealed, isSecure) and cloudStack.
     * Used to determine execution mode and target cloud environment.
     *
     * @return {@link DeviceInfoResult} or null if unavailable
     */
    DeviceInfoResult getDeviceInfo();

    /**
     * Result holder for /cdm/e2WorkpathInterop/v1/deviceInfo response.
     */
    class DeviceInfoResult {
        public final boolean isSealed;
        public final boolean isSecure;
        public final String cloudStack;

        public DeviceInfoResult(boolean isSealed, boolean isSecure, String cloudStack) {
            this.isSealed = isSealed;
            this.isSecure = isSecure;
            this.cloudStack = cloudStack;
        }

        public boolean isProduction() {
            return isSealed && isSecure;
        }
    }

    /**
     * Get device-side attestation response (printerMessage in JWT format) via CDM AppAttestation resource.
     *
     * The returned printerMessage is signed by the printer's TPM private key
     * and must be forwarded to the Attestation Service for validation.
     *
     * @param packageName  app's package name (appPackageName)
     * @param appId        unique identifier of the application
     * @param appVersion   version of the application
     * @param appSignature signature of the application
     * @param clientId     client identifier issued by the authorization server
     * @param state        opaque value used by the client to maintain state between the request and callback
     * @param nonce        string value used to associate a client session with an ID token, and to mitigate replay attacks
     * @return printerMessage JWT string signed by TPM
     */
    String getDeviceAttestationResponse(String packageName, String appId, String appVersion,
                                        String appSignature, String clientId,
                                        String state, String nonce);
}

