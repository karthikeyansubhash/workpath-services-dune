package com.hp.workpath.apitest.dut;

import static org.junit.Assert.fail;

import com.hp.jetadvantage.link.device.services.clients.TestConnector;

public class DuneDutStorageController implements DutController.Storage{
    final TestConnector testConn;
    final String deviceIP;
    final String hostHeaderIP;

    DuneDutStorageController(TestConnector testConn, String deviceIP, String hostHeaderIP) {
        this.testConn = testConn;
        this.deviceIP = deviceIP;
        this.hostHeaderIP = hostHeaderIP;
    }

    @Override
    public void connectUsbDevice() {
        try {
            testConn.getUdwClient(deviceIP).sendUnderwareCommand("1.0.0", "mainApp", "UsbHostMgr PUB_addMockStorageDevice disk1 f");
        } catch (Exception e) {
            fail("Setup failed:" + e);
        }
    }

    @Override
    public void removeUsbDevice() {
        try {
            testConn.getUdwClient(deviceIP).sendUnderwareCommand("1.0.0", "mainApp", "UsbHostMgr PUB_removeMockStorageDevice disk1");
        } catch (Exception e) {
            fail("Setup failed:" + e);
        }
    }
}
