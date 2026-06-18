package com.hp.ws.e2workpathInterop;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppToken {
    @SerializedName("solutionToken")
    @Expose
    private String solutionToken;

    @SerializedName("version")
    @Expose
    private String version;

    public String getSolutionToken() {
        return solutionToken;
    }

    public void setSolutionToken(String token) {
        this.solutionToken = token;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String ver) {
        this.version = ver;
    }
}
