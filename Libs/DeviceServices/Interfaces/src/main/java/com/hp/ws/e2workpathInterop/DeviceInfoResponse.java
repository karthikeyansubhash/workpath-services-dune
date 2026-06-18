package com.hp.ws.e2workpathInterop;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeviceInfoResponse {
    @SerializedName("bootMode")
    @Expose
    public BootMode bootMode;

    @SerializedName("cloudStack")
    @Expose
    public String cloudStack;
}
