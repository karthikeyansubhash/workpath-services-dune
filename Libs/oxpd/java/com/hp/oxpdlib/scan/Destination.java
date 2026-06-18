// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.scan;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Destination object
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Destination implements Parcelable {

    /** The uri of the destination (must match the pattern 
     * (https?|file|ftp)://([^@]*<ZWNJ>/.*|[^@]*\?.*|[^@]*)'; case sensitive, max length=256).
     * <p>The Uri scheme implicitly determines the {@see DestinationType} to be used.</p>
     * <p>The following table shows how to construct a Uri for each {@see DestinationType}:
     * <table border='1'>
     *     <tr><th>DestinationType</th><th>Example Uri</th></tr>
     *     <tr><td>Http</td><td><b>http</b>://MyWebServer[:port]/MyWebApp/MyScanReceiver.aspx</td></tr>
     *     <tr><td>Https</td><td><b>https</b>://MyWebServer[:port]/MyWebApp/MyScanReceiver.aspx</td></tr>
     *     <tr><td>NetworkFolder</td>
     *         <td><b>file</b>:////MyFileServer/MyShare[/MySubFolder]<br /><br />
     *         If MySubFolder (or any subfolder specified in the Uri) does not exist at the
     *         destination, some device implementations may create it (assuming the device has write
     *         permission), while others will fail the job. To ensure fleet-wide compatibility,
     *         solutions must not rely on the device to create subfolders. Instead, the solution
     *         must ensure that the subfolders exist before starting the scan job.</td></tr>
     *     <tr><td>Ftp</td>
     *         <td><b>ftp</b>://MyFtpServer[/MyFolder[/MySubFolder]]<br /><br />
     *         If MyFolder (or any subfolder psecified in the Uri) does not exist at the destination,
     *         some device implementations may create it (assuming the device has write permission),
     *         while others will fail the job. To ensure fleet-wide compatibility, solutions must
     *         not rely on the device to create folders or subfolders. Instead, the solution must
     *         ensure that the folder and subfolders exist before starting the scan job.</td></tr>
     * </table></p>
     */
    public final Uri uri;
    /**
     * The username credential (min length=1, max length=128, may be null). If non-null, password must also be non-null. The caller is responsible for any additional length, format, character, or other restrictions that may be required by the actual network or server hosting the uri.
     */
    public final String userName;
    /**
     * The password credential (min length=1, max length=128, may be null). If non-null, userName must also be non-null. The caller is responsible for any additional length, format, character, or other restrictions that may be required by the actual network or server hosting the uri.
     */
    public final String password;
    /**
     * The domain to which the credentials apply (min length=1, max length=256, may be null). Use domain only in combination with a uri using the FILE scheme. For all other schemes, domain must be null. While some restrictions are imposed by the regular expression, the caller is responsible for any additional length, format, character, or other restrictions that may be required by the actual network or server hosting the destination.
     */
    public final String domain;
    /**
     * The maximum time (in seconds) to allow for making a connection to the uri [1, 300].
     */
    public final int connectionTimeout;
    /**
     * The maximum time (in seconds) to allow for receiving a response after sending a request to the uri [1, 300].
     */
    public final int responseTimeout;
    /**
     * The maximum number of times to retry an attempt to access the uri [0,5]. A value of 0 indicates no retry if the first attempt fails.
     */
    public final int maxConsecutiveRetries;
    /**
     * The minimum number of seconds to wait before attempting a retry [1,300].
     */
    public final int retryInterval;
    /**
     * Constructor used to construct Destination objects.
     * @param uri
     *              The uri of the destination (must match the pattern '(https?|file|ftp)://([^@]*<ZWNJ>/.*|[^@]*\?.*|[^@]*)'; case sensitive, max length=256).<br /><br />The Uri scheme implicitly determines the {@see DestinationType} to be used (see the {@see Destination#uri|long description} to learn how to construct a Uri for each {@see DestinationType}).
     * @param userName
     *              (Optional) The username credential (min length=1, max length=128, may be null). If non-null, password must also be non-null.<p>The caller is responsible for any additional length, format, character, or other restrictions that may be required by the actual network or server hosting the uri.</p>
     * @param password
     *              (Optional) The password credential (min length=1, max length=128, may be null). If non-null, userName must also be non-null.<p>The caller is responsible for any additional length, format, character, or other restrictions that may be required by the actual network or server hosting the uri.</p>
     * @param domain
     *              (Optional) The domain to which the credentials apply (min length=1, max length=256, may be null).<p>Use domain only in combination with a uri using the FILE scheme. For all other schemes, domain must be null. While some restrictions are imposed by the regular expression, the caller is responsible for any additional length, format, character, or other restrictions that may be required by the actual network or server hosting the destination.</p>
     * @param connectionTimeout
     *              The maximum time (in seconds) to allow for making a connection to the uri [1, 300].
     * @param responseTimeout
     *              The maximum time (in seconds) to allow for receiving a response after sending a request to the uri [1, 300].
     * @param maxConsecutiveRetries
     *              The maximum number of times to retry an attempt to access the uri [0,5]. A value of 0 indicates no retry if the first attempt fails.
     * @param retryInterval
     *              The minimum number of seconds to wait before attempting a retry [1,300].
     */
    public Destination(Uri uri, String userName, String password, String domain, int connectionTimeout, int responseTimeout, int maxConsecutiveRetries, int retryInterval) {
        this.uri = uri;
        this.userName = userName;
        this.password = password;
        this.domain = domain;
        this.connectionTimeout = connectionTimeout;
        this.responseTimeout = responseTimeout;
        this.maxConsecutiveRetries = maxConsecutiveRetries;
        this.retryInterval = retryInterval;
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
        dest.writeParcelable(this.uri, flags);
        dest.writeString(this.userName);
        dest.writeString(this.password);
        dest.writeString(this.domain);
        dest.writeInt(this.connectionTimeout);
        dest.writeInt(this.responseTimeout);
        dest.writeInt(this.maxConsecutiveRetries);
        dest.writeInt(this.retryInterval);
    }

    /**
     * Constructor when rebuilding from {@link Parcel}
     * @param in Parcel from which the object should be reconstructed
     */
    private Destination(Parcel in) {
        this.uri = in.readParcelable(Uri.class.getClassLoader());
        this.userName = in.readString();
        this.password = in.readString();
        this.domain = in.readString();
        this.connectionTimeout = in.readInt();
        this.responseTimeout = in.readInt();
        this.maxConsecutiveRetries = in.readInt();
        this.retryInterval = in.readInt();
    }

    /**
     * Destination creator
     */
    public static final Parcelable.Creator<Destination> CREATOR =
            new Parcelable.Creator<Destination>() {

                /**
                 * Create a new instance of the Parcelable class, instantiating it from the given Parcel whose data had previously been written by {@link Destination#writeToParcel(Parcel, int)}
                 * @param in
                 *              The Parcel to read the object's data from.
                 * @return
                 *              Returns a new instance of the Parcelable class.
                 */
                @Override
                public Destination createFromParcel(Parcel in) {
                    return new Destination(in);
                }

                /**
                 * Create a new array of the Parcelable class.
                 * @param size
                 *              Size of the array
                 * @return
                 *              Returns an array of the Parcelable class, with every entry initialized to null.
                 */
                @Override
                public Destination[] newArray(int size) {
                    return new Destination[size];
                }
            };
}

