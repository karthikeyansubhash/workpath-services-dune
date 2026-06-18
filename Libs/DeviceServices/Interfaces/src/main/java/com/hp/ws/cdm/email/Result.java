
package com.hp.ws.cdm.email;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    /**
     * It is result status of SMTP server verification operation
     * 
     */
    @SerializedName("completionType")
    @Expose
    private CompletionType completionType;
    @SerializedName("errorMessage")
    @Expose
    private String errorMessage;
    @SerializedName("errorCode")
    @Expose
    private Integer errorCode;

    /**
     * It is result status of SMTP server verification operation
     * 
     */
    public CompletionType getCompletionType() {
        return completionType;
    }

    /**
     * It is result status of SMTP server verification operation
     * 
     */
    public void setCompletionType(CompletionType completionType) {
        this.completionType = completionType;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }


    /**
     * It is result status of SMTP server verification operation
     * 
     */
    public enum CompletionType {

        @SerializedName("success")
        SUCCESS("success"),
        @SerializedName("failed")
        FAILED("failed");
        private final String value;
        private final static Map<String, CompletionType> CONSTANTS = new HashMap<String, CompletionType>();

        static {
            for (CompletionType c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        CompletionType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static CompletionType fromValue(String value) {
            CompletionType constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
