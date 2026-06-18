
package com.hp.ws.cdm.licensing;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class InstallPackage {

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("singleUseToken")
    @Expose
    private String singleUseToken;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("state")
    @Expose
    private InstallPackage.State state;
    /**
     * local file path only
     * 
     */
    @SerializedName("sourceUri")
    @Expose
    private String sourceUri;
    /**
     * JWE singed licensing.licensePackage object
     * 
     */
    @SerializedName("jwsLicensePackage")
    @Expose
    private String jwsLicensePackage;
    @SerializedName("clientInstanceId")
    @Expose
    private String clientInstanceId;
    @SerializedName("lastResult")
    @Expose
    private InstallPackage.LastResult lastResult;
    @SerializedName("failureReason")
    @Expose
    private InstallPackage.FailureReason failureReason;
    @SerializedName("licenseIdsInstalled")
    @Expose
    private List<String> licenseIdsInstalled = new ArrayList<String>();
    /**
     * standard date and time format - according to RFC 3339. The date/time when the event was produced (according to the service).  It is of the form YYYY-MM-DD'T':HH:mm:SS.sssZ, for example '2012-04-21T08:15:13.511Z'
     * 
     */
    @SerializedName("lastResultTimeStamp")
    @Expose
    private Date lastResultTimeStamp;

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

    public String getSingleUseToken() {
        return singleUseToken;
    }

    public void setSingleUseToken(String singleUseToken) {
        this.singleUseToken = singleUseToken;
    }

    /**
     * 
     * (Required)
     * 
     */
    public InstallPackage.State getState() {
        return state;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setState(InstallPackage.State state) {
        this.state = state;
    }

    /**
     * local file path only
     * 
     */
    public String getSourceUri() {
        return sourceUri;
    }

    /**
     * local file path only
     * 
     */
    public void setSourceUri(String sourceUri) {
        this.sourceUri = sourceUri;
    }

    /**
     * JWE singed licensing.licensePackage object
     * 
     */
    public String getJwsLicensePackage() {
        return jwsLicensePackage;
    }

    /**
     * JWE singed licensing.licensePackage object
     * 
     */
    public void setJwsLicensePackage(String jwsLicensePackage) {
        this.jwsLicensePackage = jwsLicensePackage;
    }

    public String getClientInstanceId() {
        return clientInstanceId;
    }

    public void setClientInstanceId(String clientInstanceId) {
        this.clientInstanceId = clientInstanceId;
    }

    public InstallPackage.LastResult getLastResult() {
        return lastResult;
    }

    public void setLastResult(InstallPackage.LastResult lastResult) {
        this.lastResult = lastResult;
    }

    public InstallPackage.FailureReason getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(InstallPackage.FailureReason failureReason) {
        this.failureReason = failureReason;
    }

    public List<String> getLicenseIdsInstalled() {
        return licenseIdsInstalled;
    }

    public void setLicenseIdsInstalled(List<String> licenseIdsInstalled) {
        this.licenseIdsInstalled = licenseIdsInstalled;
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

    public enum FailureReason {

        @SerializedName("invalidLicensePackage")
        INVALID_LICENSE_PACKAGE("invalidLicensePackage"),
        @SerializedName("singleUseTokenMismatch")
        SINGLE_USE_TOKEN_MISMATCH("singleUseTokenMismatch"),
        @SerializedName("deviceUuidMismatch")
        DEVICE_UUID_MISMATCH("deviceUuidMismatch"),
        @SerializedName("signatureError")
        SIGNATURE_ERROR("signatureError"),
        @SerializedName("noApplicableLicenses")
        NO_APPLICABLE_LICENSES("noApplicableLicenses"),
        @SerializedName("invalidLicenseInPackage")
        INVALID_LICENSE_IN_PACKAGE("invalidLicenseInPackage");
        private final String value;
        private final static Map<String, InstallPackage.FailureReason> CONSTANTS = new HashMap<String, InstallPackage.FailureReason>();

        static {
            for (InstallPackage.FailureReason c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        FailureReason(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static InstallPackage.FailureReason fromValue(String value) {
            InstallPackage.FailureReason constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum LastResult {

        @SerializedName("failure")
        FAILURE("failure"),
        @SerializedName("success")
        SUCCESS("success");
        private final String value;
        private final static Map<String, InstallPackage.LastResult> CONSTANTS = new HashMap<String, InstallPackage.LastResult>();

        static {
            for (InstallPackage.LastResult c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        LastResult(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static InstallPackage.LastResult fromValue(String value) {
            InstallPackage.LastResult constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum State {

        @SerializedName("idle")
        IDLE("idle"),
        @SerializedName("processing")
        PROCESSING("processing");
        private final String value;
        private final static Map<String, InstallPackage.State> CONSTANTS = new HashMap<String, InstallPackage.State>();

        static {
            for (InstallPackage.State c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        State(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static InstallPackage.State fromValue(String value) {
            InstallPackage.State constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
