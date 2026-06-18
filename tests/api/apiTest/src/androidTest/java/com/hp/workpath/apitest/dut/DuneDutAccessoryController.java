package com.hp.workpath.apitest.dut;

import android.util.Log;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hp.jetadvantage.link.device.services.clients.CDMResponse;
import com.hp.jetadvantage.link.device.services.clients.TestConnector;
import com.hp.workpath.apitest.util.Utils;

import java.util.Base64;
import java.util.Objects;

public class DuneDutAccessoryController implements DutController.Accessory {
    final TestConnector testConn;
    final String deviceIP;
    final String hostHeaderIP;
    String simulatedOwnedHidDevId;
    String simulatedSharedHidDevId;

    DuneDutAccessoryController(TestConnector testConn, String deviceIP, String hostHeaderIP) {
        this.testConn = testConn;
        this.deviceIP = deviceIP;
        this.hostHeaderIP = hostHeaderIP;
    }

    @Override
    public String attachOwnedUsbHidAccessory() {
        return attachUsbHidAccessory("accessoryService/SimulatedHidDevice_77_3333.json", true);
    }

    @Override
    public void detachOwnedUsbHidAccessory() {
        detachUsbHidAccessory(simulatedOwnedHidDevId, true);
    }

    @Override
    public String attachSharedUsbHidAccessory() {
        return attachUsbHidAccessory("accessoryService/SimulatedHidDevice_88_5555.json", false);
    }

    @Override
    public void detachSharedUsbHidAccessory() {
        detachUsbHidAccessory(simulatedSharedHidDevId, false);
    }

    @Override
    public void cleanup() {
        detachOwnedUsbHidAccessory();
        detachSharedUsbHidAccessory();
    }

    @Override
    public void sendAsyncHidReport(String simulatedDeviceId, String base64EncodedData) {
        try {
            CDMResponse<String> response = testConn.getUdwClient(deviceIP, hostHeaderIP).sendUnderwareCommand("1.0.0",
                    "mainApp", "SdkSupportAdapter" +
                            " PUB_Tap " + "\"" + simulatedDeviceId + "\" " + "\"" + base64EncodedData + "\"");
        } catch (Exception e) {
            Log.e("APITEST", "sendAsyncHidReport : Exception " + e.getMessage(), e);
        }
    }

    private String attachUsbHidAccessory(String resourcePath, boolean isOwned) {
        try {
            String simulatedHidDeviceInfo =
                    Utils.loadTestJsonResource(Objects.requireNonNull(getClass().getClassLoader()), resourcePath);

            String base64EncodedInfo = Base64.getEncoder().encodeToString(simulatedHidDeviceInfo.getBytes());
            CDMResponse<String> response = testConn.getUdwClient(deviceIP, hostHeaderIP).sendUnderwareCommand("1.0.0",
                    "mainApp", "SdkSupportAdapter" +
                            " PUB_CreateSimulatedHidDevice " + base64EncodedInfo);
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = jsonParser.parse(response.httpBody).getAsJsonObject();
            String responseBase64 = jsonObject.get("response").getAsString();
            String result = new String(Base64.getDecoder().decode(responseBase64));

            String[] fields = result.split(":");
            String simulatedHidDevId = fields[1].trim();
            if (isOwned) {
                simulatedOwnedHidDevId = simulatedHidDevId;
            } else {
                simulatedSharedHidDevId = simulatedHidDevId;
            }
            Log.i("APITEST", "attachUsbHidAccessory : simulatedHidDevId " + simulatedHidDevId);
            return simulatedHidDevId;
        } catch (Exception e) {
            Log.e("APITEST", "attachUsbHidAccessory : Exception " + e.getMessage(), e);
            return null;
        }
    }

    private void detachUsbHidAccessory(String simulatedHidDevId, boolean isOwned) {
        if (simulatedHidDevId == null) {
            return;
        }
        try {
            CDMResponse<String> response = testConn.getUdwClient(deviceIP, hostHeaderIP).sendUnderwareCommand("1.0.0",
                    "mainApp", "SdkSupportAdapter" +
                            " PUB_DeleteSimulatedHidDevice " + "\"" + simulatedHidDevId + "\"");
            if (isOwned) {
                simulatedOwnedHidDevId = null;
            } else {
                simulatedSharedHidDevId = null;
            }
        } catch (Exception e) {
            Log.e("APITEST", "detachUsbHidAccessory : Exception " + e.getMessage(), e);
        }
    }
}
