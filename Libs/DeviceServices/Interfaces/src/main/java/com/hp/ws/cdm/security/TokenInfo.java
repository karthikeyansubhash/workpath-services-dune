
package com.hp.ws.cdm.security;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TokenInfo {

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * (Required)
     * 
     */
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("userInfo")
    @Expose
    private UserInfo userInfo;
    @SerializedName("permissionsGranted")
    @Expose
    private List<PermissionGranted> permissionsGranted = new ArrayList<PermissionGranted>();

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * (Required)
     * 
     */
    public String getVersion() {
        return version;
    }

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * (Required)
     * 
     */
    public void setVersion(String version) {
        this.version = version;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public List<PermissionGranted> getPermissionsGranted() {
        return permissionsGranted;
    }

    public void setPermissionsGranted(List<PermissionGranted> permissionsGranted) {
        this.permissionsGranted = permissionsGranted;
    }

}
