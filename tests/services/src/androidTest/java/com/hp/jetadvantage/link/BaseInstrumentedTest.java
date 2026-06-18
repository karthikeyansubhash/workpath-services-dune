package com.hp.jetadvantage.link;

import android.content.Intent;
import android.os.Message;

import androidx.test.platform.app.InstrumentationRegistry;

import com.hp.jetadvantage.link.device.services.clients.TestConnector;
import com.hp.jetadvantage.link.device.services.standard.common.Constants;
import com.hp.jetadvantage.link.services.joblet.jobintentservice.BaseJobIntentServiceStateMachine;

public class BaseInstrumentedTest {
    protected String deviceIp = DutInfo.IP;
    protected String accessToken = DutInfo.TOKEN;
    protected String hostHeader = DutInfo.HostHeader;

    protected TestConnector testConn;

    protected String testPackageName = Constants.TEST_PACKAGE_NAME;

    public void SetUp() {
        String deviceIpFromCommand = InstrumentationRegistry.getArguments().getString("dutInfoIp");
        if (deviceIpFromCommand != null && !deviceIpFromCommand.isEmpty()) {
            deviceIp = deviceIpFromCommand;
            hostHeader = deviceIpFromCommand;
        }

        String deviceHostFromCommand = InstrumentationRegistry.getArguments().getString("dutInfoHostHeader");
        if (deviceHostFromCommand != null && !deviceHostFromCommand.isEmpty()) {
            hostHeader = deviceHostFromCommand;
        }

        testConn = new TestConnector();

        try {
            //Disable Host Header Check on Dune
            //If the target Dune device/simulator is under NAT, turning off the host header check is required to access via http or https. Otherwise HTTP 403 returned
            testConn.getUdwClient(deviceIp, hostHeader).sendUnderwareCommand("1.0.0", "mainApp", "WebServerSecurity PUB_EnableCSRFCheck 0");

            //Disable token auth for testing
            testConn.getUdwClient(deviceIp, hostHeader).sendUnderwareCommand("1.0.0", "mainApp", "OAuth2Standard PUB_testEnableTokenAuth 0");
        } catch (Exception e) {

        }
        accessToken = testConn.getUdwClient(deviceIp).getTestToken();
    }

    protected void sendStartMsg(BaseJobIntentServiceStateMachine sm, Intent intent) {
        Message msg = sm.obtainMessage();
        msg.what = BaseJobIntentServiceStateMachine.MSG_START;
        msg.obj = intent;
        sm.sendMessage(msg);
    }

    protected void sleepMs(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            //ignore
        }
    }
}
