package com.hp.jetadvantage.link.device.services.standard.receivers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.hp.jetadvantage.link.device.services.standard.StandardDeviceInstrumentedTest;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceManagementService;

import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class DeviceReadyReceiverInstrumentedTest extends StandardDeviceInstrumentedTest {

    private void sendReadyBroadcast(DeviceReadyReceiver receiver, String ip, String token, boolean testMode) {
        //send target broadcast
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.hp.jetadvantage.link.device.services.standard.test", "com.hp.jetadvantage.link.device.services.standard.receivers.DeviceReadyReceiver"));
        intent.setAction("com.hp.workpath.system.DEVICE_READY");
        intent.putExtra("device_ip", ip);
        intent.putExtra("device_token", token);
        if(testMode) {
            //if test_mode is on, the receiver will not try to communicate with System app
            intent.putExtra("test_mode", "on");
        }
        //InstrumentationRegistry.getInstrumentation().getContext().sendBroadcast(intent);
        receiver.onReceive(InstrumentationRegistry.getInstrumentation().getContext(), intent);
    }

    @Before
    public void SetUp() {
        super.SetUp();
    }

    /**
     * DeviceReadyReceiver Test :
     * When DEVICE_READY broadcast occurs, verify that the DeviceReadyReceiver gets it and initialize StandardDeviceManagementService
     *
     * @throws InterruptedException
     */
    @Test
    public void GivenDeviceReadyReceiver_WhenTargetBroadcastOccurs_ThenInitializationShouldWork() throws InterruptedException {
        IntentFilter filter = new IntentFilter("com.hp.workpath.system.DEVICE_READY");

        //register broadcast receiver
        DeviceReadyReceiver receiver = new DeviceReadyReceiver();
        InstrumentationRegistry.getInstrumentation().getContext().registerReceiver(receiver, filter);

        boolean skipTestOnHardwareTarget = false;
        try {
            sendReadyBroadcast(receiver, deviceIp, accessToken, true);
        }
        catch (SecurityException e){
            skipTestOnHardwareTarget = true;
        }
        Assume.assumeFalse("Skipping test on hardware target Device environments", skipTestOnHardwareTarget);

        //wait until the initialization routine is completed
        int cnt = 0;
        boolean deviceConnected = false;
        while (cnt < 10) {
            if (StandardDeviceManagementService.getInstance().isDeviceConnected()) {
                deviceConnected = true;
                break;
            }
            Thread.sleep(1000);
            cnt++;
        }

        //verify that the StandardDeviceManagementService initialization is ok
        assertTrue(deviceConnected);

        InstrumentationRegistry.getInstrumentation().getContext().unregisterReceiver(receiver);
    }

    @Test
    public void GivenDeviceReadyReceiver_WhenTargetBroadcastOccursAfterInitDone_ThenUpdatingDeviceInfoShouldWork() throws InterruptedException {
        GivenDeviceReadyReceiver_WhenTargetBroadcastOccurs_ThenInitializationShouldWork();

        //register broadcast receiver
        IntentFilter filter = new IntentFilter("com.hp.workpath.system.DEVICE_READY");
        DeviceReadyReceiver receiver = new DeviceReadyReceiver();
        InstrumentationRegistry.getInstrumentation().getContext().registerReceiver(receiver, filter);

        //update token
        accessToken = testConn.getUdwClient(deviceIp).getTestToken();

        boolean skipTestOnHardwareTarget = false;
        try {
            sendReadyBroadcast(receiver,"1.2.3.4", "testToken", true);
        }
        catch (SecurityException e){
            skipTestOnHardwareTarget = true;
        }
        Assume.assumeFalse("Skipping test on hardware target Device environments", skipTestOnHardwareTarget);


        Thread.sleep(1000);
        assertEquals("1.2.3.4", StandardDeviceManagementService.getInstance().getDeviceIPAddress());

        sendReadyBroadcast(receiver, deviceIp, accessToken, true);
        Thread.sleep(1000);
        assertEquals(deviceIp, StandardDeviceManagementService.getInstance().getDeviceIPAddress());

        InstrumentationRegistry.getInstrumentation().getContext().unregisterReceiver(receiver);
    }
}
