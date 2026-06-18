// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.common.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.hp.jetadvantage.link.common.utils.Preconditions;

/**
 * Data which describes a printer. This information is used to attempt
 * connection to a printer.
 */
@SuppressWarnings({"WeakerAccess"})
public class PrinterInfo implements Parcelable {
    public static final int VERSION = 1;

    /**
     * @since 1
     */
    public int mVersion;

    /**
     * @since 1
     */
    public Uri mBaseUri;

    /**
     * @since 1
     */
    public String mConsumerKey;

    /**
     * @since 1
     */
    public String mConsumerSecret;

    /**
     * The MFP ID for the printer
     *
     * @since 1
     */
    public String mUId;

    /**
     * @since 1
     */
    public String mSessionKey;

    /**
     * @since 1
     */
    public String mSessionSecret;

    /**
     * @since 1
     */
    public long mTimeAdjust;

    /**
     * @since 1
     */
    public PrinterState mPrinterState;

    public interface Capability {
        long CAPABILITY_PRINT = 1 << 1;
        long CAPABILITY_SCAN = 1 << 2;
        long CAPABILITY_COPY = 1 << 3;
    }

    private String mIp;
    private String mBssid;
    private String mDiscoveryType;
    private String mName;

    // API type, probably API version will be needed in future also for compatibility
    private ApiType mApiType = ApiType.OXP;

    private String mMacAddress;
    private String mModelName;
    private String mNetworkInterface;
    private int mConnectTimeout;
    private int mReadTimeout;

    private long mCapabilities = 0;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(final Parcel out, int arg1) {
        out.writeInt(VERSION);
        out.writeString(mBaseUri.toString());
        out.writeString(mConsumerKey);
        out.writeString(mConsumerSecret);
        out.writeString(mSessionKey);
        out.writeString(mSessionSecret);
        out.writeLong(mTimeAdjust);
        out.writeString(mUId);
        Preconditions.checkNotNull(mPrinterState, "Printer State is required to parcel");
        out.writeString(mPrinterState.name());

        out.writeString(mBssid);
        out.writeString(mIp);
        out.writeString(mDiscoveryType);
        out.writeString(mName);
        out.writeString(mApiType.name());
        out.writeString(mMacAddress);
        out.writeString(mModelName);
        out.writeString(mNetworkInterface);
        out.writeInt(mConnectTimeout);
        out.writeInt(mReadTimeout);
        out.writeLong(mCapabilities);
    }

    public static final Parcelable.Creator<PrinterInfo> CREATOR = new Parcelable.Creator<PrinterInfo>() {
        public PrinterInfo createFromParcel(final Parcel in) {
            return new PrinterInfo(in);
        }

        public PrinterInfo[] newArray(final int size) {
            return new PrinterInfo[size];
        }
    };

    private PrinterInfo(final Parcel in) {
        mVersion = in.readInt();
        mBaseUri = Uri.parse(in.readString());
        mConsumerKey = in.readString();
        mConsumerSecret = in.readString();
        mSessionKey = in.readString();
        mSessionSecret = in.readString();
        mTimeAdjust = in.readLong();
        mUId = in.readString();
        mPrinterState = PrinterState.valueOf(in.readString());

        mBssid = in.readString();
        mIp = in.readString();
        mDiscoveryType = in.readString();
        mName = in.readString();
        mApiType = ApiType.valueOf(in.readString());
        mMacAddress = in.readString();
        mModelName = in.readString();
        mNetworkInterface = in.readString();
        mConnectTimeout = in.readInt();
        mReadTimeout = in.readInt();
        mCapabilities = in.readLong();
    }

    private PrinterInfo(final Builder builder) {
        mVersion = VERSION;
        mBaseUri = builder.mBaseUri;

        if (builder.mSessionToken != null) {
            mSessionKey = builder.mSessionToken;
            mSessionSecret = builder.mSessionSecret;
        } else {
            mSessionKey = "";
            mSessionSecret = "";
        }

        if (builder.mClientToken != null) {
            mConsumerKey = builder.mClientToken;
            mConsumerSecret = builder.mClientSecret;
        } else {
            mConsumerKey = "";
            mConsumerSecret = "";
        }

        if (TextUtils.isEmpty(builder.mIp)) {
            mIp = builder.mBaseUri.getHost();
        } else {
            mIp = builder.mIp;
        }

        mUId = "panel";
        mTimeAdjust = builder.mTimeAdjust;

        if (builder.mPrinterState != null) {
            mPrinterState = builder.mPrinterState;
        } else {
            mPrinterState = PrinterState.DISCONNECTED;
        }

        mApiType = builder.mApiType;
        mName = builder.mName;

        if (builder.mDiscoveryType != null) {
            mDiscoveryType = builder.mDiscoveryType;
        } else {
            mDiscoveryType = "LOCAL";
        }

        mBssid = builder.mBssid;

        mMacAddress = builder.mMacAddress;
        mModelName = builder.mModelName;
        mNetworkInterface = builder.mNetworkInterface;

        mConnectTimeout = builder.mConnectTimeout;
        mReadTimeout = builder.mReadTimeout;

        mCapabilities = builder.mCapabilities;
    }

    public static boolean isConnected(final PrinterInfo pi) {
        boolean connected = false;
        if(!isEmpty(pi)) {
            if (pi.getPrinterState() == PrinterState.CONNECTED) {
                connected = true;
            }
        }
        return connected;
    }

    public static boolean isEmpty(final PrinterInfo pi) {
        return null == pi || null == pi.mBaseUri || Uri.EMPTY.equals(pi.mBaseUri);
    }

    private static String replaceModelName(final String deviceName) {
        String reName;
        String ori_Name;
        try {
            if (deviceName.contains("("))
                ori_Name = deviceName.substring(0, deviceName.indexOf("("));
            else
                ori_Name = deviceName;

            String tmpName = ori_Name.substring(0, 7)
                    .toLowerCase();

            if (tmpName.equals("samsung")) {
                reName = ori_Name.substring(8, ori_Name
                        .length());
            } else {
                reName = ori_Name;
            }

            // remove MFC: MDL: string when connect usb
            if (reName.lastIndexOf(":") != -1) {
                reName = reName.substring(reName.lastIndexOf(":") + 1, reName.length());
            }

            // remove all leading white space from device name
            reName = reName.replaceAll("^\\s+", "");

            // Remove trailing white space from device name
            reName = reName.replaceAll("\\s+$", "");
            return reName;
        } catch (Exception e) {
            return deviceName;
        }
    }

    public String getClientToken() {
        return mConsumerKey;
    }

    public String getClientSecret() {
        return mConsumerSecret;
    }

    public String getSessionToken() {
        return mSessionKey;
    }

    public String getSessionSecret() {
        return mSessionSecret;
    }

    public int getPort() {
        return mBaseUri.getPort();
    }

    /**
     * Helper to get displayable printers name
     *
     * @return String name
     */
    public String getDisplayName() {
        String name = getName();
        return TextUtils.isEmpty(name) ? mBaseUri.getHost() : name;
    }

    public Uri getBaseUri() {
        return mBaseUri;
    }

    public String getName() {
        return mName;
    }

    public String getDiscoveryType() {
        return mDiscoveryType;
    }

    public long getTimeAdjust() {
        return mTimeAdjust;
    }

    public String getBssid() {
        return mBssid;
    }

    public ApiType getApiType() {
        return mApiType;
    }

    public PrinterState getPrinterState() {
        return mPrinterState;
    }

    public void setPrinterState(final PrinterState state) {
        mPrinterState = state;
    }

    public void setTimeAdjust(final long timeAdjust) {
        mTimeAdjust = timeAdjust;
    }

    public String getMfpId() {
        return "panel";
    }

    public String getMacAddress() {
        return mMacAddress;
    }

    public String getModelName() {
        return mModelName;
    }

    public String getInterfaceName() {
        return mNetworkInterface;
    }

    public int getConnectTimeout() {
        return mConnectTimeout;
    }

    public int getReadTimeout() {
        return mReadTimeout;
    }

    public long getCapabilities() {
        return mCapabilities;
    }

    public String getIP() {
        // Null IP should be avoided, but "" is fine for connection of WiFi-Direct
        if (TextUtils.isEmpty(mIp)) {
            return getBaseUri().getHost() == null ? "" : getBaseUri().getHost();
        } else {
            return mIp;
        }
    }

    /**
     * Internal parcelable version
     *
     * @return internal version
     */
    @SuppressWarnings({"unused"})
    public int getVersion() {
        return mVersion;
    }

    /**
     * Builder for PrinterInfo
     */
    public static class Builder {
        // Required parameters
        private Uri mBaseUri = Uri.EMPTY;
        private String mClientToken;
        private String mClientSecret;
        private String mSessionToken, mSessionSecret;
        private String mName = null;
        private String mDiscoveryType = "LOCAL";
        private String mBssid = "";
        private String mIp = "";
        private long mTimeAdjust = 0;
        private ApiType mApiType = ApiType.OXP;
        private PrinterState mPrinterState = null;
        private String mNetworkInterface = null;
        private String mMacAddress = null;
        private String mModelName = null;
        private int mConnectTimeout = 0;
        private int mReadTimeout = 0;
        private long mCapabilities = 0;

        /**
         * Constructor for Builder.
         *
         * @param printer CatalogedPrinter info
         */
        public Builder(final PrinterInfo printer) {
            mBaseUri = printer.mBaseUri == null ? Uri.EMPTY : printer.mBaseUri;
            mTimeAdjust = printer.mTimeAdjust;
            mPrinterState = printer.mPrinterState;
            mClientToken = printer.mConsumerKey;
            mClientSecret = printer.mConsumerSecret;
            mSessionToken = printer.mSessionKey;
            mSessionSecret = printer.mSessionSecret;

            mName = printer.mName;
            mDiscoveryType = printer.mDiscoveryType;
            mBssid = printer.mBssid;
            mIp = printer.mIp;
            mApiType = printer.mApiType;

            mMacAddress = printer.mMacAddress;
            mModelName = printer.mModelName;
            mNetworkInterface = printer.mNetworkInterface;
            mConnectTimeout = printer.mConnectTimeout;
            mReadTimeout = printer.mReadTimeout;

            mCapabilities = printer.mCapabilities;
        }

        /**
         * Default constructor
         */
        public Builder() {
            // nothing to do here
        }

        /**
         * Method baseUri.
         *
         * @param baseUri Uri
         * @return Builder
         */
        public Builder baseUri(final Uri baseUri) {
            mBaseUri = baseUri == null ? Uri.EMPTY : baseUri;
            return this;
        }

        /**
         * Method name.
         *
         * @param name CharSequence
         * @return Builder
         */
        public Builder name(final String name) {
            mName = replaceModelName(name);
            return this;
        }

        /**
         * Add the client information
         *
         * @param clientToken ClientInfo to be set
         * @return Builder
         */
        public Builder clientToken(final String clientToken) {
            mClientToken = clientToken;
            return this;
        }

        /**
         * Add the client information
         *
         * @param clientSecret ClientInfo to be set
         * @return Builder
         */
        public Builder clientSecret(final String clientSecret) {
            mClientSecret = clientSecret;
            return this;
        }

        /**
         * Add the client information
         *
         * @param sessionToken ClientInfo to be set
         * @return Builder
         */
        public Builder sessionToken(final String sessionToken) {
            mSessionToken = sessionToken;
            return this;
        }

        /**
         * Add the client information
         *
         * @param sessionSecret ClientInfo to be set
         * @return Builder
         */
        public Builder sessionTokenSecret(final String sessionSecret) {
            mSessionSecret = sessionSecret;
            return this;
        }

        /**
         * Sets printer discovery type
         *
         * @param discoveryType {@link String} to be set
         * @return Builder
         */
        public Builder discoveryType(final String discoveryType) {
            mDiscoveryType = discoveryType;
            return this;
        }

        /**
         * @param bssid {@link String} to be set
         * @return Builder
         */
        public Builder bssid(final String bssid) {
            mBssid = bssid;
            return this;
        }

        /**
         * @param ip {@link String} to be set
         * @return Builder
         */
        public Builder ip(final String ip) {
            mIp = ip;
            return this;
        }

        /**
         * Sets time adjustment value for this printer
         *
         * @param timeAdjustment to be stored
         * @return Builder
         */
        public Builder timeAdjust(final long timeAdjustment) {
            mTimeAdjust = timeAdjustment;
            return this;
        }

        /**
         * Sets time adjustment value for this printer
         *
         * @param api {@link ApiType} to be stored
         * @return Builder
         */
        public Builder api(final ApiType api) {
            mApiType = api;
            return this;
        }

        /**
         * Sets the network interface for connection to this printer
         *
         * @param interfaceName network interface to be stored
         * @return Builder
         */
        public Builder interfaceName(final String interfaceName) {
            mNetworkInterface = interfaceName;
            return this;
        }

        /**
         * Sets MAC address for connection to this printer
         *
         * @param macAddress MAC address to be stored
         * @return Builder
         */
        public Builder macAddress(final String macAddress) {
            mMacAddress = macAddress;
            return this;
        }

        /**
         * Sets Model NAme of this printer
         *
         * @param modelName to be stored
         * @return Builder
         */
        public Builder modelName(final String modelName) {
            mModelName = modelName;
            return this;
        }

        /**
         * Sets connect timeout for connection to this printer
         *
         * @param connectTimeout connect timeout to be stored
         * @return Builder
         */
        public Builder connectTimeout(final int connectTimeout) {
            mConnectTimeout = connectTimeout;
            return this;
        }

        /**
         * Sets read timeout for connection to this printer
         *
         * @param readTimeout read timeout to be stored
         * @return Builder
         */
        public Builder readTimeout(final int readTimeout) {
            mReadTimeout = readTimeout;
            return this;
        }

        /**
         * Sets the capabilities for this printer
         *
         * @param capabilities capabilities, etc {@link Capability#CAPABILITY_PRINT} or {@link Capability#CAPABILITY_SCAN} combined with OR operator
         * @return Builder
         */
        public Builder capabilities(final long capabilities) {
            mCapabilities = capabilities;
            return this;
        }

        /**
         * @param ck client key
         * @param cs client secret
         * @param sk session key
         * @param ss session secret
         * @return Builder
         */
        public Builder clientInfo(final String ck, final String cs, final String sk, final String ss) {
            if (sk != null && ss != null) {
                mSessionToken = sk;
                mSessionSecret = ss;
            }
            if (ck != null && cs != null) {
                mClientToken = ck;
                mClientSecret = cs;
            }

            return this;
        }

        /**
         * Sets printer state value
         *
         * @param state {@link PrinterState}
         * @return this
         */
        public Builder printerState(final PrinterState state) {
            mPrinterState = state;
            return this;
        }

        /**
         * Method build.
         *
         * @return CatalogedPrinter
         */
        public PrinterInfo build() {
            return new PrinterInfo(this);
        }
    }

    @Override
    public String toString() {
        return "Printer:" + mBaseUri + "," + mUId
                + "," + mPrinterState + ", tadj:" + mTimeAdjust
                + "BSSID:" + getBssid() + ","
                + "Discovered:" + getDiscoveryType() + ","
                + "Name:" + getName() + ","
                + "Api: " + mApiType.name() + ","
                + "MAC: " + mMacAddress + ","
                + "Model: " + mModelName + ","
                + "Interface: " + mNetworkInterface + ","
                + "Connect timeout: " + mConnectTimeout + ","
                + "Read timeout: " + mReadTimeout + ","
                + "Capabilities: " + mCapabilities;
    }
}
