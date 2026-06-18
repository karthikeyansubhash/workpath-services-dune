
package com.hp.ws.cdm.ioconfig;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class IoStatistics {

    @SerializedName("receivedBytes")
    @Expose
    private Integer receivedBytes;
    @SerializedName("receivedMulticastPackets")
    @Expose
    private Integer receivedMulticastPackets;
    @SerializedName("receivedPackets")
    @Expose
    private Integer receivedPackets;
    @SerializedName("receivedUnicastPackets")
    @Expose
    private Integer receivedUnicastPackets;
    @SerializedName("receiverDropped")
    @Expose
    private Integer receiverDropped;
    @SerializedName("receiverErrors")
    @Expose
    private Integer receiverErrors;
    @SerializedName("receiverFrameErrors")
    @Expose
    private Integer receiverFrameErrors;
    @SerializedName("transmittedBytes")
    @Expose
    private Integer transmittedBytes;
    @SerializedName("transmittedPackets")
    @Expose
    private Integer transmittedPackets;
    @SerializedName("transmitterCollisions")
    @Expose
    private Integer transmitterCollisions;
    @SerializedName("transmitterDropped")
    @Expose
    private Integer transmitterDropped;
    @SerializedName("transmitterErrors")
    @Expose
    private Integer transmitterErrors;
    @SerializedName("transmitterLateCollisions")
    @Expose
    private Integer transmitterLateCollisions;

    public Integer getReceivedBytes() {
        return receivedBytes;
    }

    public void setReceivedBytes(Integer receivedBytes) {
        this.receivedBytes = receivedBytes;
    }

    public Integer getReceivedMulticastPackets() {
        return receivedMulticastPackets;
    }

    public void setReceivedMulticastPackets(Integer receivedMulticastPackets) {
        this.receivedMulticastPackets = receivedMulticastPackets;
    }

    public Integer getReceivedPackets() {
        return receivedPackets;
    }

    public void setReceivedPackets(Integer receivedPackets) {
        this.receivedPackets = receivedPackets;
    }

    public Integer getReceivedUnicastPackets() {
        return receivedUnicastPackets;
    }

    public void setReceivedUnicastPackets(Integer receivedUnicastPackets) {
        this.receivedUnicastPackets = receivedUnicastPackets;
    }

    public Integer getReceiverDropped() {
        return receiverDropped;
    }

    public void setReceiverDropped(Integer receiverDropped) {
        this.receiverDropped = receiverDropped;
    }

    public Integer getReceiverErrors() {
        return receiverErrors;
    }

    public void setReceiverErrors(Integer receiverErrors) {
        this.receiverErrors = receiverErrors;
    }

    public Integer getReceiverFrameErrors() {
        return receiverFrameErrors;
    }

    public void setReceiverFrameErrors(Integer receiverFrameErrors) {
        this.receiverFrameErrors = receiverFrameErrors;
    }

    public Integer getTransmittedBytes() {
        return transmittedBytes;
    }

    public void setTransmittedBytes(Integer transmittedBytes) {
        this.transmittedBytes = transmittedBytes;
    }

    public Integer getTransmittedPackets() {
        return transmittedPackets;
    }

    public void setTransmittedPackets(Integer transmittedPackets) {
        this.transmittedPackets = transmittedPackets;
    }

    public Integer getTransmitterCollisions() {
        return transmitterCollisions;
    }

    public void setTransmitterCollisions(Integer transmitterCollisions) {
        this.transmitterCollisions = transmitterCollisions;
    }

    public Integer getTransmitterDropped() {
        return transmitterDropped;
    }

    public void setTransmitterDropped(Integer transmitterDropped) {
        this.transmitterDropped = transmitterDropped;
    }

    public Integer getTransmitterErrors() {
        return transmitterErrors;
    }

    public void setTransmitterErrors(Integer transmitterErrors) {
        this.transmitterErrors = transmitterErrors;
    }

    public Integer getTransmitterLateCollisions() {
        return transmitterLateCollisions;
    }

    public void setTransmitterLateCollisions(Integer transmitterLateCollisions) {
        this.transmitterLateCollisions = transmitterLateCollisions;
    }

}
