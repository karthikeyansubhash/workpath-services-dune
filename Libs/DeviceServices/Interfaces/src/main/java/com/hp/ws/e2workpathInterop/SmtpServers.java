
package com.hp.ws.e2workpathInterop;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

public class SmtpServers {

    @SerializedName("version")
    @Expose
    private String version;

    /**
     * SMTP servers configuration wrapper
     */
    @SerializedName("smtpServers")
    @Expose
    private SmtpServersData smtpServers;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Get the list of SMTP servers
     * For backward compatibility
     */
    public List<SmtpServer> getServers() {
        return smtpServers != null ? smtpServers.getServers() : new ArrayList<>();
    }

    /**
     * Inner class to match JSON structure: { "smtpServers": { "servers": [...] } }
     */
    public static class SmtpServersData {
        /**
         * Array of smtp server settings
         */
        @SerializedName("servers")
        @Expose
        private List<SmtpServer> servers = new ArrayList<>();

        public List<SmtpServer> getServers() {
            return servers;
        }

        public void setServers(List<SmtpServer> servers) {
            this.servers = servers;
        }
    }
    
}
