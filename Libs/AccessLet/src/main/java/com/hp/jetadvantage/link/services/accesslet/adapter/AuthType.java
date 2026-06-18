package com.hp.jetadvantage.link.services.accesslet.adapter;

public enum AuthType {
    DEVICE_ADMIN("AUTH_TYPE_DEVICEADMIN"),
    GUEST("AUTH_TYPE_GUEST"),
    DEVICE_USER("AUTH_TYPE_DEVICEUSER"),
    DEVICE_PIN("AUTH_TYPE_DEVICEPIN"),
    SMART_CARD("AUTH_TYPE_SMARTCARD"),
    DEVICE_SERVICE("AUTH_TYPE_DEVICESERVICE"),
    UNKNOWN("UNKNOWN");

    private final String value;

    AuthType(String value) {
        this.value = value;
    }

    public static AuthType fromValue(String authType) {
        if (authType == null || authType.isEmpty()) {
            return UNKNOWN;
        }

        for (AuthType type : values()) {
            if (authType.contains(type.value)) {
                return type;
            }
        }
        return UNKNOWN;
    }
}
