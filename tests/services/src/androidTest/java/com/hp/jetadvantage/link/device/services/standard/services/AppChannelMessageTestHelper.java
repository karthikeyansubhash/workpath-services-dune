/**
 * (C) Copyright 2024 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.device.services.standard.services;

public class AppChannelMessageTestHelper {
    public static String makeTestChannelSetupMessage(String channelId, String packageId, String e2ServiceGun) {
        return makeTestChannelSetupMessage(channelId, packageId, e2ServiceGun, "com.hp.TestPayloadType");
    }

    public static String makeTestChannelSetupMessage(String channelId, String packageId, String e2ServiceGun,
            String e2PayloadTypeGun){
        return "{\n" +
                "    \"channelMessage\": {\n" +
                "        \"channelId\": \"" + channelId + "\",\n" +
                "        \"message\": {\n" +
                "            \"setup\": {\n" +
                "                \"details\": {\n" +
                "                    \"payload\": {\n" +
                "                        \"e2ServiceGun\": \"" + e2ServiceGun + "\",\n" +
                "                        \"payloadGun\": \"" + e2PayloadTypeGun + "\"\n" +
                "                    }\n" +
                "                },\n" +
                "                \"packageId\": \"" + packageId + "\"\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}";
    }

    public static String makeTestChannelTeardownMessage(String channelId) {
        return "{\n" +
                "    \"channelMessage\": {\n" +
                "        \"channelId\": \"" + channelId + "\",\n" +
                "        \"message\": {\n" +
                "            \"teardown\": {}\n" +
                "        }\n" +
                "    }\n" +
                "}";
    }

    public static String makeTestChannelPayloadMessage(String channelId, String typeGUN, String message) {
        return "{\n" +
                "    \"channelMessage\": {\n" +
                "        \"channelId\": \"" + channelId + "\",\n" +
                "        \"message\": {\n" +
                "            \"payload\": {\n" +
                "                \"value\": {\n" +
                "                    \"typeGUN\": \"" + typeGUN + "\",\n" +
                "                    \"value\": {\n" +
                "                        \"message\": \"" + message + "\"\n" +
                "                    }\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}";
    }

    public static String makeTestChannelPayloadValue(String channelId, String typeGUN, String value) {
        return "{\n" +
                "    \"channelMessage\": {\n" +
                "        \"channelId\": \"" + channelId + "\",\n" +
                "        \"message\": {\n" +
                "            \"payload\": {\n" +
                "                \"value\": {\n" +
                "                    \"typeGUN\": \"" + typeGUN + "\",\n" +
                "                    \"value\":" + value +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}";
    }

    public static String makeServiceChannelSetupMessage(String channelId, String packageId, String e2ServiceGun,
                                                     String serviceGun){
        return "{\n" +
                "    \"channelMessage\": {\n" +
                "        \"channelId\": \"" + channelId + "\",\n" +
                "        \"message\": {\n" +
                "            \"setup\": {\n" +
                "                \"details\": {\n" +
                "                    \"service\": {\n" +
                "                        \"e2ServiceGun\": \"" + e2ServiceGun + "\",\n" +
                "                        \"serviceGun\": \"" + serviceGun + "\"\n" +
                "                    }\n" +
                "                },\n" +
                "                \"packageId\": \"" + packageId + "\"\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}";
    }

    public static String makeTestAppChannelServiceMessage(String channelId, String serviceCallId, String path) {
        return "{\n" +
                "    \"channelMessage\": {\n" +
                "        \"channelId\": \"" + channelId + "\",\n" +
                "        \"message\": {\n" +
                "            \"service\": {\n" +
                "                \"attachments\": [],\n" +
                "                \"httpMethod\": \"POST\",\n" +
                "                \"path\": \"" + path + "\",\n" +
                "                \"requestBody\": {\n" +
                "                    \"typeGUN\": \"com.hp.ext.types.authentication.version.1.type.prePromptResultRequest\",\n" +
                "                    \"value\": {\n" +
                "                        \"languageCode\": \"en\",\n" +
                "                        \"sessionAccessToken\": \"eyJhbGciOiAiZGlyIiwgImVuYyI6ICJBMTI4R0NNIiwgIk9BVVRIMlNUQU5EQVJEX0tFWV9JRCI6ICJ7NDNhNGJjZjctNjZkYi00MTY2LTgwZGQtN2NlOTkzNzhhNzUyfSJ9..AYQ1dpmoRntmEt4s.yIw2kqTBRFWbnP-Ef_orow-ZvqxnRdBl3JWCrYwzSYgTkwOypS-oh4D-lbwhiuxu9y0TpuYihNZFuzT0N6Est8f8X5_fZjqjnM-CbyZfO5zsyx3_E6o6OxmTETFzbdpnqI7c9Qbsf3c-H_em2QntC13kTpIZ48zza5ZWoyUmt0qgJ-GZpZy6zPt0Zc9avpez5YQZga4jOPyuVdYnZ64qZS6rOmQw2NGWxeyWqCNPNtl2YUuBN8gELNLfh_uHbAnV0bXcA3rvnLd4GUxGu5P41URjL6sXdtCK4kGkHA_O2ww7Vjw0lccUabrkmL6WSs1R01I2nivTfmZ712xCTC3DqdlFuttiRRJbT4HxdULWAQFKzSjMW3Mdx5h7JuKOoudC8rplTkkFNJTWhjDAyIcZ015FzfuUA5eeZkkHco_2n52j7bwq11KCu84oh9uYjH_RPDVMQUReVCuzYgl7WiSNMyej5boO7KAKwrC8Id__QgSsOggbhGVPfBYFpLmKUwkOyvgBS9xrz2MN73Y6eNy6E2pn3NO-5kpX3xvGVxhEi_9jSb7soR2mmgfW0_0qMljg6fOh8-M_gepDxWaeiGUVsHJkPcjv1_9FpXCVEs-kHvMh9C_guEhMWZNgCXRUwfby2b0Bz00GLrzrvBrfObFXBOOAR7ysSPMbfdl-jxn2MKiwq9fcTOEPk-IrWybWHM6W.NK5uSYZIl9BunjwXmM7rpQ\",\n" +
                "                        \"sessionId\": \"4cb1f5ec-aa0a-4560-971c-c073b89f5b59\"\n" +
                "                    }\n" +
                "                },\n" +
                "                \"serviceCallId\": \"" + serviceCallId + "\"\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}";
    }

    public static String makeTestServiceMessageWithRequestBody(String channelId, String serviceCallId,
                                                                String path, String typeGUN, String value) {
        return "{\n" +
                "    \"channelMessage\": {\n" +
                "        \"channelId\": \"" + channelId + "\",\n" +
                "        \"message\": {\n" +
                "            \"service\": {\n" +
                "                \"attachments\": [],\n" +
                "                \"httpMethod\": \"POST\",\n" +
                "                \"path\": \"" + path + "\",\n" +
                "                \"requestBody\": {\n" +
                "                    \"typeGUN\": \"" + typeGUN + "\",\n" +
                "                    \"value\":" + value +
                "                },\n" +
                "                \"serviceCallId\": \"" + serviceCallId + "\"\n" +
                "            }\n" +
                "        }\n" +
                "    }\n" +
                "}";
    }
}
