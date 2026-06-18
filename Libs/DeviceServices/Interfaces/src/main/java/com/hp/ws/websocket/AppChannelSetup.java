package com.hp.ws.websocket;

import com.google.gson.annotations.SerializedName;

/**
 * Setup message data type for Application Channel Message Data Type defined by WorkpathInterop in Dune
 * - refer to dune/src/fw/extensibility/workpath/WorkpathInteropTypes/pub/WorkpathAppChannelMessage.h
 */
public class AppChannelSetup {
    @SerializedName("details")
    Details details;

    @SerializedName("packageId")
    String packageId;

    public Details getDetails() {
        return details;
    }

    public String getPackageId() {
        return packageId;
    }

    public class Details {

        @SerializedName("payload")
        PayloadDetails payload;

        @SerializedName("stream")
        StreamDetails stream;

        @SerializedName("service")
        ServiceDetails service;

        public PayloadDetails getPayloadDetails() {
            return payload;
        }

        public StreamDetails getStreamDetails() {
            return stream;
        }

        public ServiceDetails getServiceDetails() {
            return service;
        }

        public boolean hasPayloadDetails() {
            return (payload != null);
        }

        public boolean hasStreamDetails() {
            return (stream != null);
        }

        public boolean hasServiceDetails() {
            return (service != null);
        }

        public String getE2ServiceGun() {
            if (hasPayloadDetails()) {
                return payload.getE2ServiceGun();
            } else if (hasStreamDetails()) {
                return stream.getE2ServiceGun();
            } else if (hasServiceDetails()) {
                return service.getE2ServiceGun();
            }
            return null;
        }

        public class PayloadDetails {
            @SerializedName("e2ServiceGun")
            String e2ServiceGun;

            @SerializedName("payloadGun")
            String payloadGun;

            public String getE2ServiceGun() {
                return e2ServiceGun;
            }

            public String getPayloadGun() {
                return payloadGun;
            }
        }

        public class StreamDetails {
            @SerializedName("e2ServiceGun")
            String e2ServiceGun;

            public String getE2ServiceGun() {
                return e2ServiceGun;
            }

        }

        public class ServiceDetails {
            @SerializedName("e2ServiceGun")
            String e2ServiceGun;

            @SerializedName("serviceGun")
            String serviceGun;

            public String getE2ServiceGun() {
                return e2ServiceGun;
            }

            public String getServiceGun() {
                return serviceGun;
            }
        }
    }
}
