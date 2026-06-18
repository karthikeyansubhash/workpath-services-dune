
package com.hp.ws.cdm.ioconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.Link;


/**
 * Wireless station configuration
 * 
 */
public class WirelessStationConfig {

    /**
     * Wireless association state
     * 
     */
    @SerializedName("associationState")
    @Expose
    private WirelessStationConfig.WifiAssociationState associationState;
    /**
     * The wireless band of operation
     * 
     */
    @SerializedName("band")
    @Expose
    private WirelessStationConfig.WifiBand band;
    @SerializedName("links")
    @Expose
    private List<Link> links = new ArrayList<Link>();
    /**
     * The wireless locale/region
     * 
     */
    @SerializedName("locale")
    @Expose
    private WirelessStationConfig.WifiLocale locale;
    /**
     * Wireless network details
     * 
     */
    @SerializedName("wirelessNetwork")
    @Expose
    private WifiNetwork wirelessNetwork;

    /**
     * Wireless association state
     * 
     */
    public WirelessStationConfig.WifiAssociationState getAssociationState() {
        return associationState;
    }

    /**
     * Wireless association state
     * 
     */
    public void setAssociationState(WirelessStationConfig.WifiAssociationState associationState) {
        this.associationState = associationState;
    }

    /**
     * The wireless band of operation
     * 
     */
    public WirelessStationConfig.WifiBand getBand() {
        return band;
    }

    /**
     * The wireless band of operation
     * 
     */
    public void setBand(WirelessStationConfig.WifiBand band) {
        this.band = band;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    /**
     * The wireless locale/region
     * 
     */
    public WirelessStationConfig.WifiLocale getLocale() {
        return locale;
    }

    /**
     * The wireless locale/region
     * 
     */
    public void setLocale(WirelessStationConfig.WifiLocale locale) {
        this.locale = locale;
    }

    /**
     * Wireless network details
     * 
     */
    public WifiNetwork getWirelessNetwork() {
        return wirelessNetwork;
    }

    /**
     * Wireless network details
     * 
     */
    public void setWirelessNetwork(WifiNetwork wirelessNetwork) {
        this.wirelessNetwork = wirelessNetwork;
    }


    /**
     * Wireless association state
     * 
     */
    public enum WifiAssociationState {

        @SerializedName("notConfigured")
        NOT_CONFIGURED("notConfigured"),
        @SerializedName("disabled")
        DISABLED("disabled"),
        @SerializedName("noCompatibleNetwork")
        NO_COMPATIBLE_NETWORK("noCompatibleNetwork"),
        @SerializedName("invalidPassPhrase")
        INVALID_PASS_PHRASE("invalidPassPhrase"),
        @SerializedName("otherError")
        OTHER_ERROR("otherError"),
        @SerializedName("associating")
        ASSOCIATING("associating"),
        @SerializedName("associated")
        ASSOCIATED("associated");
        private final String value;
        private final static Map<String, WirelessStationConfig.WifiAssociationState> CONSTANTS = new HashMap<String, WirelessStationConfig.WifiAssociationState>();

        static {
            for (WirelessStationConfig.WifiAssociationState c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        WifiAssociationState(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static WirelessStationConfig.WifiAssociationState fromValue(String value) {
            WirelessStationConfig.WifiAssociationState constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * The wireless band of operation
     * 
     */
    public enum WifiBand {

        @SerializedName("band2pt4Or5Ghz")
        BAND_2_PT_4_OR_5_GHZ("band2pt4Or5Ghz"),
        @SerializedName("band2pt4Ghz")
        BAND_2_PT_4_GHZ("band2pt4Ghz"),
        @SerializedName("band5Ghz")
        BAND_5_GHZ("band5Ghz");
        private final String value;
        private final static Map<String, WirelessStationConfig.WifiBand> CONSTANTS = new HashMap<String, WirelessStationConfig.WifiBand>();

        static {
            for (WirelessStationConfig.WifiBand c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        WifiBand(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static WirelessStationConfig.WifiBand fromValue(String value) {
            WirelessStationConfig.WifiBand constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * The wireless locale/region
     * 
     */
    public enum WifiLocale {

        @SerializedName("unitedStates")
        UNITED_STATES("unitedStates"),
        @SerializedName("restOfWorld")
        REST_OF_WORLD("restOfWorld"),
        @SerializedName("worldWideSafe")
        WORLD_WIDE_SAFE("worldWideSafe");
        private final String value;
        private final static Map<String, WirelessStationConfig.WifiLocale> CONSTANTS = new HashMap<String, WirelessStationConfig.WifiLocale>();

        static {
            for (WirelessStationConfig.WifiLocale c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        WifiLocale(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static WirelessStationConfig.WifiLocale fromValue(String value) {
            WirelessStationConfig.WifiLocale constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
