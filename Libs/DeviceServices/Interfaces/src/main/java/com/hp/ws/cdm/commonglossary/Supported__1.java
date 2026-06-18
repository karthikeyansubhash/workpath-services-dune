
package com.hp.ws.cdm.commonglossary;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * This field indicates if a property is supported on this device. The default value is true. Provided in capabilities
 * 
 */
public class Supported__1 {

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * (Required)
     * 
     */
    @SerializedName("value")
    @Expose
    private Property.FeatureEnabled value;
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
     * (Required)
     * 
     */
    public Property.FeatureEnabled getValue() {
        return value;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * (Required)
     * 
     */
    public void setValue(Property.FeatureEnabled value) {
        this.value = value;
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

}
