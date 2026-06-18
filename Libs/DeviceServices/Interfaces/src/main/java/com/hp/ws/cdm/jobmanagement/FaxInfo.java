
package com.hp.ws.cdm.jobmanagement;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * provides the fax job specific details
 * 
 */
public class FaxInfo {

    @SerializedName("faxInInfo")
    @Expose
    private FaxInInfo faxInInfo;
    @SerializedName("faxJobType")
    @Expose
    private FaxInfo.FaxJobType faxJobType;
    @SerializedName("faxOutInfo")
    @Expose
    private FaxOutInfo faxOutInfo;

    public FaxInInfo getFaxInInfo() {
        return faxInInfo;
    }

    public void setFaxInInfo(FaxInInfo faxInInfo) {
        this.faxInInfo = faxInInfo;
    }

    public FaxInfo.FaxJobType getFaxJobType() {
        return faxJobType;
    }

    public void setFaxJobType(FaxInfo.FaxJobType faxJobType) {
        this.faxJobType = faxJobType;
    }

    public FaxOutInfo getFaxOutInfo() {
        return faxOutInfo;
    }

    public void setFaxOutInfo(FaxOutInfo faxOutInfo) {
        this.faxOutInfo = faxOutInfo;
    }

    public enum FaxJobType {

        @SerializedName("faxReport")
        FAX_REPORT("faxReport"),
        @SerializedName("faxSend")
        FAX_SEND("faxSend"),
        @SerializedName("faxReceive")
        FAX_RECEIVE("faxReceive"),
        @SerializedName("faxReprint")
        FAX_REPRINT("faxReprint");
        private final String value;
        private final static Map<String, FaxInfo.FaxJobType> CONSTANTS = new HashMap<String, FaxInfo.FaxJobType>();

        static {
            for (FaxInfo.FaxJobType c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        FaxJobType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static FaxInfo.FaxJobType fromValue(String value) {
            FaxInfo.FaxJobType constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
