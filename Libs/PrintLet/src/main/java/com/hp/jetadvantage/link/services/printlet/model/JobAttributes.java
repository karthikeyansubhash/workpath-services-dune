// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.printlet.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.hp.jetadvantage.link.services.printlet.model.ipp.IppAttribute;
import com.hp.jetadvantage.link.services.printlet.model.ipp.IppConstants;
import com.hp.jetadvantage.link.services.printlet.model.ipp.IppDateAttribute;
import com.hp.jetadvantage.link.services.printlet.model.ipp.IppIntegerAttribute;
import com.hp.jetadvantage.link.services.printlet.model.ipp.IppResponse;
import com.hp.jetadvantage.link.services.printlet.model.ipp.IppStringAttribute;
import com.hp.jetadvantage.link.services.printlet.model.ipp.util.IppErrorGenerator;

import java.util.List;
import java.util.UUID;

/**
 * Job Attribute holder
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class JobAttributes implements Parcelable {
    /**
     * Job UUID.
     */
    public final UUID jobUuid;
    /**
     * Job name.
     */
    public final String jobName;
    /**
     * Job originating user.
     */
    public final String jobUser;
    /**
     * Job created time.
     */
    public final long jobCreatedTime;
    /**
     * Job completed time.
     */
    public final long jobCompletedTime;
    /**
     * Current job state.
     */
    public final JobState jobState;
    /**
     * List of reasons for the current job state (may be null).
     */
    public final List<String> jobStateReasons;
    /**
     * If jobState is 'aborted' and jobStateReasons contains 'document-access-error', this string
     * should provide error details (may be null).
     */
    public final String documentAccessError;
    /**
     * Number of job impressions completed
     */
    public final int jobImpressionsCompleted;
    /**
     * Number of media sheets completed
     */
    public final int jobMediaSheetsCompleted;

    /**
     * Constructor used by the library to construct JobAttributes objects.
     * @param response
     *              Parsed response received from the device.
     * @throws Error
     *              If an error occurs.
     */
    public JobAttributes(IppResponse response) throws Error
    {
        IppAttribute attribute;
        attribute = response.findAttribute(IppConstants.IppTag.IPP_TAG_JOB, IppConstants.IppTag.IPP_TAG_URI, IppConstants.IPP_ATTRIBUTE_NAME__JOB_UUID);
        if (attribute instanceof IppStringAttribute) {
            String uuid = ((IppStringAttribute) attribute).get(0);
            if (uuid.startsWith("urn:uuid:")) {
                uuid = uuid.substring("urn:uuid:".length());
            }
            this.jobUuid = UUID.fromString(uuid);
        } else {
            this.jobUuid = null;
        }

        int[] nameTags = { IppConstants.IppTag.IPP_TAG_NAMELANG.getValue(), IppConstants.IppTag.IPP_TAG_NAME.getValue() };

        attribute = response.findAttributeByAnyTag(IppConstants.IppTag.IPP_TAG_JOB, nameTags, IppConstants.IPP_ATTRIBUTE_NAME__JOB_NAME);
        if (attribute instanceof IppStringAttribute) {
            this.jobName = ((IppStringAttribute)attribute).get(0);
        } else {
            this.jobName = null;
        }

        attribute = response.findAttributeByAnyTag(IppConstants.IppTag.IPP_TAG_JOB, nameTags, IppConstants.IPP_ATTRIBUTE_NAME__JOB_USER);
        if (attribute instanceof IppStringAttribute) {
            this.jobUser = ((IppStringAttribute)attribute).get(0);
        } else {
            this.jobUser =  null;
        }

        attribute = response.findAttribute(IppConstants.IppTag.IPP_TAG_JOB, IppConstants.IppTag.IPP_TAG_DATE, IppConstants.IPP_ATTRIBUTE_NAME__JOB_TIME_CREATED);
        if (attribute instanceof IppDateAttribute) {
            this.jobCreatedTime = ((IppDateAttribute) attribute).get(0).getTime();
        } else {
            this.jobCreatedTime = 0;
        }


        attribute = response.findAttribute(IppConstants.IppTag.IPP_TAG_JOB, IppConstants.IppTag.IPP_TAG_DATE, IppConstants.IPP_ATTRIBUTE_NAME__JOB_TIME_COMPLETED);
        if (attribute instanceof IppDateAttribute) {
            this.jobCompletedTime = ((IppDateAttribute) attribute).get(0).getTime();
        } else {
            this.jobCompletedTime = 0;
        }

        attribute = response.findAttribute(IppConstants.IppTag.IPP_TAG_JOB, IppConstants.IppTag.IPP_TAG_ENUM, IppConstants.IPP_ATTRIBUTE_NAME__JOB_STATE);
        if (!(attribute instanceof IppIntegerAttribute)) IppErrorGenerator.attributeNotFoundError(IppConstants.IPP_ATTRIBUTE_NAME__JOB_STATE);
        this.jobState = JobState.fromAttributeValue(((IppIntegerAttribute)attribute).get(0));

        attribute = response.findAttribute(IppConstants.IppTag.IPP_TAG_JOB, IppConstants.IppTag.IPP_TAG_KEYWORD, IppConstants.IPP_ATTRIBUTE_NAME__JOB_STATE_REASONS);
        if (!(attribute instanceof IppStringAttribute)) IppErrorGenerator.attributeNotFoundError(IppConstants.IPP_ATTRIBUTE_NAME__JOB_STATE_REASONS);
        this.jobStateReasons = (((IppStringAttribute)attribute).getValues());

        attribute = response.findAttribute(IppConstants.IppTag.IPP_TAG_JOB, IppConstants.IppTag.IPP_TAG_TEXT, IppConstants.IPP_ATTRIBUTE_NAME__JOB_DOCUMENT_ACCESS_ERROR);
        if ((attribute instanceof IppStringAttribute)) {
            this.documentAccessError = ((IppStringAttribute)attribute).get(0);
        } else {
            this.documentAccessError = null;
        }

        attribute = response.findAttribute(IppConstants.IppTag.IPP_TAG_JOB, IppConstants.IppTag.IPP_TAG_INTEGER, IppConstants.IPP_ATTRIBUTE_NAME__JOB_IMPRESSIONS_COMPLETED);
        if (!(attribute instanceof IppIntegerAttribute)) IppErrorGenerator.attributeNotFoundError(IppConstants.IPP_ATTRIBUTE_NAME__JOB_IMPRESSIONS_COMPLETED);
        this.jobImpressionsCompleted = ((IppIntegerAttribute)attribute).get(0);

        attribute = response.findAttribute(IppConstants.IppTag.IPP_TAG_JOB, IppConstants.IppTag.IPP_TAG_INTEGER, IppConstants.IPP_ATTRIBUTE_NAME__JOB_MEDIA_SHEETS_COMPLETED);
        if (!(attribute instanceof IppIntegerAttribute)) IppErrorGenerator.attributeNotFoundError(IppConstants.IPP_ATTRIBUTE_NAME__JOB_MEDIA_SHEETS_COMPLETED);
        this.jobMediaSheetsCompleted = ((IppIntegerAttribute)attribute).get(0);
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
        dest.writeString(this.jobUuid.toString());
        dest.writeString(this.jobName);
        dest.writeString(this.jobUser);
        dest.writeLong(this.jobCreatedTime);
        dest.writeLong(this.jobCompletedTime);
        dest.writeInt(this.jobState.ordinal());
        dest.writeStringList(this.jobStateReasons);
        dest.writeString(this.documentAccessError);
        dest.writeInt(this.jobImpressionsCompleted);
        dest.writeInt(this.jobMediaSheetsCompleted);
    }

    /**
     * Constructor when rebuilding from {@link Parcel}
     * @param in Parcel from which the object should be reconstructed
     */
    private JobAttributes(Parcel in) {
        this.jobUuid = UUID.fromString(in.readString());
        this.jobName = in.readString();
        this.jobUser = in.readString();
        this.jobCreatedTime = in.readLong();
        this.jobCompletedTime = in.readLong();
        this.jobState = JobState.values()[in.readInt()];
        this.jobStateReasons = in.createStringArrayList();
        this.documentAccessError = in.readString();
        this.jobImpressionsCompleted = in.readInt();
        this.jobMediaSheetsCompleted = in.readInt();
    }

    /**
     * JobAttributes creator
     */
    public static final Parcelable.Creator<JobAttributes> CREATOR =
            new Parcelable.Creator<JobAttributes>() {

                /**
                 * Create a new instance of the Parcelable class, instantiating it from the given Parcel whose data had previously been written by {@link JobAttributes#writeToParcel(Parcel, int)}
                 * @param in
                 *              The Parcel to read the object's data from.
                 * @return
                 *              Returns a new instance of the Parcelable class.
                 */
                @Override
                public JobAttributes createFromParcel(Parcel in) {
                    return new JobAttributes(in);
                }

                /**
                 * Create a new array of the Parcelable class.
                 * @param size
                 *              Size of the array
                 * @return
                 *              Returns an array of the Parcelable class, with every entry initialized to null.
                 */
                @Override
                public JobAttributes[] newArray(int size) {
                    return new JobAttributes[size];
                }
            };
}
