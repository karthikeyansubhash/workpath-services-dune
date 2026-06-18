
package com.hp.ws.cdm.ioconfig;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VsaCodes {

    @SerializedName("vsaCode")
    @Expose
    private VsaCodes.VsaCode vsaCode;
    @SerializedName("severity")
    @Expose
    private VsaCodes.Severity severity;
    @SerializedName("vsaParams")
    @Expose
    private VsaParams vsaParams;

    public VsaCodes.VsaCode getVsaCode() {
        return vsaCode;
    }

    public void setVsaCode(VsaCodes.VsaCode vsaCode) {
        this.vsaCode = vsaCode;
    }

    public VsaCodes.Severity getSeverity() {
        return severity;
    }

    public void setSeverity(VsaCodes.Severity severity) {
        this.severity = severity;
    }

    public VsaParams getVsaParams() {
        return vsaParams;
    }

    public void setVsaParams(VsaParams vsaParams) {
        this.vsaParams = vsaParams;
    }

    public enum Severity {

        @SerializedName("fatal")
        FATAL("fatal"),
        @SerializedName("warning")
        WARNING("warning"),
        @SerializedName("info")
        INFO("info");
        private final String value;
        private final static Map<String, VsaCodes.Severity> CONSTANTS = new HashMap<String, VsaCodes.Severity>();

        static {
            for (VsaCodes.Severity c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        Severity(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static VsaCodes.Severity fromValue(String value) {
            VsaCodes.Severity constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum VsaCode {

        @SerializedName("ethPluggedIn")
        ETH_PLUGGED_IN("ethPluggedIn"),
        @SerializedName("netFoundEthPluggedIn")
        NET_FOUND_ETH_PLUGGED_IN("netFoundEthPluggedIn"),
        @SerializedName("disabled")
        DISABLED("disabled"),
        @SerializedName("linkError")
        LINK_ERROR("linkError"),
        @SerializedName("hwError")
        HW_ERROR("hwError"),
        @SerializedName("ssidMismatch")
        SSID_MISMATCH("ssidMismatch"),
        @SerializedName("netNotFound")
        NET_NOT_FOUND("netNotFound"),
        @SerializedName("incorrectWepKey")
        INCORRECT_WEP_KEY("incorrectWepKey"),
        @SerializedName("nonStandardWepIndex")
        NON_STANDARD_WEP_INDEX("nonStandardWepIndex"),
        @SerializedName("nonDefaultWepAuth")
        NON_DEFAULT_WEP_AUTH("nonDefaultWepAuth"),
        @SerializedName("incorrectWpaPassphrase")
        INCORRECT_WPA_PASSPHRASE("incorrectWpaPassphrase"),
        @SerializedName("nonDefaultWpaEnc")
        NON_DEFAULT_WPA_ENC("nonDefaultWpaEnc"),
        @SerializedName("nonDefaultWpaAuth")
        NON_DEFAULT_WPA_AUTH("nonDefaultWpaAuth"),
        @SerializedName("nonStandardWpaMode")
        NON_STANDARD_WPA_MODE("nonStandardWpaMode"),
        @SerializedName("settingsMismatch")
        SETTINGS_MISMATCH("settingsMismatch"),
        @SerializedName("macFilteringPossible")
        MAC_FILTERING_POSSIBLE("macFilteringPossible"),
        @SerializedName("signalStrength")
        SIGNAL_STRENGTH("signalStrength"),
        @SerializedName("autoIpUsed")
        AUTO_IP_USED("autoIpUsed"),
        @SerializedName("signalStrengthAssociated")
        SIGNAL_STRENGTH_ASSOCIATED("signalStrengthAssociated"),
        @SerializedName("signalQuality")
        SIGNAL_QUALITY("signalQuality"),
        @SerializedName("multiNetFound")
        MULTI_NET_FOUND("multiNetFound"),
        @SerializedName("mfgDefaultSsid")
        MFG_DEFAULT_SSID("mfgDefaultSsid");
        private final String value;
        private final static Map<String, VsaCodes.VsaCode> CONSTANTS = new HashMap<String, VsaCodes.VsaCode>();

        static {
            for (VsaCodes.VsaCode c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        VsaCode(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static VsaCodes.VsaCode fromValue(String value) {
            VsaCodes.VsaCode constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
