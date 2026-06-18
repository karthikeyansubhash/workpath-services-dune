
package com.hp.ws.cdm.security;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Describes a single credential that is required
 * 
 */
public class CredentialMetadataItem {

    /**
     * An indication of the type of the credential. Future revisions of this type may add new values.
     * 
     */
    @SerializedName("metaType")
    @Expose
    private CredentialMetadataItem.MetaType metaType;
    /**
     * A non-localized name for the credential
     * 
     */
    @SerializedName("metaTitle")
    @Expose
    private String metaTitle;
    /**
     * The minimum length in unicode characters required
     * 
     */
    @SerializedName("minLength")
    @Expose
    private Integer minLength;
    /**
     * The maximum length in unicode characters allowed
     * 
     */
    @SerializedName("maxLength")
    @Expose
    private Integer maxLength;
    /**
     * The format for the field. Future revisions of this type may add new values.
     * 
     */
    @SerializedName("format")
    @Expose
    private CredentialMetadataItem.Format format;

    /**
     * An indication of the type of the credential. Future revisions of this type may add new values.
     * 
     */
    public CredentialMetadataItem.MetaType getMetaType() {
        return metaType;
    }

    /**
     * An indication of the type of the credential. Future revisions of this type may add new values.
     * 
     */
    public void setMetaType(CredentialMetadataItem.MetaType metaType) {
        this.metaType = metaType;
    }

    /**
     * A non-localized name for the credential
     * 
     */
    public String getMetaTitle() {
        return metaTitle;
    }

    /**
     * A non-localized name for the credential
     * 
     */
    public void setMetaTitle(String metaTitle) {
        this.metaTitle = metaTitle;
    }

    /**
     * The minimum length in unicode characters required
     * 
     */
    public Integer getMinLength() {
        return minLength;
    }

    /**
     * The minimum length in unicode characters required
     * 
     */
    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    /**
     * The maximum length in unicode characters allowed
     * 
     */
    public Integer getMaxLength() {
        return maxLength;
    }

    /**
     * The maximum length in unicode characters allowed
     * 
     */
    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    /**
     * The format for the field. Future revisions of this type may add new values.
     * 
     */
    public CredentialMetadataItem.Format getFormat() {
        return format;
    }

    /**
     * The format for the field. Future revisions of this type may add new values.
     * 
     */
    public void setFormat(CredentialMetadataItem.Format format) {
        this.format = format;
    }


    /**
     * The format for the field. Future revisions of this type may add new values.
     * 
     */
    public enum Format {

        @SerializedName("ascii")
        ASCII("ascii"),
        @SerializedName("any")
        ANY("any"),
        @SerializedName("numeric")
        NUMERIC("numeric"),
        @SerializedName("email")
        EMAIL("email"),
        @SerializedName("domainName")
        DOMAIN_NAME("domainName");
        private final String value;
        private final static Map<String, CredentialMetadataItem.Format> CONSTANTS = new HashMap<String, CredentialMetadataItem.Format>();

        static {
            for (CredentialMetadataItem.Format c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        Format(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static CredentialMetadataItem.Format fromValue(String value) {
            CredentialMetadataItem.Format constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * An indication of the type of the credential. Future revisions of this type may add new values.
     * 
     */
    public enum MetaType {

        @SerializedName("domainName")
        DOMAIN_NAME("domainName"),
        @SerializedName("userName")
        USER_NAME("userName"),
        @SerializedName("password")
        PASSWORD("password"),
        @SerializedName("pin")
        PIN("pin");
        private final String value;
        private final static Map<String, CredentialMetadataItem.MetaType> CONSTANTS = new HashMap<String, CredentialMetadataItem.MetaType>();

        static {
            for (CredentialMetadataItem.MetaType c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        MetaType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static CredentialMetadataItem.MetaType fromValue(String value) {
            CredentialMetadataItem.MetaType constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
