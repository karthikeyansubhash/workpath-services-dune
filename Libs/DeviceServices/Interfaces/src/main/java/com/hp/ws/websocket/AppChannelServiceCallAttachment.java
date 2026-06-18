package com.hp.ws.websocket;

import com.google.gson.annotations.SerializedName;

public class AppChannelServiceCallAttachment {
    @SerializedName("contentTypeMainType")
    String contentTypeMainType;
    @SerializedName("contentTypeSubtype")
    String contentTypeSubtype;
    @SerializedName("contentTypeCharset")
    String contentTypeCharset;
    @SerializedName("disposition")
    String disposition;
    @SerializedName("dispositionName")
    String dispositionName;
    @SerializedName("dispositionFileName")
    String dispositionFileName;
    @SerializedName("content")
    String content;

    public String getContentTypeMainType() {
        return contentTypeMainType;
    }

    public void setContentTypeMainType(String contentTypeMainType) {
        this.contentTypeMainType = contentTypeMainType;
    }

    public String getContentTypeSubtype() {
        return contentTypeSubtype;
    }

    public void setContentTypeSubtype(String contentTypeSubtype) {
        this.contentTypeSubtype = contentTypeSubtype;
    }

    public String getContentTypeCharset() {
        return contentTypeCharset;
    }

    public void setContentTypeCharset(String contentTypeCharset) {
        this.contentTypeCharset = contentTypeCharset;
    }

    public String getDisposition() {
        return disposition;
    }

    public void setDisposition(String disposition) {
        this.disposition = disposition;
    }

    public String getDispositionName() {
        return dispositionName;
    }

    public void setDispositionName(String dispositionName) {
        this.dispositionName = dispositionName;
    }

    public String getDispositionFileName() {
        return dispositionFileName;
    }

    public void setDispositionFileName(String dispositionFileName) {
        this.dispositionFileName = dispositionFileName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
