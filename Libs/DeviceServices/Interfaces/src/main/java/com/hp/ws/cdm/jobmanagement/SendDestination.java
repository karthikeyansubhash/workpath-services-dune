
package com.hp.ws.cdm.jobmanagement;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendDestination {

    /**
     * result of the send destination
     * 
     */
    @SerializedName("destResult")
    @Expose
    private SendDestination.DestResult destResult;
    /**
     * destination address
     * 
     */
    @SerializedName("destination")
    @Expose
    private String destination;
    /**
     * send job destination type
     * 
     */
    @SerializedName("destType")
    @Expose
    private SendDestination.DestType destType;

    /**
     * result of the send destination
     * 
     */
    public SendDestination.DestResult getDestResult() {
        return destResult;
    }

    /**
     * result of the send destination
     * 
     */
    public void setDestResult(SendDestination.DestResult destResult) {
        this.destResult = destResult;
    }

    /**
     * destination address
     * 
     */
    public String getDestination() {
        return destination;
    }

    /**
     * destination address
     * 
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }

    /**
     * send job destination type
     * 
     */
    public SendDestination.DestType getDestType() {
        return destType;
    }

    /**
     * send job destination type
     * 
     */
    public void setDestType(SendDestination.DestType destType) {
        this.destType = destType;
    }


    /**
     * result of the send destination
     * 
     */
    public enum DestResult {

        @SerializedName("failed")
        FAILED("failed"),
        @SerializedName("successful")
        SUCCESSFUL("successful"),
        @SerializedName("cancelled")
        CANCELLED("cancelled"),
        @SerializedName("unprocessed")
        UNPROCESSED("unprocessed"),
        @SerializedName("invalidCredentials")
        INVALID_CREDENTIALS("invalidCredentials"),
        @SerializedName("insufficientPermissions")
        INSUFFICIENT_PERMISSIONS("insufficientPermissions"),
        @SerializedName("accessDenied")
        ACCESS_DENIED("accessDenied"),
        @SerializedName("invalidPath")
        INVALID_PATH("invalidPath"),
        @SerializedName("networkError")
        NETWORK_ERROR("networkError"),
        @SerializedName("serverMemoryFull")
        SERVER_MEMORY_FULL("serverMemoryFull"),
        @SerializedName("fileExist")
        FILE_EXIST("fileExist"),
        @SerializedName("protocolError")
        PROTOCOL_ERROR("protocolError"),
        @SerializedName("connectionRefused")
        CONNECTION_REFUSED("connectionRefused"),
        @SerializedName("invalidCertificate")
        INVALID_CERTIFICATE("invalidCertificate"),
        @SerializedName("maximumRecipientsReached")
        MAXIMUM_RECIPIENTS_REACHED("maximumRecipientsReached"),
        @SerializedName("authenticationRequired")
        AUTHENTICATION_REQUIRED("authenticationRequired"),
        @SerializedName("invalidSender")
        INVALID_SENDER("invalidSender"),
        @SerializedName("invalidRecipient")
        INVALID_RECIPIENT("invalidRecipient"),
        @SerializedName("sslFailure")
        SSL_FAILURE("sslFailure"),
        @SerializedName("maxSizeReached")
        MAX_SIZE_REACHED("maxSizeReached"),
        @SerializedName("userMismatch")
        USER_MISMATCH("userMismatch"),
        @SerializedName("connectionTimeout")
        CONNECTION_TIMEOUT("connectionTimeout"),
        @SerializedName("invalidFilename")
        INVALID_FILENAME("invalidFilename"),
        @SerializedName("writeError")
        WRITE_ERROR("writeError"),
        @SerializedName("driveNotFound")
        DRIVE_NOT_FOUND("driveNotFound"),
        @SerializedName("invalidHostname")
        INVALID_HOSTNAME("invalidHostname"),
        @SerializedName("badRequest")
        BAD_REQUEST("badRequest"),
        @SerializedName("urlTooLong")
        URL_TOO_LONG("urlTooLong"),
        @SerializedName("contentTooLarge")
        CONTENT_TOO_LARGE("contentTooLarge");
        private final String value;
        private final static Map<String, SendDestination.DestResult> CONSTANTS = new HashMap<String, SendDestination.DestResult>();

        static {
            for (SendDestination.DestResult c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        DestResult(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static SendDestination.DestResult fromValue(String value) {
            SendDestination.DestResult constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * send job destination type
     * 
     */
    public enum DestType {

        @SerializedName("to")
        TO("to"),
        @SerializedName("cc")
        CC("cc"),
        @SerializedName("bcc")
        BCC("bcc"),
        @SerializedName("folder")
        FOLDER("folder"),
        @SerializedName("sharepoint")
        SHAREPOINT("sharepoint"),
        @SerializedName("usb")
        USB("usb"),
        @SerializedName("http")
        HTTP("http");
        private final String value;
        private final static Map<String, SendDestination.DestType> CONSTANTS = new HashMap<String, SendDestination.DestType>();

        static {
            for (SendDestination.DestType c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        DestType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static SendDestination.DestType fromValue(String value) {
            SendDestination.DestType constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
