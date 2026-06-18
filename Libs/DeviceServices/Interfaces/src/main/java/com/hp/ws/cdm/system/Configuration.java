
package com.hp.ws.cdm.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.Link;


/**
 * resource to retrieve the device's configuration
 * 
 */
public class Configuration {

    /**
     * The device asset number.
     * 
     */
    @SerializedName("assetNumber")
    @Expose
    private String assetNumber;
    /**
     * The name of the person who is responsible for servicing this printer. It is suggested that this string include information that would enable other humans to reach the service person, such as a phone number.
     * 
     */
    @SerializedName("companyContact")
    @Expose
    private String companyContact;
    /**
     * The company using the device (there is a legal requirement to set company name on a fax machine).
     * 
     */
    @SerializedName("companyName")
    @Expose
    private String companyName;
    /**
     * country and region codes as per ISO 3166-1 alpha-2
     * 
     */
    @SerializedName("countryRegion")
    @Expose
    private com.hp.ws.cdm.system.Identity.CountryRegionIso countryRegion;
    /**
     * Configurable description for the device.
     * 
     */
    @SerializedName("deviceDescription")
    @Expose
    private String deviceDescription;
    /**
     * language code as per ISO 639-1. The country extension should only be used when variants or dialects are supported. This list should only have the subset of languages that are actuall supported
     * 
     */
    @SerializedName("deviceLanguage")
    @Expose
    private com.hp.ws.cdm.controlpanel.Configuration.Language deviceLanguage;
    /**
     * The user defined device location.
     * 
     */
    @SerializedName("deviceLocation")
    @Expose
    private String deviceLocation;
    /**
     * User can set or get the current units of measurement that will be applied globally in the system for lengths, square areas or volumes.
     * 
     */
    @SerializedName("displayUnitOfMeasure")
    @Expose
    private Configuration.UnitsOfMeasurement displayUnitOfMeasure;
    /**
     * Specifies the name of the person that users can contact for device support.
     * 
     */
    @SerializedName("supportContact")
    @Expose
    private String supportContact;
    @SerializedName("links")
    @Expose
    private List<Link> links = new ArrayList<Link>();
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
     * The device asset number.
     * 
     */
    public String getAssetNumber() {
        return assetNumber;
    }

    /**
     * The device asset number.
     * 
     */
    public void setAssetNumber(String assetNumber) {
        this.assetNumber = assetNumber;
    }

    /**
     * The name of the person who is responsible for servicing this printer. It is suggested that this string include information that would enable other humans to reach the service person, such as a phone number.
     * 
     */
    public String getCompanyContact() {
        return companyContact;
    }

    /**
     * The name of the person who is responsible for servicing this printer. It is suggested that this string include information that would enable other humans to reach the service person, such as a phone number.
     * 
     */
    public void setCompanyContact(String companyContact) {
        this.companyContact = companyContact;
    }

    /**
     * The company using the device (there is a legal requirement to set company name on a fax machine).
     * 
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * The company using the device (there is a legal requirement to set company name on a fax machine).
     * 
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * country and region codes as per ISO 3166-1 alpha-2
     * 
     */
    public com.hp.ws.cdm.system.Identity.CountryRegionIso getCountryRegion() {
        return countryRegion;
    }

    /**
     * country and region codes as per ISO 3166-1 alpha-2
     * 
     */
    public void setCountryRegion(com.hp.ws.cdm.system.Identity.CountryRegionIso countryRegion) {
        this.countryRegion = countryRegion;
    }

    /**
     * Configurable description for the device.
     * 
     */
    public String getDeviceDescription() {
        return deviceDescription;
    }

    /**
     * Configurable description for the device.
     * 
     */
    public void setDeviceDescription(String deviceDescription) {
        this.deviceDescription = deviceDescription;
    }

    /**
     * language code as per ISO 639-1. The country extension should only be used when variants or dialects are supported. This list should only have the subset of languages that are actuall supported
     * 
     */
    public com.hp.ws.cdm.controlpanel.Configuration.Language getDeviceLanguage() {
        return deviceLanguage;
    }

    /**
     * language code as per ISO 639-1. The country extension should only be used when variants or dialects are supported. This list should only have the subset of languages that are actuall supported
     * 
     */
    public void setDeviceLanguage(com.hp.ws.cdm.controlpanel.Configuration.Language deviceLanguage) {
        this.deviceLanguage = deviceLanguage;
    }

    /**
     * The user defined device location.
     * 
     */
    public String getDeviceLocation() {
        return deviceLocation;
    }

    /**
     * The user defined device location.
     * 
     */
    public void setDeviceLocation(String deviceLocation) {
        this.deviceLocation = deviceLocation;
    }

    /**
     * User can set or get the current units of measurement that will be applied globally in the system for lengths, square areas or volumes.
     * 
     */
    public Configuration.UnitsOfMeasurement getDisplayUnitOfMeasure() {
        return displayUnitOfMeasure;
    }

    /**
     * User can set or get the current units of measurement that will be applied globally in the system for lengths, square areas or volumes.
     * 
     */
    public void setDisplayUnitOfMeasure(Configuration.UnitsOfMeasurement displayUnitOfMeasure) {
        this.displayUnitOfMeasure = displayUnitOfMeasure;
    }

    /**
     * Specifies the name of the person that users can contact for device support.
     * 
     */
    public String getSupportContact() {
        return supportContact;
    }

    /**
     * Specifies the name of the person that users can contact for device support.
     * 
     */
    public void setSupportContact(String supportContact) {
        this.supportContact = supportContact;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
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


    /**
     * User can set or get the current units of measurement that will be applied globally in the system for lengths, square areas or volumes.
     * 
     */
    public enum UnitsOfMeasurement {

        @SerializedName("imperial")
        IMPERIAL("imperial"),
        @SerializedName("metric")
        METRIC("metric");
        private final String value;
        private final static Map<String, Configuration.UnitsOfMeasurement> CONSTANTS = new HashMap<String, Configuration.UnitsOfMeasurement>();

        static {
            for (Configuration.UnitsOfMeasurement c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        UnitsOfMeasurement(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Configuration.UnitsOfMeasurement fromValue(String value) {
            Configuration.UnitsOfMeasurement constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
