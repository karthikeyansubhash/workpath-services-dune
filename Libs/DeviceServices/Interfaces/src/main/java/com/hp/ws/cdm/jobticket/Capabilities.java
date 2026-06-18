
package com.hp.ws.cdm.jobticket;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Capabilities {

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("print")
    @Expose
    private PrintCapabilities print;
    @SerializedName("email")
    @Expose
    private EmailCapabilities email;
    @SerializedName("folder")
    @Expose
    private FolderCapabilities folder;
    @SerializedName("pipelineOptions")
    @Expose
    private PipelineOptionsCapabilities pipelineOptions;
    @SerializedName("scan")
    @Expose
    private ScanCapabilities scan;
    @SerializedName("usbSrc")
    @Expose
    private UsbSrcCapabilities usbSrc;

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

    public PrintCapabilities getPrint() {
        return print;
    }

    public void setPrint(PrintCapabilities print) {
        this.print = print;
    }

    public EmailCapabilities getEmail() {
        return email;
    }

    public void setEmail(EmailCapabilities email) {
        this.email = email;
    }

    public FolderCapabilities getFolder() {
        return folder;
    }

    public void setFolder(FolderCapabilities folder) {
        this.folder = folder;
    }

    public PipelineOptionsCapabilities getPipelineOptions() {
        return pipelineOptions;
    }

    public void setPipelineOptions(PipelineOptionsCapabilities pipelineOptions) {
        this.pipelineOptions = pipelineOptions;
    }

    public ScanCapabilities getScan() {
        return scan;
    }

    public void setScan(ScanCapabilities scan) {
        this.scan = scan;
    }

    public UsbSrcCapabilities getUsbSrc() {
        return usbSrc;
    }

    public void setUsbSrc(UsbSrcCapabilities usbSrc) {
        this.usbSrc = usbSrc;
    }

}
