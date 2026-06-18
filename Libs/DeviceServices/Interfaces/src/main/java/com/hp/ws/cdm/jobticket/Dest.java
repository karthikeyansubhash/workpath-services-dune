
package com.hp.ws.cdm.jobticket;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Dest {

    @SerializedName("print")
    @Expose
    private Print print;
    @SerializedName("usb")
    @Expose
    private Usb usb;
    @SerializedName("fax")
    @Expose
    private Fax fax;
    @SerializedName("folder")
    @Expose
    private Folder folder;
    @SerializedName("email")
    @Expose
    private Email email;
    @SerializedName("sharePoint")
    @Expose
    private SharePoint sharePoint;
    @SerializedName("internalStorage")
    @Expose
    private InternalStorage internalStorage;
    @SerializedName("archive")
    @Expose
    private Archive archive;

    public Print getPrint() {
        return print;
    }

    public void setPrint(Print print) {
        this.print = print;
    }

    public Usb getUsb() {
        return usb;
    }

    public void setUsb(Usb usb) {
        this.usb = usb;
    }

    public Fax getFax() {
        return fax;
    }

    public void setFax(Fax fax) {
        this.fax = fax;
    }

    public Folder getFolder() {
        return folder;
    }

    public void setFolder(Folder folder) {
        this.folder = folder;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public SharePoint getSharePoint() {
        return sharePoint;
    }

    public void setSharePoint(SharePoint sharePoint) {
        this.sharePoint = sharePoint;
    }

    public InternalStorage getInternalStorage() {
        return internalStorage;
    }

    public void setInternalStorage(InternalStorage internalStorage) {
        this.internalStorage = internalStorage;
    }

    public Archive getArchive() {
        return archive;
    }

    public void setArchive(Archive archive) {
        this.archive = archive;
    }

}
