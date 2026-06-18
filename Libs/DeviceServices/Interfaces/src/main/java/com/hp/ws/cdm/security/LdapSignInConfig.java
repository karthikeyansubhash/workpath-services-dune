
package com.hp.ws.cdm.security;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

public class LdapSignInConfig {

    /**
     * LDAP server Address, Can be Name or IP address of the server
     * 
     */
    @SerializedName("serverAddress")
    @Expose
    private String serverAddress;
    /**
     * LDAP server port number
     * 
     */
    @SerializedName("port")
    @Expose
    private Integer port;
    /**
     * LDAP server default TCP port number
     * 
     */
    @SerializedName("defaultTcpPort")
    @Expose
    private Integer defaultTcpPort;
    /**
     * LDAP server default TLS port number
     * 
     */
    @SerializedName("defaultTlsPort")
    @Expose
    private Integer defaultTlsPort;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("secureConnectionEnabled")
    @Expose
    private Property.FeatureEnabled secureConnectionEnabled;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("productCredentialsEnabled")
    @Expose
    private Property.FeatureEnabled productCredentialsEnabled;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("adminstratorCredentialsEnabled")
    @Expose
    private Property.FeatureEnabled adminstratorCredentialsEnabled;
    /**
     * LDAP Bind Prefix
     * 
     */
    @SerializedName("ldapBindPrefix")
    @Expose
    private String ldapBindPrefix;
    /**
     * LDAP Administrator's DN, (example: cn=admin, dc=users, dc=com), It Is used when adminstratorCredentialsEnabled is true
     * 
     */
    @SerializedName("ldapAdministratorDN")
    @Expose
    private String ldapAdministratorDN;
    /**
     * LDAP Administrator's Password, It is used when adminstratorCredentialsEnabled is true
     * 
     */
    @SerializedName("ldapAdministratorPassword")
    @Expose
    private String ldapAdministratorPassword;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("ldapAdministratorPasswordConfigured")
    @Expose
    private Property.FeatureEnabled ldapAdministratorPasswordConfigured;
    @SerializedName("ldapBindRoots")
    @Expose
    private List<String> ldapBindRoots = new ArrayList<String>();
    /**
     * This attribute is used to match the name entered with this attribute on ldap database
     * 
     */
    @SerializedName("matchLdapNameAttribute")
    @Expose
    private String matchLdapNameAttribute;
    /**
     * This attribute is used to retrieve User's Email Address from ldap database
     * 
     */
    @SerializedName("retrieveLdapEmailAttribute")
    @Expose
    private String retrieveLdapEmailAttribute;
    /**
     * This attribute is used to retrieve User's Name from ldap database
     * 
     */
    @SerializedName("retrieveLdapNameAttribute")
    @Expose
    private String retrieveLdapNameAttribute;
    /**
     * This attribute is used to retrieve User's group from ldap database
     * 
     */
    @SerializedName("retrieveLdapGroupAttribute")
    @Expose
    private String retrieveLdapGroupAttribute;
    /**
     * This attribute is used to retrieve User's home directory from ldap database
     * 
     */
    @SerializedName("retrieveLdapHomedirectoryAttribute")
    @Expose
    private String retrieveLdapHomedirectoryAttribute;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("enableGroupExactMatch")
    @Expose
    private Property.FeatureEnabled enableGroupExactMatch;
    /**
     * The role ID of the default role for LDAP authentication
     * 
     */
    @SerializedName("defaultRole")
    @Expose
    private String defaultRole;
    /**
     * List of userId to role mappings
     * 
     */
    @SerializedName("userRoleMap")
    @Expose
    private List<UserRoleMap> userRoleMap = new ArrayList<UserRoleMap>();

    /**
     * LDAP server Address, Can be Name or IP address of the server
     * 
     */
    public String getServerAddress() {
        return serverAddress;
    }

    /**
     * LDAP server Address, Can be Name or IP address of the server
     * 
     */
    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    /**
     * LDAP server port number
     * 
     */
    public Integer getPort() {
        return port;
    }

    /**
     * LDAP server port number
     * 
     */
    public void setPort(Integer port) {
        this.port = port;
    }

    /**
     * LDAP server default TCP port number
     * 
     */
    public Integer getDefaultTcpPort() {
        return defaultTcpPort;
    }

    /**
     * LDAP server default TCP port number
     * 
     */
    public void setDefaultTcpPort(Integer defaultTcpPort) {
        this.defaultTcpPort = defaultTcpPort;
    }

    /**
     * LDAP server default TLS port number
     * 
     */
    public Integer getDefaultTlsPort() {
        return defaultTlsPort;
    }

    /**
     * LDAP server default TLS port number
     * 
     */
    public void setDefaultTlsPort(Integer defaultTlsPort) {
        this.defaultTlsPort = defaultTlsPort;
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
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getProductCredentialsEnabled() {
        return productCredentialsEnabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setProductCredentialsEnabled(Property.FeatureEnabled productCredentialsEnabled) {
        this.productCredentialsEnabled = productCredentialsEnabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getAdminstratorCredentialsEnabled() {
        return adminstratorCredentialsEnabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setAdminstratorCredentialsEnabled(Property.FeatureEnabled adminstratorCredentialsEnabled) {
        this.adminstratorCredentialsEnabled = adminstratorCredentialsEnabled;
    }

    /**
     * LDAP Bind Prefix
     * 
     */
    public String getLdapBindPrefix() {
        return ldapBindPrefix;
    }

    /**
     * LDAP Bind Prefix
     * 
     */
    public void setLdapBindPrefix(String ldapBindPrefix) {
        this.ldapBindPrefix = ldapBindPrefix;
    }

    /**
     * LDAP Administrator's DN, (example: cn=admin, dc=users, dc=com), It Is used when adminstratorCredentialsEnabled is true
     * 
     */
    public String getLdapAdministratorDN() {
        return ldapAdministratorDN;
    }

    /**
     * LDAP Administrator's DN, (example: cn=admin, dc=users, dc=com), It Is used when adminstratorCredentialsEnabled is true
     * 
     */
    public void setLdapAdministratorDN(String ldapAdministratorDN) {
        this.ldapAdministratorDN = ldapAdministratorDN;
    }

    /**
     * LDAP Administrator's Password, It is used when adminstratorCredentialsEnabled is true
     * 
     */
    public String getLdapAdministratorPassword() {
        return ldapAdministratorPassword;
    }

    /**
     * LDAP Administrator's Password, It is used when adminstratorCredentialsEnabled is true
     * 
     */
    public void setLdapAdministratorPassword(String ldapAdministratorPassword) {
        this.ldapAdministratorPassword = ldapAdministratorPassword;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getLdapAdministratorPasswordConfigured() {
        return ldapAdministratorPasswordConfigured;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setLdapAdministratorPasswordConfigured(Property.FeatureEnabled ldapAdministratorPasswordConfigured) {
        this.ldapAdministratorPasswordConfigured = ldapAdministratorPasswordConfigured;
    }

    public List<String> getLdapBindRoots() {
        return ldapBindRoots;
    }

    public void setLdapBindRoots(List<String> ldapBindRoots) {
        this.ldapBindRoots = ldapBindRoots;
    }

    /**
     * This attribute is used to match the name entered with this attribute on ldap database
     * 
     */
    public String getMatchLdapNameAttribute() {
        return matchLdapNameAttribute;
    }

    /**
     * This attribute is used to match the name entered with this attribute on ldap database
     * 
     */
    public void setMatchLdapNameAttribute(String matchLdapNameAttribute) {
        this.matchLdapNameAttribute = matchLdapNameAttribute;
    }

    /**
     * This attribute is used to retrieve User's Email Address from ldap database
     * 
     */
    public String getRetrieveLdapEmailAttribute() {
        return retrieveLdapEmailAttribute;
    }

    /**
     * This attribute is used to retrieve User's Email Address from ldap database
     * 
     */
    public void setRetrieveLdapEmailAttribute(String retrieveLdapEmailAttribute) {
        this.retrieveLdapEmailAttribute = retrieveLdapEmailAttribute;
    }

    /**
     * This attribute is used to retrieve User's Name from ldap database
     * 
     */
    public String getRetrieveLdapNameAttribute() {
        return retrieveLdapNameAttribute;
    }

    /**
     * This attribute is used to retrieve User's Name from ldap database
     * 
     */
    public void setRetrieveLdapNameAttribute(String retrieveLdapNameAttribute) {
        this.retrieveLdapNameAttribute = retrieveLdapNameAttribute;
    }

    /**
     * This attribute is used to retrieve User's group from ldap database
     * 
     */
    public String getRetrieveLdapGroupAttribute() {
        return retrieveLdapGroupAttribute;
    }

    /**
     * This attribute is used to retrieve User's group from ldap database
     * 
     */
    public void setRetrieveLdapGroupAttribute(String retrieveLdapGroupAttribute) {
        this.retrieveLdapGroupAttribute = retrieveLdapGroupAttribute;
    }

    /**
     * This attribute is used to retrieve User's home directory from ldap database
     * 
     */
    public String getRetrieveLdapHomedirectoryAttribute() {
        return retrieveLdapHomedirectoryAttribute;
    }

    /**
     * This attribute is used to retrieve User's home directory from ldap database
     * 
     */
    public void setRetrieveLdapHomedirectoryAttribute(String retrieveLdapHomedirectoryAttribute) {
        this.retrieveLdapHomedirectoryAttribute = retrieveLdapHomedirectoryAttribute;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getEnableGroupExactMatch() {
        return enableGroupExactMatch;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setEnableGroupExactMatch(Property.FeatureEnabled enableGroupExactMatch) {
        this.enableGroupExactMatch = enableGroupExactMatch;
    }

    /**
     * The role ID of the default role for LDAP authentication
     * 
     */
    public String getDefaultRole() {
        return defaultRole;
    }

    /**
     * The role ID of the default role for LDAP authentication
     * 
     */
    public void setDefaultRole(String defaultRole) {
        this.defaultRole = defaultRole;
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

}
