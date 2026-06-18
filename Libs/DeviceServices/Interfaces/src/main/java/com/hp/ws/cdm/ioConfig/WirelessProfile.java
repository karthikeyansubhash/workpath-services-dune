
package com.hp.ws.cdm.ioconfig;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

public class WirelessProfile {

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("activeProfile")
    @Expose
    private Property.FeatureEnabled activeProfile;
    /**
     * Wireless network authentication mode
     * 
     */
    @SerializedName("authenticationMode")
    @Expose
    private com.hp.ws.cdm.ioconfig.WifiNetwork.WifiAuthenticationMode authenticationMode;
    /**
     *  802.1x authentication configuration
     * 
     */
    @SerializedName("dot1xAuthConfig")
    @Expose
    private Dot1xAuthentication dot1xAuthConfig;
    /**
     * Wireless Encryption protocol
     * 
     */
    @SerializedName("encryptionType")
    @Expose
    private com.hp.ws.cdm.ioconfig.WifiNetwork.WifiEncryptionType encryptionType;
    /**
     * Pass phrase
     * 
     */
    @SerializedName("passPhrase")
    @Expose
    private String passPhrase;
    /**
     * The Service Set Identifier(SSID) aka name of the wireless network (LAN)
     * 
     */
    @SerializedName("ssid")
    @Expose
    private String ssid;
    /**
     * WEP key
     * 
     */
    @SerializedName("wepKey")
    @Expose
    private String wepKey;
    /**
     * WEP key index in range 0 to 3
     * 
     */
    @SerializedName("wepKeyIndex")
    @Expose
    private Integer wepKeyIndex;
    /**
     * Preferred WPA version
     * 
     */
    @SerializedName("wpaVersionPreference")
    @Expose
    private WirelessProfile.WifiWpaVersion wpaVersionPreference;

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getActiveProfile() {
        return activeProfile;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setActiveProfile(Property.FeatureEnabled activeProfile) {
        this.activeProfile = activeProfile;
    }

    /**
     * Wireless network authentication mode
     * 
     */
    public com.hp.ws.cdm.ioconfig.WifiNetwork.WifiAuthenticationMode getAuthenticationMode() {
        return authenticationMode;
    }

    /**
     * Wireless network authentication mode
     * 
     */
    public void setAuthenticationMode(com.hp.ws.cdm.ioconfig.WifiNetwork.WifiAuthenticationMode authenticationMode) {
        this.authenticationMode = authenticationMode;
    }

    /**
     *  802.1x authentication configuration
     * 
     */
    public Dot1xAuthentication getDot1xAuthConfig() {
        return dot1xAuthConfig;
    }

    /**
     *  802.1x authentication configuration
     * 
     */
    public void setDot1xAuthConfig(Dot1xAuthentication dot1xAuthConfig) {
        this.dot1xAuthConfig = dot1xAuthConfig;
    }

    /**
     * Wireless Encryption protocol
     * 
     */
    public com.hp.ws.cdm.ioconfig.WifiNetwork.WifiEncryptionType getEncryptionType() {
        return encryptionType;
    }

    /**
     * Wireless Encryption protocol
     * 
     */
    public void setEncryptionType(com.hp.ws.cdm.ioconfig.WifiNetwork.WifiEncryptionType encryptionType) {
        this.encryptionType = encryptionType;
    }

    /**
     * Pass phrase
     * 
     */
    public String getPassPhrase() {
        return passPhrase;
    }

    /**
     * Pass phrase
     * 
     */
    public void setPassPhrase(String passPhrase) {
        this.passPhrase = passPhrase;
    }

    /**
     * The Service Set Identifier(SSID) aka name of the wireless network (LAN)
     * 
     */
    public String getSsid() {
        return ssid;
    }

    /**
     * The Service Set Identifier(SSID) aka name of the wireless network (LAN)
     * 
     */
    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    /**
     * WEP key
     * 
     */
    public String getWepKey() {
        return wepKey;
    }

    /**
     * WEP key
     * 
     */
    public void setWepKey(String wepKey) {
        this.wepKey = wepKey;
    }

    /**
     * WEP key index in range 0 to 3
     * 
     */
    public Integer getWepKeyIndex() {
        return wepKeyIndex;
    }

    /**
     * WEP key index in range 0 to 3
     * 
     */
    public void setWepKeyIndex(Integer wepKeyIndex) {
        this.wepKeyIndex = wepKeyIndex;
    }

    /**
     * Preferred WPA version
     * 
     */
    public WirelessProfile.WifiWpaVersion getWpaVersionPreference() {
        return wpaVersionPreference;
    }

    /**
     * Preferred WPA version
     * 
     */
    public void setWpaVersionPreference(WirelessProfile.WifiWpaVersion wpaVersionPreference) {
        this.wpaVersionPreference = wpaVersionPreference;
    }


    /**
     * Preferred WPA version
     * 
     */
    public enum WifiWpaVersion {

        @SerializedName("auto")
        AUTO("auto"),
        @SerializedName("wpa2OrWpa3")
        WPA_2_OR_WPA_3("wpa2OrWpa3"),
        @SerializedName("wpa2")
        WPA_2("wpa2"),
        @SerializedName("wpa3")
        WPA_3("wpa3");
        private final String value;
        private final static Map<String, WirelessProfile.WifiWpaVersion> CONSTANTS = new HashMap<String, WirelessProfile.WifiWpaVersion>();

        static {
            for (WirelessProfile.WifiWpaVersion c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        WifiWpaVersion(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static WirelessProfile.WifiWpaVersion fromValue(String value) {
            WirelessProfile.WifiWpaVersion constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
