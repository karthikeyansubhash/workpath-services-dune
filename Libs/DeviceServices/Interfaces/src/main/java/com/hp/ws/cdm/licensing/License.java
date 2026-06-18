
package com.hp.ws.cdm.licensing;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.Link;

public class License {

    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("licenseId")
    @Expose
    private String licenseId;
    /**
     * License property
     * (Required)
     * 
     */
    @SerializedName("licenseType")
    @Expose
    private License.LicenseType licenseType;
    /**
     * standard date and time format - according to RFC 3339. The date/time when the event was produced (according to the service).  It is of the form YYYY-MM-DD'T':HH:mm:SS.sssZ, for example '2012-04-21T08:15:13.511Z'
     * 
     */
    @SerializedName("startDateTime")
    @Expose
    private Date startDateTime;
    /**
     * License property
     * 
     */
    @SerializedName("durationInDays")
    @Expose
    private Long durationInDays;
    /**
     * License property. UUID provided by the licensing service
     * 
     */
    @SerializedName("transactionId")
    @Expose
    private String transactionId;
    /**
     * Derived on Read, not part of a license install, Delta between localTimeInUtc-(grantDateAdjustedToLocalUtc+durationInDays)   lessThan or equil to 0, 0 reported which means expired. If Max than it never expired
     * 
     */
    @SerializedName("durationRemainingInMinutes")
    @Expose
    private Long durationRemainingInMinutes;
    /**
     * License property
     * 
     */
    @SerializedName("nodeLockDeviceUuid")
    @Expose
    private String nodeLockDeviceUuid;
    /**
     * Optional License metadata for solution licenses
     * 
     */
    @SerializedName("opaqueData")
    @Expose
    private String opaqueData;
    @SerializedName("links")
    @Expose
    private List<Link> links = new ArrayList<Link>();

    /**
     * 
     * (Required)
     * 
     */
    public String getLicenseId() {
        return licenseId;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setLicenseId(String licenseId) {
        this.licenseId = licenseId;
    }

    /**
     * License property
     * (Required)
     * 
     */
    public License.LicenseType getLicenseType() {
        return licenseType;
    }

    /**
     * License property
     * (Required)
     * 
     */
    public void setLicenseType(License.LicenseType licenseType) {
        this.licenseType = licenseType;
    }

    /**
     * standard date and time format - according to RFC 3339. The date/time when the event was produced (according to the service).  It is of the form YYYY-MM-DD'T':HH:mm:SS.sssZ, for example '2012-04-21T08:15:13.511Z'
     * 
     */
    public Date getStartDateTime() {
        return startDateTime;
    }

    /**
     * standard date and time format - according to RFC 3339. The date/time when the event was produced (according to the service).  It is of the form YYYY-MM-DD'T':HH:mm:SS.sssZ, for example '2012-04-21T08:15:13.511Z'
     * 
     */
    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    /**
     * License property
     * 
     */
    public Long getDurationInDays() {
        return durationInDays;
    }

    /**
     * License property
     * 
     */
    public void setDurationInDays(Long durationInDays) {
        this.durationInDays = durationInDays;
    }

    /**
     * License property. UUID provided by the licensing service
     * 
     */
    public String getTransactionId() {
        return transactionId;
    }

    /**
     * License property. UUID provided by the licensing service
     * 
     */
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    /**
     * Derived on Read, not part of a license install, Delta between localTimeInUtc-(grantDateAdjustedToLocalUtc+durationInDays)   lessThan or equil to 0, 0 reported which means expired. If Max than it never expired
     * 
     */
    public Long getDurationRemainingInMinutes() {
        return durationRemainingInMinutes;
    }

    /**
     * Derived on Read, not part of a license install, Delta between localTimeInUtc-(grantDateAdjustedToLocalUtc+durationInDays)   lessThan or equil to 0, 0 reported which means expired. If Max than it never expired
     * 
     */
    public void setDurationRemainingInMinutes(Long durationRemainingInMinutes) {
        this.durationRemainingInMinutes = durationRemainingInMinutes;
    }

    /**
     * License property
     * 
     */
    public String getNodeLockDeviceUuid() {
        return nodeLockDeviceUuid;
    }

    /**
     * License property
     * 
     */
    public void setNodeLockDeviceUuid(String nodeLockDeviceUuid) {
        this.nodeLockDeviceUuid = nodeLockDeviceUuid;
    }

    /**
     * Optional License metadata for solution licenses
     * 
     */
    public String getOpaqueData() {
        return opaqueData;
    }

    /**
     * Optional License metadata for solution licenses
     * 
     */
    public void setOpaqueData(String opaqueData) {
        this.opaqueData = opaqueData;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }


    /**
     * License property
     * 
     */
    public enum LicenseType {

        @SerializedName("feature")
        FEATURE("feature"),
        @SerializedName("solution")
        SOLUTION("solution");
        private final String value;
        private final static Map<String, License.LicenseType> CONSTANTS = new HashMap<String, License.LicenseType>();

        static {
            for (License.LicenseType c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        LicenseType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static License.LicenseType fromValue(String value) {
            License.LicenseType constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
