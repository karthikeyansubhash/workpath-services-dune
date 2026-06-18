package com.hp.jetadvantage.link.services.accesslet.adapter;

public enum ContextKey {
    AUTH_AGENT_ID("AUTH_AGENT_ID"),
    AUTH_TYPE("AUTH_TYPE"),
    DISPLAY_NAME("DISPLAY_NAME"),
    EMAIL_ADDRESS("EMAIL_ADDRESS"),
    FQ_USER_NAME("FQ_USER_NAME"),
    USER_DOMAIN("USER_DOMAIN"),
    USER_NAME("USER_NAME");

    private final String value;

    ContextKey(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static ContextKey fromValue(String value) {
        for (ContextKey key : values()) {
            if (key.value.equals(value)) {
                return key;
            }
        }
        return null;
    }
}
