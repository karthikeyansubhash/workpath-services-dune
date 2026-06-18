package com.hp.jetadvantage.link.device.services.standard;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.hp.jetadvantage.link.device.services.interfaces.ICdmCallback;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceManagementService;
import com.hp.ws.cdm.pubsub.Message;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class StandardDeviceSubscriptionServiceInstrumentedTest extends StandardDeviceInstrumentedTest {
    private ICdmCallback testCdmCallback = new ICdmCallback() {
        @Override
        public void onChangeEvent(List<Message> reports) {

        }
    };

    @Before
    public void SetUp() {
        super.SetUp();
    }

    @Test
    public void GivenStandardDeviceSubscriptionService_WhenConstructorCalled_ThenObjectCreated() {
        StandardDeviceSubscriptionService subService = new StandardDeviceSubscriptionService();
        assertNotNull(subService);
    }

    //@Test
    public void GivenStandardDeviceSubscriptionService_WhenSubscriptionCalled_ThenSubscriptionIdShouldBeReturned() {

        //1. initialize device management service : try to connect to the device simulator
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        //2. create subscription service
        StandardDeviceSubscriptionService subService = new StandardDeviceSubscriptionService();
        assertNotNull(subService);

        String[] gun = {"com.hp.cdm.service.clock.version.1.resource.configuration"};
        String subscriptionId = subService.Subscribe(gun, testCdmCallback);

        assertTrue(subscriptionId.length() > 0 );

        //clean up
        subService.Unsubscribe(subscriptionId);
    }

    //@Test
    public void GivenStandardDeviceSubscriptionService_WhenUnSubscriptionCalled_ThenUnSubscribedSuccessfully() {

        //1. initialize device management service : try to connect to the device simulator
        StandardDeviceManagementService.getInstance().initialize(deviceIp, accessToken);

        //2. create subscription service
        StandardDeviceSubscriptionService subService = new StandardDeviceSubscriptionService();
        assertNotNull(subService);

        //3. subscribe cdm
        String[] gun = {"com.hp.cdm.service.clock.version.1.resource.configuration"};
        String subscriptionId = subService.Subscribe(gun, testCdmCallback);

        assertTrue(subscriptionId.length() > 0 );

        //4. delete the subscription
        boolean result = subService.Unsubscribe(subscriptionId);
        assertTrue(result);
    }

}
