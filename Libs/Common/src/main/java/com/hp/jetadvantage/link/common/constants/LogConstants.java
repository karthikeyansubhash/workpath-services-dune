package com.hp.jetadvantage.link.common.constants;

/**
 * Centralized log tag constants for WorkPath Service components.
 * Prefix `[WS]` allows quick filtering in aggregated logs.
 * Keep each tag within Android's 23 character limit (including prefix) if used with android.util.Log.
 */
public final class LogConstants {
    private LogConstants() {
        // Prevent instantiation
    }

    public static final String PREFIX_WORKPATH_SERVICE      = "[WS]";
    public static final String TAG_ACCESS_SERVICE           = PREFIX_WORKPATH_SERVICE + "AUTH";
    public static final String TAG_ACCESSORY_SERVICE        = PREFIX_WORKPATH_SERVICE + "ACCE";
    public static final String TAG_CONFIG_SERVICE           = PREFIX_WORKPATH_SERVICE + "CONF";
    public static final String TAG_DEVICE_SERVICE           = PREFIX_WORKPATH_SERVICE + "DEVI";
    public static final String TAG_DEVICE_EVENT_SERVICE     = PREFIX_WORKPATH_SERVICE + "EVEN";
    public static final String TAG_DEVICE_SETTING_SERVICE   = PREFIX_WORKPATH_SERVICE + "SETT";
    public static final String TAG_DEVICE_USAGE_SERVICE     = PREFIX_WORKPATH_SERVICE + "USAG";
    public static final String TAG_EMAIL_SERVICE            = PREFIX_WORKPATH_SERVICE + "EMAI";
    public static final String TAG_JOB_SERVICE              = PREFIX_WORKPATH_SERVICE + "JOB";
    public static final String TAG_LAUNCHER_SERVICE         = PREFIX_WORKPATH_SERVICE + "LAUN";
    public static final String TAG_PRINT_SERVICE            = PREFIX_WORKPATH_SERVICE + "PRIN";
    public static final String TAG_SCAN_SERVICE             = PREFIX_WORKPATH_SERVICE + "SCAN";
    public static final String TAG_STATISTICS_SERVICE       = PREFIX_WORKPATH_SERVICE + "STAT";
    public static final String TAG_STORAGE_SERVICE          = PREFIX_WORKPATH_SERVICE + "STOR";
    public static final String TAG_SUPPLIES_SERVICE         = PREFIX_WORKPATH_SERVICE + "SUPP";
}
