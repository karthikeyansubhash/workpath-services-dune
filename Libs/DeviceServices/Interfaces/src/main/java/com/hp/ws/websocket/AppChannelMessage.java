package com.hp.ws.websocket;

import com.google.gson.annotations.SerializedName;

/**
 * Application Channel Message Data Type defined by WorkpathInterop in Dune
 * - refer to dune/src/fw/extensibility/workpath/WorkpathInteropTypes/pub/WorkpathAppChannelMessage.h
 */
public class AppChannelMessage {
    @SerializedName("channelMessage")
    ChannelMessage channelMessage;

    public ChannelMessage getChannelMessage() {
        return channelMessage;
    }

    public class ChannelMessage {
        @SerializedName("channelId")
        String channelId;
        @SerializedName("message")
        Message message;

        public String getChannelId() {
            return channelId;
        }

        public Message getMessage() {
            return message;
        }

        public class Message {
            @SerializedName("setup")
            AppChannelSetup setup;

            @SerializedName("payload")
            Payload payload;

            @SerializedName("stream")
            Stream stream;

            @SerializedName("service")
            AppChannelService service;

            @SerializedName("teardown")
            Teardown teardown;

            public AppChannelSetup getSetup() {
                return setup;
            }

            public Payload getPayload() {
                return payload;
            }

            public Stream getStream() {
                return stream;
            }

            public AppChannelService getService() {
                return service;
            }

            public Teardown getTeardown() {
                return teardown;
            }

            public boolean hasSetup() {
                return (setup != null);
            }

            public boolean hasPayload() {
                return (payload != null);
            }

            public boolean hasStream() {
                return (stream != null);
            }

            public boolean hasService() {
                return (service != null);
            }

            public boolean hasTeardown() {
                return (teardown != null);
            }

            public class Payload {
                @SerializedName("value")
                JsonTypedObject value;

                public JsonTypedObject getValue() {
                    return value;
                }
            }

            public class Stream {

            }

            public class Teardown {
            }
        }
    }
}
