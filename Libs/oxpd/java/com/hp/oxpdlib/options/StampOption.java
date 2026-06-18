package com.hp.oxpdlib.options;

import android.os.Parcel;
import android.os.Parcelable;

public class StampOption implements Parcelable {
    public StampFormat format;
    public StampPolicyType policyType;
    public String text;
    public StampType type;


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
