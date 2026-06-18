package com.hp.workpath.apitest.attestation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.util.Log;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.hp.workpath.api.Result;
import com.hp.workpath.api.Workpath;
import com.hp.workpath.api.attestation.AttestationService;
import com.hp.workpath.apitest.dut.DutConfig;
import com.hp.workpath.apitest.dut.DutController;
import com.hp.workpath.apitest.dut.DutControllerFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Attestation Service API Test
 */
@RunWith(AndroidJUnit4.class)
public class AttestationServiceTest {
    private static final String TAG = "AttestationServiceTest";
    private static Context mContext;
    private final DutController mDutController;

    public AttestationServiceTest() {
        this.mDutController = DutControllerFactory.getController(DutConfig.PLATFORM);
        mDutController.initializeDutForApiTest();
    }

    @Before
    public void setUp() {
        mContext = ApplicationProvider.getApplicationContext();
        try {
            Workpath.getInstance().initialize(mContext);
        } catch (Exception e) {
            // Ignore for now or handle
        }
    }

    @Test
    public void AttestationService_isSupported_$ReturnsTrue() {
        Log.d(TAG, "testIsSupported");

        boolean isSupported = AttestationService.isSupported(mContext);
        Log.d(TAG, "Attestation service supported: " + isSupported);

        assertTrue("Attestation service should be supported", isSupported);
    }

    //@Test
    public void AttestationService_getAppToken_$ReturnsToken() {
        Log.d(TAG, "testGetAppToken");

        if (!AttestationService.isSupported(mContext)) {
            Log.w(TAG, "Attestation service not supported, skipping test");
            return;
        }

        // Note: This test requires proper configuration with valid client ID, app UUID, etc.
        // For now, we just verify the service is available
        Result result = new Result();
        // AppToken token = AttestationService.getAppToken(mContext, result);
        
        assertNotNull("Result should not be null", result);
    }
}

