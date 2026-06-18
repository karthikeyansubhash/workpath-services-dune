package com.hp.jetadvantage.link.device.services.standard.services.appchannel;

public enum AppChannelHttpStatus {
    // --- 1xx Informational ---
    CONTINUE(100, "Continue"),
    SWITCHING_PROTOCOLS(101, "Switching Protocols"),

    // --- 2xx Success ---
    OK(200, "OK"),
    CREATED(201, "Created"),
    ACCEPTED(202, "Accepted"),
    NO_CONTENT(204, "No Content"),

    // --- 3xx Redirection ---
    MOVED_PERMANENTLY(301, "Moved Permanently"),
    FOUND(302, "Found"),
    NOT_MODIFIED(304, "Not Modified"),

    // --- 4xx Client Error ---
    BAD_REQUEST(400, "Bad Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    CONFLICT(409, "Conflict"),
    UNPROCESSABLE_ENTITY(422, "Unprocessable Entity"),
    // --- 5xx Server Error ---
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    NOT_IMPLEMENTED(501, "Not Implemented"),
    BAD_GATEWAY(502, "Bad Gateway"),
    SERVICE_UNAVAILABLE(503, "Service Unavailable");

    private final int code;
    private final String reason;

    AppChannelHttpStatus(int code, String reason) {
        this.code = code;
        this.reason = reason;
    }

    public static AppChannelHttpStatus getByCode(int code) {
        for (AppChannelHttpStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        return null; // or throw exception if preferred
    }

    public int code() {
        return code;
    }

    public String reason() {
        return reason;
    }

    @Override
    public String toString() {
        return code + " " + reason;
    }
}
