
package com.hp.ws.cdm.commonglossary;

import java.util.Date;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * A validator of type integer that contains a messageId and optional message.
 * 
 */
public class DateTimeValidator {

    /**
     * standard date and time format - according to RFC 3339. The date/time when the event was produced (according to the service).  It is of the form YYYY-MM-DD'T':HH:mm:SS.sssZ, for example '2012-04-21T08:15:13.511Z'
     * (Required)
     * 
     */
    @SerializedName("value")
    @Expose
    private Date value;
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
     * standard date and time format - according to RFC 3339. The date/time when the event was produced (according to the service).  It is of the form YYYY-MM-DD'T':HH:mm:SS.sssZ, for example '2012-04-21T08:15:13.511Z'
     * (Required)
     * 
     */
    public Date getValue() {
        return value;
    }

    /**
     * standard date and time format - according to RFC 3339. The date/time when the event was produced (according to the service).  It is of the form YYYY-MM-DD'T':HH:mm:SS.sssZ, for example '2012-04-21T08:15:13.511Z'
     * (Required)
     * 
     */
    public void setValue(Date value) {
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
