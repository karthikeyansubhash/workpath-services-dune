
package com.hp.ws.cdm.clock;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SystemTimeSyncInformation {

    @SerializedName("systemTimeSync")
    @Expose
    private String systemTimeSync;

    public String getSystemTimeSync() {
        return systemTimeSync;
    }

    public void setSystemTimeSync(String systemTimeSync) {
        this.systemTimeSync = systemTimeSync;
    }

}
