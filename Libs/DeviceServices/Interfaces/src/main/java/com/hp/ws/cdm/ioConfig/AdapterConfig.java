
package com.hp.ws.cdm.ioconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.Link;
import com.hp.ws.cdm.commonglossary.Property;

public class AdapterConfig {

    /**
     * IO adapter names
     * 
     */
    @SerializedName("adapterName")
    @Expose
    private AdapterConfig.AdapterNames adapterName;
    /**
     * IO adapter types
     * 
     */
    @SerializedName("adapterType")
    @Expose
    private AdapterConfig.AdapterTypes adapterType = AdapterConfig.AdapterTypes.fromValue("unknown");
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("adminEnabled")
    @Expose
    private Property.FeatureEnabled adminEnabled;
    /**
     * Adapter connection state
     * 
     */
    @SerializedName("connectionState")
    @Expose
    private AdapterConfig.ConnectionStates connectionState = AdapterConfig.ConnectionStates.fromValue("unknown");
    /**
     * DNS suffix configuration
     * 
     */
    @SerializedName("dnsSuffixList")
    @Expose
    private SuffixListConfig dnsSuffixList;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("enabled")
    @Expose
    private Property.FeatureEnabled enabled;
    /**
     * Ethernet details
     * 
     */
    @SerializedName("ethConfig")
    @Expose
    private EthConfig ethConfig;
    /**
     * Device host, domain names
     * 
     */
    @SerializedName("identity")
    @Expose
    private DeviceIdentity identity;
    /**
     * Config precedence determines the order of configuration methods to be used for selecting configuration parameter values available via different methods.
     * 
     */
    @SerializedName("ipConfigPrecedence")
    @Expose
    private List<ConfigPrecedenceMethod> ipConfigPrecedence = new ArrayList<ConfigPrecedenceMethod>();
    /**
     * IPv4 configuration
     * 
     */
    @SerializedName("ipv4")
    @Expose
    private Ipv4Config ipv4;
    /**
     * IPv6 configuration
     * 
     */
    @SerializedName("ipv6")
    @Expose
    private Ipv6Config ipv6;
    @SerializedName("links")
    @Expose
    private List<Link> links = new ArrayList<Link>();
    @SerializedName("macAddress")
    @Expose
    private String macAddress;
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
     * Wireless station configuration
     * 
     */
    @SerializedName("wifiStationConfig")
    @Expose
    private WirelessStationConfig wifiStationConfig;
    @SerializedName("usbConfig")
    @Expose
    private UsbConfig usbConfig;

    /**
     * IO adapter names
     * 
     */
    public AdapterConfig.AdapterNames getAdapterName() {
        return adapterName;
    }

    /**
     * IO adapter names
     * 
     */
    public void setAdapterName(AdapterConfig.AdapterNames adapterName) {
        this.adapterName = adapterName;
    }

    /**
     * IO adapter types
     * 
     */
    public AdapterConfig.AdapterTypes getAdapterType() {
        return adapterType;
    }

    /**
     * IO adapter types
     * 
     */
    public void setAdapterType(AdapterConfig.AdapterTypes adapterType) {
        this.adapterType = adapterType;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getAdminEnabled() {
        return adminEnabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setAdminEnabled(Property.FeatureEnabled adminEnabled) {
        this.adminEnabled = adminEnabled;
    }

    /**
     * Adapter connection state
     * 
     */
    public AdapterConfig.ConnectionStates getConnectionState() {
        return connectionState;
    }

    /**
     * Adapter connection state
     * 
     */
    public void setConnectionState(AdapterConfig.ConnectionStates connectionState) {
        this.connectionState = connectionState;
    }

    /**
     * DNS suffix configuration
     * 
     */
    public SuffixListConfig getDnsSuffixList() {
        return dnsSuffixList;
    }

    /**
     * DNS suffix configuration
     * 
     */
    public void setDnsSuffixList(SuffixListConfig dnsSuffixList) {
        this.dnsSuffixList = dnsSuffixList;
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
     * Ethernet details
     * 
     */
    public EthConfig getEthConfig() {
        return ethConfig;
    }

    /**
     * Ethernet details
     * 
     */
    public void setEthConfig(EthConfig ethConfig) {
        this.ethConfig = ethConfig;
    }

    /**
     * Device host, domain names
     * 
     */
    public DeviceIdentity getIdentity() {
        return identity;
    }

    /**
     * Device host, domain names
     * 
     */
    public void setIdentity(DeviceIdentity identity) {
        this.identity = identity;
    }

    /**
     * Config precedence determines the order of configuration methods to be used for selecting configuration parameter values available via different methods.
     * 
     */
    public List<ConfigPrecedenceMethod> getIpConfigPrecedence() {
        return ipConfigPrecedence;
    }

    /**
     * Config precedence determines the order of configuration methods to be used for selecting configuration parameter values available via different methods.
     * 
     */
    public void setIpConfigPrecedence(List<ConfigPrecedenceMethod> ipConfigPrecedence) {
        this.ipConfigPrecedence = ipConfigPrecedence;
    }

    /**
     * IPv4 configuration
     * 
     */
    public Ipv4Config getIpv4() {
        return ipv4;
    }

    /**
     * IPv4 configuration
     * 
     */
    public void setIpv4(Ipv4Config ipv4) {
        this.ipv4 = ipv4;
    }

    /**
     * IPv6 configuration
     * 
     */
    public Ipv6Config getIpv6() {
        return ipv6;
    }

    /**
     * IPv6 configuration
     * 
     */
    public void setIpv6(Ipv6Config ipv6) {
        this.ipv6 = ipv6;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
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
     * Wireless station configuration
     * 
     */
    public WirelessStationConfig getWifiStationConfig() {
        return wifiStationConfig;
    }

    /**
     * Wireless station configuration
     * 
     */
    public void setWifiStationConfig(WirelessStationConfig wifiStationConfig) {
        this.wifiStationConfig = wifiStationConfig;
    }

    public UsbConfig getUsbConfig() {
        return usbConfig;
    }

    public void setUsbConfig(UsbConfig usbConfig) {
        this.usbConfig = usbConfig;
    }


    /**
     * IO adapter names
     * 
     */
    public enum AdapterNames {

        @SerializedName("eth0")
        ETH_0("eth0"),
        @SerializedName("eth1")
        ETH_1("eth1"),
        @SerializedName("wifi0")
        WIFI_0("wifi0"),
        @SerializedName("wifi1")
        WIFI_1("wifi1"),
        @SerializedName("usb")
        USB("usb");
        private final String value;
        private final static Map<String, AdapterConfig.AdapterNames> CONSTANTS = new HashMap<String, AdapterConfig.AdapterNames>();

        static {
            for (AdapterConfig.AdapterNames c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        AdapterNames(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static AdapterConfig.AdapterNames fromValue(String value) {
            AdapterConfig.AdapterNames constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * IO adapter types
     * 
     */
    public enum AdapterTypes {

        @SerializedName("wiredEthernet")
        WIRED_ETHERNET("wiredEthernet"),
        @SerializedName("wifiAccessPoint")
        WIFI_ACCESS_POINT("wifiAccessPoint"),
        @SerializedName("wifiStation")
        WIFI_STATION("wifiStation"),
        @SerializedName("usbHost")
        USB_HOST("usbHost"),
        @SerializedName("usbDevice")
        USB_DEVICE("usbDevice"),
        @SerializedName("ble")
        BLE("ble"),
        @SerializedName("unknown")
        UNKNOWN("unknown");
        private final String value;
        private final static Map<String, AdapterConfig.AdapterTypes> CONSTANTS = new HashMap<String, AdapterConfig.AdapterTypes>();

        static {
            for (AdapterConfig.AdapterTypes c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        AdapterTypes(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static AdapterConfig.AdapterTypes fromValue(String value) {
            AdapterConfig.AdapterTypes constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * Adapter connection state
     * 
     */
    public enum ConnectionStates {

        @SerializedName("unknown")
        UNKNOWN("unknown"),
        @SerializedName("associating")
        ASSOCIATING("associating"),
        @SerializedName("acquiringAddress")
        ACQUIRING_ADDRESS("acquiringAddress"),
        @SerializedName("connected")
        CONNECTED("connected"),
        @SerializedName("connctedLocal")
        CONNCTED_LOCAL("connctedLocal"),
        @SerializedName("connectedLocal")
        CONNECTED_LOCAL("connectedLocal"),
        @SerializedName("connectedInternet")
        CONNECTED_INTERNET("connectedInternet"),
        @SerializedName("disabled")
        DISABLED("disabled"),
        @SerializedName("disconnected")
        DISCONNECTED("disconnected"),
        @SerializedName("off")
        OFF("off"),
        @SerializedName("notConfigured")
        NOT_CONFIGURED("notConfigured");
        private final String value;
        private final static Map<String, AdapterConfig.ConnectionStates> CONSTANTS = new HashMap<String, AdapterConfig.ConnectionStates>();

        static {
            for (AdapterConfig.ConnectionStates c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        ConnectionStates(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static AdapterConfig.ConnectionStates fromValue(String value) {
            AdapterConfig.ConnectionStates constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
