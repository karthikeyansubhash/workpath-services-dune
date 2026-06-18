package com.hp.ws.websocket;

import com.google.gson.annotations.SerializedName;
import com.hp.ext.types.common.E2Type;
import com.hp.workpath.internal.utils.adapter.E2TypeJsonConverter;

import java.util.List;

/**
 * Service message data type for Application Channel Message Data Type defined by WorkpathInterop in Dune
 * - refer to dune/src/fw/extensibility/workpath/WorkpathInteropTypes/pub/WorkpathAppChannelMessage.h
 */
public class AppChannelServiceResponse {
    @SerializedName("serviceCallId")
    String serviceCallId;
    @SerializedName("httpStatus")
    int httpStatus;
    @SerializedName("responseBody")
    JsonTypedObject responseBody;
    @SerializedName("attachments")
    List<AppChannelServiceCallAttachment> attachments;

    public AppChannelServiceResponse(String serviceCallId, int httpStatus) {
        this.serviceCallId = serviceCallId;
        this.httpStatus = httpStatus;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public JsonTypedObject getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(JsonTypedObject responseBody) {
        this.responseBody = responseBody;
    }

    public void setResponseBody(E2Type responseBody) {
        this.responseBody = E2TypeJsonConverter.toJsonTypedObject(responseBody);
    }

    public String getServiceCallId() {
        return serviceCallId;
    }

    public void setServiceCallId(String serviceCallId) {
        this.serviceCallId = serviceCallId;
    }

    public List<AppChannelServiceCallAttachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<AppChannelServiceCallAttachment> attachments) {
        this.attachments = attachments;
    }
}
