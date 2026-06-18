
package com.hp.ws.cdm.ioconfig;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;


/**
 *  802.1x authentication configuration
 * 
 */
public class Dot1xAuthentication {

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("eapLeapEnabled")
    @Expose
    private Property.FeatureEnabled eapLeapEnabled;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("eapPeapEnabled")
    @Expose
    private Property.FeatureEnabled eapPeapEnabled;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("eapTlsEnabled")
    @Expose
    private Property.FeatureEnabled eapTlsEnabled;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("exactServerIdMatch")
    @Expose
    private Property.FeatureEnabled exactServerIdMatch;
    /**
     *  802.1x Password
     * 
     */
    @SerializedName("password")
    @Expose
    private String password;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("blockNetworkOnAuthFailure")
    @Expose
    private Property.FeatureEnabled blockNetworkOnAuthFailure;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("reauthenticate")
    @Expose
    private Property.FeatureEnabled reauthenticate;
    /**
     * Server ID/name to validate the authentication server certificate
     * 
     */
    @SerializedName("serverIdentity")
    @Expose
    private String serverIdentity;
    /**
     *  802.1x user name
     * 
     */
    @SerializedName("userName")
    @Expose
    private String userName;

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getEapLeapEnabled() {
        return eapLeapEnabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setEapLeapEnabled(Property.FeatureEnabled eapLeapEnabled) {
        this.eapLeapEnabled = eapLeapEnabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getEapPeapEnabled() {
        return eapPeapEnabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setEapPeapEnabled(Property.FeatureEnabled eapPeapEnabled) {
        this.eapPeapEnabled = eapPeapEnabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getEapTlsEnabled() {
        return eapTlsEnabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setEapTlsEnabled(Property.FeatureEnabled eapTlsEnabled) {
        this.eapTlsEnabled = eapTlsEnabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getExactServerIdMatch() {
        return exactServerIdMatch;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setExactServerIdMatch(Property.FeatureEnabled exactServerIdMatch) {
        this.exactServerIdMatch = exactServerIdMatch;
    }

    /**
     *  802.1x Password
     * 
     */
    public String getPassword() {
        return password;
    }

    /**
     *  802.1x Password
     * 
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getBlockNetworkOnAuthFailure() {
        return blockNetworkOnAuthFailure;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setBlockNetworkOnAuthFailure(Property.FeatureEnabled blockNetworkOnAuthFailure) {
        this.blockNetworkOnAuthFailure = blockNetworkOnAuthFailure;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getReauthenticate() {
        return reauthenticate;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setReauthenticate(Property.FeatureEnabled reauthenticate) {
        this.reauthenticate = reauthenticate;
    }

    /**
     * Server ID/name to validate the authentication server certificate
     * 
     */
    public String getServerIdentity() {
        return serverIdentity;
    }

    /**
     * Server ID/name to validate the authentication server certificate
     * 
     */
    public void setServerIdentity(String serverIdentity) {
        this.serverIdentity = serverIdentity;
    }

    /**
     *  802.1x user name
     * 
     */
    public String getUserName() {
        return userName;
    }

    /**
     *  802.1x user name
     * 
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

}
