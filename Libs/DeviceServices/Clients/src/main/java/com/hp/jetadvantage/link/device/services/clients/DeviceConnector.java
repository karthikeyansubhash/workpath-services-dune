/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.device.services.clients;

import com.hp.ext.clients.InjectedHttpClient;

import okhttp3.OkHttpClient;

public class DeviceConnector {
    private static final String TAG = "[WS]DSC/DeviceConnector";
    private static final String DuneEngineIPAddress = "156.152.79.233";
    private final E2Client e2Client;
    private final CDMClient cdmClient;
    private final OkHttpClient httpsClient;

    public DeviceConnector(String ipAddress, String token) {
        this.httpsClient = DeviceConnectorHelper.createUnsafeOkHttpClient(null);

        e2Client = new E2Client(httpsClient, token);
        cdmClient = new CDMClient(httpsClient, ipAddress, token);
    }

    public void updateDeviceInfo(String ipAddress, String token) {
        cdmClient.updateDeviceInfo(ipAddress, token);
        e2Client.updateDeviceInfo(ipAddress, token);
    }

    public InjectedHttpClient getE2Client() {
        return this.e2Client;
    }

    public CDMClient getCDMClient() {
        return this.cdmClient;
    }
}
