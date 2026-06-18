
package com.hp.ws.cdm.ioconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.Link;
import com.hp.ws.cdm.commonglossary.Property;

public class WifiDirectConfig {

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("awcActive")
    @Expose
    private Property.FeatureEnabled awcActive;
    /**
     * Specifies the Wi-Fi Direct connection method
     * 
     */
    @SerializedName("connectionMethod")
    @Expose
    private WifiDirectConfig.ConnectionMethod connectionMethod;
    /**
     * The current operating channel number of the Wi-Fi Direct network
     * 
     */
    @SerializedName("currentChannel")
    @Expose
    private Integer currentChannel;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("defaultState")
    @Expose
    private Property.FeatureEnabled defaultState;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("enabled")
    @Expose
    private Property.FeatureEnabled enabled;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("hideSsid")
    @Expose
    private Property.FeatureEnabled hideSsid;
    @SerializedName("links")
    @Expose
    private List<Link> links = new ArrayList<Link>();
    /**
     * The pass phrase for the Wi-Fi Direct network
     * 
     */
    @SerializedName("passphrase")
    @Expose
    private String passphrase;
    /**
     * The preferred channel number for the Wi-Fi Direct network
     * 
     */
    @SerializedName("preferredChannel")
    @Expose
    private Integer preferredChannel;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("requiresSecurityCode")
    @Expose
    private Property.FeatureEnabled requiresSecurityCode;
    /**
     * Security code, applicable when connectionMethod is Advanced
     * 
     */
    @SerializedName("securityCode")
    @Expose
    private Integer securityCode = 1;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("showPassphrase")
    @Expose
    private Property.FeatureEnabled showPassphrase;
    /**
     * The Wi-Fi Direct network name/SSID
     * 
     */
    @SerializedName("ssid")
    @Expose
    private String ssid;
    /**
     * The fixed prefix of the SSID
     * 
     */
    @SerializedName("ssidPrefix")
    @Expose
    private String ssidPrefix;
    /**
     * The configurable suffix of the SSID
     * 
     */
    @SerializedName("ssidSuffix")
    @Expose
    private String ssidSuffix;
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
    public Property.FeatureEnabled getAwcActive() {
        return awcActive;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setAwcActive(Property.FeatureEnabled awcActive) {
        this.awcActive = awcActive;
    }

    /**
     * Specifies the Wi-Fi Direct connection method
     * 
     */
    public WifiDirectConfig.ConnectionMethod getConnectionMethod() {
        return connectionMethod;
    }

    /**
     * Specifies the Wi-Fi Direct connection method
     * 
     */
    public void setConnectionMethod(WifiDirectConfig.ConnectionMethod connectionMethod) {
        this.connectionMethod = connectionMethod;
    }

    /**
     * The current operating channel number of the Wi-Fi Direct network
     * 
     */
    public Integer getCurrentChannel() {
        return currentChannel;
    }

    /**
     * The current operating channel number of the Wi-Fi Direct network
     * 
     */
    public void setCurrentChannel(Integer currentChannel) {
        this.currentChannel = currentChannel;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getDefaultState() {
        return defaultState;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setDefaultState(Property.FeatureEnabled defaultState) {
        this.defaultState = defaultState;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getEnabled() {
        return enabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setEnabled(Property.FeatureEnabled enabled) {
        this.enabled = enabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getHideSsid() {
        return hideSsid;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setHideSsid(Property.FeatureEnabled hideSsid) {
        this.hideSsid = hideSsid;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    /**
     * The pass phrase for the Wi-Fi Direct network
     * 
     */
    public String getPassphrase() {
        return passphrase;
    }

    /**
     * The pass phrase for the Wi-Fi Direct network
     * 
     */
    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
    }

    /**
     * The preferred channel number for the Wi-Fi Direct network
     * 
     */
    public Integer getPreferredChannel() {
        return preferredChannel;
    }

    /**
     * The preferred channel number for the Wi-Fi Direct network
     * 
     */
    public void setPreferredChannel(Integer preferredChannel) {
        this.preferredChannel = preferredChannel;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getRequiresSecurityCode() {
        return requiresSecurityCode;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setRequiresSecurityCode(Property.FeatureEnabled requiresSecurityCode) {
        this.requiresSecurityCode = requiresSecurityCode;
    }

    /**
     * Security code, applicable when connectionMethod is Advanced
     * 
     */
    public Integer getSecurityCode() {
        return securityCode;
    }

    /**
     * Security code, applicable when connectionMethod is Advanced
     * 
     */
    public void setSecurityCode(Integer securityCode) {
        this.securityCode = securityCode;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getShowPassphrase() {
        return showPassphrase;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setShowPassphrase(Property.FeatureEnabled showPassphrase) {
        this.showPassphrase = showPassphrase;
    }

    /**
     * The Wi-Fi Direct network name/SSID
     * 
     */
    public String getSsid() {
        return ssid;
    }

    /**
     * The Wi-Fi Direct network name/SSID
     * 
     */
    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    /**
     * The fixed prefix of the SSID
     * 
     */
    public String getSsidPrefix() {
        return ssidPrefix;
    }

    /**
     * The fixed prefix of the SSID
     * 
     */
    public void setSsidPrefix(String ssidPrefix) {
        this.ssidPrefix = ssidPrefix;
    }

    /**
     * The configurable suffix of the SSID
     * 
     */
    public String getSsidSuffix() {
        return ssidSuffix;
    }

    /**
     * The configurable suffix of the SSID
     * 
     */
    public void setSsidSuffix(String ssidSuffix) {
        this.ssidSuffix = ssidSuffix;
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
     * Specifies the Wi-Fi Direct connection method
     * 
     */
    public enum ConnectionMethod {

        @SerializedName("automatic")
        AUTOMATIC("automatic"),
        @SerializedName("manual")
        MANUAL("manual"),
        @SerializedName("advanced")
        ADVANCED("advanced");
        private final String value;
        private final static Map<String, WifiDirectConfig.ConnectionMethod> CONSTANTS = new HashMap<String, WifiDirectConfig.ConnectionMethod>();

        static {
            for (WifiDirectConfig.ConnectionMethod c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        ConnectionMethod(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static WifiDirectConfig.ConnectionMethod fromValue(String value) {
            WifiDirectConfig.ConnectionMethod constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
