/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */

package com.hp.ext.clients;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.ResponseBody;

public interface InjectedHttpClient {
    String getResponseAsString(Request httpRequest) throws IOException;

    ResponseBody getResponseBody(Request httpRequest) throws IOException;
}
