package com.hp.jetadvantage.link.common.utils;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SecurityUtility {
    private static final String TAG = "SecurityUtility";

    private static final String LINK_APPLICATION_UUID = "link_application_uuid";
    private static final String LINK_PROPERTY_FILE = "link.properties";

    public static String loadApplicationIdFromAssets(Context context) {
        InputStream is = null;
        try {
            is = context.getAssets().open(LINK_PROPERTY_FILE);

            Properties properties = new Properties();
            properties.load(is);
            return properties.getProperty(LINK_APPLICATION_UUID);
        } catch (Exception e) {
            // no file - no application ID
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    SLog.e(TAG, "Failed to close asset file " + LINK_PROPERTY_FILE);
                }
            }
        }

        return null;
    }
}
