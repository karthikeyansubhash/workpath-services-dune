
package com.hp.ws.cdm.jobmanagement;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FaxCall {

    /**
     * value in seconds
     * 
     */
    @SerializedName("duration")
    @Expose
    private Integer duration;
    /**
     * delivery status of the fax job
     * 
     */
    @SerializedName("faxResult")
    @Expose
    private FaxCall.FaxResult faxResult;
    /**
     * index to the faxCall array.
     * 
     */
    @SerializedName("indexNumber")
    @Expose
    private Integer indexNumber;
    /**
     * Resolution for send and archive job.
     * 
     */
    @SerializedName("faxResolution")
    @Expose
    private FaxCall.FaxResolution faxResolution;
    @SerializedName("colorMode")
    @Expose
    private com.hp.ws.cdm.jobmanagement.PrintSettings.ColorModes colorMode;
    /**
     * active fax line on which current job in progress
     * 
     */
    @SerializedName("line")
    @Expose
    private String line;
    /**
     * stationId can be contact or fax number for send fax.It can be a callerId/stationId for the receive fax and will be faxId for IP fax
     * 
     */
    @SerializedName("stationId")
    @Expose
    private String stationId;
    /**
     * number of fax pages being processed for send fax and it will be total received images for receive fax
     * 
     */
    @SerializedName("totalImagesProcessed")
    @Expose
    private Integer totalImagesProcessed;

    /**
     * value in seconds
     * 
     */
    public Integer getDuration() {
        return duration;
    }

    /**
     * value in seconds
     * 
     */
    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    /**
     * delivery status of the fax job
     * 
     */
    public FaxCall.FaxResult getFaxResult() {
        return faxResult;
    }

    /**
     * delivery status of the fax job
     * 
     */
    public void setFaxResult(FaxCall.FaxResult faxResult) {
        this.faxResult = faxResult;
    }

    /**
     * index to the faxCall array.
     * 
     */
    public Integer getIndexNumber() {
        return indexNumber;
    }

    /**
     * index to the faxCall array.
     * 
     */
    public void setIndexNumber(Integer indexNumber) {
        this.indexNumber = indexNumber;
    }

    /**
     * Resolution for send and archive job.
     * 
     */
    public FaxCall.FaxResolution getFaxResolution() {
        return faxResolution;
    }

    /**
     * Resolution for send and archive job.
     * 
     */
    public void setFaxResolution(FaxCall.FaxResolution faxResolution) {
        this.faxResolution = faxResolution;
    }

    public com.hp.ws.cdm.jobmanagement.PrintSettings.ColorModes getColorMode() {
        return colorMode;
    }

    public void setColorMode(com.hp.ws.cdm.jobmanagement.PrintSettings.ColorModes colorMode) {
        this.colorMode = colorMode;
    }

    /**
     * active fax line on which current job in progress
     * 
     */
    public String getLine() {
        return line;
    }

    /**
     * active fax line on which current job in progress
     * 
     */
    public void setLine(String line) {
        this.line = line;
    }

    /**
     * stationId can be contact or fax number for send fax.It can be a callerId/stationId for the receive fax and will be faxId for IP fax
     * 
     */
    public String getStationId() {
        return stationId;
    }

    /**
     * stationId can be contact or fax number for send fax.It can be a callerId/stationId for the receive fax and will be faxId for IP fax
     * 
     */
    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    /**
     * number of fax pages being processed for send fax and it will be total received images for receive fax
     * 
     */
    public Integer getTotalImagesProcessed() {
        return totalImagesProcessed;
    }

    /**
     * number of fax pages being processed for send fax and it will be total received images for receive fax
     * 
     */
    public void setTotalImagesProcessed(Integer totalImagesProcessed) {
        this.totalImagesProcessed = totalImagesProcessed;
    }


    /**
     * Resolution for send and archive job.
     * 
     */
    public enum FaxResolution {

        @SerializedName("standard")
        STANDARD("standard"),
        @SerializedName("fine")
        FINE("fine"),
        @SerializedName("superfine")
        SUPERFINE("superfine"),
        @SerializedName("ultrafine")
        ULTRAFINE("ultrafine");
        private final String value;
        private final static Map<String, FaxCall.FaxResolution> CONSTANTS = new HashMap<String, FaxCall.FaxResolution>();

        static {
            for (FaxCall.FaxResolution c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        FaxResolution(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static FaxCall.FaxResolution fromValue(String value) {
            FaxCall.FaxResolution constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * delivery status of the fax job
     * 
     */
    public enum FaxResult {

        @SerializedName("failed")
        FAILED("failed"),
        @SerializedName("busy")
        BUSY("busy"),
        @SerializedName("partial")
        PARTIAL("partial"),
        @SerializedName("successful")
        SUCCESSFUL("successful"),
        @SerializedName("cancelled")
        CANCELLED("cancelled"),
        @SerializedName("invalidCredentials")
        INVALID_CREDENTIALS("invalidCredentials"),
        @SerializedName("connectionFailed")
        CONNECTION_FAILED("connectionFailed"),
        @SerializedName("noAnswer")
        NO_ANSWER("noAnswer"),
        @SerializedName("noDialTone")
        NO_DIAL_TONE("noDialTone"),
        @SerializedName("noFaxDetected")
        NO_FAX_DETECTED("noFaxDetected"),
        @SerializedName("modemFailed")
        MODEM_FAILED("modemFailed"),
        @SerializedName("communicationError")
        COMMUNICATION_ERROR("communicationError"),
        @SerializedName("lanfaxServerUnableToDeliverTheFax")
        LANFAX_SERVER_UNABLE_TO_DELIVER_THE_FAX("lanfaxServerUnableToDeliverTheFax"),
        @SerializedName("blocked")
        BLOCKED("blocked"),
        @SerializedName("noCarrier")
        NO_CARRIER("noCarrier"),
        @SerializedName("pollInvalid")
        POLL_INVALID("pollInvalid"),
        @SerializedName("lineSurge")
        LINE_SURGE("lineSurge"),
        @SerializedName("unprocessed")
        UNPROCESSED("unprocessed");
        private final String value;
        private final static Map<String, FaxCall.FaxResult> CONSTANTS = new HashMap<String, FaxCall.FaxResult>();

        static {
            for (FaxCall.FaxResult c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        FaxResult(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static FaxCall.FaxResult fromValue(String value) {
            FaxCall.FaxResult constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
