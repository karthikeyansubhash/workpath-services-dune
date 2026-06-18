// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.common.constants;

import android.content.Context;
import android.content.Intent;

public class CommonConstants {
    public static final String REQUEST_SWITCH_EVENT = "com.hp.jetadvantage.link.REQUEST_SWITCH";
    public static final String ACTION_SWITCH_EVENT = "com.hp.jetadvantage.link.SWITCH";
    public static final String SYSTEM_SERVICE_PACKAGE_NAME = "com.hp.jetadvantage.link.system";
    public static final String SYSTEM_PACMAN_PACKAGE_NAME = "com.hp.jetadvantage.link.packagemanager";
    public static final String SYSTEM_DOR_PACKAGE_NAME = "com.hp.jetadvantage.link.datacollector";

    public static final String SYSTEM_SVC_FLAG = "SYSTEM_SVC_FLAG";


    public static void sendBroadCastForBoot(Context context, String tag) {
        Intent intent = new Intent(tag);
        intent.setPackage(SYSTEM_SERVICE_PACKAGE_NAME);
        context.sendBroadcast(intent);
    }

    public static class BroadcastActions {
        public static final String ACCESSORY_READY = "com.hp.jetadvantage.link.action.ACCESSORY_READY";

        public static final String READY_ACCESSLET = "com.hp.jetadvantage.link.action.READY_ACCESSLET";
        public static final String READY_ACCESSORYLET = "com.hp.jetadvantage.link.action.READY_ACCESSORYLET";
        public static final String READY_DEVICEEVENTLET = "com.hp.jetadvantage.link.action.READY_DEVICEEVENTLET";
        public static final String READY_STATISTICSLET = "com.hp.jetadvantage.link.action.READY_STATISTICSLET";
        public static final String READY_STORAGELET = "com.hp.jetadvantage.link.action.READY_STORAGELET";

        /**
         * WORKPATH_SERVICE_READY : Broadcast action to notify Workpath service modules that the Workpath core
         * service is ready after boot-up.
         * - Sender : Workpath System app
         * - Receiver : Workpath service modules
         * - Permission : com.hp.jetadvantage.link.system.SYSTEM_PERMISSION
         */
        public static final String WORKPATH_SERVICE_READY = "com.hp.workpath.system.WORKPATH_SERVICE_READY";
    }

    public static class BroadcastFlags {
        // hidden from Android API
        public static final int FLAG_RECEIVER_INCLUDE_BACKGROUND = 0x01000000;
    }

    public static class Permissions {
        public static final String SYSTEM_PERMISSION = "com.hp.jetadvantage.link.system.SYSTEM_PERMISSION";
    }
}
