package com.hp.jetadvantage.link.device.services.utils;

import static org.junit.Assert.assertNotNull;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class Utils {
    public static String loadTestJsonResource(ClassLoader classLoader, String fileName) throws IOException{
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
    }

    public static String loadTestTxtResource(ClassLoader classLoader, String fileName) throws IOException{
        InputStream is = classLoader.getResourceAsStream(fileName);
        assertNotNull("The file " + fileName + "could not be found in resources.", is);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line).append("\r\n");
        }
        String json = stringBuilder.toString();
        is.close();
        return json;
    }
}
