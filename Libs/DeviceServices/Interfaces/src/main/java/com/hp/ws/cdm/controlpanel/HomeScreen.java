
package com.hp.ws.cdm.controlpanel;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HomeScreen {

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
     * Array of widgets
     * 
     */
    @SerializedName("widgets")
    @Expose
    private List<Widget> widgets = new ArrayList<Widget>();
    /**
     * array of launchers
     * 
     */
    @SerializedName("launchers")
    @Expose
    private List<Launcher> launchers = new ArrayList<Launcher>();

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
     * Array of widgets
     * 
     */
    public List<Widget> getWidgets() {
        return widgets;
    }

    /**
     * Array of widgets
     * 
     */
    public void setWidgets(List<Widget> widgets) {
        this.widgets = widgets;
    }

    /**
     * array of launchers
     * 
     */
    public List<Launcher> getLaunchers() {
        return launchers;
    }

    /**
     * array of launchers
     * 
     */
    public void setLaunchers(List<Launcher> launchers) {
        this.launchers = launchers;
    }

}
