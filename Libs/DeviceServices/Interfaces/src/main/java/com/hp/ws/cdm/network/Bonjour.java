
package com.hp.ws.cdm.network;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

public class Bonjour {

    @SerializedName("domainName")
    @Expose
    private String domainName;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("enabled")
    @Expose
    private Property.FeatureEnabled enabled;
    @SerializedName("highestPriorityPrintService")
    @Expose
    private Bonjour.HighestPriorityPrintService highestPriorityPrintService;
    @SerializedName("serviceName")
    @Expose
    private String serviceName;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("periodicAdvertisementEnabled")
    @Expose
    private Property.FeatureEnabled periodicAdvertisementEnabled;
    /**
     * Setting for configuring the periodic Bonjour advertisement interval in seconds
     * 
     */
    @SerializedName("periodicAdvertisementInterval")
    @Expose
    private Integer periodicAdvertisementInterval;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("goodbyeOnDeepSleepEnabled")
    @Expose
    private Property.FeatureEnabled goodbyeOnDeepSleepEnabled;

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

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

    public Bonjour.HighestPriorityPrintService getHighestPriorityPrintService() {
        return highestPriorityPrintService;
    }

    public void setHighestPriorityPrintService(Bonjour.HighestPriorityPrintService highestPriorityPrintService) {
        this.highestPriorityPrintService = highestPriorityPrintService;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getPeriodicAdvertisementEnabled() {
        return periodicAdvertisementEnabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setPeriodicAdvertisementEnabled(Property.FeatureEnabled periodicAdvertisementEnabled) {
        this.periodicAdvertisementEnabled = periodicAdvertisementEnabled;
    }

    /**
     * Setting for configuring the periodic Bonjour advertisement interval in seconds
     * 
     */
    public Integer getPeriodicAdvertisementInterval() {
        return periodicAdvertisementInterval;
    }

    /**
     * Setting for configuring the periodic Bonjour advertisement interval in seconds
     * 
     */
    public void setPeriodicAdvertisementInterval(Integer periodicAdvertisementInterval) {
        this.periodicAdvertisementInterval = periodicAdvertisementInterval;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getGoodbyeOnDeepSleepEnabled() {
        return goodbyeOnDeepSleepEnabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setGoodbyeOnDeepSleepEnabled(Property.FeatureEnabled goodbyeOnDeepSleepEnabled) {
        this.goodbyeOnDeepSleepEnabled = goodbyeOnDeepSleepEnabled;
    }

    public enum HighestPriorityPrintService {

        @SerializedName("port9100")
        PORT_9100("port9100"),
        @SerializedName("ipp")
        IPP("ipp"),
        @SerializedName("ipps")
        IPPS("ipps"),
        @SerializedName("wsPrint")
        WS_PRINT("wsPrint"),
        @SerializedName("lpd")
        LPD("lpd");
        private final String value;
        private final static Map<String, Bonjour.HighestPriorityPrintService> CONSTANTS = new HashMap<String, Bonjour.HighestPriorityPrintService>();

        static {
            for (Bonjour.HighestPriorityPrintService c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        HighestPriorityPrintService(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Bonjour.HighestPriorityPrintService fromValue(String value) {
            Bonjour.HighestPriorityPrintService constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
