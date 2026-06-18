package com.hp.jetadvantage.link.api.job;

import android.annotation.SuppressLint;
import android.os.Parcel;

import androidx.annotation.Keep;
import androidx.core.util.Preconditions;

import com.hp.jetadvantage.link.api.copier.CopyAttributes;
import com.hp.jetadvantage.link.common.Sdk;

/**
 * Copy Job details
 *
 * @since API 3
 */
public class CopyJobData implements JobInfo.JobData {
    @Keep
    private int mVersion;
    @Keep
    private int mImagesScanned;
    @Keep
    private int mSheetsPrinted;
    @Keep
    private CopyAttributes.Duplex mDuplex;
    @Keep
    private CopyAttributes.ScanSize mScanSize;
    @Keep
    private CopyJobState mJobState;
    @Keep
    private CopyAttributes.JobExecutionMode mJobExecutionMode;

    /**
     * @hide trivial
     */
    public CopyJobData() {
        mVersion = Sdk.VERSION.LEVEL;
    }

    @SuppressLint("RestrictedApi")
    private CopyJobData(Parcel in) {
        mVersion = in.readInt();
        Preconditions.checkArgument(mVersion >= Sdk.VERSION_LEVEL.ONE);

        mImagesScanned = in.readInt();
        mSheetsPrinted = in.readInt();
        mDuplex = (CopyAttributes.Duplex) in.readSerializable();
        mScanSize = (CopyAttributes.ScanSize) in.readSerializable();
        mJobState = in.readParcelable(PrintJobState.class.getClassLoader());
        mJobExecutionMode = (CopyAttributes.JobExecutionMode) in.readSerializable();
    }

    /**
     * @hide trivial
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mVersion);
        dest.writeInt(mImagesScanned);
        dest.writeInt(mSheetsPrinted);
        dest.writeSerializable(mDuplex);
        dest.writeSerializable(mScanSize);
        dest.writeParcelable(mJobState, 0);
        dest.writeSerializable(mJobExecutionMode);
    }

    /**
     * @hide trivial
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * @hide trivial
     */
    public static final Creator<CopyJobData> CREATOR = new Creator<CopyJobData>() {
        @Override
        public CopyJobData createFromParcel(Parcel in) {
            return new CopyJobData(in);
        }

        @Override
        public CopyJobData[] newArray(int size) {
            return new CopyJobData[size];
        }
    };

    /**
     * Returns number of scanned images
     *
     * @return number of scanned images
     * @since API 3
     */
    @Keep
    public int getImagesScanned() {
        return mImagesScanned;
    }

    /**
     * Set the number of of scanned images
     *
     * @param imagesScanned number of images
     * @hide for internal use.
     * @since API 3
     */
    public void setImagesScanned(int imagesScanned) {
        this.mImagesScanned = imagesScanned;
    }

    /**
     * Returns total number of sheets (one or two sides)
     *
     * @return number of sheets
     * @since API 3
     */
    @Keep
    public int getSheetsPrinted() {
        return mSheetsPrinted;
    }

    /**
     * Sets the total number of sheets for the job
     *
     * @param sheetsPrinted number of sheets
     * @hide for internal use.
     * @since API 3
     */
    public void setSheetsPrinted(int sheetsPrinted) {
        this.mSheetsPrinted = sheetsPrinted;
    }

    /**
     * Duplex mode of the job
     *
     * @return Duplex duplex mode
     * @since API 3
     */
    @Keep
    public CopyAttributes.Duplex getDuplex() {
        return mDuplex;
    }

    /**
     * Sets duplex mode for the job
     *
     * @param duplex duplex mode
     * @hide for internal use.
     * @since API 3
     */
    public void setDuplex(CopyAttributes.Duplex duplex) {
        this.mDuplex = duplex;
    }

    /**
     * Media size of scanned image
     *
     * @return ScanSize
     * @since API 3
     */
    @Keep
    public CopyAttributes.ScanSize getScanSize() {
        return mScanSize;
    }

    /**
     * Sets media size for the job
     *
     * @param scanSize media size of scanned document
     * @hide for internal use.
     * @since API 3
     */
    public void setScanSize(CopyAttributes.ScanSize scanSize) {
        this.mScanSize = scanSize;
    }

    /**
     * State of copy job
     *
     * @return job state
     * @since API 3
     */
    @SuppressWarnings({"unused"})
    @Keep
    public CopyJobState getJobState() {
        return mJobState;
    }

    /**
     * Set the state for the copy job
     *
     * @param state of scan job
     * @hide for internal use
     * @since API 3
     */
    public void setJobState(CopyJobState state) {
        mJobState = state;
    }

    /**
     * Job execution mode
     *
     * @return job state
     * @since API 3
     */
    @SuppressWarnings({"unused"})
    @Keep
    public CopyAttributes.JobExecutionMode getJobExecutionMode() {
        return mJobExecutionMode;
    }


    /**
     * Set the job execution mode for the copy job
     *
     * @param mJobExecutionMode job execution mode
     * @hide for internal use
     * @since API 3
     */
    public void setJobExecutionMode(CopyAttributes.JobExecutionMode mJobExecutionMode) {
        this.mJobExecutionMode = mJobExecutionMode;
    }

    /**
     * Internal parcelable version
     *
     * @return internal version
     * @hide for internal use
     */
    @SuppressWarnings({"unused"})
    public int getVersion() {
        return mVersion;
    }

    /**
     * Internal parcelable version
     * @param version version for client
     * @hide for internal use
     */
    @SuppressWarnings({"unused"})
    public void setVersion(int version) {
        mVersion = version;
    }

    /**
     * @hide This is hidden because it should be understood without documenting in the javadoc
     */
    @Override
    @Keep
    public String toString() {
        return getClass().getSimpleName() + "[" +
                "jobState=" + mJobState +
                ", imagesScanned=" + mImagesScanned +
                ", sheetsPrinted=" + mSheetsPrinted +
                ", duplex=" + mDuplex +
                ", scanSize=" + mScanSize +
                ", jobExecutionMode=" + mJobExecutionMode +
                "]";
    }
}
