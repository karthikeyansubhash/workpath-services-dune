
package com.hp.ws.cdm.network;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.hp.ws.cdm.Link;

public class PrintServices {

    @SerializedName("airPrint")
    @Expose
    private AirPrint airPrint;
    /**
     * FTP print configuration
     * 
     */
    @SerializedName("ftpPrint")
    @Expose
    private FtpPrint ftpPrint;
    @SerializedName("ipp")
    @Expose
    private Ipp ipp;
    @SerializedName("links")
    @Expose
    private List<Link> links = new ArrayList<Link>();
    @SerializedName("lpdPrint")
    @Expose
    private LpdPrint lpdPrint;
    @SerializedName("port9100")
    @Expose
    private Port9100 port9100;
    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * 
     */
    @SerializedName("version")
    @Expose
    private String version;
    @SerializedName("wsPrint")
    @Expose
    private WsPrint wsPrint;

    public AirPrint getAirPrint() {
        return airPrint;
    }

    public void setAirPrint(AirPrint airPrint) {
        this.airPrint = airPrint;
    }

    /**
     * FTP print configuration
     * 
     */
    public FtpPrint getFtpPrint() {
        return ftpPrint;
    }

    /**
     * FTP print configuration
     * 
     */
    public void setFtpPrint(FtpPrint ftpPrint) {
        this.ftpPrint = ftpPrint;
    }

    public Ipp getIpp() {
        return ipp;
    }

    public void setIpp(Ipp ipp) {
        this.ipp = ipp;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public LpdPrint getLpdPrint() {
        return lpdPrint;
    }

    public void setLpdPrint(LpdPrint lpdPrint) {
        this.lpdPrint = lpdPrint;
    }

    public Port9100 getPort9100() {
        return port9100;
    }

    public void setPort9100(Port9100 port9100) {
        this.port9100 = port9100;
    }

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

    public WsPrint getWsPrint() {
        return wsPrint;
    }

    public void setWsPrint(WsPrint wsPrint) {
        this.wsPrint = wsPrint;
    }

}
