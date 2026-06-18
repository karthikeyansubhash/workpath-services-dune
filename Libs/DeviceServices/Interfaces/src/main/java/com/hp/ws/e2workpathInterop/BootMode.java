package com.hp.ws.e2workpathInterop;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BootMode {
    @SerializedName("isSealed")
    @Expose
    public Boolean isSealed;

    @SerializedName("isSecure")
    @Expose
    public Boolean isSecure;
}
