
package com.hp.ws.cdm.ioconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.Link;

public class WirelessConfig {

    /**
     * The wireless band of operation
     * 
     */
    @SerializedName("band")
    @Expose
    private com.hp.ws.cdm.ioconfig.WirelessStationConfig.WifiBand band;
    @SerializedName("links")
    @Expose
    private List<Link> links = new ArrayList<Link>();
    @SerializedName("preferredProfile")
    @Expose
    private WirelessConfig.ProfileName preferredProfile;
    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("wlanProfile1")
    @Expose
    private WirelessProfile wlanProfile1;
    @SerializedName("wlanProfile2")
    @Expose
    private WirelessProfile wlanProfile2;
    @SerializedName("wlanProfile3")
    @Expose
    private WirelessProfile wlanProfile3;
    @SerializedName("wlanProfile4")
    @Expose
    private WirelessProfile wlanProfile4;

    /**
     * The wireless band of operation
     * 
     */
    public com.hp.ws.cdm.ioconfig.WirelessStationConfig.WifiBand getBand() {
        return band;
    }

    /**
     * The wireless band of operation
     * 
     */
    public void setBand(com.hp.ws.cdm.ioconfig.WirelessStationConfig.WifiBand band) {
        this.band = band;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public WirelessConfig.ProfileName getPreferredProfile() {
        return preferredProfile;
    }

    public void setPreferredProfile(WirelessConfig.ProfileName preferredProfile) {
        this.preferredProfile = preferredProfile;
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

    public WirelessProfile getWlanProfile1() {
        return wlanProfile1;
    }

    public void setWlanProfile1(WirelessProfile wlanProfile1) {
        this.wlanProfile1 = wlanProfile1;
    }

    public WirelessProfile getWlanProfile2() {
        return wlanProfile2;
    }

    public void setWlanProfile2(WirelessProfile wlanProfile2) {
        this.wlanProfile2 = wlanProfile2;
    }

    public WirelessProfile getWlanProfile3() {
        return wlanProfile3;
    }

    public void setWlanProfile3(WirelessProfile wlanProfile3) {
        this.wlanProfile3 = wlanProfile3;
    }

    public WirelessProfile getWlanProfile4() {
        return wlanProfile4;
    }

    public void setWlanProfile4(WirelessProfile wlanProfile4) {
        this.wlanProfile4 = wlanProfile4;
    }

    public enum ProfileName {

        @SerializedName("wlanProfile1")
        WLAN_PROFILE_1("wlanProfile1"),
        @SerializedName("wlanProfile2")
        WLAN_PROFILE_2("wlanProfile2"),
        @SerializedName("wlanProfile3")
        WLAN_PROFILE_3("wlanProfile3"),
        @SerializedName("wlanProfile4")
        WLAN_PROFILE_4("wlanProfile4");
        private final String value;
        private final static Map<String, WirelessConfig.ProfileName> CONSTANTS = new HashMap<String, WirelessConfig.ProfileName>();

        static {
            for (WirelessConfig.ProfileName c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        ProfileName(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static WirelessConfig.ProfileName fromValue(String value) {
            WirelessConfig.ProfileName constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
