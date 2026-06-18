package com.hp.ws.websocket;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Service message data type for Application Channel Message Data Type defined by WorkpathInterop in Dune
 * - refer to dune/src/fw/extensibility/workpath/WorkpathInteropTypes/pub/WorkpathAppChannelMessage.h
 */
public class AppChannelService {
    @SerializedName("serviceCallId")
    String serviceCallId;

    @SerializedName("path")
    String path;

    @SerializedName("httpMethod")
    String httpMethod;

    @SerializedName("queryParameters")
    JsonTypedObject queryParameters;

    @SerializedName("requestBody")
    JsonTypedObject requestBody;

    @SerializedName("attachments")
    List<AppChannelServiceCallAttachment> attachments;

    public String getServiceCallId() {
        return this.serviceCallId;
    }

    public String getPath() {
        return this.path;
    }

    public String getHttpMethod() {
        return this.httpMethod;
    }

    public JsonTypedObject getRequestBody() {
        return this.requestBody;
    }

    public List<AppChannelServiceCallAttachment> getAttachments() {
        return this.attachments;
    }

}
