// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.copy;

import android.text.TextUtils;

import com.hp.oxpdlib.OXPdDevice;
import com.hp.oxpdlib.media.MediaSizeId;
import com.hp.oxpdlib.options.ColorMode;
import com.hp.oxpdlib.options.DuplexFormat;
import com.hp.oxpdlib.options.PlexMode;
import com.hp.sdd.jabberwocky.chat.HttpRequestResponseContainer;
import com.hp.sdd.jabberwocky.xml.RestXMLTagHandler;
import com.hp.sdd.jabberwocky.xml.RestXMLTagStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StoredCopyJob{
    public String jobId;
    public String storeJobFolderName;
    public String storeJobName;
    public String storeJobUserName;
    public PasswordType storeJobPasswordType;
    public String timestamp;
    public int copies;
    public ColorMode colorMode;
    public MediaSizeId originalMediaSize;
    public PlexMode outputSides;
    public int totalPages;

    /**
     * Constructor used by the library to construct StoredCopyJob objects.
     *
     * @param tagHandler XML handler to extract data from
     */
    @SuppressWarnings("unchecked")
    private StoredCopyJob(RestXMLTagHandler tagHandler) {
        jobId = (String) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__RELEASE_COPY_TICKET_JOB_ID);
        storeJobFolderName = (String) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__STORE_JOB_FOLDER_NAME);
        storeJobName = (String) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__STORE_JOB_NAME);
        storeJobUserName = (String) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__STORE_JOB_USER_NAME);
        storeJobPasswordType = (PasswordType) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__STORE_JOB_PASSWORD_TYPE);
        timestamp = (String) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__STORE_JOB_TIMESTAMP);
        copies = (int) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__COPIES);
        colorMode = (ColorMode) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__COLOR_MODE);
        originalMediaSize = (MediaSizeId) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__ORIGINAL_MEDIA_SIZE);
        outputSides = (PlexMode) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__OUTPUT_SIDES);
        totalPages = (int) tagHandler.getTagData(OXPdCopy.Constants.XML_TAG__COPY__TOTAL_PAGES);
    }

    /**
     * Setup the XML handler required to capture the elements of a StoredCopyJob object
     * @param tagHandler
     *              XML tag handler
     */
    @SuppressWarnings("unchecked")
    static void setupXMLTagHandler ( final RestXMLTagHandler tagHandler){
        RestXMLTagHandler.XMLEndTagHandler storedCopyJobEnd = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                StoredCopyJob storedCopyJob = new StoredCopyJob(tagHandler);

                List<StoredCopyJob> list = (List<StoredCopyJob>) handler.getTagData(
                        OXPdCopy.Constants.XML_TAG__COPY__ENUMERATE_STORED_COPY_JOB_RESULT);
                list.add(storedCopyJob);
            }
        };

        RestXMLTagHandler.XMLEndTagHandler objectCollector = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri, String localName, String data) {
                if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__STORE_JOB_PASSWORD_TYPE, localName)) {
                    handler.setTagData(localName, PasswordType.fromAttributeValue(data));
                } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__COLOR_MODE, localName)) {
                    handler.setTagData(localName, ColorMode.fromAttributeValue(data));
                } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__ORIGINAL_MEDIA_SIZE, localName)) {
                    handler.setTagData(localName, MediaSizeId.fromAttributeValue(data));
                } else if (TextUtils.equals(OXPdCopy.Constants.XML_TAG__COPY__OUTPUT_SIDES, localName)) {
                    handler.setTagData(localName, PlexMode.fromAttributeValue(data));
                }
            }
        };

        RestXMLTagHandler.XMLEndTagHandler integerCreator = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri,
                                String localName, String data) {
                try {
                    handler.setTagData(localName, Integer.valueOf(data));
                } catch (NumberFormatException ignored) {
                }
            }
        };

        RestXMLTagHandler.XMLEndTagHandler stringCollector = new RestXMLTagHandler.XMLEndTagHandler() {
            @Override
            public void process(RestXMLTagHandler handler, RestXMLTagStack xmlTagStack, String uri,
                                String localName, String data) {
                handler.setTagData(localName, data);
            }
        };

        tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__STORED_COPY_JOB, null, storedCopyJobEnd);

        tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__RELEASE_COPY_TICKET_JOB_ID, null, stringCollector);
        tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__STORE_JOB_FOLDER_NAME, null, stringCollector);
        tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__STORE_JOB_NAME, null, stringCollector);
        tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__STORE_JOB_USER_NAME, null, stringCollector);
        tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__STORE_JOB_PASSWORD_TYPE, null, objectCollector);
        tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__STORE_JOB_TIMESTAMP, null, stringCollector);
        tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__COPIES, null, integerCreator);
        tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__COLOR_MODE, null, objectCollector);
        tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__ORIGINAL_MEDIA_SIZE, null, objectCollector);
        tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__OUTPUT_SIDES, null, objectCollector);
        tagHandler.setXMLHandler(OXPdCopy.Constants.XML_TAG__COPY__TOTAL_PAGES, null, integerCreator);
    }

    public static List<StoredCopyJob> parseRequestResult(OXPdDevice mDevice, HttpRequestResponseContainer requestResponse, RestXMLTagHandler tagHandler) throws Error {
        setupXMLTagHandler(tagHandler);

        List<StoredCopyJob> storedCopyJobList = new ArrayList<StoredCopyJob>();
        tagHandler.setTagData(OXPdCopy.Constants.XML_TAG__COPY__ENUMERATE_STORED_COPY_JOB_RESULT, storedCopyJobList);
        mDevice.parseXMLResponse(requestResponse, tagHandler, OXPdDevice.HTTP_REQUEST_DEBUG_OPTION__USE_GLOBAL);
        OXPdCopy.faultExceptionCheck(tagHandler);
        return Collections.unmodifiableList(storedCopyJobList);
    }

    /**
     * String representation of StoredCopyJobData
     *
     */
    @Override
    public String toString() {
        return new StringBuilder().append("[")
                .append("jobId=")
                .append(((jobId == null)?"null":jobId))
                .append(", ")
                .append("storeJobFolderName=")
                .append(((storeJobFolderName == null)?"null":storeJobFolderName))
                .append(", ")
                .append("storeJobName=")
                .append(((storeJobName == null)?"null":storeJobName))
                .append(", ")
                .append("storeJobUserName=")
                .append(((storeJobUserName == null)?"null":storeJobUserName))
                .append(", ")
                .append("storeJobPasswordType=")
                .append(((storeJobPasswordType== null)?"null":storeJobPasswordType.toString()))
                .append(", ")
                .append("timestamp=")
                .append(((timestamp == null)?"null":timestamp.toString()))
                .append(", ")
                .append("copies=")
                .append(copies)
                .append(", ")
                .append("colorMode=")
                .append(((colorMode == null)?"null":colorMode.toString()))
                .append(", ")
                .append("originalMediaSize=")
                .append(((originalMediaSize == null)?"null":originalMediaSize.toString()))
                .append(", ")
                .append("outputSides=")
                .append(((outputSides == null)?"null":outputSides.toString()))
                .append(", ")
                .append("totalPages=")
                .append(totalPages)
                .append("]").toString();
    }
}
