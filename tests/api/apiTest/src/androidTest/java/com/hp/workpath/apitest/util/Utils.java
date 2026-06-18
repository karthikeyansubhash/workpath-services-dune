package com.hp.workpath.apitest.util;

import static org.junit.Assert.assertNotNull;

import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class Utils {
    public static String loadTestJsonResource(ClassLoader classLoader, String fileName) {
        try {
            InputStream is = classLoader.getResourceAsStream(fileName);
            assertNotNull("The file " + fileName + "could not be found in resources.", is);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            String json = stringBuilder.toString();
            is.close();
            return json;
        } catch (IOException ignored) {
            return null;
        }
    }

    public static <T> T loadTestJsonResource(ClassLoader classLoader, Class<T> clazz, String fileName) {
        String json = loadTestJsonResource(classLoader, fileName);
        Gson gson = new Gson();
        return gson.fromJson(json, clazz);
    }

    private static final int MAX_LOG_LENGTH = 4000;
    public static void logLongMessage(String tag, String message) {
        if (message.length() > MAX_LOG_LENGTH) {
            int chunkCount = message.length() / MAX_LOG_LENGTH;
            for (int i = 0; i <= chunkCount; i++) {
                int max = MAX_LOG_LENGTH * (i + 1);
                if (max >= message.length()) {
                    Log.i(tag, message.substring(MAX_LOG_LENGTH * i));
                } else {
                    Log.i(tag, message.substring(MAX_LOG_LENGTH * i, max));
                }
            }
        } else {
            Log.i(tag, message);
        }
    }

    public static boolean isValidUUID(String str) {
        if (str == null || str.length() != 36) {
            return false;
        }
        try {
            UUID.fromString(str);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
