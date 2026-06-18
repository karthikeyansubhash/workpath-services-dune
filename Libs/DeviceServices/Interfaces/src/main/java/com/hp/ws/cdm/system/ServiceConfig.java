
package com.hp.ws.cdm.system;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.Link;


/**
 * resource for service configuration
 * 
 */
public class ServiceConfig {

    /**
     * Born-on Date of the device. Both serviceId and installationDate fields represent the same data. It is required to set ONLY one of them, setting both is not allowed, and can be set ONLY ONCE.
     * 
     */
    @SerializedName("installationDate")
    @Expose
    private InstallationDate installationDate;
    @SerializedName("links")
    @Expose
    private List<Link> links = new ArrayList<Link>();
    /**
     * serial number of the device
     * 
     */
    @SerializedName("serialNumber")
    @Expose
    private String serialNumber;
    /**
     * Service Id of the device, it is the 5 digit encoded install date. Both serviceId and installationDate fields represent the same data. It is required to set ONLY one of them, setting both is not allowed, and can be set ONLY ONCE.
     * 
     */
    @SerializedName("serviceId")
    @Expose
    private String serviceId;
    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    @SerializedName("version")
    @Expose
    private String version;

    /**
     * Born-on Date of the device. Both serviceId and installationDate fields represent the same data. It is required to set ONLY one of them, setting both is not allowed, and can be set ONLY ONCE.
     * 
     */
    public InstallationDate getInstallationDate() {
        return installationDate;
    }

    /**
     * Born-on Date of the device. Both serviceId and installationDate fields represent the same data. It is required to set ONLY one of them, setting both is not allowed, and can be set ONLY ONCE.
     * 
     */
    public void setInstallationDate(InstallationDate installationDate) {
        this.installationDate = installationDate;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    /**
     * serial number of the device
     * 
     */
    public String getSerialNumber() {
        return serialNumber;
    }

    /**
     * serial number of the device
     * 
     */
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    /**
     * Service Id of the device, it is the 5 digit encoded install date. Both serviceId and installationDate fields represent the same data. It is required to set ONLY one of them, setting both is not allowed, and can be set ONLY ONCE.
     * 
     */
    public String getServiceId() {
        return serviceId;
    }

    /**
     * Service Id of the device, it is the 5 digit encoded install date. Both serviceId and installationDate fields represent the same data. It is required to set ONLY one of them, setting both is not allowed, and can be set ONLY ONCE.
     * 
     */
    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
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
