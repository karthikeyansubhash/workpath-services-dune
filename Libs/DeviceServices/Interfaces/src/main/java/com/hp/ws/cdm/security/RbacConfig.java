
package com.hp.ws.cdm.security;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RbacConfig {

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    @SerializedName("version")
    @Expose
    private String version;
    /**
     * The mappings of roles to permissions
     * 
     */
    @SerializedName("rolePermissionMappings")
    @Expose
    private List<RolePermissionMap> rolePermissionMappings = new ArrayList<RolePermissionMap>();

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    public String getVersion() {
        return version;
    }

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * The mappings of roles to permissions
     * 
     */
    public List<RolePermissionMap> getRolePermissionMappings() {
        return rolePermissionMappings;
    }

    /**
     * The mappings of roles to permissions
     * 
     */
    public void setRolePermissionMappings(List<RolePermissionMap> rolePermissionMappings) {
        this.rolePermissionMappings = rolePermissionMappings;
    }

}
