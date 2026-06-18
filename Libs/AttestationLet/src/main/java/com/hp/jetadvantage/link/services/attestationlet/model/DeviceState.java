// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.attestationlet.model;

public class DeviceState {
    private String href;
    private String id;
    private Boolean serviceMode;
    private String deviceState;
    private String componentState;
    private String executionMode;
    private Integer inactivityTimerFired;
    private Integer clientRequestedSleep;
    private Boolean atHomeScreen;
    private Boolean sleepEnabled;
    private Boolean linkDebugBridgeEnabled;
    private String deviceResourcesState;
    private Integer pausedSync;
    private String deviceResetFactory;
    private Boolean activeJobInSystem;
    private Boolean pausableJobsInSystem;
    private String linkServicesValidationKey;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getServiceMode() {
        return serviceMode;
    }

    public void setServiceMode(Boolean serviceMode) {
        this.serviceMode = serviceMode;
    }

    public String getDeviceState() {
        return deviceState;
    }

    public void setDeviceState(String deviceState) {
        this.deviceState = deviceState;
    }

    public String getComponentState() {
        return componentState;
    }

    public void setComponentState(String componentState) {
        this.componentState = componentState;
    }

    public String getExecutionMode() {
        return executionMode;
    }

    public void setExecutionMode(String executionMode) {
        this.executionMode = executionMode;
    }

    public Integer getInactivityTimerFired() {
        return inactivityTimerFired;
    }

    public void setInactivityTimerFired(Integer inactivityTimerFired) {
        this.inactivityTimerFired = inactivityTimerFired;
    }

    public Integer getClientRequestedSleep() {
        return clientRequestedSleep;
    }

    public void setClientRequestedSleep(Integer clientRequestedSleep) {
        this.clientRequestedSleep = clientRequestedSleep;
    }

    public Boolean getAtHomeScreen() {
        return atHomeScreen;
    }

    public void setAtHomeScreen(Boolean atHomeScreen) {
        this.atHomeScreen = atHomeScreen;
    }

    public Boolean getSleepEnabled() {
        return sleepEnabled;
    }

    public void setSleepEnabled(Boolean sleepEnabled) {
        this.sleepEnabled = sleepEnabled;
    }

    public Boolean getLinkDebugBridgeEnabled() {
        return linkDebugBridgeEnabled;
    }

    public void setLinkDebugBridgeEnabled(Boolean linkDebugBridgeEnabled) {
        this.linkDebugBridgeEnabled = linkDebugBridgeEnabled;
    }

    public String getDeviceResourcesState() {
        return deviceResourcesState;
    }

    public void setDeviceResourcesState(String deviceResourcesState) {
        this.deviceResourcesState = deviceResourcesState;
    }

    public Integer getPausedSync() {
        return pausedSync;
    }

    public void setPausedSync(Integer pausedSync) {
        this.pausedSync = pausedSync;
    }

    public String getDeviceResetFactory() {
        return deviceResetFactory;
    }

    public void setDeviceResetFactory(String deviceResetFactory) {
        this.deviceResetFactory = deviceResetFactory;
    }

    public Boolean getActiveJobInSystem() {
        return activeJobInSystem;
    }

    public void setActiveJobInSystem(Boolean activeJobInSystem) {
        this.activeJobInSystem = activeJobInSystem;
    }

    public Boolean getPausableJobsInSystem() {
        return pausableJobsInSystem;
    }

    public void setPausableJobsInSystem(Boolean pausableJobsInSystem) {
        this.pausableJobsInSystem = pausableJobsInSystem;
    }

    public String getLinkServicesValidationKey() {
        return linkServicesValidationKey;
    }

    public void setLinkServicesValidationKey(String linkServicesValidationKey) {
        this.linkServicesValidationKey = linkServicesValidationKey;
    }
}
