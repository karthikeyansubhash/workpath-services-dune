package com.hp.jetadvantage.link.device.services.standard;

import androidx.test.platform.app.InstrumentationRegistry;

import com.hp.jetadvantage.link.DutInfo;
import com.hp.jetadvantage.link.device.services.clients.TestConnector;
import com.hp.jetadvantage.link.device.services.standard.common.Constants;

public class StandardDeviceInstrumentedTest {
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
}
