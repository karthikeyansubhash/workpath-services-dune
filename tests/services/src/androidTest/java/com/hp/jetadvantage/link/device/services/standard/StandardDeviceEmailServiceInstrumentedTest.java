package com.hp.jetadvantage.link.device.services.standard;

import static org.junit.Assert.assertNotNull;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.hp.jetadvantage.link.device.services.interfaces.IDeviceEmailService;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceManagementService;
import com.hp.jetadvantage.link.device.services.types.EmailSettingsData;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class StandardDeviceEmailServiceInstrumentedTest extends StandardDeviceInstrumentedTest {

    @Before
    public void SetUp() {
        super.SetUp();
    }

    @Test
    public void GivenStandardDeviceEmailService_WhenConstructorCalled_ThenObjectCreated() {
        StandardDeviceEmailService deviceEmailService = new StandardDeviceEmailService();
        assertNotNull(deviceEmailService);
    }
    
    @Test
    public void GivenStandardDeviceEmailService_WhenDeviceManagementServiceInitiated_AndConstructorCalled_ThenObjectCreated() {

        //initialize device management service : try to connect to the device simulator
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        //create email service object
        StandardDeviceEmailService deviceEmailService = new StandardDeviceEmailService();
        assertNotNull(deviceEmailService);
    }

    @Test
    public void GivenStandardDeviceEmailService_WhenGetEmailSettingsCalled_ThenEmailSettingsDataShouldBeReturned() {

        //1. initialize device management service : try to connect to the device simulator
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        //2. create device email service object
        IDeviceEmailService deviceEmailService = new StandardDeviceEmailService();
        assertNotNull(deviceEmailService);

        //3. get email settings data (email & smtp)
        EmailSettingsData emailSettingsData = deviceEmailService.getEmailSettings();

        //4. verify received get email settings data
        assertNotNull(emailSettingsData);
    }
}
