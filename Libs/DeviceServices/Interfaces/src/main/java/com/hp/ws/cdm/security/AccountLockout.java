
package com.hp.ws.cdm.security;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;


/**
 * Account Lockout Configuration
 * 
 */
public class AccountLockout {

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("enabled")
    @Expose
    private Property.FeatureEnabled enabled;
    /**
     * Maximum failure attempts allowed
     * 
     */
    @SerializedName("maximumAttempts")
    @Expose
    private Integer maximumAttempts;
    /**
     * Account Lockout interval
     * 
     */
    @SerializedName("lockoutInterval")
    @Expose
    private Integer lockoutInterval;
    /**
     * Reset Lockout counter interval
     * 
     */
    @SerializedName("resetLockoutCounterInterval")
    @Expose
    private Integer resetLockoutCounterInterval;

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
     * Maximum failure attempts allowed
     * 
     */
    public Integer getMaximumAttempts() {
        return maximumAttempts;
    }

    /**
     * Maximum failure attempts allowed
     * 
     */
    public void setMaximumAttempts(Integer maximumAttempts) {
        this.maximumAttempts = maximumAttempts;
    }

    /**
     * Account Lockout interval
     * 
     */
    public Integer getLockoutInterval() {
        return lockoutInterval;
    }

    /**
     * Account Lockout interval
     * 
     */
    public void setLockoutInterval(Integer lockoutInterval) {
        this.lockoutInterval = lockoutInterval;
    }

    /**
     * Reset Lockout counter interval
     * 
     */
    public Integer getResetLockoutCounterInterval() {
        return resetLockoutCounterInterval;
    }

    /**
     * Reset Lockout counter interval
     * 
     */
    public void setResetLockoutCounterInterval(Integer resetLockoutCounterInterval) {
        this.resetLockoutCounterInterval = resetLockoutCounterInterval;
    }

}
