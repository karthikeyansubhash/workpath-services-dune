
package com.hp.ws.cdm.storage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SystemStorage {

    @SerializedName("systemDevice1")
    @Expose
    private SystemDeviceDetail systemDevice1;
    @SerializedName("systemDevice2")
    @Expose
    private SystemDeviceDetail systemDevice2;
    @SerializedName("systemDevice3")
    @Expose
    private SystemDeviceDetail systemDevice3;

    public SystemDeviceDetail getSystemDevice1() {
        return systemDevice1;
    }

    public void setSystemDevice1(SystemDeviceDetail systemDevice1) {
        this.systemDevice1 = systemDevice1;
    }

    public SystemDeviceDetail getSystemDevice2() {
        return systemDevice2;
    }

    public void setSystemDevice2(SystemDeviceDetail systemDevice2) {
        this.systemDevice2 = systemDevice2;
    }

    public SystemDeviceDetail getSystemDevice3() {
        return systemDevice3;
    }

    public void setSystemDevice3(SystemDeviceDetail systemDevice3) {
        this.systemDevice3 = systemDevice3;
    }

}
