package com.hp.workpath.apitest.dut;

import android.content.Context;

public interface DutController {

    void initializeDutForApiTest();

    void startApp(Context context);

    void stopApp(Context context);

    Accessory getAccessoryController();

    Storage getStorageController();

    interface Accessory {
        /**
         * Create and Attach simulated USB HID accessory device
         * @return simulated device Id
         */
        String attachOwnedUsbHidAccessory();
        void detachOwnedUsbHidAccessory();

        String attachSharedUsbHidAccessory();
        void detachSharedUsbHidAccessory();

        void cleanup();
        void sendAsyncHidReport(String simulatedDeviceId, String base64EncodedData);
    }

    interface Storage{
        void connectUsbDevice();
        void removeUsbDevice();
    }
}
