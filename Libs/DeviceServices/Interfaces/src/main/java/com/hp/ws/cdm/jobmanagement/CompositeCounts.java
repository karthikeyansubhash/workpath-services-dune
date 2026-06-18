
package com.hp.ws.cdm.jobmanagement;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * for tri-color cartridges only
 * 
 */
public class CompositeCounts {

    @SerializedName("c")
    @Expose
    private C c;
    @SerializedName("m")
    @Expose
    private M m;
    @SerializedName("y")
    @Expose
    private Y y;

    public C getC() {
        return c;
    }

    public void setC(C c) {
        this.c = c;
    }

    public M getM() {
        return m;
    }

    public void setM(M m) {
        this.m = m;
    }

    public Y getY() {
        return y;
    }

    public void setY(Y y) {
        this.y = y;
    }

}
