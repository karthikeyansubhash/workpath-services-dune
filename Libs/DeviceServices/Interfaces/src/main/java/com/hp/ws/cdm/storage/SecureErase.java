
package com.hp.ws.cdm.storage;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SecureErase {

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
     * The state of operation
     * 
     */
    @SerializedName("state")
    @Expose
    private com.hp.ws.cdm.storage.Format.State state;
    /**
     * The type of Secure Erase methods
     * 
     */
    @SerializedName("secureEraseMethod")
    @Expose
    private SecureErase.SecureEraseMethod secureEraseMethod;
    /**
     * The last result of the operation
     * 
     */
    @SerializedName("lastResult")
    @Expose
    private com.hp.ws.cdm.storage.Format.LastResult lastResult;
    /**
     * standard date and time format - according to RFC 3339. The date/time when the event was produced (according to the service).  It is of the form YYYY-MM-DD'T':HH:mm:SS.sssZ, for example '2012-04-21T08:15:13.511Z'
     * 
     */
    @SerializedName("lastResultTimeStamp")
    @Expose
    private Date lastResultTimeStamp;
    @SerializedName("failureReason")
    @Expose
    private com.hp.ws.cdm.storage.Format.FailureReason failureReason;
    /**
     * The driveId of storage device to be securely erased.
     * 
     */
    @SerializedName("driveId")
    @Expose
    private String driveId;

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
     * The state of operation
     * 
     */
    public com.hp.ws.cdm.storage.Format.State getState() {
        return state;
    }

    /**
     * The state of operation
     * 
     */
    public void setState(com.hp.ws.cdm.storage.Format.State state) {
        this.state = state;
    }

    /**
     * The type of Secure Erase methods
     * 
     */
    public SecureErase.SecureEraseMethod getSecureEraseMethod() {
        return secureEraseMethod;
    }

    /**
     * The type of Secure Erase methods
     * 
     */
    public void setSecureEraseMethod(SecureErase.SecureEraseMethod secureEraseMethod) {
        this.secureEraseMethod = secureEraseMethod;
    }

    /**
     * The last result of the operation
     * 
     */
    public com.hp.ws.cdm.storage.Format.LastResult getLastResult() {
        return lastResult;
    }

    /**
     * The last result of the operation
     * 
     */
    public void setLastResult(com.hp.ws.cdm.storage.Format.LastResult lastResult) {
        this.lastResult = lastResult;
    }

    /**
     * standard date and time format - according to RFC 3339. The date/time when the event was produced (according to the service).  It is of the form YYYY-MM-DD'T':HH:mm:SS.sssZ, for example '2012-04-21T08:15:13.511Z'
     * 
     */
    public Date getLastResultTimeStamp() {
        return lastResultTimeStamp;
    }

    /**
     * standard date and time format - according to RFC 3339. The date/time when the event was produced (according to the service).  It is of the form YYYY-MM-DD'T':HH:mm:SS.sssZ, for example '2012-04-21T08:15:13.511Z'
     * 
     */
    public void setLastResultTimeStamp(Date lastResultTimeStamp) {
        this.lastResultTimeStamp = lastResultTimeStamp;
    }

    public com.hp.ws.cdm.storage.Format.FailureReason getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(com.hp.ws.cdm.storage.Format.FailureReason failureReason) {
        this.failureReason = failureReason;
    }

    /**
     * The driveId of storage device to be securely erased.
     * 
     */
    public String getDriveId() {
        return driveId;
    }

    /**
     * The driveId of storage device to be securely erased.
     * 
     */
    public void setDriveId(String driveId) {
        this.driveId = driveId;
    }


    /**
     * The type of Secure Erase methods
     * 
     */
    public enum SecureEraseMethod {

        @SerializedName("cryptoSecureErase")
        CRYPTO_SECURE_ERASE("cryptoSecureErase"),
        @SerializedName("nonSecureFastErase")
        NON_SECURE_FAST_ERASE("nonSecureFastErase"),
        @SerializedName("secureFastErase")
        SECURE_FAST_ERASE("secureFastErase"),
        @SerializedName("secureSanitizingErase")
        SECURE_SANITIZING_ERASE("secureSanitizingErase");
        private final String value;
        private final static Map<String, SecureErase.SecureEraseMethod> CONSTANTS = new HashMap<String, SecureErase.SecureEraseMethod>();

        static {
            for (SecureErase.SecureEraseMethod c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        SecureEraseMethod(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static SecureErase.SecureEraseMethod fromValue(String value) {
            SecureErase.SecureEraseMethod constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
