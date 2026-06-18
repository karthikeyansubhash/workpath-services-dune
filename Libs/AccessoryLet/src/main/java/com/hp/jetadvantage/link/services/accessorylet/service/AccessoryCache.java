/**
 * (C) Copyright 2025 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.services.accessorylet.service;

import static com.hp.jetadvantage.link.common.constants.LogConstants.TAG_ACCESSORY_SERVICE;

import android.util.Log;

import com.hp.jetadvantage.link.api.accessory.RegistrationType;
import com.hp.jetadvantage.link.api.accessory.hid.HIDAccessoryInfo;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Cache to store the attached accessory data
 */
public class AccessoryCache {
    private static final String TAG = TAG_ACCESSORY_SERVICE + "/SVC/Cache";
    /**
     * Singleton instance
     */
    private static AccessoryCache instance;

    /**
     * Map to cache the attached accessory data
     * ConcurrentHashMap<resourceId, AccessoryData>
     */
    private ConcurrentHashMap<String, AccessoryData> accessoriesCachedMap =
            new ConcurrentHashMap<>();

    /**
     * Private constructor to prevent instantiation
     */
    private AccessoryCache() {
    }

    /**
     * Public method to provide access to the singleton instance
     */
    public static synchronized AccessoryCache getInstance() {
        if (instance == null) {
            instance = new AccessoryCache();
        }
        return instance;
    }

    public void add(String resourceId, String appPackageName, HIDAccessoryInfo accessoryInfo) {
        if (resourceId != null) {
            accessoriesCachedMap.put(resourceId, new AccessoryData(resourceId, appPackageName, accessoryInfo));
            Log.d(TAG, "add: " + resourceId + ", " + appPackageName + ", " + accessoryInfo);
        }
    }

    public String getPackageName(String resourceId) {
        if (resourceId != null) {
            AccessoryData data = accessoriesCachedMap.get(resourceId);
            if (data != null) {
                return data.getAppPackageName();
            }
        }
        return null;
    }

    public boolean isCached(String resourceId) {
        return accessoriesCachedMap.containsKey(resourceId);
    }

    /**
     * Get the accessory resource Id (UUID string) for the given HIDAccessoryInfo that is owned type
     *
     * @param hidAccessoryInfo
     * @param appPackageName   app's package name that owns the accessory
     * @return accessory resource Id (UUID string) if found, null otherwise
     */
    public String getOwnedAccessoryId(String appPackageName, HIDAccessoryInfo hidAccessoryInfo) {
        for (AccessoryData accessoryData : accessoriesCachedMap.values()) {
            if (accessoryData.getAppPackageName().equals(appPackageName) &&
                    accessoryData.getAccessoryInfo().equals(hidAccessoryInfo) &&
                    accessoryData.isOwnedType()) {
                return accessoryData.getResourceId();
            }
        }
        return null;
    }

    public UUID getOpenHidId(String resourceId) {
        UUID openHidId = null;
        AccessoryData accessoryData = accessoriesCachedMap.get(resourceId);
        if (accessoryData != null) {
            openHidId = accessoryData.getOpenHidId();
        } else {
            Log.e(TAG, "getOpenHidId: AccessoryData is not cached for resourceId: " + resourceId);
        }
        return openHidId;
    }

    public boolean isOwnedType(String resourceId) {
        boolean isOwned = false;
        AccessoryData accessoryData = accessoriesCachedMap.get(resourceId);
        if (accessoryData != null) {
            isOwned = accessoryData.isOwnedType();
        } else {
            Log.e(TAG, "isOwnedType: AccessoryData is not cached for resourceId: " + resourceId);
        }
        return isOwned;
    }

    public boolean isInvalidated(String resourceId) {
        if (resourceId != null) {
            AccessoryData data = accessoriesCachedMap.get(resourceId);
            if (data != null) {
                return data.isInvalidated();
            }
        }
        return false;
    }

    public void invalidate(String resourceId) {
        if (resourceId != null) {
            AccessoryData data = accessoriesCachedMap.get(resourceId);
            if (data != null) {
                data.invalidate();
            } else {
                Log.e(TAG, "invalidate: AccessoryData is not cached for resourceId: " + resourceId);
            }
        }
    }

    public boolean setOpenHidId(String resourceId, UUID openHidId) {
        boolean result = false;
        AccessoryData accessoryData = accessoriesCachedMap.get(resourceId);
        if (accessoryData != null) {
            accessoryData.setOpenHidId(openHidId);
            result = true;
        } else {
            Log.e(TAG, "setOpenHidId: AccessoryData is null for resourceId: " + resourceId);
            result = false;
        }
        return result;
    }

    public void clear() {
        accessoriesCachedMap.clear();
    }

    protected void remove(String resourceId) {
        if (resourceId != null) {
            accessoriesCachedMap.remove(resourceId);
            Log.d(TAG, "remove: " + resourceId);
        }
    }

    protected HIDAccessoryInfo getHidAccessoryInfo(String resourceId) {
        AccessoryData accessoryData = null;
        if (resourceId != null) {
            accessoryData = accessoriesCachedMap.get(resourceId);
            if (accessoryData == null) {
                Log.e(TAG, "getHidAccessoryInfo: AccessoryData is null for resourceId: " + resourceId);
            } else {
                Log.d(TAG, "getHidAccessoryInfo: " + resourceId + ", " + accessoryData.getAccessoryInfo());
            }
        }
        return accessoryData != null ? accessoryData.getAccessoryInfo() : null;
    }

    public static class AccessoryData {
        private String mResourceId;
        private String mAppPackageName;
        private HIDAccessoryInfo mAccessoryInfo = null;
        private UUID mOpenHidId = null;
        private boolean mIsInvalidated = false;

        public AccessoryData(String resourceId, String appPackageName, HIDAccessoryInfo accessoryInfo) {
            this.mResourceId = resourceId;
            this.mAppPackageName = appPackageName;
            this.mAccessoryInfo = accessoryInfo;
        }

        public String getResourceId() {
            return mResourceId;
        }

        public String getAppPackageName() {
            return mAppPackageName;
        }

        public HIDAccessoryInfo getAccessoryInfo() {
            return mAccessoryInfo;
        }

        public UUID getOpenHidId() {
            return mOpenHidId;
        }

        public void setOpenHidId(UUID openHidId) {
            this.mOpenHidId = openHidId;
        }

        public boolean isOwnedType() {
            boolean isOwnedType = false;
            if (mAccessoryInfo != null) {
                isOwnedType = mAccessoryInfo.getRegistrationType() == RegistrationType.OWNED;
            } else {
                Log.e(TAG, "isOwnedType: AccessoryInfo is null, resourceId:" + mResourceId);
            }
            return isOwnedType;
        }

        public boolean isInvalidated() {
            return mIsInvalidated;
        }

        public void invalidate() {
            mIsInvalidated = true;
        }
    }
}
