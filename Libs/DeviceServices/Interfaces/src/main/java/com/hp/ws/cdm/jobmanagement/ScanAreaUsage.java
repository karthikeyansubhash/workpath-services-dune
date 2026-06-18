
package com.hp.ws.cdm.jobmanagement;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ScanAreaUsage {

    /**
     * counter object with int64 maximum and unit string enum
     * 
     */
    @SerializedName("scanArea")
    @Expose
    private LargeCounter scanArea;
    /**
     * counter object with int64 maximum and unit string enum
     * 
     */
    @SerializedName("scanLength")
    @Expose
    private LargeCounter scanLength;

    /**
     * counter object with int64 maximum and unit string enum
     * 
     */
    public LargeCounter getScanArea() {
        return scanArea;
    }

    /**
     * counter object with int64 maximum and unit string enum
     * 
     */
    public void setScanArea(LargeCounter scanArea) {
        this.scanArea = scanArea;
    }

    /**
     * counter object with int64 maximum and unit string enum
     * 
     */
    public LargeCounter getScanLength() {
        return scanLength;
    }

    /**
     * counter object with int64 maximum and unit string enum
     * 
     */
    public void setScanLength(LargeCounter scanLength) {
        this.scanLength = scanLength;
    }

}
