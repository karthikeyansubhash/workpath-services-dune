// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.attestationlet.model;

import com.hp.jetadvantage.link.services.attestationlet.ClientInfoInternal;

import java.util.List;

public class AttestationInfo {
    private String appId;
    private List<ClientInfoInternal> clients;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public List<ClientInfoInternal> getClients() {
        return clients;
    }

    public void setClients(List<ClientInfoInternal> clients) {
        this.clients = clients;
    }
}
