package com.hp.jetadvantage.link.services.accesslet.adapter;

public enum UserProperty {
    AUTH_AGENT_NAME("AuthAgentName"),
    AUTH_AGENT_UUID("AuthAgentUuid"),
    DISPLAY_NAME("DisplayName"),
    EMAIL_ADDRESS("EmailAddress"),
    USER_NAME("UserName"),
    USER_NAME_LOWER("userName"),
    DOMAIN("domain");
    private final String value;

    UserProperty(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
