
package com.hp.ws.cdm.usbhost;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * USB port information
 * 
 */
public class UsbPort {

    /**
     * The USB port location to which the device is connected
     * 
     */
    @SerializedName("portLocation")
    @Expose
    private UsbPortLocation portLocation;

    /**
     * The USB port location to which the device is connected
     * 
     */
    public UsbPortLocation getPortLocation() {
        return portLocation;
    }

    /**
     * The USB port location to which the device is connected
     * 
     */
    public void setPortLocation(UsbPortLocation portLocation) {
        this.portLocation = portLocation;
    }


    /**
     * The USB port location to which the device is connected
     * 
     */
    public enum UsbPortLocation {

        @SerializedName("frontUsb")
        FRONT_USB("frontUsb"),
        @SerializedName("rearUsb")
        REAR_USB("rearUsb"),
        @SerializedName("hipPocket")
        HIP_POCKET("hipPocket");
        private final String value;
        private final static Map<String, UsbPortLocation> CONSTANTS = new HashMap<String, UsbPortLocation>();

        static {
            for (UsbPortLocation c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        UsbPortLocation(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static UsbPortLocation fromValue(String value) {
            UsbPortLocation constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
