package com.hp.jetadvantage.link.api.copier;

import android.os.Parcel;
import android.os.Parcelable;

public class StampFormat implements Parcelable {
    public String font;
    public int startingPage;
    public String textColor;
    public int textSize;
    public boolean whiteBackground;

    /**
     * Default no-arg constructor
     *
     */
    public StampFormat() {
    }

    public StampFormat(String font, int textSize, String textColor, boolean whiteBackground, int startingPage) {
        this.font=font;
        this.textSize=textSize;
        this.startingPage=startingPage;
        this.whiteBackground=whiteBackground;
        this.textColor=textColor;
    }

    protected StampFormat(Parcel in) {
        font = in.readString();
        startingPage = in.readInt();
        textColor = in.readString();
        textSize = in.readInt();
        whiteBackground = in.readByte() != 0;
    }

    public static final Creator<StampFormat> CREATOR = new Creator<StampFormat>() {
        @Override
        public StampFormat createFromParcel(Parcel in) {
            return new StampFormat(in);
        }

        @Override
        public StampFormat[] newArray(int size) {
            return new StampFormat[size];
        }
    };

    public String getFont() {
        return font;
    }

    public void setFont(String font) {
        this.font = font;
    }

    public int getStartingPage() {
        return startingPage;
    }

    public void setStartingPage(int startingPage) {
        this.startingPage = startingPage;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public boolean isWhiteBackground() {
        return whiteBackground;
    }

    public void setWhiteBackground(boolean whiteBackground) {
        this.whiteBackground = whiteBackground;
    }

    @Override
    public String toString() {
        return "StampFormat{" +
                "font='" + font + '\'' +
                ", startingPage=" + startingPage +
                ", textColor='" + textColor + '\'' +
                ", textSize=" + textSize +
                ", whiteBackground=" + whiteBackground +
                '}';
    }

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
        dest.writeString(font);
        dest.writeInt(startingPage);
        dest.writeString(textColor);
        dest.writeInt(textSize);
        dest.writeByte((byte) (whiteBackground ? 1 : 0));
    }
}
