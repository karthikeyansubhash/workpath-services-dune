
package com.hp.ws.cdm.ioconfig;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;


/**
 * Wireless network details
 * 
 */
public class WifiNetwork {

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
    private WifiNetwork.WifiAuthenticationMode authenticationMode;
    /**
     * The Basic Service Set Identifier(SSID) of the wireless network (LAN)
     * 
     */
    @SerializedName("bssid")
    @Expose
    private String bssid;
    /**
     * The channel on which the wireless network operates
     * 
     */
    @SerializedName("channel")
    @Expose
    private Integer channel;
    /**
     * Wireless communication mode
     * 
     */
    @SerializedName("communicationMode")
    @Expose
    private WifiNetwork.WifiCommunicationMode communicationMode;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("compatibility")
    @Expose
    private Property.FeatureEnabled compatibility;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("dfsChannel")
    @Expose
    private Property.FeatureEnabled dfsChannel;
    /**
     * Wireless Encryption protocol
     * 
     */
    @SerializedName("encryptionType")
    @Expose
    private WifiNetwork.WifiEncryptionType encryptionType;
    /**
     * Wireless signal strength
     * 
     */
    @SerializedName("signalStrength")
    @Expose
    private SignalStrengthInfo signalStrength;
    /**
     * The Service Set Identifier(SSID) aka name of the wireless network (LAN)
     * 
     */
    @SerializedName("ssid")
    @Expose
    private String ssid;
    /**
     * WPA version supported by access point
     * 
     */
    @SerializedName("wpaVersion")
    @Expose
    private WifiNetwork.WifiWpaVersionSupported wpaVersion;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("wpsSupport")
    @Expose
    private Property.FeatureEnabled wpsSupport;

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
    public WifiNetwork.WifiAuthenticationMode getAuthenticationMode() {
        return authenticationMode;
    }

    /**
     * Wireless network authentication mode
     * 
     */
    public void setAuthenticationMode(WifiNetwork.WifiAuthenticationMode authenticationMode) {
        this.authenticationMode = authenticationMode;
    }

    /**
     * The Basic Service Set Identifier(SSID) of the wireless network (LAN)
     * 
     */
    public String getBssid() {
        return bssid;
    }

    /**
     * The Basic Service Set Identifier(SSID) of the wireless network (LAN)
     * 
     */
    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    /**
     * The channel on which the wireless network operates
     * 
     */
    public Integer getChannel() {
        return channel;
    }

    /**
     * The channel on which the wireless network operates
     * 
     */
    public void setChannel(Integer channel) {
        this.channel = channel;
    }

    /**
     * Wireless communication mode
     * 
     */
    public WifiNetwork.WifiCommunicationMode getCommunicationMode() {
        return communicationMode;
    }

    /**
     * Wireless communication mode
     * 
     */
    public void setCommunicationMode(WifiNetwork.WifiCommunicationMode communicationMode) {
        this.communicationMode = communicationMode;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getCompatibility() {
        return compatibility;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setCompatibility(Property.FeatureEnabled compatibility) {
        this.compatibility = compatibility;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getDfsChannel() {
        return dfsChannel;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setDfsChannel(Property.FeatureEnabled dfsChannel) {
        this.dfsChannel = dfsChannel;
    }

    /**
     * Wireless Encryption protocol
     * 
     */
    public WifiNetwork.WifiEncryptionType getEncryptionType() {
        return encryptionType;
    }

    /**
     * Wireless Encryption protocol
     * 
     */
    public void setEncryptionType(WifiNetwork.WifiEncryptionType encryptionType) {
        this.encryptionType = encryptionType;
    }

    /**
     * Wireless signal strength
     * 
     */
    public SignalStrengthInfo getSignalStrength() {
        return signalStrength;
    }

    /**
     * Wireless signal strength
     * 
     */
    public void setSignalStrength(SignalStrengthInfo signalStrength) {
        this.signalStrength = signalStrength;
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
     * WPA version supported by access point
     * 
     */
    public WifiNetwork.WifiWpaVersionSupported getWpaVersion() {
        return wpaVersion;
    }

    /**
     * WPA version supported by access point
     * 
     */
    public void setWpaVersion(WifiNetwork.WifiWpaVersionSupported wpaVersion) {
        this.wpaVersion = wpaVersion;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getWpsSupport() {
        return wpsSupport;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setWpsSupport(Property.FeatureEnabled wpsSupport) {
        this.wpsSupport = wpsSupport;
    }


    /**
     * Wireless network authentication mode
     * 
     */
    public enum WifiAuthenticationMode {

        @SerializedName("open")
        OPEN("open"),
        @SerializedName("openThenShared")
        OPEN_THEN_SHARED("openThenShared"),
        @SerializedName("wpaPersonal")
        WPA_PERSONAL("wpaPersonal"),
        @SerializedName("wpaEnterprise")
        WPA_ENTERPRISE("wpaEnterprise"),
        @SerializedName("wepEnterprise")
        WEP_ENTERPRISE("wepEnterprise");
        private final String value;
        private final static Map<String, WifiNetwork.WifiAuthenticationMode> CONSTANTS = new HashMap<String, WifiNetwork.WifiAuthenticationMode>();

        static {
            for (WifiNetwork.WifiAuthenticationMode c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        WifiAuthenticationMode(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static WifiNetwork.WifiAuthenticationMode fromValue(String value) {
            WifiNetwork.WifiAuthenticationMode constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * Wireless communication mode
     * 
     */
    public enum WifiCommunicationMode {

        @SerializedName("adhoc")
        ADHOC("adhoc"),
        @SerializedName("infrastructure")
        INFRASTRUCTURE("infrastructure");
        private final String value;
        private final static Map<String, WifiNetwork.WifiCommunicationMode> CONSTANTS = new HashMap<String, WifiNetwork.WifiCommunicationMode>();

        static {
            for (WifiNetwork.WifiCommunicationMode c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        WifiCommunicationMode(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static WifiNetwork.WifiCommunicationMode fromValue(String value) {
            WifiNetwork.WifiCommunicationMode constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * Wireless Encryption protocol
     * 
     */
    public enum WifiEncryptionType {

        @SerializedName("none")
        NONE("none"),
        @SerializedName("wep")
        WEP("wep"),
        @SerializedName("tkip")
        TKIP("tkip"),
        @SerializedName("aes")
        AES("aes"),
        @SerializedName("aesOrTkip")
        AES_OR_TKIP("aesOrTkip");
        private final String value;
        private final static Map<String, WifiNetwork.WifiEncryptionType> CONSTANTS = new HashMap<String, WifiNetwork.WifiEncryptionType>();

        static {
            for (WifiNetwork.WifiEncryptionType c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        WifiEncryptionType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static WifiNetwork.WifiEncryptionType fromValue(String value) {
            WifiNetwork.WifiEncryptionType constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * WPA version supported by access point
     * 
     */
    public enum WifiWpaVersionSupported {

        @SerializedName("none")
        NONE("none"),
        @SerializedName("wpa")
        WPA("wpa"),
        @SerializedName("wpa2")
        WPA_2("wpa2"),
        @SerializedName("wpa3")
        WPA_3("wpa3"),
        @SerializedName("wpaOrWpa2")
        WPA_OR_WPA_2("wpaOrWpa2"),
        @SerializedName("wpa2OrWpa3")
        WPA_2_OR_WPA_3("wpa2OrWpa3"),
        @SerializedName("auto")
        AUTO("auto");
        private final String value;
        private final static Map<String, WifiNetwork.WifiWpaVersionSupported> CONSTANTS = new HashMap<String, WifiNetwork.WifiWpaVersionSupported>();

        static {
            for (WifiNetwork.WifiWpaVersionSupported c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        WifiWpaVersionSupported(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static WifiNetwork.WifiWpaVersionSupported fromValue(String value) {
            WifiNetwork.WifiWpaVersionSupported constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
