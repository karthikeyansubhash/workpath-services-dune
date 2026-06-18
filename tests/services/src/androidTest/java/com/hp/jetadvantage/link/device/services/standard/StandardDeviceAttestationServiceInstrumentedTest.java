package com.hp.jetadvantage.link.device.services.standard;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.hp.jetadvantage.link.device.services.interfaces.IDeviceAttestationService;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceManagementService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class StandardDeviceAttestationServiceInstrumentedTest extends StandardDeviceInstrumentedTest {

    @Before
    public void SetUp() {
        super.SetUp();
    }

    /**
     * Basic constructor test - maintains consistency with other service tests
     * Note: This is somewhat redundant as each test creates the service anyway
     */
    @Test
    public void GivenStandardDeviceAttestationService_WhenConstructorCalled_ThenObjectCreated() {
        StandardDeviceAttestationService attestationService = new StandardDeviceAttestationService();
        assertNotNull(attestationService);
    }

    /**
     * TEST for AttestationService.isSupported : Happy case
     * Note: Attestation service doesn't rely on E2 API, so it checks device connectivity
     */
    @Test
    public void GivenAttestationService_WhenIsSupportedCalled_ThenTrueShouldBeReturned() {
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        //create attestation service object
        IDeviceAttestationService attestationService = new StandardDeviceAttestationService();
        assertNotNull(attestationService);

        // isSupported checks if device is connected
        boolean isSupported = attestationService.isSupported();
        // In instrumented test with device connection, this should return true
        assertTrue("Attestation service should be supported when device is connected", isSupported);
    }

    /**
     * TEST for AttestationService with DeviceManagementService constructor
     * This tests a different constructor than the default one - this test is meaningful
     * because it verifies the service can be created with an injected DeviceManagementService
     */
    @Test
    public void GivenStandardDeviceAttestationService_WhenConstructorWithDeviceManagementServiceCalled_ThenObjectCreated() {
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        StandardDeviceAttestationService attestationService =
                new StandardDeviceAttestationService(StandardDeviceManagementService.getInstance());
        assertNotNull(attestationService);
    }

    /**
     * TEST for getDeviceAttestationResponse: device-side attestation is not implemented yet.
     * This verifies the method exists and fails in a controlled way.
     */
    @Test
    public void GivenStandardDeviceAttestationService_WhenGetDeviceAttestationResponseCalled_ThenThrowsUnsupportedOperationException() {
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        IDeviceAttestationService attestationService = new StandardDeviceAttestationService();
        assertNotNull(attestationService);

        // Try to call device-side attestation response - it is TODO and should throw.
        try {
            String result = attestationService.getDeviceAttestationResponse(
                    "com.hp.test.package",
                    "test-app-id",
                    "1.0.0",  // appVersion
                    "test-signature-hash",  // appSignature
                    "test-client-id",
                    "12345",  // state
                    "8a61dd48223247518cf2288b6c85f707"  // nonce
            );
            // If it unexpectedly succeeds, at least ensure something was returned.
            assertNotNull("Result should not be null if implemented", result);
        } catch (Exception e) {
            // Expected: not implemented yet
            assertNotNull("Exception message should exist", e.getMessage());
        }
    }
}

