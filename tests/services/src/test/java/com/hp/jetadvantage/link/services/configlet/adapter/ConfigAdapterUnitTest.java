package com.hp.jetadvantage.link.services.configlet.adapter;

import static com.hp.jetadvantage.link.api.Result.KEY_CODE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.os.Bundle;

import com.hp.ext.service.solutionManager.Configuration;
import com.hp.ext.service.solutionManager.Configuration_Modify;
import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceSolutionManager;
import com.hp.jetadvantage.link.device.services.standard.common.Constants;
import com.hp.jetadvantage.link.services.common.exception.SdkInvalidParamException;
import com.hp.jetadvantage.link.services.common.exception.SdkServiceErrorException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@RunWith(MockitoJUnitRunner.class)
public class ConfigAdapterUnitTest {
    @Mock(lenient = true)
    private IDeviceSolutionManager mockSolutionManager;

    @Mock
    private Bundle mockBundle;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void GivenConfigAdapter_WhenGetConfigDataCalled_ThenInitializeConfigMetaAndResultOK() throws Exception {
        String packageName = Constants.TEST_PACKAGE_NAME;
        Configuration configBeforeInit = new Configuration();
        configBeforeInit.setMimeType("application/octet-stream");
        Configuration configAfterInit = new Configuration();
        configAfterInit.setMimeType("application/json");

        //define the behavior of the mock objects
        when(mockSolutionManager.getConfiguration(eq(packageName))).thenReturn(configBeforeInit);
        when(mockSolutionManager.modifyConfiguration(eq(packageName), any())).thenReturn(configAfterInit,
                configAfterInit);
        when(mockSolutionManager.getConfigurationData(eq(packageName))).thenReturn(null);

        //Invoke the method under test
        Bundle result = ConfigAdapter.getConfigData(mockSolutionManager, mockBundle, packageName);

        //Verify the results
        verify(mockBundle).putInt(KEY_CODE, Result.RESULT_OK);
        verify(mockBundle).putString(Result.KEY_RESULT, "{}");

        ArgumentCaptor<Configuration_Modify> captor = ArgumentCaptor.forClass(Configuration_Modify.class);
        verify(mockSolutionManager).modifyConfiguration(eq(packageName), captor.capture());
        Configuration_Modify capturedParam = captor.getValue();
        assertEquals("application/json", capturedParam.getMimeType());
    }

    @Test
    public void GivenConfigAdapter_WhenGetConfigDataCalledAndConfigMetaAlreadyInitialized_ThenResultOK() throws Exception {
        String packageName = Constants.TEST_PACKAGE_NAME;
        String configData = "{\"key\":\"value\"}";
        Configuration configMeta = new Configuration();
        configMeta.setMimeType("application/json");

        //define the behavior of the mock objects
        when(mockSolutionManager.getConfiguration(eq(packageName))).thenReturn(configMeta);
        when(mockSolutionManager.modifyConfiguration(eq(packageName), any())).thenReturn(configMeta);
        when(mockSolutionManager.getConfigurationData(eq(packageName))).thenReturn(configData);

        //Invoke the method under test
        ConfigAdapter.getConfigData(mockSolutionManager, mockBundle, packageName);

        //Verify the results
        verify(mockBundle).putInt(KEY_CODE, Result.RESULT_OK);
        verify(mockBundle).putString(Result.KEY_RESULT, configData);
        verify(mockSolutionManager, never()).modifyConfiguration(eq(packageName), any());
    }

    @Test
    public void GivenConfigAdapter_WhenGetConfigDataCalledWithNullDeviceSolutionManager_ThenThrowSdkException() {
        assertThrows(SdkServiceErrorException.class, () -> {
            ConfigAdapter.getConfigData(null, mockBundle, Constants.TEST_PACKAGE_NAME);
        });
    }

    @Test
    public void GivenConfigAdapter_WhenGetConfigDataCalledAndDeviceReturnsEmpty_ThenResultShouldBeEmptyJson() throws Exception {
        String packageName = Constants.TEST_PACKAGE_NAME;
        Configuration configAfterInit = new Configuration();
        configAfterInit.setMimeType("application/json");

        //define the behavior of the mock objects
        when(mockSolutionManager.getConfiguration(eq(packageName))).thenReturn(configAfterInit);
        when(mockSolutionManager.getConfigurationData(eq(packageName))).thenReturn("");

        //Invoke the method under test
        Bundle result = ConfigAdapter.getConfigData(mockSolutionManager, mockBundle, packageName);

        //Verify the results
        verify(mockBundle).putInt(KEY_CODE, Result.RESULT_OK);
        verify(mockBundle).putString(Result.KEY_RESULT, "{}");
    }

    @Test
    public void GivenConfigAdapter_WhenSetConfigDataCalled_ThenResultOk() throws Exception {
        String packageName = Constants.TEST_PACKAGE_NAME;
        String configData = "{\"key\":\"value\"}";

        Configuration configBeforeInit = new Configuration();
        configBeforeInit.setMimeType("application/octet-stream");
        Configuration configAfterInit = new Configuration();
        configAfterInit.setMimeType("application/json");

        //define the behavior of the mock objects
        when(mockSolutionManager.getConfiguration(eq(packageName))).thenReturn(configBeforeInit);
        when(mockSolutionManager.modifyConfiguration(eq(packageName), any())).thenReturn(configAfterInit,
                configAfterInit);

        //Invoke the method under test
        Bundle result = ConfigAdapter.setConfigData(mockSolutionManager, mockBundle, packageName, configData);

        //Verify the results
        verify(mockBundle).putInt(KEY_CODE, Result.RESULT_OK);
        verify(mockSolutionManager).replaceConfigurationData(eq(packageName), any(ByteArrayInputStream.class));

        ArgumentCaptor<ByteArrayInputStream> captor = ArgumentCaptor.forClass(ByteArrayInputStream.class);
        verify(mockSolutionManager).replaceConfigurationData(eq(packageName), captor.capture());
        ByteArrayInputStream capturedParam = captor.getValue();

        // Read the input stream from capturedParam
        BufferedReader reader = new BufferedReader(new InputStreamReader(capturedParam, StandardCharsets.UTF_8));
        StringBuilder capturedData = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            capturedData.append(line);
        }

        // Verify the input stream matches the configData string
        assertEquals("configData", configData, capturedData.toString());
    }

    @Test
    public void GivenConfigAdapter_WhenSetConfigDataCalledAndConfigMetaAlreadyInitialized_ThenResultOk() throws Exception {
        String packageName = Constants.TEST_PACKAGE_NAME;
        String configData = "{\"key\":\"value\"}";

        Configuration configAfterInit = new Configuration();
        configAfterInit.setMimeType("application/json");

        //define the behavior of the mock objects
        when(mockSolutionManager.getConfiguration(eq(packageName))).thenReturn(configAfterInit);

        //Invoke the method under test
        Bundle result = ConfigAdapter.setConfigData(mockSolutionManager, mockBundle, packageName, configData);

        //Verify the results
        verify(mockBundle).putInt(KEY_CODE, Result.RESULT_OK);
        verify(mockSolutionManager).replaceConfigurationData(eq(packageName), any(ByteArrayInputStream.class));

        ArgumentCaptor<ByteArrayInputStream> captor = ArgumentCaptor.forClass(ByteArrayInputStream.class);
        verify(mockSolutionManager).replaceConfigurationData(eq(packageName), captor.capture());
        ByteArrayInputStream capturedParam = captor.getValue();

        // Read the input stream from capturedParam
        BufferedReader reader = new BufferedReader(new InputStreamReader(capturedParam, StandardCharsets.UTF_8));
        StringBuilder capturedData = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            capturedData.append(line);
        }

        // Verify the input stream matches the configData string
        assertEquals("configData", configData, capturedData.toString());
    }

    @Test
    public void GivenConfigAdapter_WhenSetConfigDataCalledWithInvalidJsonData_ThenThrowSdkException() throws Exception {
        String packageName = Constants.TEST_PACKAGE_NAME;
        String invalidConfigData = "{\"key\":\"value";

        Configuration configAfterInit = new Configuration();
        configAfterInit.setMimeType("application/json");

        //define the behavior of the mock objects
        when(mockSolutionManager.getConfiguration(eq(packageName))).thenReturn(configAfterInit);

        //Invoke the method under test
        assertThrows(SdkInvalidParamException.class, () -> {
            ConfigAdapter.setConfigData(mockSolutionManager, mockBundle, packageName, invalidConfigData);
        });
    }

    @Test
    public void GivenConfigAdapter_WhenSetConfigDataCalledWithNullDeviceSolutionManager_ThenThrowSdkException() {
        assertThrows(SdkServiceErrorException.class, () -> {
            ConfigAdapter.setConfigData(null, mockBundle, Constants.TEST_PACKAGE_NAME, "{}");
        });
    }

    @Test
    public void GivenConfigAdapter_WhenSetConfigDataCalledWithNullData_ThenThrowSdkException() throws Exception {
        assertThrows(SdkInvalidParamException.class, () -> {
            ConfigAdapter.setConfigData(mockSolutionManager, mockBundle, Constants.TEST_PACKAGE_NAME, null);
        });
    }

    @Test
    public void GivenConfigAdapter_WhenSetConfigDataCalledWithEmptyData_ThenThrowSdkException() throws Exception {
        assertThrows(SdkInvalidParamException.class, () -> {
            ConfigAdapter.setConfigData(mockSolutionManager, mockBundle, Constants.TEST_PACKAGE_NAME, "");
        });
    }
}
