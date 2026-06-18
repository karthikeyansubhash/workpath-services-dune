
package com.hp.ws.cdm.pubsub;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * subscription resource
 * <p>
 * This represents the inforamtion for resource in pubsub subscription
 * 
 */
public class Resource {

    /**
     * identifier of the filter to be applied for this resource
     * 
     */
    @SerializedName("filterId")
    @Expose
    private Integer filterId;
    /**
     * GUN of the resource included in the subscription
     * (Required)
     * 
     */
    @SerializedName("gun")
    @Expose
    private String gun;
    /**
     * <validation> Behaviour for handling errors ONLY during subscription for this resource included in the subscription. This flag is not persistent
     * 
     */
    @SerializedName("gunValidationEnforced")
    @Expose
    private Resource.GunValidationEnforced gunValidationEnforced = Resource.GunValidationEnforced.fromValue("true");
    /**
     * Behaviour for notifying the baseline on boot for this resource included in the subscription
     * 
     */
    @SerializedName("syncOnBoot")
    @Expose
    private Resource.SyncOnBoot syncOnBoot = Resource.SyncOnBoot.fromValue("true");
    /**
     * Default baseline limit for notifying for collection resource in the subscription
     * 
     */
    @SerializedName("baselineLimit")
    @Expose
    private Integer baselineLimit;

    /**
     * identifier of the filter to be applied for this resource
     * 
     */
    public Integer getFilterId() {
        return filterId;
    }

    /**
     * identifier of the filter to be applied for this resource
     * 
     */
    public void setFilterId(Integer filterId) {
        this.filterId = filterId;
    }

    /**
     * GUN of the resource included in the subscription
     * (Required)
     * 
     */
    public String getGun() {
        return gun;
    }

    /**
     * GUN of the resource included in the subscription
     * (Required)
     * 
     */
    public void setGun(String gun) {
        this.gun = gun;
    }

    /**
     * <validation> Behaviour for handling errors ONLY during subscription for this resource included in the subscription. This flag is not persistent
     * 
     */
    public Resource.GunValidationEnforced getGunValidationEnforced() {
        return gunValidationEnforced;
    }

    /**
     * <validation> Behaviour for handling errors ONLY during subscription for this resource included in the subscription. This flag is not persistent
     * 
     */
    public void setGunValidationEnforced(Resource.GunValidationEnforced gunValidationEnforced) {
        this.gunValidationEnforced = gunValidationEnforced;
    }

    /**
     * Behaviour for notifying the baseline on boot for this resource included in the subscription
     * 
     */
    public Resource.SyncOnBoot getSyncOnBoot() {
        return syncOnBoot;
    }

    /**
     * Behaviour for notifying the baseline on boot for this resource included in the subscription
     * 
     */
    public void setSyncOnBoot(Resource.SyncOnBoot syncOnBoot) {
        this.syncOnBoot = syncOnBoot;
    }

    /**
     * Default baseline limit for notifying for collection resource in the subscription
     * 
     */
    public Integer getBaselineLimit() {
        return baselineLimit;
    }

    /**
     * Default baseline limit for notifying for collection resource in the subscription
     * 
     */
    public void setBaselineLimit(Integer baselineLimit) {
        this.baselineLimit = baselineLimit;
    }


    /**
     * <validation> Behaviour for handling errors ONLY during subscription for this resource included in the subscription. This flag is not persistent
     * 
     */
    public enum GunValidationEnforced {

        @SerializedName("true")
        TRUE("true"),
        @SerializedName("false")
        FALSE("false");
        private final String value;
        private final static Map<String, Resource.GunValidationEnforced> CONSTANTS = new HashMap<String, Resource.GunValidationEnforced>();

        static {
            for (Resource.GunValidationEnforced c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        GunValidationEnforced(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Resource.GunValidationEnforced fromValue(String value) {
            Resource.GunValidationEnforced constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * Behaviour for notifying the baseline on boot for this resource included in the subscription
     * 
     */
    public enum SyncOnBoot {

        @SerializedName("true")
        TRUE("true"),
        @SerializedName("false")
        FALSE("false");
        private final String value;
        private final static Map<String, Resource.SyncOnBoot> CONSTANTS = new HashMap<String, Resource.SyncOnBoot>();

        static {
            for (Resource.SyncOnBoot c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        SyncOnBoot(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Resource.SyncOnBoot fromValue(String value) {
            Resource.SyncOnBoot constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
