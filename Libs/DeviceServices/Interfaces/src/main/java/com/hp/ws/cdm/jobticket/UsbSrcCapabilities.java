
package com.hp.ws.cdm.jobticket;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UsbSrcCapabilities {

    /**
     * Supported file format for photo print
     * 
     */
    @SerializedName("photoFileFormatSupported")
    @Expose
    private List<Usb.FileFormat> photoFileFormatSupported = new ArrayList<Usb.FileFormat>();

    /**
     * Supported file format for photo print
     * 
     */
    public List<Usb.FileFormat> getPhotoFileFormatSupported() {
        return photoFileFormatSupported;
    }

    /**
     * Supported file format for photo print
     * 
     */
    public void setPhotoFileFormatSupported(List<Usb.FileFormat> photoFileFormatSupported) {
        this.photoFileFormatSupported = photoFileFormatSupported;
    }

}
