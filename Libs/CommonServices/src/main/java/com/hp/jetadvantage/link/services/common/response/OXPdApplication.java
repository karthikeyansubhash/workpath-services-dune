// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.common.response;

@Deprecated  //Will be removed for Dune
public class OXPdApplication {
    private String href;
    private String id;
    private String type;
    private String intentUri;
    private String applicationId;
    private String uiContext;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIntentUri() {
        return intentUri;
    }

    public void setIntentUri(String intentUri) {
        this.intentUri = intentUri;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getUiContext() {
        return uiContext;
    }

    public void setUiContext(String uiContext) {
        this.uiContext = uiContext;
    }
}
