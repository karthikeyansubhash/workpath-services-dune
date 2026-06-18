
package com.hp.ws.cdm.network;

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
     * Duration in seconds for which the account will be locked
     * 
     */
    @SerializedName("lockoutInterval")
    @Expose
    private Integer lockoutInterval;
    /**
     * Maximum failure attempts before lockout
     * 
     */
    @SerializedName("maximumAttempts")
    @Expose
    private Integer maximumAttempts;
    /**
     * Idle duration in seconds after which the failed attempts conter will be reset
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
     * Duration in seconds for which the account will be locked
     * 
     */
    public Integer getLockoutInterval() {
        return lockoutInterval;
    }

    /**
     * Duration in seconds for which the account will be locked
     * 
     */
    public void setLockoutInterval(Integer lockoutInterval) {
        this.lockoutInterval = lockoutInterval;
    }

    /**
     * Maximum failure attempts before lockout
     * 
     */
    public Integer getMaximumAttempts() {
        return maximumAttempts;
    }

    /**
     * Maximum failure attempts before lockout
     * 
     */
    public void setMaximumAttempts(Integer maximumAttempts) {
        this.maximumAttempts = maximumAttempts;
    }

    /**
     * Idle duration in seconds after which the failed attempts conter will be reset
     * 
     */
    public Integer getResetLockoutCounterInterval() {
        return resetLockoutCounterInterval;
    }

    /**
     * Idle duration in seconds after which the failed attempts conter will be reset
     * 
     */
    public void setResetLockoutCounterInterval(Integer resetLockoutCounterInterval) {
        this.resetLockoutCounterInterval = resetLockoutCounterInterval;
    }

}
