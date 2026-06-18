// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.attestationlet.model;

public class AppPlatformConfiguration {
    public boolean androidDebugBridgeEnabled;
    public String executionMode;
    public String deviceState;
    public String host;
    public String clientId;

    public boolean isAndroidDebugBridgeEnabled() {
        return androidDebugBridgeEnabled;
    }

    public void setAndroidDebugBridgeEnabled(boolean androidDebugBridgeEnabled) {
        this.androidDebugBridgeEnabled = androidDebugBridgeEnabled;
    }

    public String getExecutionMode() {
        return executionMode;
    }

    public void setExecutionMode(String executionMode) {
        this.executionMode = executionMode;
    }

    public String getDeviceState() {
        return deviceState;
    }

    public void setDeviceState(String deviceState) {
        this.deviceState = deviceState;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}
