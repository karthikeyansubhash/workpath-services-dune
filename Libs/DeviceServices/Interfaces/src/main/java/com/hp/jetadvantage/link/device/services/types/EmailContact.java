package com.hp.jetadvantage.link.device.services.types;

import com.google.gson.annotations.SerializedName;

public class EmailContact {
    @SerializedName("type")
    private String mType;

    @SerializedName("emailAddress")
    private String mEmailAddress;

    @SerializedName("address")
    private String mDefaultEmailAddress;

    @SerializedName("displayName")
    private String mDisplayName;

    public void setType(String type) {
        this.mType = type;
    }

    public String getType() {
        return mType;
    }

    public void setEmailAddress(String emailAddress) {
        this.mEmailAddress = emailAddress;
    }

    public String getEmailAddress() {
        return mEmailAddress;
    }

    public void setDefaultEmailAddress(String defaultEmailAddress) {
        this.mDefaultEmailAddress = defaultEmailAddress;
    }

    public String getDefaultEmailAddress() {
        return mDefaultEmailAddress;
    }

    public void setDisplayName(String displayName) {
        this.mDisplayName = displayName;
    }

    public String getDisplayName() {
        return mDisplayName;
    }
}
