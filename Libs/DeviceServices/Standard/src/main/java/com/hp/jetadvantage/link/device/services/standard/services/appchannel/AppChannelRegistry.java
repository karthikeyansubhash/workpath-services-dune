package com.hp.jetadvantage.link.device.services.standard.services.appchannel;

import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.hp.jetadvantage.link.common.utils.StringUtility;
import com.hp.jetadvantage.link.device.services.standard.common.Constants;
import com.hp.jetadvantage.link.device.services.standard.common.StandardJsonParser;
import com.hp.jetadvantage.link.device.services.standard.common.StandardSecureAppStorage;
import com.hp.jetadvantage.link.device.services.standard.services.StandardWebsocketCallbackService;
import com.hp.ws.websocket.AppChannelSetup;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

public class AppChannelRegistry {
    private static final String TAG = Constants.TAG + "/AppChn/Reg";
    private static final String KEY_APP_CHANNEL_MAP = "app_channel_map";

    // The map of channelId to AppChannelSetup
    private final HashMap<String, AppChannelSetup> channelMap = new HashMap<>();
    private final StandardWebsocketCallbackService websocketService;

    public AppChannelRegistry(StandardWebsocketCallbackService websocketService) {
        this.websocketService = websocketService;
        loadChannels();
    }

    protected synchronized boolean addChannel(String channelId, AppChannelSetup setup) {
        if (channelMap.containsKey(channelId)) return false;
        channelMap.put(channelId, setup);
        saveChannels();
        return true;
    }

    protected synchronized void removeChannel(String channelId) {
        channelMap.remove(channelId);
        saveChannels();
    }

    protected synchronized AppChannelSetup getChannel(String channelId) {
        return channelMap.get(channelId);
    }

    protected synchronized String getPackageId(String channelId) {
        AppChannelSetup setup = channelMap.get(channelId);
        if (setup == null) {
            Log.e(TAG, "getChannelPackageId: channelId not found :" + channelId);
            return "";
        }
        return setup.getPackageId();
    }

    protected synchronized List<AppChannelSetup> getChannels() {
        if (channelMap.isEmpty()) {
            return null;
        }
        return List.copyOf(channelMap.values());
    }

    protected synchronized void clearChannels() {
        channelMap.clear();
        StandardSecureAppStorage.setSharedPreference(websocketService.getApplicationContext(), KEY_APP_CHANNEL_MAP,
                null);
    }

    private synchronized void loadChannels() {
        try {
            String json = StandardSecureAppStorage.getSharedPreference(websocketService.getApplicationContext(),
                    KEY_APP_CHANNEL_MAP);
            if (!StringUtility.isEmpty(json)) {
                Log.i(TAG, "loadChannels: " + json);
                //For compatibility with R8 optimization and obfuscation in release builds, use a parameterized type
                Type type = TypeToken.getParameterized(HashMap.class, String.class, AppChannelSetup.class).getType();
                channelMap.putAll(StandardJsonParser.INSTANCE.fromJson(json, type));
            }
        } catch (RuntimeException e) {
            Log.e(TAG, "loadChannels: " + e.getMessage(), e);
        }
    }

    private synchronized void saveChannels() {
        String json = StandardJsonParser.INSTANCE.toJson(channelMap);
        StandardSecureAppStorage.setSharedPreference(websocketService.getApplicationContext(), KEY_APP_CHANNEL_MAP,
                json);
    }
}
