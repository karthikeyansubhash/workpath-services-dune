
package com.hp.ws.cdm.security;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SmartcardSystemStatus {

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
     * The type of smartcard reader identified by the system.
     * 
     */
    @SerializedName("smartCardReaderTypeIdentified")
    @Expose
    private SmartcardSystemStatus.SmartcardReaderTypeCdm smartCardReaderTypeIdentified;

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
     * The type of smartcard reader identified by the system.
     * 
     */
    public SmartcardSystemStatus.SmartcardReaderTypeCdm getSmartCardReaderTypeIdentified() {
        return smartCardReaderTypeIdentified;
    }

    /**
     * The type of smartcard reader identified by the system.
     * 
     */
    public void setSmartCardReaderTypeIdentified(SmartcardSystemStatus.SmartcardReaderTypeCdm smartCardReaderTypeIdentified) {
        this.smartCardReaderTypeIdentified = smartCardReaderTypeIdentified;
    }


    /**
     * The type of smartcard reader identified by the system.
     * 
     */
    public enum SmartcardReaderTypeCdm {

        @SerializedName("undefined")
        UNDEFINED("undefined"),
        @SerializedName("nipr")
        NIPR("nipr"),
        @SerializedName("sipr")
        SIPR("sipr"),
        @SerializedName("generic")
        GENERIC("generic");
        private final String value;
        private final static Map<String, SmartcardSystemStatus.SmartcardReaderTypeCdm> CONSTANTS = new HashMap<String, SmartcardSystemStatus.SmartcardReaderTypeCdm>();

        static {
            for (SmartcardSystemStatus.SmartcardReaderTypeCdm c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        SmartcardReaderTypeCdm(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static SmartcardSystemStatus.SmartcardReaderTypeCdm fromValue(String value) {
            SmartcardSystemStatus.SmartcardReaderTypeCdm constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
