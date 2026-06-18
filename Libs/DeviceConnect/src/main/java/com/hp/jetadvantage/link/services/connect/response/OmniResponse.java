// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.connect.response;

import com.google.gson.annotations.SerializedName;

public class OmniResponse {

    @SerializedName("TransactionId")
    private String transactionId;

    @SerializedName("Status")
    private int status;

    @SerializedName("ETag")
    private String eTag;

    @SerializedName("Data")
    private String data;

    public String getTransactionId() {
        return transactionId;
    }

    public int getStatus() {
        return status;
    }

    public String geteTag() {
        return eTag;
    }

    public String getData() {
        return data;
    }
}
