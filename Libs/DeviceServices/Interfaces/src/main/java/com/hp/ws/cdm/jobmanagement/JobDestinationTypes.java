
package com.hp.ws.cdm.jobmanagement;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.SerializedName;


/**
 * Identifies one or more destinations for the job
 * 
 */
public enum JobDestinationTypes {

    @SerializedName("printEngine")
    PRINT_ENGINE("printEngine"),
    @SerializedName("internalStorage")
    INTERNAL_STORAGE("internalStorage"),
    @SerializedName("removableStorage")
    REMOVABLE_STORAGE("removableStorage"),
    @SerializedName("http")
    HTTP("http"),
    @SerializedName("ftp")
    FTP("ftp"),
    @SerializedName("email")
    EMAIL("email"),
    @SerializedName("scanner")
    SCANNER("scanner"),
    @SerializedName("faxCard")
    FAX_CARD("faxCard"),
    @SerializedName("networkFolder")
    NETWORK_FOLDER("networkFolder"),
    @SerializedName("imageQueue")
    IMAGE_QUEUE("imageQueue");
    private final String value;
    private final static Map<String, JobDestinationTypes> CONSTANTS = new HashMap<String, JobDestinationTypes>();

    static {
        for (JobDestinationTypes c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    JobDestinationTypes(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public String value() {
        return this.value;
    }

    public static JobDestinationTypes fromValue(String value) {
        JobDestinationTypes constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
