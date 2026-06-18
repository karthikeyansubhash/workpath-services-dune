
package com.hp.ws.cdm.jobticket;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Src {

    @SerializedName("scan")
    @Expose
    private Scan scan;
    @SerializedName("usb")
    @Expose
    private Usb usb;
    @SerializedName("network")
    @Expose
    private Network network;
    @SerializedName("fax")
    @Expose
    private Fax fax;
    @SerializedName("folderSrc")
    @Expose
    private FolderSrc folderSrc;
    @SerializedName("usbSrc")
    @Expose
    private UsbSrc usbSrc;

    public Scan getScan() {
        return scan;
    }

    public void setScan(Scan scan) {
        this.scan = scan;
    }

    public Usb getUsb() {
        return usb;
    }

    public void setUsb(Usb usb) {
        this.usb = usb;
    }

    public Network getNetwork() {
        return network;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public Fax getFax() {
        return fax;
    }

    public void setFax(Fax fax) {
        this.fax = fax;
    }

    public FolderSrc getFolderSrc() {
        return folderSrc;
    }

    public void setFolderSrc(FolderSrc folderSrc) {
        this.folderSrc = folderSrc;
    }

    public UsbSrc getUsbSrc() {
        return usbSrc;
    }

    public void setUsbSrc(UsbSrc usbSrc) {
        this.usbSrc = usbSrc;
    }

}
