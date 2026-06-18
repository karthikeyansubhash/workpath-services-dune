
package com.hp.ws.cdm.security;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

public class SmartcardSignInConfig {

    /**
     * List of trusted domains for Smartcard authentication
     * 
     */
    @SerializedName("trustedDomains")
    @Expose
    private List<String> trustedDomains = new ArrayList<String>();
    /**
     * Default domain for Smartcard authentication
     * 
     */
    @SerializedName("defaultDomain")
    @Expose
    private String defaultDomain;
    /**
     * The role ID of the default role for Smartcard authentication
     * 
     */
    @SerializedName("defaultRole")
    @Expose
    private String defaultRole;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("numericOnlyKeypad")
    @Expose
    private Property.FeatureEnabled numericOnlyKeypad;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("reverseDnsCheckEnabled")
    @Expose
    private Property.FeatureEnabled reverseDnsCheckEnabled;
    /**
     * List of preferred domain servers
     * 
     */
    @SerializedName("preferredDomainServers")
    @Expose
    private List<String> preferredDomainServers = new ArrayList<String>();
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("preferredDomainServersEnabled")
    @Expose
    private Property.FeatureEnabled preferredDomainServersEnabled;
    /**
     * This attribute is used to retrieve user's display name from ldap database
     * 
     */
    @SerializedName("retrieveDisplayNameAttribute")
    @Expose
    private String retrieveDisplayNameAttribute;
    /**
     * This attribute is used to retrieve user's email address from ldap database
     * 
     */
    @SerializedName("retrieveEmailAttribute")
    @Expose
    private String retrieveEmailAttribute;
    /**
     * This attribute is used to retrieve user's home directory from ldap database
     * 
     */
    @SerializedName("retrieveHomedirectoryAttribute")
    @Expose
    private String retrieveHomedirectoryAttribute;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("secureConnectionEnabled")
    @Expose
    private Property.FeatureEnabled secureConnectionEnabled;
    /**
     * List of userId to role mappings
     * 
     */
    @SerializedName("userRoleMap")
    @Expose
    private List<UserRoleMap> userRoleMap = new ArrayList<UserRoleMap>();
    /**
     * port number
     * 
     */
    @SerializedName("port")
    @Expose
    private Integer port;
    /**
     * default TCP port number
     * 
     */
    @SerializedName("defaultTcpPort")
    @Expose
    private Integer defaultTcpPort;
    /**
     * default TLS port number
     * 
     */
    @SerializedName("defaultTlsPort")
    @Expose
    private Integer defaultTlsPort;

    /**
     * List of trusted domains for Smartcard authentication
     * 
     */
    public List<String> getTrustedDomains() {
        return trustedDomains;
    }

    /**
     * List of trusted domains for Smartcard authentication
     * 
     */
    public void setTrustedDomains(List<String> trustedDomains) {
        this.trustedDomains = trustedDomains;
    }

    /**
     * Default domain for Smartcard authentication
     * 
     */
    public String getDefaultDomain() {
        return defaultDomain;
    }

    /**
     * Default domain for Smartcard authentication
     * 
     */
    public void setDefaultDomain(String defaultDomain) {
        this.defaultDomain = defaultDomain;
    }

    /**
     * The role ID of the default role for Smartcard authentication
     * 
     */
    public String getDefaultRole() {
        return defaultRole;
    }

    /**
     * The role ID of the default role for Smartcard authentication
     * 
     */
    public void setDefaultRole(String defaultRole) {
        this.defaultRole = defaultRole;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getNumericOnlyKeypad() {
        return numericOnlyKeypad;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setNumericOnlyKeypad(Property.FeatureEnabled numericOnlyKeypad) {
        this.numericOnlyKeypad = numericOnlyKeypad;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getReverseDnsCheckEnabled() {
        return reverseDnsCheckEnabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setReverseDnsCheckEnabled(Property.FeatureEnabled reverseDnsCheckEnabled) {
        this.reverseDnsCheckEnabled = reverseDnsCheckEnabled;
    }

    /**
     * List of preferred domain servers
     * 
     */
    public List<String> getPreferredDomainServers() {
        return preferredDomainServers;
    }

    /**
     * List of preferred domain servers
     * 
     */
    public void setPreferredDomainServers(List<String> preferredDomainServers) {
        this.preferredDomainServers = preferredDomainServers;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getPreferredDomainServersEnabled() {
        return preferredDomainServersEnabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setPreferredDomainServersEnabled(Property.FeatureEnabled preferredDomainServersEnabled) {
        this.preferredDomainServersEnabled = preferredDomainServersEnabled;
    }

    /**
     * This attribute is used to retrieve user's display name from ldap database
     * 
     */
    public String getRetrieveDisplayNameAttribute() {
        return retrieveDisplayNameAttribute;
    }

    /**
     * This attribute is used to retrieve user's display name from ldap database
     * 
     */
    public void setRetrieveDisplayNameAttribute(String retrieveDisplayNameAttribute) {
        this.retrieveDisplayNameAttribute = retrieveDisplayNameAttribute;
    }

    /**
     * This attribute is used to retrieve user's email address from ldap database
     * 
     */
    public String getRetrieveEmailAttribute() {
        return retrieveEmailAttribute;
    }

    /**
     * This attribute is used to retrieve user's email address from ldap database
     * 
     */
    public void setRetrieveEmailAttribute(String retrieveEmailAttribute) {
        this.retrieveEmailAttribute = retrieveEmailAttribute;
    }

    /**
     * This attribute is used to retrieve user's home directory from ldap database
     * 
     */
    public String getRetrieveHomedirectoryAttribute() {
        return retrieveHomedirectoryAttribute;
    }

    /**
     * This attribute is used to retrieve user's home directory from ldap database
     * 
     */
    public void setRetrieveHomedirectoryAttribute(String retrieveHomedirectoryAttribute) {
        this.retrieveHomedirectoryAttribute = retrieveHomedirectoryAttribute;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getSecureConnectionEnabled() {
        return secureConnectionEnabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setSecureConnectionEnabled(Property.FeatureEnabled secureConnectionEnabled) {
        this.secureConnectionEnabled = secureConnectionEnabled;
    }

    /**
     * List of userId to role mappings
     * 
     */
    public List<UserRoleMap> getUserRoleMap() {
        return userRoleMap;
    }

    /**
     * List of userId to role mappings
     * 
     */
    public void setUserRoleMap(List<UserRoleMap> userRoleMap) {
        this.userRoleMap = userRoleMap;
    }

    /**
     * port number
     * 
     */
    public Integer getPort() {
        return port;
    }

    /**
     * port number
     * 
     */
    public void setPort(Integer port) {
        this.port = port;
    }

    /**
     * default TCP port number
     * 
     */
    public Integer getDefaultTcpPort() {
        return defaultTcpPort;
    }

    /**
     * default TCP port number
     * 
     */
    public void setDefaultTcpPort(Integer defaultTcpPort) {
        this.defaultTcpPort = defaultTcpPort;
    }

    /**
     * default TLS port number
     * 
     */
    public Integer getDefaultTlsPort() {
        return defaultTlsPort;
    }

    /**
     * default TLS port number
     * 
     */
    public void setDefaultTlsPort(Integer defaultTlsPort) {
        this.defaultTlsPort = defaultTlsPort;
    }

}
