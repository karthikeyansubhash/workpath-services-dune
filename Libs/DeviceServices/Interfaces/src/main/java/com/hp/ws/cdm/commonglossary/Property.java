
package com.hp.ws.cdm.commonglossary;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Property {

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("disabled")
    @Expose
    private Property.FeatureEnabled disabled;
    @SerializedName("sValue")
    @Expose
    private String sValue;
    @SerializedName("seValue")
    @Expose
    private String seValue;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("sbValue")
    @Expose
    private Property.FeatureEnabled sbValue;
    @SerializedName("iValue")
    @Expose
    private Integer iValue;
    @SerializedName("iLargeValue")
    @Expose
    private Long iLargeValue;
    @SerializedName("dValue")
    @Expose
    private Float dValue;
    /**
     * The message id is the numeric representation of a string id.  It identifies the string message associated with the validator.
     * 
     */
    @SerializedName("messageId")
    @Expose
    private Integer messageId;
    /**
     * The message is the localized string message associated with the validator.  It is only provided if the "Accept-Language" header is present and valid.
     * 
     */
    @SerializedName("message")
    @Expose
    private String message;

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getDisabled() {
        return disabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setDisabled(Property.FeatureEnabled disabled) {
        this.disabled = disabled;
    }

    public String getsValue() {
        return sValue;
    }

    public void setsValue(String sValue) {
        this.sValue = sValue;
    }

    public String getSeValue() {
        return seValue;
    }

    public void setSeValue(String seValue) {
        this.seValue = seValue;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getSbValue() {
        return sbValue;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setSbValue(Property.FeatureEnabled sbValue) {
        this.sbValue = sbValue;
    }

    public Integer getiValue() {
        return iValue;
    }

    public void setiValue(Integer iValue) {
        this.iValue = iValue;
    }

    public Long getiLargeValue() {
        return iLargeValue;
    }

    public void setiLargeValue(Long iLargeValue) {
        this.iLargeValue = iLargeValue;
    }

    public Float getdValue() {
        return dValue;
    }

    public void setdValue(Float dValue) {
        this.dValue = dValue;
    }

    /**
     * The message id is the numeric representation of a string id.  It identifies the string message associated with the validator.
     * 
     */
    public Integer getMessageId() {
        return messageId;
    }

    /**
     * The message id is the numeric representation of a string id.  It identifies the string message associated with the validator.
     * 
     */
    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    /**
     * The message is the localized string message associated with the validator.  It is only provided if the "Accept-Language" header is present and valid.
     * 
     */
    public String getMessage() {
        return message;
    }

    /**
     * The message is the localized string message associated with the validator.  It is only provided if the "Accept-Language" header is present and valid.
     * 
     */
    public void setMessage(String message) {
        this.message = message;
    }


    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public enum FeatureEnabled {

        @SerializedName("true")
        TRUE("true"),
        @SerializedName("false")
        FALSE("false");
        private final String value;
        private final static Map<String, Property.FeatureEnabled> CONSTANTS = new HashMap<String, Property.FeatureEnabled>();

        static {
            for (Property.FeatureEnabled c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        FeatureEnabled(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Property.FeatureEnabled fromValue(String value) {
            Property.FeatureEnabled constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
