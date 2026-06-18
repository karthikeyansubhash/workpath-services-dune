// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.printlet.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.hp.jetadvantage.link.services.printlet.model.common.VersionForOXP;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Defines all of the settings to be used to start a 'print from uri' job.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class PrintTicket implements Parcelable {

    int version = 1;

    /**
     * Uri referencing a printable file.
     */
    public final String documentUri;

    /**
     * Input stream printable file.
     */
    public transient InputStream printStream;

    /**
     * User name credential.
     * (Optional) If provided, the device will use this user name for HTTP Basic Authentication
     * credentials when fetching the URI specified in this request. (HTTPS is recommended for both
     * the Print-URI operation and the specified URI to ensure the credentials are encrypted on
     * the network.)
     */
    public final String userName;
    /**
     * Password credential.
     * (Optional) Password credential. If provided, the device will use this password in addition
     * to the user name for HTTP Basic Authentication credentials when fetching the URI specified
     * in this request. (HTTPS is recommended for both the Print-URI operation and the specified
     * URI to ensure the credentials are encrypted on the network.)
     */
    public final String password;
    /**
     * HTTP cookie.
     * (Optional) HTTP Cookie. If provided, the device will use this an opaque value to populate a
     * standard HTTP Cookie header on the HTTP request to the URI. (Must be a validly formed cookie
     * in HTTP Cookie header syntax. Will be truncated to 4092 characters.)
     */
    public final String cookie;
    /**
     * The number of copies to print.
     * (Optional) The number of copies to print (default is 1 if undefined or null).
     */
    public final int copies;
    /**
     * Plex mode to use for this document.
     * (Optional) Plex mode to use for this document (default is {@link PlexMode}.Simplex if undefined or null).
     */
    public final PlexMode plexMode;
    /**
     * Color mode to use for this document (default is {@link ColorMode}.Auto if undefined or null).
     */
    public final ColorMode colorMode;
    /**
     * Scaling mode to use for this document (default is {@link ScalingMode}.Auto if undefined or null).
     */
    public final ScalingMode scalingMode;
    /**
     * Staple mode to use for this document (default is {@link StapleMode}.None if undefined or null).
     */
    public final StapleMode stapleMode;
    /**
     * Media source to use for this document (default is {@link MediaSource}.Auto if undefined or null).
     */
    public final MediaSource mediaSource;
    /**
     * Media size to use for this document (default is {@link MediaSize}.Default if undefined or null).
     */
    public final MediaSize mediaSize;
    /**
     * Media type to use for this document (default is null).
     */
    public final MediaType mediaType;
    /**
     * File type to use for this document (default is {@link FileType|.PDF if undefined or null).
     */
    public final FileType fileType;
    /**
     * Collate mode to use for this document (default is {@link CollateMode}.Default if undefined or null).
     */
    public final CollateMode collateMode;
    /**
     * JobName to use for this document (default is null).
     */
    public final String jobName;
    /**
     * Orientation to use for this document (default is {@link Orientation}.Default if undefined or null).
     */
    public final Orientation orientation;
    /**
     * Print quality to use for this document (default is {@link PrintQuality}.Default if undefined or null).
     */
    public final PrintQuality printQuality;
    /**
     * Output-bin to use for this document (default is {@link OutputBin}.Default if undefined or null).
     */
    public final OutputBin outputBin;
    /**
     * The start page of page ranges to print.
     */
    public final int startPageRanges;
    /**
     * The end page of page ranges to print.
     */
    public final int endPageRanges;
    /**
     * Finishing to use for this document (default is {@link Finishings}.None if undefined or null).
     */
    public final List<Finishings> finishingsList;
    /**
     * UI Context for priority print request
     */
    public final String uiContextID;

    /**
     * Constructor used to construct PrintUriTicket objects.
     * @param documentUri
     *             URL referencing a printable file
     * @param userName
     *             User name credential
     * @param password
     *             Password credential
     * @param cookie
     *             HTTP Cookie
     * @param copies
     *             The number of copies to print
     * @param plexMode
     *             Plex mode to use for this document
     * @param colorMode
     *             Color mode to use for this document
     * @param scalingMode
     *             Scaling mode to use for this document
     * @param stapleMode
     *             Staple mode to use for this document
     * @param mediaSource
     *             Media source to use for this document
     * @param mediaSize
     *             Media source to use for this document
     * @param fileType
     *             File type to use for this document
     * @param jobName
     *             Job name to use for this document
     * @param orientation
     *             The orientation to use for this document
     * @param printQuality
     *             The print quality to use for this document
     * @param outputBin
     *             The output-bin to use for this document
     * @param startPageRanges
     *             The start page of page ranges to print
     * @param endPageRanges
     *             The end page of page ranges to print
     * @param finishingsList
     *             Finishings to use for this document
     */
    private PrintTicket(int version, String documentUri, InputStream printStream, String userName, String password, String cookie,
                        int copies, PlexMode plexMode, ColorMode colorMode, ScalingMode scalingMode,
                        StapleMode stapleMode, MediaSource mediaSource, MediaSize mediaSize, MediaType mediaType,
                        FileType fileType, CollateMode collateMode, String jobName,
                        Orientation orientation, PrintQuality printQuality, OutputBin outputBin,
                        int startPageRanges, int endPageRanges, List<Finishings> finishingsList)
    {
        this.version = version;
        this.documentUri = documentUri;
        this.printStream = printStream;
        this.userName = userName;
        this.password = password;
        this.cookie = cookie;
        this.copies = copies;
        this.plexMode = plexMode;
        this.colorMode = colorMode;
        this.scalingMode = scalingMode;
        this.stapleMode = stapleMode;
        this.mediaSource = mediaSource;
        this.mediaSize = mediaSize;
        this.mediaType = mediaType;
        this.fileType = fileType;
        this.collateMode = collateMode;
        this.jobName = jobName;
        this.orientation = orientation;
        this.printQuality = printQuality;
        this.outputBin = outputBin;
        this.startPageRanges = startPageRanges;
        this.endPageRanges = endPageRanges;
        this.finishingsList = finishingsList;
        this.uiContextID = null;
    }

    /**
     * Constructor used to construct PrintUriTicket objects.
     * @param printTicket
     *              Print ticket to copy settings from
     * @param uiContextID
     *              The currently valid OXPd UIContextId
     */
    public PrintTicket(PrintTicket printTicket, String uiContextID) {
        this.documentUri = printTicket.documentUri;
        this.printStream = printTicket.printStream;
        this.userName = printTicket.userName;
        this.password = printTicket.password;
        this.cookie = printTicket.cookie;
        this.copies = printTicket.copies;
        this.plexMode = printTicket.plexMode;
        this.colorMode = printTicket.colorMode;
        this.scalingMode = printTicket.scalingMode;
        this.stapleMode = printTicket.stapleMode;
        this.mediaSource = printTicket.mediaSource;
        this.mediaSize = printTicket.mediaSize;
        this.mediaType = printTicket.mediaType;
        this.fileType = printTicket.fileType;
        this.collateMode = printTicket.collateMode;
        this.jobName = printTicket.jobName;
        this.orientation = printTicket.orientation;
        this.printQuality = printTicket.printQuality;
        this.outputBin = printTicket.outputBin;
        this.startPageRanges = printTicket.startPageRanges;
        this.endPageRanges = printTicket.endPageRanges;
        this.finishingsList = printTicket.finishingsList;
        this.uiContextID = uiContextID;
    }

    /**
     * Builder class for creating PrintTicket objects
     */
    public static class Builder {

        /** SDK version */
        private int version = VersionForOXP.VERSION.LEVEL;

        /** Default FileType */
        private static final FileType DEFAULT_FILE_TYPE = FileType.PDF;

        /**
         * URL referencing a printable file.
         */
        private String documentUri = null;
        /**
         * Input stream printable file.
         */
        private InputStream printStream = null;
        /**
         * User name credential.
         */
        private String userName = null;
        /**
         * Password credential.
         */
        private String password = null;
        /**
         * HTTP cookie.
         */
        private String cookie = null;
        /**
         * The number of copies to print.
         */
        private int copies = 1;
        /**
         * Plex mode to use for this document.
         */
        private PlexMode plexMode = null;
        /**
         * Color mode to use for this document.
         */
        private ColorMode colorMode = null;
        /**
         * Scaling mode to use for this document.
         */
        private ScalingMode scalingMode = null;
        /**
         * Staple mode to use for this document.
         */
        private StapleMode stapleMode = null;
        /**
         * Paper source to use for this document.
         */
        private MediaSource mediaSource = null;
        /**
         * Paper size to use for this document.
         */
        private MediaSize mediaSize = null;
        /**
         * Paper type to use for this document.
         */
        private MediaType mediaType = null;
        /**
         * File type to use for this document.
         */
        private FileType fileType = FileType.PDF;
        /**
         * Collate mode to use for this document.
         */
        private CollateMode collateMode = null;
        /**
         * JobName to use for this document.
         */
        private String jobName = null;
        /**
         * Orientation to use for this document.
         */
        private Orientation orientation = null;
        /**
         * PrintQuality to use for this document.
         */
        private PrintQuality printQuality = null;
        /**
         * OutputBin to use for this document.
         */
        private OutputBin outputBin = null;
        /**
         * The start page of page ranges to print.
         */
        private int startPageRanges;
        /**
         * The end page of page ranges to print.
         */
        private int endPageRanges;
        /**
         * The finishing to use for this document.
         */
        private List<Finishings> finishingsList = null;

        /**
         * Constructs a new Builder with the defaults.
         */
        public Builder() {
        }

        /**
         * Set the version
         * @param version
         *                  Caller version
         * @return
         *                  this builder
         */
        public Builder setVersion(int version) {
            this.version = version;
            return this;
        }

        /**
         * Set the document uri
         * @param documentUri
         *                  Uri referencing a printable file.
         * @return
         *                  this builder
         */
        public Builder setDocumentUri(String documentUri) {
            this.documentUri = documentUri;
            return this;
        }

        /**
         * Set the print stream
         * @param inputStream
         *                  Input stream sourcing a printable file.
         * @return
         *                  this builder
         */
        public Builder setPrintStream(InputStream inputStream) {
            this.printStream = inputStream;
            return this;
        }

        /**
         * Set the username to access the document uri
         * @param userName
         *                  username value
         * @return
         *                  this builder
         */
        public Builder setUserName(String userName) {
            this.userName = userName;
            return this;
        }

        /**
         * Sets the password to access the document uri
         * @param password
         *                  password value
         * @return this builder
         */
        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        /**
         * Sets the HTTP cookie to access the document uri
         * @param cookie
         *                  HTTP cookie to use
         * @return
         *                  this builder
         */
        public Builder setCookie(String cookie) {
            this.cookie = cookie;
            return this;
        }

        /**
         * Sets the number of copies to print
         * Default is 1
         * @param copies
         *                  Copies setting
         * @return
         *                  this builder
         */
        public Builder setCopies(int copies) {
            this.copies = copies;
            return this;
        }

        /**
         * Set the plex mode to use for this document
         * Default is {@link PlexMode#Simplex}
         * @param plexMode
         *                  Plex mode setting
         * @return
         *                  this builder
         */
        public Builder setPlexMode(PlexMode plexMode) {
            this.plexMode = plexMode;
            return this;
        }

        /**
         * Set the color mode to use for this document
         * Default is {@link ColorMode#Auto}
         * @param colorMode
         *                  Color mode setting
         * @return
         *                  this builder
         */
        public Builder setColorMode(ColorMode colorMode) {
            this.colorMode = colorMode;
            return this;
        }

        /**
         * Set the scaling mode to use for this document
         * Default is {@link ScalingMode#Auto}
         * @param scalingMode
         *                  Scaling mode setting
         * @return
         *                  this builder
         */
        public Builder setScalingMode(ScalingMode scalingMode) {
            this.scalingMode = scalingMode;
            return this;
        }

        /**
         * Set the staple mode to use for this document
         * Default is {@link StapleMode#None}
         * @param stapleMode
         *                  Staple mode setting
         * @return
         *                  this builder
         */

        public Builder setStapleMode(StapleMode stapleMode) {
            this.stapleMode = stapleMode;
            return this;
        }

        /**
         * Set the media source to use for this document
         * Default is {@link MediaSource#Auto}
         * @param mediaSource
         *                  Media source setting
         * @return
         *                  this builder
         */
        public Builder setMediaSource(MediaSource mediaSource) {
            this.mediaSource = mediaSource;
            return this;
        }

        /**
         * Set the media size to use for this document
         * Default is device selected
         * @param mediaSize
         *                  Media size setting
         * @return
         *                  this builder
         */
        public Builder setMediaSize(MediaSize mediaSize) {
            this.mediaSize = mediaSize;
            return this;
        }

        /**
         * Set the media type to use for this document
         * Default is device selected
         * @param mediaType
         *                  Media type setting
         * @return
         *                  this builder
         */
        public Builder setMediaType(MediaType mediaType) {
            this.mediaType = mediaType;
            return this;
        }

        /**
         * Set the file type to use for this document
         * Default is device selected
         * @param fileType
         *                  File type setting
         * @return
         *                  this builder
         */
        public Builder setFileType(FileType fileType) {
            this.fileType = fileType;
            return this;
        }

        /**
         * Set the collate mode to use for this document
         * Default is device selected
         * @param collateMode
         *                  Collate mode setting
         * @return
         *                  this builder
         */
        public Builder setCollateMode(CollateMode collateMode) {
            this.collateMode = collateMode;
            return this;
        }

        /**
         * Set the job name to use for this document
         * Default is device selected
         * @param jobName
         *                  Job name setting
         * @return
         *                  this builder
         */
        public Builder setJobName(String jobName) {
            this.jobName = jobName;
            return this;
        }

        /**
         * Set the orientation to use for this document
         * Default is device selected
         * @param orientation
         *                  Orientation setting
         * @return
         *                  this builder
         */
        public Builder setOrientation(Orientation orientation) {
            this.orientation = orientation;
            return this;
        }

        /**
         * Set the print quality to use for this document
         * Default is device selected
         * @param printQuality
         *                  Print quality setting
         * @return
         *                  this builder
         */
        public Builder setPrintQuality(PrintQuality printQuality) {
            this.printQuality = printQuality;
            return this;
        }

        /**
         * Set the output bin to use for this document
         * Default is device selected
         * @param outputBin
         *                  Printer resolution setting
         * @return
         *                  this builder
         */
        public Builder setOutputBin(OutputBin outputBin) {
            this.outputBin = outputBin;
            return this;
        }

        /**
         * Sets the start page of page ranges to print
         * @param startPageRanges
         *                  startPageRanges setting
         * @return
         *                  this builder
         */
        public Builder setStartPageRanges(int startPageRanges) {
            this.startPageRanges = startPageRanges;
            return this;
        }

        /**
         * Sets the end page of page ranges to print
         * @param endPageRanges
         *                  startPageRanges setting
         * @return
         *                  this builder
         */
        public Builder setEndPageRanges(int endPageRanges) {
            this.endPageRanges = endPageRanges;
            return this;
        }

        /**
         * Set the finishings to use for this document
         * Default is {@link Finishings#None}
         * @param finishingsList
         *                  Finishings setting
         * @return
         *                  this builder
         */
        public Builder setFinishingsList(List<Finishings> finishingsList) {
            this.finishingsList = finishingsList;
            return this;
        }

        /**
         * Create the PrintTicket using the provided options
         * @return
         *                  PrintTicket
         * @throws Error
         *                  if document uri is invalid
         * @throws Error
         *                  if username or password are zero-length strings
         * @throws Error
         *                  if cookie is zero-length or greater than 4092 characters
         * @throws Error
         *                  if copies is less than 1 or greater than 999
         */
        public PrintTicket build() throws Error {

            if (printStream == null) {
                try {
                    new URL(documentUri);
                } catch (MalformedURLException ignored) {
                    if (!documentUri.startsWith("content://")) {
                        throw new Error(ErrorName.BadRequest, "Invalid printTicket.documentUri");
                    }
                }
            }

            if ((userName != null) && (userName.length() < 1)) {
                throw new Error(ErrorName.BadRequest, "Invalid printTicket.userName");
            }
            if ((password != null) && (password.length() < 1)) {
                throw new Error(ErrorName.BadRequest, "Invalid printTicket.password");
            }
            if ((cookie != null) && ((cookie.length() < 1) || (cookie.length() > 4092))) {
                throw new Error(ErrorName.BadRequest, "Invalid printTicket.cookie");
            }
            if ((copies < 1) || (copies > 32000)) {
                throw new Error(ErrorName.BadRequest, "Invalid printTicket.copies");
            }
            if (fileType == null) fileType = DEFAULT_FILE_TYPE;

            if (version < VersionForOXP.VERSION_LEVEL.ONE) version = VersionForOXP.VERSION_LEVEL.ONE;

            return new PrintTicket(version, documentUri, printStream, userName, password, cookie, copies, plexMode,
                    colorMode, scalingMode, stapleMode, mediaSource, mediaSize, mediaType, fileType, collateMode, jobName,
                    orientation, printQuality, outputBin, startPageRanges, endPageRanges, finishingsList);
        }
    }

    /**
     * Describe the kinds of special objects contained in this Parcelable instance's marshaled representation.
     * @return
     *              0 for no special objects
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     * @param dest The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written. May be 0 or {@link Parcelable#PARCELABLE_WRITE_RETURN_VALUE}
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.documentUri);
        dest.writeString(this.userName);
        dest.writeString(this.password);
        dest.writeString(this.cookie);
        dest.writeInt(this.copies);
        dest.writeInt(this.colorMode.ordinal());
        dest.writeInt(this.plexMode.ordinal());
        dest.writeString(this.uiContextID);
        dest.writeInt(this.scalingMode.ordinal());
        dest.writeInt(this.stapleMode.ordinal());
        dest.writeInt(this.mediaSource.ordinal());
        dest.writeInt(this.mediaSize.ordinal());
        dest.writeInt(this.mediaType.ordinal());
        dest.writeInt(this.fileType.ordinal());
        dest.writeInt(this.collateMode.ordinal());
        dest.writeString(this.jobName);
        dest.writeInt(this.orientation.ordinal());
        dest.writeInt(this.printQuality.ordinal());
        dest.writeInt(this.outputBin.ordinal());
        dest.writeInt(this.startPageRanges);
        dest.writeInt(this.endPageRanges);
        ArrayList<String> finishingsList = new ArrayList<>();
        for(Finishings finishings: this.finishingsList) {
            finishingsList.add(finishings.name());
        }
        dest.writeStringList(finishingsList);
    }

    /**
     * Constructor when rebuilding from {@link Parcel}
     * @param in Parcel from which the object should be reconstructed
     */
    private PrintTicket(Parcel in) {
        this.finishingsList = new ArrayList<>();
        this.documentUri = in.readString();
        this.userName = in.readString();
        this.password = in.readString();
        this.cookie = in.readString();
        this.copies = in.readInt();
        this.colorMode = ColorMode.values()[in.readInt()];
        this.plexMode = PlexMode.values()[in.readInt()];
        this.uiContextID = in.readString();
        this.scalingMode = ScalingMode.values()[in.readInt()];
        this.stapleMode = StapleMode.values()[in.readInt()];
        this.mediaSource = MediaSource.values()[in.readInt()];
        this.mediaSize = MediaSize.values()[in.readInt()];
        this.mediaType = MediaType.values()[in.readInt()];
        this.fileType = FileType.values()[in.readInt()];
        this.collateMode = CollateMode.values()[in.readInt()];

        if (version >= VersionForOXP.VERSION_LEVEL.THREE) {
            this.jobName = in.readString();
        } else {
            this.jobName = null;
        }

        this.orientation = Orientation.values()[in.readInt()];
        this.printQuality = PrintQuality.values()[in.readInt()];
        this.outputBin = OutputBin.values()[in.readInt()];
        this.startPageRanges = in.readInt();
        this.endPageRanges = in.readInt();

        ArrayList<String> finishingsList = new ArrayList<>();
        in.readStringList(finishingsList);
        for (String finishingsStr: finishingsList) {
            this.finishingsList.add(Finishings.valueOf(finishingsStr));
        }
    }

    /**
     * PrintTicket creator
     */
    public static final Parcelable.Creator<PrintTicket> CREATOR =
            new Parcelable.Creator<PrintTicket>() {

                /**
                 * Create a new instance of the Parcelable class, instantiating it from the given Parcel whose data had previously been written by {@link PrintTicket#writeToParcel(Parcel, int)}
                 * @param in
                 *              The Parcel to read the object's data from.
                 * @return
                 *              Returns a new instance of the Parcelable class.
                 */
                @Override
                public PrintTicket createFromParcel(Parcel in) {
                    return new PrintTicket(in);
                }

                /**
                 * Create a new array of the Parcelable class.
                 * @param size
                 *              Size of the array
                 * @return
                 *              Returns an array of the Parcelable class, with every entry initialized to null.
                 */
                @Override
                public PrintTicket[] newArray(int size) {
                    return new PrintTicket[size];
                }
            };
}
