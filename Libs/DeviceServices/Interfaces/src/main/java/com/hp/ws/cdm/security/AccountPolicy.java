
package com.hp.ws.cdm.security;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

public class AccountPolicy {

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
     * Account Lockout Configuration
     * 
     */
    @SerializedName("accountLockout")
    @Expose
    private AccountLockout accountLockout;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("enablePasswordComplexity")
    @Expose
    private Property.FeatureEnabled enablePasswordComplexity;
    /**
     * Minimum password length to be configured
     * 
     */
    @SerializedName("minimumPasswordLength")
    @Expose
    private Integer minimumPasswordLength;

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
     * Account Lockout Configuration
     * 
     */
    public AccountLockout getAccountLockout() {
        return accountLockout;
    }

    /**
     * Account Lockout Configuration
     * 
     */
    public void setAccountLockout(AccountLockout accountLockout) {
        this.accountLockout = accountLockout;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getEnablePasswordComplexity() {
        return enablePasswordComplexity;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setEnablePasswordComplexity(Property.FeatureEnabled enablePasswordComplexity) {
        this.enablePasswordComplexity = enablePasswordComplexity;
    }

    /**
     * Minimum password length to be configured
     * 
     */
    public Integer getMinimumPasswordLength() {
        return minimumPasswordLength;
    }

    /**
     * Minimum password length to be configured
     * 
     */
    public void setMinimumPasswordLength(Integer minimumPasswordLength) {
        this.minimumPasswordLength = minimumPasswordLength;
    }

}
