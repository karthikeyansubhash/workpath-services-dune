
package com.hp.ws.cdm.network;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Network services
 * 
 */
public class Com_hp_cdm_service_network_version_1_schema {

    @SerializedName("snmpConfig")
    @Expose
    private SnmpConfig snmpConfig;
    @SerializedName("snmpTrapConfig")
    @Expose
    private SnmpTrapConfig snmpTrapConfig;
    @SerializedName("printServices")
    @Expose
    private PrintServices printServices;
    @SerializedName("networkScanServices")
    @Expose
    private NetworkScanServices networkScanServices;
    @SerializedName("discoveryServices")
    @Expose
    private DiscoveryServices discoveryServices;
    @SerializedName("nameResolverServices")
    @Expose
    private NameResolverServices nameResolverServices;
    @SerializedName("proxyConfig")
    @Expose
    private ProxyConfig proxyConfig;
    @SerializedName("proxyDetect")
    @Expose
    private ProxyDetect proxyDetect;
    @SerializedName("internetDiagnostics")
    @Expose
    private InternetDiagnostics internetDiagnostics;

    public SnmpConfig getSnmpConfig() {
        return snmpConfig;
    }

    public void setSnmpConfig(SnmpConfig snmpConfig) {
        this.snmpConfig = snmpConfig;
    }

    public SnmpTrapConfig getSnmpTrapConfig() {
        return snmpTrapConfig;
    }

    public void setSnmpTrapConfig(SnmpTrapConfig snmpTrapConfig) {
        this.snmpTrapConfig = snmpTrapConfig;
    }

    public PrintServices getPrintServices() {
        return printServices;
    }

    public void setPrintServices(PrintServices printServices) {
        this.printServices = printServices;
    }

    public NetworkScanServices getNetworkScanServices() {
        return networkScanServices;
    }

    public void setNetworkScanServices(NetworkScanServices networkScanServices) {
        this.networkScanServices = networkScanServices;
    }

    public DiscoveryServices getDiscoveryServices() {
        return discoveryServices;
    }

    public void setDiscoveryServices(DiscoveryServices discoveryServices) {
        this.discoveryServices = discoveryServices;
    }

    public NameResolverServices getNameResolverServices() {
        return nameResolverServices;
    }

    public void setNameResolverServices(NameResolverServices nameResolverServices) {
        this.nameResolverServices = nameResolverServices;
    }

    public ProxyConfig getProxyConfig() {
        return proxyConfig;
    }

    public void setProxyConfig(ProxyConfig proxyConfig) {
        this.proxyConfig = proxyConfig;
    }

    public ProxyDetect getProxyDetect() {
        return proxyDetect;
    }

    public void setProxyDetect(ProxyDetect proxyDetect) {
        this.proxyDetect = proxyDetect;
    }

    public InternetDiagnostics getInternetDiagnostics() {
        return internetDiagnostics;
    }

    public void setInternetDiagnostics(InternetDiagnostics internetDiagnostics) {
        this.internetDiagnostics = internetDiagnostics;
    }

}
