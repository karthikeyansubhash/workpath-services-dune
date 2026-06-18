package com.hp.jetadvantage.link.services.configlet.provider;

import static com.hp.jetadvantage.link.api.Result.KEY_CODE;
import static com.hp.jetadvantage.link.services.configlet.provider.ConfigLetContentProvider.FREQUENT_MODIFIED_THRESHOLD_COUNT;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.os.Bundle;

import com.hp.ext.service.solutionManager.Configuration;
import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceSolutionManager;
import com.hp.jetadvantage.link.device.services.standard.common.Constants;
import com.hp.jetadvantage.link.services.common.exception.SdkException;
import com.hp.jetadvantage.link.services.common.exception.SdkServiceErrorException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ConfigLetContentProviderUnitTest {
    @Mock(lenient = true)
    private IDeviceSolutionManager mockSolutionManager;

    @Mock
    private Bundle mockBundle;

    private static String generateLargeString(int lengthInChars) {
        StringBuilder sb = new StringBuilder(lengthInChars);
        for (int i = 0; i < lengthInChars; i++) {
            sb.append('A');
        }
        return sb.toString();
    }

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void GivenConfigLetContentProvider_WhenIsModifiedFrequentlyCalled_ThenReturnTrue() {
        String appId = Constants.TEST_PACKAGE_NAME;

        // Simulate frequent modifications
        addModifications(appId, FREQUENT_MODIFIED_THRESHOLD_COUNT + 1);

        Boolean modifiedFrequently = ConfigLetContentProvider.isModifiedFrequently(appId, false);
        assertTrue("Modified without cleanup", modifiedFrequently);

        modifiedFrequently = ConfigLetContentProvider.isModifiedFrequently(appId, true);
        assertTrue("Modified with cleanup", modifiedFrequently);

        modifiedFrequently = ConfigLetContentProvider.isModifiedFrequently(appId, true);
        assertFalse("Modified after cleanup", modifiedFrequently);

        // Simulate frequent modifications again
        addModifications(appId, FREQUENT_MODIFIED_THRESHOLD_COUNT + 2);

        modifiedFrequently = ConfigLetContentProvider.isModifiedFrequently(appId, true);
        assertTrue("Modified with cleanup", modifiedFrequently);

        modifiedFrequently = ConfigLetContentProvider.isModifiedFrequently(appId, false);
        assertFalse("Modified after cleanup", modifiedFrequently);
    }

    @Test
    public void GivenConfigLetContentProvider_WhenIsModifiedFrequentlyCalled_ThenReturnFalse() {
        String appId = Constants.TEST_PACKAGE_NAME;

        Boolean modifiedFrequently = ConfigLetContentProvider.isModifiedFrequently(appId, false);
        assertFalse("check 1", modifiedFrequently);

        modifiedFrequently = ConfigLetContentProvider.isModifiedFrequently(appId, true);
        assertFalse("check 2", modifiedFrequently);

        // Simulate a modification
        addModifications(appId, 1);

        modifiedFrequently = ConfigLetContentProvider.isModifiedFrequently(appId, false);
        assertFalse("check 3", modifiedFrequently);

        modifiedFrequently = ConfigLetContentProvider.isModifiedFrequently(appId, true);
        assertFalse("check 4", modifiedFrequently);

        // Simulate modifications again
        addModifications(appId, 2);


        modifiedFrequently = ConfigLetContentProvider.isModifiedFrequently(appId, false);
        assertFalse("check 5", modifiedFrequently);

        modifiedFrequently = ConfigLetContentProvider.isModifiedFrequently(appId, true);
        assertFalse("check 6", modifiedFrequently);
    }

    @Test
    public void GivenConfigLetContentProvider_WhenIsSupportedCalled_ThenReturnTrue() {
        ConfigLetContentProvider configLetContentProvider = new ConfigLetContentProvider();
        boolean supported = configLetContentProvider.isSupported();
        assertTrue("Supported", supported);
    }

    @Test
    public void GivenConfigLetContentProvider_WhenGetConfigCalled_ThenReturnBundle() throws SdkException {
        String packageName = Constants.TEST_PACKAGE_NAME;
        String configData = "{\"key\":\"value\"}";
        Configuration configMeta = new Configuration();
        configMeta.setMimeType("application/json");

        when(mockSolutionManager.getConfiguration(eq(packageName))).thenReturn(configMeta);
        when(mockSolutionManager.modifyConfiguration(eq(packageName), any())).thenReturn(configMeta);
        when(mockSolutionManager.getConfigurationData(eq(packageName))).thenReturn(configData);

        ConfigLetContentProvider configLetContentProvider = new ConfigLetContentProvider(mockSolutionManager);
        configLetContentProvider.getConfig(mockBundle, packageName);

        verify(mockBundle).putInt(KEY_CODE, Result.RESULT_OK);
        verify(mockBundle).putString(Result.KEY_RESULT, configData);
    }

    @Test
    public void GivenConfigLetContentProvider_WhenSetConfigCalled_ThenReturnTrue() {
        String packageName = Constants.TEST_PACKAGE_NAME;
        String configData = "{\"key\":\"" + generateLargeString(66561) + "\"}";

        ConfigLetContentProvider configLetContentProvider = new ConfigLetContentProvider(mockSolutionManager);
        assertThrows("Expected Exception for large data", SdkServiceErrorException.class, () -> {
            configLetContentProvider.setConfig(mockBundle, packageName, configData);
        });
    }

    private void addModifications(String pkg, int times) {
        ConfigLetContentProvider provider = new ConfigLetContentProvider();
        for (int i = 0; i < times; i++) {
            provider.updateLastModifiedTime(pkg);
        }
    }
}
