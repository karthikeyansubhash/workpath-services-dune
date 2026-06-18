package com.hp.ws.websocket;

import com.google.gson.annotations.SerializedName;

public class SystemManagementMessage {

    @SerializedName("systemManagement")
    private SystemManagementDetails systemManagement;

    public SystemManagementDetails getSystemManagement() {
        return systemManagement;
    }

    public void setSystemManagement(SystemManagementDetails systemManagement) {
        this.systemManagement = systemManagement;
    }

    public boolean hasAuthnSessionChange() {
        if (getSystemManagement() != null && getSystemManagement().getDetails() != null) {
            SystemManagementDetails.DetailsData details = getSystemManagement().getDetails();
            return details.getAuthnSessionChange() != null && details.getAuthnSessionChange().getEvent() != null;
        }
        return false;
    }

    public boolean hasSystemStateChange() {
        if (getSystemManagement() != null && getSystemManagement().getDetails() != null) {
            SystemManagementDetails.DetailsData details = getSystemManagement().getDetails();
            return details.getSystemStateChange() != null && details.getSystemStateChange().getEvent() != null;
        }
        return false;
    }

    public enum PowerLevel {
        @SerializedName("plUserActivity")
        PL_USER_ACTIVITY("plUserActivity"),

        @SerializedName("plSystemActivity")
        PL_SYSTEM_ACTIVITY("plSystemActivity"),

        @SerializedName("plSleep")
        PL_SLEEP("plSleep"),

        @SerializedName("plDeepSleep")
        PL_DEEP_SLEEP("plDeepSleep");

        private final String powerLevel;

        PowerLevel(String powerLevel) {
            this.powerLevel = powerLevel;
        }

        @Override
        public String toString() {
            return powerLevel;
        }
    }

    public enum AuthnSessionEvent {
        @SerializedName("asFrontPanelLogin")
        AS_FRONT_PANEL_LOGIN("asFrontPanelLogin"),

        @SerializedName("asFrontPanelLogout")
        AS_FRONT_PANEL_LOGOUT("asFrontPanelLogout");

        private final String authnSession;

        AuthnSessionEvent(String authnSession) {
            this.authnSession = authnSession;
        }

        @Override
        public String toString() {
            return authnSession;
        }
    }

    public enum SystemState {
        @SerializedName("sceUsbStorageEvent")
        SCE_USB_STORAGE_EVENT("sceUsbStorageEvent");

        private final String systemState;

        SystemState(String systemState) {
            this.systemState = systemState;
        }

        @Override
        public String toString() {
            return systemState;
        }
    }

    public static class SystemManagementDetails {
        @SerializedName("details")
        private DetailsData details;

        @SerializedName("traceId")
        private Integer traceId;

        public DetailsData getDetails() {
            return details;
        }

        public void setDetails(DetailsData details) {
            this.details = details;
        }

        public Integer getTraceId() {
            return traceId;
        }

        public void setTraceId(Integer traceId) {
            this.traceId = traceId;
        }

        public static class DetailsData {
            @SerializedName("authnSessionChange")
            private AuthnSessionChange authnSessionChange;

            @SerializedName("powerLevelChange")
            private PowerLevelChange powerLevelChange;

            @SerializedName("systemStateChange")
            private SystemStateChange systemStateChange;

            public AuthnSessionChange getAuthnSessionChange() {
                return authnSessionChange;
            }

            public void setAuthnSessionChange(AuthnSessionChange authnSessionChange) {
                this.authnSessionChange = authnSessionChange;
            }

            public PowerLevelChange getPowerLevelChange() {
                return powerLevelChange;
            }

            public void setPowerLevelChange(PowerLevelChange powerLevelChange) {
                this.powerLevelChange = powerLevelChange;
            }

            public SystemStateChange getSystemStateChange() {
                return systemStateChange;
            }

            public void setSystemStateChange(SystemStateChange systemStateChange) {
                this.systemStateChange = systemStateChange;
            }

            public static class AuthnSessionChange {
                @SerializedName("event")
                private AuthnSessionEvent event;

                public AuthnSessionEvent getEvent() {
                    return event;
                }

                public void setEvent(AuthnSessionEvent event) {
                    this.event = event;
                }
            }

            public static class PowerLevelChange {
                @SerializedName("from")
                private PowerLevel from;

                @SerializedName("to")
                private PowerLevel to;

                public PowerLevel getFrom() {
                    return from;
                }

                public void setFrom(PowerLevel from) {
                    this.from = from;
                }

                public PowerLevel getTo() {
                    return to;
                }

                public void setTo(PowerLevel to) {
                    this.to = to;
                }
            }

            public static class SystemStateChange {
                @SerializedName("event")
                private SystemState event;

                public SystemState getEvent() {
                    return event;
                }

                public void setEvent(SystemState event) {
                    this.event = event;
                }
            }
        }
    }
}
