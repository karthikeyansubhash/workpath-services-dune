package com.hp.jetadvantage.link.device.services.standard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.hp.jetadvantage.link.device.services.interfaces.IDeviceSettingsService;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceManagementService;
import com.hp.ws.cdm.network.PrintServices;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class StandardDeviceSettingsServiceInstrumentedTest extends StandardDeviceInstrumentedTest {

    @Before
    public void SetUp() {
        super.SetUp();
    }

    @Test
    public void GivenStandardDeviceSettingsService_WhenConstructorCalled_ThenObjectCreated() {
        StandardDeviceSettingsService deviceSettingsService = new StandardDeviceSettingsService();
        assertNotNull(deviceSettingsService);
    }

    @Test
    public void GivenStandardDeviceSettingsService_WhenDeviceManagementServiceInitiated_AndConstructorCalled_ThenObjectCreated() {

        //initialize device management service : try to connect to the device simulator
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        //create scan job service object
        StandardDeviceSettingsService deviceSettingsService = new StandardDeviceSettingsService();
        assertNotNull(deviceSettingsService);
    }

    /**
     * prerequisite : get print services configuration (Ipp, LpdPrint, Port9100, WsPrint)
     */
    @Test
    public void GivenStandardDeviceSettingsService_WhenGetPrintServicesCalled_ThenPrintServicesConfigurationShouldBeReturned() {

        //0. Setup dune simulator

        //1. initialize device management service : try to connect to the device simulator
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        //2. create device settings service object
        IDeviceSettingsService deviceSettingsService = new StandardDeviceSettingsService();
        assertNotNull(deviceSettingsService);

        //3. get print services configuration (Ipp, LpdPrint, Port9100, WsPrint)
        PrintServices printServices = deviceSettingsService.getPrintServices();

        //4. verify received get print services configurations
        assertNotNull(printServices);
    }

    /**
     * prerequisite : disable print services AirPrint, Ipp, LpdPrint, Port9100, WsPrint
     */
    @Test
    public void GivenStandardDeviceSettingsService_WhenDisablePrintServices_ThenPrintServicesShouldBeDisabled() {

        //1. initialize device management service : try to connect to the device simulator
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        //2. create device settings service object
        IDeviceSettingsService deviceSettingsService = new StandardDeviceSettingsService();
        assertNotNull(deviceSettingsService);

        //3. disable print services configuration (AirPrint, Ipp, LpdPrint, Port9100, WsPrint)
        boolean result = deviceSettingsService.disableNetworkPrintServices();
        assertTrue(result);

        try {
            Thread.sleep(3000);
        } catch (Exception e) {
        }
        //4. verify print services disabled
        PrintServices printServices = deviceSettingsService.getPrintServices();
        assertEquals("getAirPrint", "false", printServices.getAirPrint().getEnabled().toString());
        assertEquals("getIpp", "false", printServices.getIpp().getIpp().toString());
        assertEquals("getIppSecure", "false", printServices.getIpp().getIppSecure().toString());
        assertEquals("getLpdPrint", "false", printServices.getLpdPrint().getEnabled().toString());
        assertEquals("getPort9100", "false", printServices.getPort9100().getEnabled().toString());
        assertEquals("getWsPrint","false", printServices.getWsPrint().getEnabled().toString());

        //clean up - restore settings for other testing
        deviceSettingsService.enableNetworkPrintServices();
    }

    /**
     * prerequisite : enable print services AirPrint, Ipp, LpdPrint, Port9100, WsPrint
     */
    @Test
    public void GivenStandardDeviceSettingsService_WhenEnablePrintServices_ThenPrintServicesShouldBeEnabled() {

        //1. initialize device management service : try to connect to the device simulator
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        //2. create device settings service object
        IDeviceSettingsService deviceSettingsService = new StandardDeviceSettingsService();
        assertNotNull(deviceSettingsService);

        //3. enable print services configuration (AirPrint, Ipp, LpdPrint, Port9100, WsPrint)
        boolean result = deviceSettingsService.enableNetworkPrintServices();
        assertTrue(result);

        try {
            Thread.sleep(3000);
        } catch (Exception e) {
        }
        //4. verify print services enabled
        PrintServices printServices = deviceSettingsService.getPrintServices();
        assertEquals("getAirPrint", "true", printServices.getAirPrint().getEnabled().toString());
        assertEquals("getIpp", "true", printServices.getIpp().getIpp().toString());
        assertEquals("getIppSecure", "true", printServices.getIpp().getIppSecure().toString());
        assertEquals("getLpdPrint", "true", printServices.getLpdPrint().getEnabled().toString());
        assertEquals("getPort9100","true", printServices.getPort9100().getEnabled().toString());
        assertEquals("getWsPrint","true", printServices.getWsPrint().getEnabled().toString());
    }
}
