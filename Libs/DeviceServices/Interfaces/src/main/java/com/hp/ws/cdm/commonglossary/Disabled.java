
package com.hp.ws.cdm.commonglossary;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * When disabled is applied to an individual data property, it means that property may not be modified. When disabled is applied to an object within options, it means that the disabled option may not be selected. Depending on the experience, views may either grey-out or hide a widget with a data property that has a disabled constraint.The default value for disabled is false.
 * 
 */
public class Disabled {

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
     * (Required)
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
     * (Required)
     * 
     */
    public Integer getMessageId() {
        return messageId;
    }

    /**
     * The message id is the numeric representation of a string id.  It identifies the string message associated with the validator.
     * (Required)
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
