package com.hp.jetadvantage.link.device.services.clients;

import okhttp3.OkHttpClient;

/**
 * TestConnector class : testing purpose only for the Dune simulator - underwear requests
 */
public class TestConnector {
    private static final String TAG = "[WS]DSC/TestConnector";
    private OkHttpClient httpsClient;

    public TestConnector() {
        this.httpsClient = DeviceConnectorHelper.createUnsafeOkHttpClient(null);
    }

    public UdwClient getUdwClient(String deviceIpAddress) {
        return new UdwClient(httpsClient, deviceIpAddress);
    }

    public UdwClient getUdwClient(String deviceIpAddress, String httpHostHeader) {
        return new UdwClient(httpsClient, deviceIpAddress, httpHostHeader);
    }
}
