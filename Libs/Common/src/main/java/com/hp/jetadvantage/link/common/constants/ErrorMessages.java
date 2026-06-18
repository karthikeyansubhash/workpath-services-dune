package com.hp.jetadvantage.link.common.constants;

public class ErrorMessages {
    public static class ServiceError {
        // =========================== Accessory Service Errors ===========================
        public static final String ACCESSORY_SERVICE_NOT_READY =
                "The accessory service is not ready.";
        public static final String ACCESSORY_NOT_READY =
                "The requested accessory is temporarily unavailable. Please try again later.";
        public static final String ACCESSORY_NOT_FOUND =
                "The requested accessory is not found in the device.";
        public static final String ACCESSORY_REGISTRATION_FAILURE =
                "Failed to register the accessory";
        public static final String ACCESSORY_GET_INFO_FAILURE =
                "Failed to get accessory info.";
        public static final String ACCESSORY_RESEND_FAILURE =
                "Failed to resend an owned accessory event.";
        public static final String ACCESSORY_RESERVE_FAILURE =
                "Failed to reserve the accessory.";
        public static final String ACCESSORY_OPEN_FAILURE =
                "Failed to open the HID accessory.";
        public static final String ACCESSORY_CLOSE_FAILURE =
                "Failed to close the HID accessory.";
        public static final String ACCESSORY_START_READING_FAILURE =
                "Failed to start reading HID accessory.";
        public static final String ACCESSORY_STOP_READING_FAILURE =
                "Failed to stop reading HID accessory.";
        public static final String ACCESSORY_NOT_OPENED =
                "The HID accessory is not opened. Please open the accessory before performing this operation.";
        public static final String ACCESSORY_GET_HID_INFO_FAILURE =
                "Failed to get open HID Info.";
        public static final String ACCESSORY_READ_REPORT_FAILURE =
                "Failed to read report from the HID accessory.";
        public static final String ACCESSORY_WRITE_REPORT_FAILURE =
                "Failed to write report to the HID accessory.";

        // =========================== Device Service Errors ===========================
        public static final String DEVICE_SERVICE_GET_ATTRIBUTE_FAILURE =
                "Failed to retrieve the requested device attribute value.";
    }

    public static class InvalidParam {
        // =========================== Accessory Service Invalid Param ===========================
        public static final String INVALID_ACCESSORY_INFO =
                "The requested accessory info is empty or invalid.";
        public static final String INVALID_ACCESSORY_REPORT_TYPE =
                "The requested accessory report type is empty or invalid.";
        public static final String INVALID_ACCESSORY_REPORT =
                "The requested accessory report is empty or invalid.";
        public static final String ACCESSORY_NOT_REGISTERED =
                "The requested accessory is not found in this app's registered accessory record.";

        // =========================== Device Service Invalid Param ===========================
        public static final String INVALID_METHOD =
                "The requested method is not supported.";
        public static final String EMPTY_ATTRIBUTE_KEY =
                "The requested attribute Key is empty.";
    }

    public static class Unauthorized {
        public static final String NO_ACTIVE_UI_SESSION =
                "The operation cannot be performed as the app currently doesn't have an active UI session.";
    }

    public static class Connection {
        public static final String DEVICE_NOT_CONNECTED =
                "The device is not connected.";
    }

    public static class NotSupported {
        public static final String API_TYPE_NOT_SUPPORTED =
                "The specified API type is not supported.";
        public static final String CLIENT_VERSION_NOT_SUPPORTED =
                "The Workpath SDK version used by the application does not support the method or service.";
        public static final String DEVICE_SERVICE_NOT_SUPPORTED =
                "The Device Service is not supported.";
    }
}
