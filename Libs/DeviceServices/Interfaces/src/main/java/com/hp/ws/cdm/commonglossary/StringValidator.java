
package com.hp.ws.cdm.commonglossary;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * A validator of type string that contains a messageId and optional message.
 * 
 */
public class StringValidator {

    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("value")
    @Expose
    private String value;
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
     * 
     * (Required)
     * 
     */
    public String getValue() {
        return value;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setValue(String value) {
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
