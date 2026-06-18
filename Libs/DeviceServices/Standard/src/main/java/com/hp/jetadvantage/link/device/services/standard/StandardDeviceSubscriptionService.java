/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.device.services.standard;

import androidx.annotation.NonNull;

import com.hp.jetadvantage.link.device.services.interfaces.ICdmCallback;
import com.hp.jetadvantage.link.device.services.interfaces.IDeviceSubscriptionService;
import com.hp.jetadvantage.link.device.services.standard.common.Constants;
import com.hp.jetadvantage.link.device.services.standard.common.DeviceManagementService;
import com.hp.jetadvantage.link.device.services.standard.common.StandardDeviceService;
import com.hp.jetadvantage.link.device.services.standard.common.StandardJsonParser;
import com.hp.ws.cdm.pubsub.Resource;
import com.hp.ws.cdm.pubsub.Subscription;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class StandardDeviceSubscriptionService extends StandardDeviceService implements IDeviceSubscriptionService {
    private static final String TAG = Constants.TAG + "/Subscript";

    private static final String CLIENT_ID = "workpathsvc";
    private static HashMap<String, ICdmCallback> callbackHashMap = new HashMap<>();

    public StandardDeviceSubscriptionService() {
        super();
    }

    public StandardDeviceSubscriptionService(DeviceManagementService deviceManagementService) {
        super(deviceManagementService);
    }

    protected static synchronized void addCallback(@NonNull String subscriptionId, @NonNull ICdmCallback callback) {
        if (subscriptionId.length() > 0) {
            callbackHashMap.put(subscriptionId, callback);
        }
    }

    protected static synchronized void removeCallback(String subscriptionId) {
        callbackHashMap.remove(subscriptionId);
    }

    public static synchronized Collection<ICdmCallback> getCallbacks() {
        return callbackHashMap.values();
    }

    public static synchronized ICdmCallback getCallback(String subscriptionId) {
        return callbackHashMap.get(subscriptionId);
    }

    @Override
    public String Subscribe(@NonNull String[] guns, @NonNull ICdmCallback callback) {
        String subscriptionId = "";
        try {
            Subscription result = Subscribe(guns);
            if (result != null) {
                subscriptionId = result.getSubscriptionId();
                addCallback(subscriptionId, callback);
            }
        } catch (IOException e) {
            return subscriptionId;
        }
        return subscriptionId;
    }

    private Subscription Subscribe(@NonNull String[] guns) throws IOException {
        Subscription newSubscription = new Subscription();
        newSubscription.setClientId(CLIENT_ID);
        newSubscription.setClientInstanceId(CLIENT_ID);
        newSubscription.setCallbackUri(Url.CDM_CALLBACK_URI);

        List<Resource> resources = new ArrayList<>();
        for (String gun : guns) {
            Resource resource = new Resource();
            resource.setGun(gun);
            resource.setSyncOnBoot(Resource.SyncOnBoot.FALSE);
            resources.add(resource);
        }
        newSubscription.setResources(resources);

        CdmCall call = () -> getCDMClient().sendPostRequest(Url.CDM_SUBSCRIPTIONS, StandardJsonParser.INSTANCE.toJson(newSubscription));
        Subscription response = perform(call, Subscription.class);

        return response;
    }

    public boolean Unsubscribe(String subscriptionId) {
        if (subscriptionId == null || subscriptionId.length() == 0) {
            return false;
        }

        removeCallback(subscriptionId);

        String url = Url.CDM_SUBSCRIPTIONS + "/" + subscriptionId + "?clientId=" + CLIENT_ID;
        CdmCall call = () -> getCDMClient().sendDeleteRequest(url);
        try {
            perform(call);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static final class Url {
        public static final String CDM_SUBSCRIPTIONS = "/cdm/pubsub/v2/subscriptions";
        public static final String CDM_CALLBACK_URI = "http://localhost/cdm/e2WorkpathInterop/v1/cdmPubData";
    }
}
