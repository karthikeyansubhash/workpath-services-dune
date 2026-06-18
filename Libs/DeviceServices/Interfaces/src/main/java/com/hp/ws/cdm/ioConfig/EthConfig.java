
package com.hp.ws.cdm.ioconfig;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;


/**
 * Ethernet details
 * 
 */
public class EthConfig {

    /**
     *  802.1x authentication configuration
     * 
     */
    @SerializedName("dot1xAuthConfig")
    @Expose
    private Dot1xAuthentication dot1xAuthConfig;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("dot1xEnabled")
    @Expose
    private Property.FeatureEnabled dot1xEnabled;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("energyEfficientEthernet")
    @Expose
    private Property.FeatureEnabled energyEfficientEthernet;
    /**
     * Ethernet link configuration
     * 
     */
    @SerializedName("linkConfig")
    @Expose
    private EthConfig.EthLinkConfigTypes linkConfig;
    /**
     * Ethernet link speed
     * 
     */
    @SerializedName("linkSpeed")
    @Expose
    private EthConfig.EthLinkSpeedTypes linkSpeed;

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
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getDot1xEnabled() {
        return dot1xEnabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setDot1xEnabled(Property.FeatureEnabled dot1xEnabled) {
        this.dot1xEnabled = dot1xEnabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getEnergyEfficientEthernet() {
        return energyEfficientEthernet;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setEnergyEfficientEthernet(Property.FeatureEnabled energyEfficientEthernet) {
        this.energyEfficientEthernet = energyEfficientEthernet;
    }

    /**
     * Ethernet link configuration
     * 
     */
    public EthConfig.EthLinkConfigTypes getLinkConfig() {
        return linkConfig;
    }

    /**
     * Ethernet link configuration
     * 
     */
    public void setLinkConfig(EthConfig.EthLinkConfigTypes linkConfig) {
        this.linkConfig = linkConfig;
    }

    /**
     * Ethernet link speed
     * 
     */
    public EthConfig.EthLinkSpeedTypes getLinkSpeed() {
        return linkSpeed;
    }

    /**
     * Ethernet link speed
     * 
     */
    public void setLinkSpeed(EthConfig.EthLinkSpeedTypes linkSpeed) {
        this.linkSpeed = linkSpeed;
    }


    /**
     * Ethernet link configuration
     * 
     */
    public enum EthLinkConfigTypes {

        @SerializedName("link10half")
        LINK_10_HALF("link10half"),
        @SerializedName("link10full")
        LINK_10_FULL("link10full"),
        @SerializedName("link10auto")
        LINK_10_AUTO("link10auto"),
        @SerializedName("link100half")
        LINK_100_HALF("link100half"),
        @SerializedName("link100full")
        LINK_100_FULL("link100full"),
        @SerializedName("link100auto")
        LINK_100_AUTO("link100auto"),
        @SerializedName("link1000full")
        LINK_1000_FULL("link1000full"),
        @SerializedName("automatic")
        AUTOMATIC("automatic");
        private final String value;
        private final static Map<String, EthConfig.EthLinkConfigTypes> CONSTANTS = new HashMap<String, EthConfig.EthLinkConfigTypes>();

        static {
            for (EthConfig.EthLinkConfigTypes c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        EthLinkConfigTypes(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static EthConfig.EthLinkConfigTypes fromValue(String value) {
            EthConfig.EthLinkConfigTypes constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * Ethernet link speed
     * 
     */
    public enum EthLinkSpeedTypes {

        @SerializedName("unknown")
        UNKNOWN("unknown"),
        @SerializedName("link10half")
        LINK_10_HALF("link10half"),
        @SerializedName("link10full")
        LINK_10_FULL("link10full"),
        @SerializedName("link100half")
        LINK_100_HALF("link100half"),
        @SerializedName("link100full")
        LINK_100_FULL("link100full"),
        @SerializedName("link1000full")
        LINK_1000_FULL("link1000full");
        private final String value;
        private final static Map<String, EthConfig.EthLinkSpeedTypes> CONSTANTS = new HashMap<String, EthConfig.EthLinkSpeedTypes>();

        static {
            for (EthConfig.EthLinkSpeedTypes c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        EthLinkSpeedTypes(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static EthConfig.EthLinkSpeedTypes fromValue(String value) {
            EthConfig.EthLinkSpeedTypes constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
