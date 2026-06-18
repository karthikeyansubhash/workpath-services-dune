/**
 * (C) Copyright 2026 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.services.scanlet.model;

public class Metadata {
    private MetadataFile metadataFile;
    private String solutionUuid;

    public MetadataFile getMetadataFile() {
        return metadataFile;
    }

    public void setMetadataFile (MetadataFile metadataFile) {
        this.metadataFile = metadataFile;
    }

    public String getSolutionUuid() {
        return solutionUuid;
    }

    public void setSolutionUuid(String solutionUuid) {
        this.solutionUuid = solutionUuid;
    }
}
