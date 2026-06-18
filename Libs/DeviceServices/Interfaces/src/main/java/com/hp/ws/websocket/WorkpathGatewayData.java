package com.hp.ws.websocket;

import com.google.gson.annotations.SerializedName;

public class WorkpathGatewayData {

    @SerializedName("applicationId")
    private String applicationId;

    @SerializedName("gatewayType")
    private GatewayType gatewayType;

    @SerializedName("solutionId")
    private String solutionId;

    @SerializedName("uiContextToken")
    private String uiContextToken;

    @SerializedName("workpathActionType")
    private ActionType workpathActionType;

    public String getApplicationId() {
        return applicationId;
    }

    public GatewayType getGatewayType() {
        return gatewayType;
    }

    public String getUiContextToken() {
        return uiContextToken;
    }

    public String getSolutionId() {
        return solutionId;
    }

    public ActionType getWorkpathActionType() {
        return workpathActionType;
    }

    public enum ActionType {
        @SerializedName("wgdatShowDisplay")
        SHOW_DISPLAY("wgdatShowDisplay"),

        @SerializedName("wgdatCloseDisplay")
        CLOSE_DISPLAY("wgdatCloseDisplay"),

        /////////////////////// For backward compatibility with old Dune firmware : START - remove this later
        @SerializedName("showDisplay")
        LEGACY_SHOW_DISPLAY("showDisplay"),

        @SerializedName("closeDisplay")
        LEGACY_CLOSE_DISPLAY("closeDisplay");
        /////////////////////// For backward compatibility with old Dune firmware : END

        private final String actionType;

        ActionType(String actionType) {
            this.actionType = actionType;
        }

        @Override
        public String toString() {
            return actionType;
        }
    }

    public enum GatewayType {
        @SerializedName("wgdgtApplication")
        APPLICATION("wgdgtApplication"),

        @SerializedName("wgdgtService")
        SERVICE("wgdgtService"),

        @SerializedName("wgdgtStatusCenter")
        STATUS_CENTER("wgdgtStatusCenter"),

        @SerializedName("wgdgtModal")
        MODAL("wgdgtModal"),

        /////////////////////// For backward compatibility with old Dune firmware : START - remove this later
        @SerializedName("application")
        LEGACY_APPLICATION("application"),

        @SerializedName("service")
        LEGACY_SERVICE("service"),

        @SerializedName("statusCenter")
        LEGACY_STATUS_CENTER("statusCenter"),

        @SerializedName("modal")
        LEGACY_MODAL("modal");
        /////////////////////// For backward compatibility with old Dune firmware : END

        private final String gatewayType;

        GatewayType(String gatewayType) {
            this.gatewayType = gatewayType;
        }

        @Override
        public String toString() {
            return gatewayType;
        }
    }
}
