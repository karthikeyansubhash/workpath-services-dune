// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.connect.response;

import com.google.gson.annotations.SerializedName;

public class OmniValueData<T> {

    @SerializedName("Values")
    public T values;

    public T getValues() {
        return values;
    }

    public void setValues(T values) {
        this.values = values;
    }
}
