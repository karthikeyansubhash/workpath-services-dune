
package com.hp.ws.cdm.network;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.Link;

public class SnmpConfig {

    @SerializedName("links")
    @Expose
    private List<Link> links = new ArrayList<Link>();
    @SerializedName("snmpV1V2Config")
    @Expose
    private SnmpV1V2Config snmpV1V2Config;
    @SerializedName("snmpV3Config")
    @Expose
    private SnmpV3Config snmpV3Config;
    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
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

    public SnmpV1V2Config getSnmpV1V2Config() {
        return snmpV1V2Config;
    }

    public void setSnmpV1V2Config(SnmpV1V2Config snmpV1V2Config) {
        this.snmpV1V2Config = snmpV1V2Config;
    }

    public SnmpV3Config getSnmpV3Config() {
        return snmpV3Config;
    }

    public void setSnmpV3Config(SnmpV3Config snmpV3Config) {
        this.snmpV3Config = snmpV3Config;
    }

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    public String getVersion() {
        return version;
    }

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    public void setVersion(String version) {
        this.version = version;
    }

}
