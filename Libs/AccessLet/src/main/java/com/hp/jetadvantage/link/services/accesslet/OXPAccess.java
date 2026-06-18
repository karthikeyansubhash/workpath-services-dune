// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.accesslet;

import com.hp.jetadvantage.link.common.Platform;
import com.hp.jetadvantage.link.common.utils.JsonParser;
import com.hp.jetadvantage.link.services.accesslet.model.TokenData;
import com.hp.jetadvantage.link.services.common.exception.SdkNotSupportedException;
import com.hp.jetadvantage.link.services.common.exception.SdkServiceErrorException;
import com.hp.jetadvantage.link.services.connect.OXPdConnect;
import com.hp.jetadvantage.link.services.connect.request.OmniRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class OXPAccess {

    public static String getDeviceToken(String uuid) throws Exception {
        if (Platform.isPanel()) {
            TokenData tokenData = new TokenData();
            tokenData.setClientId(uuid);

            String response = OXPdConnect.getInstance().callOmniInternal(OmniRequest.POST,
                    OmniRequest.SYSTEM_CLOUD_AUTHZ,
                    JsonParser.getInstance().toJson(tokenData));
            try {
                JSONObject obj = new JSONObject(response);
                return obj.getString("code");
            } catch (JSONException jso) {
                throw new SdkServiceErrorException("AccessService is failed to retrieve device token");
            }
        } else {
            throw new SdkNotSupportedException("AccessService is not supported on mobile");
        }
    }
}
