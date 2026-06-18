/**
 * (C) Copyright 2026 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.services.scanlet.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.hp.jetadvantage.link.common.utils.JsonParser;

import org.junit.Test;

public class MetadataModelUnitTest {

    @Test
    public void GivenMetadataJson_WhenParsed_ThenMetadataObjectShouldBeCreatedCorrectly() {
        String json = "{\n" +
                "  \"metadataFile\": {\n" +
                "    \"jobId\": \"job123\",\n" +
                "    \"jobName\": \"My Scan Job\",\n" +
                "    \"userName\": \"user1\",\n" +
                "    \"scanSize\": \"A4\",\n" +
                "    \"scanSource\": \"BOOK\",\n" +
                "    \"scanCount\": \"10\",\n" +
                "    \"attachmentCount\": \"1\",\n" +
                "    \"extension\": \"pdf\"\n" +
                "  }\n" +
                "}";

        Metadata metadata = JsonParser.getInstance().fromJson(json, Metadata.class);

        assertNotNull("Metadata object should not be null", metadata);
        MetadataFile metadataFile = metadata.getMetadataFile();
        assertNotNull("MetadataFile object should not be null", metadataFile);

        assertEquals("job123", metadataFile.getJobId());
        assertEquals("My Scan Job", metadataFile.getJobName());
        assertEquals("user1", metadataFile.getUsername());
        assertEquals("A4", metadataFile.getScanSize());
        assertEquals("BOOK", metadataFile.getScanSource());
        assertEquals("10", metadataFile.getScanCount());
        assertEquals("1", metadataFile.getAttachmentCount());
        assertEquals("pdf", metadataFile.getExtension());
    }

    @Test
    public void GivenMetadataJsonWithMissingFields_WhenParsed_ThenObjectShouldHaveNullFields() {
        String json = "{\n" +
                "  \"metadataFile\": {\n" +
                "    \"jobId\": \"job123\"\n" +
                "  }\n" +
                "}";

        Metadata metadata = JsonParser.getInstance().fromJson(json, Metadata.class);

        assertNotNull(metadata);
        MetadataFile metadataFile = metadata.getMetadataFile();
        assertNotNull(metadataFile);

        assertEquals("job123", metadataFile.getJobId());
        assertNull(metadataFile.getJobName());
        assertNull(metadataFile.getUsername());
        assertNull(metadataFile.getScanSize());
        assertNull(metadataFile.getScanSource());
    }

    @Test
    public void GivenPartialMetadataJson_WhenParsed_ThenParsedFieldsShouldBeCorrect() {
        // Test with only new fields being present
         String json = "{\n" +
                "  \"metadataFile\": {\n" +
                "    \"scanSize\": \"A3\",\n" +
                "    \"scanSource\": \"FLIP\"\n" +
                "  }\n" +
                "}";

        Metadata metadata = JsonParser.getInstance().fromJson(json, Metadata.class);
        MetadataFile metadataFile = metadata.getMetadataFile();

        assertEquals("A3", metadataFile.getScanSize());
        assertEquals("FLIP", metadataFile.getScanSource());
        assertNull(metadataFile.getJobId());
    }
}
