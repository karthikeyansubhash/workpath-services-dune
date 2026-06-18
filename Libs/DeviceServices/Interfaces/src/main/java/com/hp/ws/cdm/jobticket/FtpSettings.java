
package com.hp.ws.cdm.jobticket;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FtpSettings {

    @SerializedName("server")
    @Expose
    private String server;
    @SerializedName("port")
    @Expose
    private Integer port;
    @SerializedName("transferMode")
    @Expose
    private TransferMode transferMode;

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public TransferMode getTransferMode() {
        return transferMode;
    }

    public void setTransferMode(TransferMode transferMode) {
        this.transferMode = transferMode;
    }

    public enum TransferMode {

        @SerializedName("passive")
        PASSIVE("passive"),
        @SerializedName("active")
        ACTIVE("active");
        private final String value;
        private final static Map<String, TransferMode> CONSTANTS = new HashMap<String, TransferMode>();

        static {
            for (TransferMode c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        TransferMode(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static TransferMode fromValue(String value) {
            TransferMode constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
