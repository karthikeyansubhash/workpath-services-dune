package com.hp.jetadvantage.link.device.services.standard.common;

import android.util.Log;

import com.hp.jetadvantage.link.device.services.clients.CDMResponse;

import java.io.IOException;

public class StandardDeviceToken extends StandardDeviceService {
    private static final String TAG = Constants.TAG + "/Token";

    public boolean checkIfUnauthorizedToken() {
        try {
            CDMResponse<String> response = getCDMClient().sendGetRequest(InteropUrl.CAPABILITIES, true);
            if(response.httpStatusCode == Constants.HTTP_STATUS_CODE_UNAUTHORIZED) {
                Log.i(TAG, "checkIfUnauthorizedToken : UnAuthorized");
                return true;
            }
            else {
                Log.i(TAG, "checkIfUnauthorizedToken : No -" + response.httpStatusCode);
            }
        }
        catch (IOException e) {
            Log.i(TAG, "checkIfUnauthorizedToken : IOException=" + e.getMessage());
        }
        return false;
    }

    public static final class InteropUrl {
        public static final String CAPABILITIES = "/cdm/e2WorkpathInterop/v1/capabilities";
    }
}
