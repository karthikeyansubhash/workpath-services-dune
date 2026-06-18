package com.hp.jetadvantage.link.api.copier;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class StampOption implements Parcelable {
    public StampFormat format;
    public StampPolicyType policyType;
    public String text;
    public StampType type;

    private List<StampType> stampTypeList = new ArrayList<>();
    private List<String> stampFormatFontList = new ArrayList<>();
    private List<Integer> stampFormatTextSizeList = new ArrayList<>();
    private Range stampFormatTextStartingPage = new Range(0,0);
    private List<String> stampFormatTextColorList = new ArrayList<>();
    private List<Boolean> stampFormatWhiteBackgroundList = new ArrayList<>();
    private List<StampPolicyType> stampPolicyTypeList = new ArrayList<>();

    public StampOption(){
        this.format = new StampFormat();
        this.policyType = StampPolicyType.NONE;
        this.type = StampType.NONE;
    }
    public StampOption(StampFormat format, StampPolicyType policyType, String text, StampType type) {
        this.format = format;
        this.policyType = policyType;
        this.text = text;
        this.type = type;
    }

    public void addStampType(StampType stampType) {
        stampTypeList.add(stampType);
    }

    public List<StampType> getStampTypeList() {
        return stampTypeList;
    }

    public List<String> getStampFormatFontList() {
        return stampFormatFontList;
    }

    public void addStampFormatFontList(String stampFormatFont) {
        stampFormatFontList.add(stampFormatFont);
    }

    public List<Integer> getStampFormatTextSizeList() {
        return stampFormatTextSizeList;
    }

    public void addStampFormatTextSizeList(int stampFormatTextSize) {
        stampFormatTextSizeList.add(stampFormatTextSize);
    }

    public Range getStampFormatTextStartingPage() {
        return stampFormatTextStartingPage;
    }

    public void setStampFormatTextStartingPage(int lowerBound, int upperBound) {
        this.stampFormatTextStartingPage = new Range(lowerBound,upperBound);
    }

    public List<String> getStampFormatTextColorList() {
        return stampFormatTextColorList;
    }

    public void addStampFormatTextColorList(String stampFormatTextColor) {
        stampFormatTextColorList.add(stampFormatTextColor);
    }

    public List<Boolean> getStampFormatWhiteBackgroundList() {
        return stampFormatWhiteBackgroundList;
    }

    public void addStampFormatWhiteBackgroundList(boolean stampFormatWhiteBackground) {
        stampFormatWhiteBackgroundList.add(stampFormatWhiteBackground);
    }

    public List<StampPolicyType> getStampPolicyTypeList() {
        return stampPolicyTypeList;
    }

    public void addStampPolicyTypeList(StampPolicyType stampPolicyType) {
        stampPolicyTypeList.add(stampPolicyType);
    }

    public StampFormat getFormat() {
        return format;
    }

    public void setFormat(StampFormat format) {
        this.format = format;
    }

    public StampPolicyType getPolicyType() {
        return policyType;
    }

    public void setPolicyType(StampPolicyType policyType) {
        this.policyType = policyType;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public StampType getType() {
        return type;
    }

    public void setType(StampType type) {
        this.type = type;
    }

    public static Creator<StampOption> getCREATOR() {
        return CREATOR;
    }

    protected StampOption(Parcel in) {
        text = in.readString();
        format = in.readParcelable(StampFormat.class.getClassLoader());
        policyType = (StampPolicyType) in.readSerializable();
        type = (StampType) in.readSerializable();
    }

    public static final Creator<StampOption> CREATOR = new Creator<StampOption>() {
        @Override
        public StampOption createFromParcel(Parcel in) {
            return new StampOption(in);
        }

        @Override
        public StampOption[] newArray(int size) {
            return new StampOption[size];
        }
    };

    /**
     * Describe the kinds of special objects contained in this Parcelable
     * instance's marshaled representation. For example, if the object will
     * include a file descriptor in the output of {@link #writeToParcel(Parcel, int)},
     * the return value of this method must include the
     * {@link #CONTENTS_FILE_DESCRIPTOR} bit.
     *
     * @return a bitmask indicating the set of special object types marshaled
     * by this Parcelable object instance.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * Flatten this object in to a Parcel.
     *
     * @param dest  The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     *              May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.text);
        dest.writeParcelable(this.format,0);
        dest.writeSerializable(this.policyType);
        dest.writeSerializable(this.type);
    }
}
