
package com.hp.ws.cdm.storage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

public class StorageDeviceStatus {

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("fullDiskEncryptionStatus")
    @Expose
    private Property.FeatureEnabled fullDiskEncryptionStatus;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("lockable")
    @Expose
    private Property.FeatureEnabled lockable;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("locked")
    @Expose
    private Property.FeatureEnabled locked;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("inUse")
    @Expose
    private Property.FeatureEnabled inUse;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("functional")
    @Expose
    private Property.FeatureEnabled functional;

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getFullDiskEncryptionStatus() {
        return fullDiskEncryptionStatus;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setFullDiskEncryptionStatus(Property.FeatureEnabled fullDiskEncryptionStatus) {
        this.fullDiskEncryptionStatus = fullDiskEncryptionStatus;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getLockable() {
        return lockable;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setLockable(Property.FeatureEnabled lockable) {
        this.lockable = lockable;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getLocked() {
        return locked;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setLocked(Property.FeatureEnabled locked) {
        this.locked = locked;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getInUse() {
        return inUse;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setInUse(Property.FeatureEnabled inUse) {
        this.inUse = inUse;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getFunctional() {
        return functional;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setFunctional(Property.FeatureEnabled functional) {
        this.functional = functional;
    }

}
