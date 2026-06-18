package com.hp.workpath.apitest.dut;

import static com.hp.workpath.apitest.launcher.CustomTestLauncherService.exitFromDeviceHome;
import static com.hp.workpath.apitest.launcher.CustomTestLauncherService.launchMeFromDeviceHome;
import static org.junit.Assert.assertEquals;
import static java.lang.Thread.sleep;

import android.content.Context;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;

import com.hp.jetadvantage.link.device.services.clients.TestConnector;
import com.hp.workpath.api.Result;

public class DuneDutController implements DutController {
    boolean appLaunched = false;
    TestConnector testConn;
    String deviceIP = DutConfig.IP;
    String hostHeaderIP = DutConfig.HOST_HEADER_IP;
    Accessory accessoryController;
    Storage storageController;


    DuneDutController() {
        String deviceIpFromCommand = InstrumentationRegistry.getArguments().getString("dutInfoIp");
        if (deviceIpFromCommand != null && !deviceIpFromCommand.isEmpty()) {
            deviceIP = deviceIpFromCommand;
            hostHeaderIP = deviceIpFromCommand;
        }

        String deviceHostFromCommand = InstrumentationRegistry.getArguments().getString("dutInfoHostHeader");
        if (deviceHostFromCommand != null && !deviceHostFromCommand.isEmpty()) {
            hostHeaderIP = deviceHostFromCommand;
        }
        testConn = new TestConnector();
    }

    @Override
    public void initializeDutForApiTest() {
        try {
            //Enable Host Header Check on Dune
            testConn.getUdwClient(deviceIP, hostHeaderIP).sendUnderwareCommand("1.0.0", "mainApp", "WebServerSecurity PUB_EnableCSRFCheck 0");

            //Enable token auth
            testConn.getUdwClient(deviceIP, hostHeaderIP).sendUnderwareCommand("1.0.0", "mainApp", "OAuth2Standard PUB_testEnableTokenAuth 1");

        } catch (Exception e) {
            Log.e("DuneDutController", "Failed to initialize DUT for API test: " + e.getMessage());
        }
    }

    @Override
    public synchronized void startApp(Context context) {
        Result result = new Result();
        launchMeFromDeviceHome(context, result);   //This is required to get UIContext
        assertEquals("launch API test app from the device home", result.getCode(), Result.RESULT_OK);
        appLaunched = true;
        try {
            sleep(1000);
        } catch (Exception e) {

        }
    }

    @Override
    public synchronized void stopApp(Context context) {
        if (appLaunched) {
            Result result = new Result();
            exitFromDeviceHome(context, result);
            appLaunched = false;
        }
    }

    @Override
    public Accessory getAccessoryController() {
        if (accessoryController == null) {
            synchronized (this) {
                if (accessoryController == null) {
                    accessoryController = new DuneDutAccessoryController(testConn, deviceIP, hostHeaderIP);
                }
            }
        }
        return accessoryController;
    }

    @Override
    public Storage getStorageController() {
        if (storageController == null) {
            synchronized (this) {
                if (storageController == null) {
                    storageController = new DuneDutStorageController(testConn, deviceIP, hostHeaderIP);
                }
            }
        }
        return storageController;
    }
}
