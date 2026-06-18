
package com.hp.ws.cdm.security;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

public class WindowsSignInConfig {

    /**
     * List of trusted domains for Windows authentication
     * 
     */
    @SerializedName("trustedDomains")
    @Expose
    private List<String> trustedDomains = new ArrayList<String>();
    /**
     * Default domain for Windows authentication
     * 
     */
    @SerializedName("defaultDomain")
    @Expose
    private String defaultDomain;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("preferredDomainServersEnabled")
    @Expose
    private Property.FeatureEnabled preferredDomainServersEnabled;
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
    @SerializedName("secureConnectionEnabled")
    @Expose
    private Property.FeatureEnabled secureConnectionEnabled;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("reverseDnsCheckEnabled")
    @Expose
    private Property.FeatureEnabled reverseDnsCheckEnabled;
    /**
     * This attribute is used to match the name entered with this attribute on ldap database
     * 
     */
    @SerializedName("matchNameAttribute")
    @Expose
    private String matchNameAttribute;
    /**
     * This attribute is used to retrieve user's email address from ldap database
     * 
     */
    @SerializedName("retrieveEmailAttribute")
    @Expose
    private String retrieveEmailAttribute;
    /**
     * This attribute is used to retrieve user's display name from ldap database
     * 
     */
    @SerializedName("retrieveDisplayNameAttribute")
    @Expose
    private String retrieveDisplayNameAttribute;
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
    @SerializedName("inheritParentGroupPermission")
    @Expose
    private Property.FeatureEnabled inheritParentGroupPermission;
    /**
     * The role ID of the default role for Windows authentication
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
     * List of trusted domains for Windows authentication
     * 
     */
    public List<String> getTrustedDomains() {
        return trustedDomains;
    }

    /**
     * List of trusted domains for Windows authentication
     * 
     */
    public void setTrustedDomains(List<String> trustedDomains) {
        this.trustedDomains = trustedDomains;
    }

    /**
     * Default domain for Windows authentication
     * 
     */
    public String getDefaultDomain() {
        return defaultDomain;
    }

    /**
     * Default domain for Windows authentication
     * 
     */
    public void setDefaultDomain(String defaultDomain) {
        this.defaultDomain = defaultDomain;
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
     * This attribute is used to match the name entered with this attribute on ldap database
     * 
     */
    public String getMatchNameAttribute() {
        return matchNameAttribute;
    }

    /**
     * This attribute is used to match the name entered with this attribute on ldap database
     * 
     */
    public void setMatchNameAttribute(String matchNameAttribute) {
        this.matchNameAttribute = matchNameAttribute;
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
    public Property.FeatureEnabled getInheritParentGroupPermission() {
        return inheritParentGroupPermission;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setInheritParentGroupPermission(Property.FeatureEnabled inheritParentGroupPermission) {
        this.inheritParentGroupPermission = inheritParentGroupPermission;
    }

    /**
     * The role ID of the default role for Windows authentication
     * 
     */
    public String getDefaultRole() {
        return defaultRole;
    }

    /**
     * The role ID of the default role for Windows authentication
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
