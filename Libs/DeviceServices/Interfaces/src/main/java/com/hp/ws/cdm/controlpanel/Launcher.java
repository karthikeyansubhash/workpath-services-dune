
package com.hp.ws.cdm.controlpanel;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.commonglossary.Property;

public class Launcher {

    /**
     * type of launcher
     * 
     */
    @SerializedName("launcherType")
    @Expose
    private Launcher.LauncherType launcherType = Launcher.LauncherType.fromValue("application");
    @SerializedName("application")
    @Expose
    private Application application;
    @SerializedName("folder")
    @Expose
    private Folder folder;
    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    @SerializedName("visible")
    @Expose
    private Property.FeatureEnabled visible;

    /**
     * type of launcher
     * 
     */
    public Launcher.LauncherType getLauncherType() {
        return launcherType;
    }

    /**
     * type of launcher
     * 
     */
    public void setLauncherType(Launcher.LauncherType launcherType) {
        this.launcherType = launcherType;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public Folder getFolder() {
        return folder;
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public Property.FeatureEnabled getVisible() {
        return visible;
    }

    /**
     * String based boolean. Pattern to follow to enable and disable service functionalities.
     * 
     */
    public void setVisible(Property.FeatureEnabled visible) {
        this.visible = visible;
    }


    /**
     * type of launcher
     * 
     */
    public enum LauncherType {

        @SerializedName("application")
        APPLICATION("application"),
        @SerializedName("folder")
        FOLDER("folder");
        private final String value;
        private final static Map<String, Launcher.LauncherType> CONSTANTS = new HashMap<String, Launcher.LauncherType>();

        static {
            for (Launcher.LauncherType c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        LauncherType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Launcher.LauncherType fromValue(String value) {
            Launcher.LauncherType constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
