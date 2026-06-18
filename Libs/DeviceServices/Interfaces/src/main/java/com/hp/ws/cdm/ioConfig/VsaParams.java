
package com.hp.ws.cdm.ioconfig;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VsaParams {

    @SerializedName("ssid")
    @Expose
    private String ssid;
    @SerializedName("macAddress")
    @Expose
    private String macAddress;
    @SerializedName("signalStrengthDbm")
    @Expose
    private Integer signalStrengthDbm;
    @SerializedName("numberAccessPoints")
    @Expose
    private Integer numberAccessPoints;
    @SerializedName("wepKeyIndex")
    @Expose
    private Integer wepKeyIndex;
    /**
     * Wireless network authentication mode
     * 
     */
    @SerializedName("authenticationMode")
    @Expose
    private com.hp.ws.cdm.ioconfig.WifiNetwork.WifiAuthenticationMode authenticationMode;
    /**
     * Wireless Encryption protocol
     * 
     */
    @SerializedName("encryptionType")
    @Expose
    private com.hp.ws.cdm.ioconfig.WifiNetwork.WifiEncryptionType encryptionType;
    @SerializedName("ipAddress")
    @Expose
    private String ipAddress;
    @SerializedName("bssid")
    @Expose
    private String bssid;
    @SerializedName("caseInsensitiveSsid")
    @Expose
    private String caseInsensitiveSsid;
    @SerializedName("numberInconsistentAccessPoints")
    @Expose
    private Integer numberInconsistentAccessPoints;
    @SerializedName("recommendedChannel")
    @Expose
    private Integer recommendedChannel;

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public Integer getSignalStrengthDbm() {
        return signalStrengthDbm;
    }

    public void setSignalStrengthDbm(Integer signalStrengthDbm) {
        this.signalStrengthDbm = signalStrengthDbm;
    }

    public Integer getNumberAccessPoints() {
        return numberAccessPoints;
    }

    public void setNumberAccessPoints(Integer numberAccessPoints) {
        this.numberAccessPoints = numberAccessPoints;
    }

    public Integer getWepKeyIndex() {
        return wepKeyIndex;
    }

    public void setWepKeyIndex(Integer wepKeyIndex) {
        this.wepKeyIndex = wepKeyIndex;
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

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public String getCaseInsensitiveSsid() {
        return caseInsensitiveSsid;
    }

    public void setCaseInsensitiveSsid(String caseInsensitiveSsid) {
        this.caseInsensitiveSsid = caseInsensitiveSsid;
    }

    public Integer getNumberInconsistentAccessPoints() {
        return numberInconsistentAccessPoints;
    }

    public void setNumberInconsistentAccessPoints(Integer numberInconsistentAccessPoints) {
        this.numberInconsistentAccessPoints = numberInconsistentAccessPoints;
    }

    public Integer getRecommendedChannel() {
        return recommendedChannel;
    }

    public void setRecommendedChannel(Integer recommendedChannel) {
        this.recommendedChannel = recommendedChannel;
    }

}
