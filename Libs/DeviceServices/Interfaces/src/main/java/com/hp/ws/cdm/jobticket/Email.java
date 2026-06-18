
package com.hp.ws.cdm.jobticket;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.Link;
import com.hp.ws.cdm.commonglossary.Property;

public class Email {

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    @SerializedName("version")
    @Expose
    private String version;
    /**
     * It is array of emailContact objects for bcc list
     * 
     */
    @SerializedName("bccList")
    @Expose
    private List<EmailContact> bccList = new ArrayList<EmailContact>();
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("bccListIsEditable")
    @Expose
    private Property.FeatureEnabled bccListIsEditable;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("bccListSignInRequired")
    @Expose
    private Property.FeatureEnabled bccListSignInRequired;
    /**
     * body of email message
     * 
     */
    @SerializedName("body")
    @Expose
    private String body;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("isBodyEditable")
    @Expose
    private Property.FeatureEnabled isBodyEditable;
    /**
     * It is array of emailContact objects for cc list
     * 
     */
    @SerializedName("ccList")
    @Expose
    private List<EmailContact> ccList = new ArrayList<EmailContact>();
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("ccListIsEditable")
    @Expose
    private Property.FeatureEnabled ccListIsEditable;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("ccListSignInRequired")
    @Expose
    private Property.FeatureEnabled ccListSignInRequired;
    /**
     * Encrypt the email using provided algorithm
     * 
     */
    @SerializedName("emailEncryption")
    @Expose
    private EmailEncryption emailEncryption;
    /**
     * Email signing using the provided algorithm
     * 
     */
    @SerializedName("emailSigning")
    @Expose
    private EmailSigning emailSigning;
    /**
     * This object will have each emailContact details
     * 
     */
    @SerializedName("from")
    @Expose
    private EmailContact from;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("isFromEditable")
    @Expose
    private Property.FeatureEnabled isFromEditable;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("fromSignInRequired")
    @Expose
    private Property.FeatureEnabled fromSignInRequired;
    /**
     * This field contains the subject line of the email 
     * 
     */
    @SerializedName("subject")
    @Expose
    private String subject;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("isSubjectEditable")
    @Expose
    private Property.FeatureEnabled isSubjectEditable;
    /**
     * It is array of emailContact objects for to list
     * 
     */
    @SerializedName("toList")
    @Expose
    private List<EmailContact> toList = new ArrayList<EmailContact>();
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("toListIsEditable")
    @Expose
    private Property.FeatureEnabled toListIsEditable;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("toListSignInRequired")
    @Expose
    private Property.FeatureEnabled toListSignInRequired;
    /**
     * Profile name which has SMTP details for specific user
     * 
     */
    @SerializedName("senderProfileName")
    @Expose
    private String senderProfileName;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("addressFieldRestrictionsEnabled")
    @Expose
    private Property.FeatureEnabled addressFieldRestrictionsEnabled;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("allowInvalidEmailAddress")
    @Expose
    private Property.FeatureEnabled allowInvalidEmailAddress;
    @SerializedName("links")
    @Expose
    private List<Link> links = new ArrayList<Link>();
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("emailEncryptionEnabled")
    @Expose
    private Property.FeatureEnabled emailEncryptionEnabled;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("emailSigningEnabled")
    @Expose
    private Property.FeatureEnabled emailSigningEnabled;

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    public String getVersion() {
        return version;
    }

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * It is array of emailContact objects for bcc list
     * 
     */
    public List<EmailContact> getBccList() {
        return bccList;
    }

    /**
     * It is array of emailContact objects for bcc list
     * 
     */
    public void setBccList(List<EmailContact> bccList) {
        this.bccList = bccList;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getBccListIsEditable() {
        return bccListIsEditable;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setBccListIsEditable(Property.FeatureEnabled bccListIsEditable) {
        this.bccListIsEditable = bccListIsEditable;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getBccListSignInRequired() {
        return bccListSignInRequired;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setBccListSignInRequired(Property.FeatureEnabled bccListSignInRequired) {
        this.bccListSignInRequired = bccListSignInRequired;
    }

    /**
     * body of email message
     * 
     */
    public String getBody() {
        return body;
    }

    /**
     * body of email message
     * 
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getIsBodyEditable() {
        return isBodyEditable;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setIsBodyEditable(Property.FeatureEnabled isBodyEditable) {
        this.isBodyEditable = isBodyEditable;
    }

    /**
     * It is array of emailContact objects for cc list
     * 
     */
    public List<EmailContact> getCcList() {
        return ccList;
    }

    /**
     * It is array of emailContact objects for cc list
     * 
     */
    public void setCcList(List<EmailContact> ccList) {
        this.ccList = ccList;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getCcListIsEditable() {
        return ccListIsEditable;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setCcListIsEditable(Property.FeatureEnabled ccListIsEditable) {
        this.ccListIsEditable = ccListIsEditable;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getCcListSignInRequired() {
        return ccListSignInRequired;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setCcListSignInRequired(Property.FeatureEnabled ccListSignInRequired) {
        this.ccListSignInRequired = ccListSignInRequired;
    }

    /**
     * Encrypt the email using provided algorithm
     * 
     */
    public EmailEncryption getEmailEncryption() {
        return emailEncryption;
    }

    /**
     * Encrypt the email using provided algorithm
     * 
     */
    public void setEmailEncryption(EmailEncryption emailEncryption) {
        this.emailEncryption = emailEncryption;
    }

    /**
     * Email signing using the provided algorithm
     * 
     */
    public EmailSigning getEmailSigning() {
        return emailSigning;
    }

    /**
     * Email signing using the provided algorithm
     * 
     */
    public void setEmailSigning(EmailSigning emailSigning) {
        this.emailSigning = emailSigning;
    }

    /**
     * This object will have each emailContact details
     * 
     */
    public EmailContact getFrom() {
        return from;
    }

    /**
     * This object will have each emailContact details
     * 
     */
    public void setFrom(EmailContact from) {
        this.from = from;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getIsFromEditable() {
        return isFromEditable;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setIsFromEditable(Property.FeatureEnabled isFromEditable) {
        this.isFromEditable = isFromEditable;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getFromSignInRequired() {
        return fromSignInRequired;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setFromSignInRequired(Property.FeatureEnabled fromSignInRequired) {
        this.fromSignInRequired = fromSignInRequired;
    }

    /**
     * This field contains the subject line of the email 
     * 
     */
    public String getSubject() {
        return subject;
    }

    /**
     * This field contains the subject line of the email 
     * 
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getIsSubjectEditable() {
        return isSubjectEditable;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setIsSubjectEditable(Property.FeatureEnabled isSubjectEditable) {
        this.isSubjectEditable = isSubjectEditable;
    }

    /**
     * It is array of emailContact objects for to list
     * 
     */
    public List<EmailContact> getToList() {
        return toList;
    }

    /**
     * It is array of emailContact objects for to list
     * 
     */
    public void setToList(List<EmailContact> toList) {
        this.toList = toList;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getToListIsEditable() {
        return toListIsEditable;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setToListIsEditable(Property.FeatureEnabled toListIsEditable) {
        this.toListIsEditable = toListIsEditable;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getToListSignInRequired() {
        return toListSignInRequired;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setToListSignInRequired(Property.FeatureEnabled toListSignInRequired) {
        this.toListSignInRequired = toListSignInRequired;
    }

    /**
     * Profile name which has SMTP details for specific user
     * 
     */
    public String getSenderProfileName() {
        return senderProfileName;
    }

    /**
     * Profile name which has SMTP details for specific user
     * 
     */
    public void setSenderProfileName(String senderProfileName) {
        this.senderProfileName = senderProfileName;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getAddressFieldRestrictionsEnabled() {
        return addressFieldRestrictionsEnabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setAddressFieldRestrictionsEnabled(Property.FeatureEnabled addressFieldRestrictionsEnabled) {
        this.addressFieldRestrictionsEnabled = addressFieldRestrictionsEnabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getAllowInvalidEmailAddress() {
        return allowInvalidEmailAddress;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setAllowInvalidEmailAddress(Property.FeatureEnabled allowInvalidEmailAddress) {
        this.allowInvalidEmailAddress = allowInvalidEmailAddress;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getEmailEncryptionEnabled() {
        return emailEncryptionEnabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setEmailEncryptionEnabled(Property.FeatureEnabled emailEncryptionEnabled) {
        this.emailEncryptionEnabled = emailEncryptionEnabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getEmailSigningEnabled() {
        return emailSigningEnabled;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setEmailSigningEnabled(Property.FeatureEnabled emailSigningEnabled) {
        this.emailSigningEnabled = emailSigningEnabled;
    }

}
