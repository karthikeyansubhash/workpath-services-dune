
package com.hp.ws.cdm.security;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.Link;
import com.hp.ws.cdm.commonglossary.Property;

public class SmartcardGenericReader {

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
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("genericReaderVisibility")
    @Expose
    private Property.FeatureEnabled genericReaderVisibility;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("genericReaderEnabled")
    @Expose
    private Property.FeatureEnabled genericReaderEnabled;
    /**
     * USB Vendor ID of generic Smartcard Reader
     * 
     */
    @SerializedName("genericReaderVendorId")
    @Expose
    private String genericReaderVendorId;
    /**
     * USB Product ID of generic Smartcard Reader
     * 
     */
    @SerializedName("genericReaderProductId")
    @Expose
    private String genericReaderProductId;
    @SerializedName("links")
    @Expose
    private List<Link> links = new ArrayList<Link>();

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
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getGenericReaderVisibility() {
        return genericReaderVisibility;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setGenericReaderVisibility(Property.FeatureEnabled genericReaderVisibility) {
        this.genericReaderVisibility = genericReaderVisibility;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getGenericReaderEnabled() {
        return genericReaderEnabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setGenericReaderEnabled(Property.FeatureEnabled genericReaderEnabled) {
        this.genericReaderEnabled = genericReaderEnabled;
    }

    /**
     * USB Vendor ID of generic Smartcard Reader
     * 
     */
    public String getGenericReaderVendorId() {
        return genericReaderVendorId;
    }

    /**
     * USB Vendor ID of generic Smartcard Reader
     * 
     */
    public void setGenericReaderVendorId(String genericReaderVendorId) {
        this.genericReaderVendorId = genericReaderVendorId;
    }

    /**
     * USB Product ID of generic Smartcard Reader
     * 
     */
    public String getGenericReaderProductId() {
        return genericReaderProductId;
    }

    /**
     * USB Product ID of generic Smartcard Reader
     * 
     */
    public void setGenericReaderProductId(String genericReaderProductId) {
        this.genericReaderProductId = genericReaderProductId;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

}
