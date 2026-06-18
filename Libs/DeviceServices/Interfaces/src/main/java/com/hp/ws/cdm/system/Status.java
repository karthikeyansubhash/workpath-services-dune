
package com.hp.ws.cdm.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.Link;


/**
 * resource to retrieve the device's current status
 * 
 */
public class Status {

    @SerializedName("links")
    @Expose
    private List<Link> links = new ArrayList<Link>();
    /**
     * Device Status Category
     * (Required)
     * 
     */
    @SerializedName("status")
    @Expose
    private Status.StatusCategory status;
    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * (Required)
     * 
     */
    @SerializedName("version")
    @Expose
    private String version;

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    /**
     * Device Status Category
     * (Required)
     * 
     */
    public Status.StatusCategory getStatus() {
        return status;
    }

    /**
     * Device Status Category
     * (Required)
     * 
     */
    public void setStatus(Status.StatusCategory status) {
        this.status = status;
    }

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * (Required)
     * 
     */
    public String getVersion() {
        return version;
    }

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * (Required)
     * 
     */
    public void setVersion(String version) {
        this.version = version;
    }


    /**
     * Device Status Category
     * 
     */
    public enum StatusCategory {

        @SerializedName("initializing")
        INITIALIZING("initializing"),
        @SerializedName("ready")
        READY("ready"),
        @SerializedName("shuttingDown")
        SHUTTING_DOWN("shuttingDown"),
        @SerializedName("inPowerSave")
        IN_POWER_SAVE("inPowerSave"),
        @SerializedName("inError")
        IN_ERROR("inError"),
        @SerializedName("unreachable")
        UNREACHABLE("unreachable"),
        @SerializedName("deviceSetupPending")
        DEVICE_SETUP_PENDING("deviceSetupPending"),
        @SerializedName("inService")
        IN_SERVICE("inService");
        private final String value;
        private final static Map<String, Status.StatusCategory> CONSTANTS = new HashMap<String, Status.StatusCategory>();

        static {
            for (Status.StatusCategory c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        StatusCategory(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Status.StatusCategory fromValue(String value) {
            Status.StatusCategory constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
