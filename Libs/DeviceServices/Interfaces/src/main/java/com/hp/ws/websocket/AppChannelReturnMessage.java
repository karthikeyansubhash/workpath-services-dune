package com.hp.ws.websocket;

import com.google.gson.annotations.SerializedName;

/**
 * Application Channel Message Data Type defined by WorkpathInterop in Dune
 * - refer to dune/src/fw/extensibility/workpath/WorkpathInteropTypes/pub/WorkpathAppChannelMessage.h
 */
public class AppChannelReturnMessage {
    @SerializedName("channelMessage")
    ChannelMessage channelMessage;

    public AppChannelReturnMessage() {
        this.channelMessage = new ChannelMessage();
        this.channelMessage.setMessage(this.channelMessage.new Message());
    }

    public AppChannelReturnMessage(String channelId) {
        this.channelMessage = new ChannelMessage();
        this.channelMessage.setChannelId(channelId);
        this.channelMessage.setMessage(this.channelMessage.new Message());
    }

    public ChannelMessage getChannelMessage() {
        return channelMessage;
    }

    public void setChannelMessage(ChannelMessage channelMessage) {
        this.channelMessage = channelMessage;
    }

    public class ChannelMessage {
        @SerializedName("channelId")
        String channelId;
        @SerializedName("message")
        Message message;

        public String getChannelId() {
            return this.channelId;
        }

        public void setChannelId(String channelId) {
            this.channelId = channelId;
        }

        public Message getMessage() {
            return this.message;
        }

        public void setMessage(Message message) {
            this.message = message;
        }

        public class Message {
            @SerializedName("setup")
            AppChannelSetup setup;
            @SerializedName("payload")
            Payload payload;
            @SerializedName("stream")
            Stream stream;
            @SerializedName("service")
            AppChannelServiceResponse serviceResponse;
            @SerializedName("error")
            Error error;

            public AppChannelSetup getSetup() {
                return this.setup;
            }

            public void setSetup(AppChannelSetup setup) {
                this.setup = setup;
            }

            public Payload getPayload() {
                return this.payload;
            }

            public void setPayload(Payload payload) {
                this.payload = payload;
            }

            public Stream getStream() {
                return this.stream;
            }

            public void setStream(Stream stream) {
                this.stream = stream;
            }

            public AppChannelServiceResponse getServiceResponse() {
                return this.serviceResponse;
            }

            public void setServiceResponse(AppChannelServiceResponse serviceResponse) {
                this.serviceResponse = serviceResponse;
            }

            public Error getError() {
                return this.error;
            }

            public void setError(Error error) {
                this.error = error;
            }

            public void setError() {
                this.error = new Error();
            }

            public boolean hasSetup() {
                return (this.setup != null);
            }

            public boolean hasPayload() {
                return (this.payload != null);
            }

            public boolean hasStream() {
                return (this.stream != null);
            }

            public boolean hasServiceResponse() {
                return (this.serviceResponse != null);
            }

            public class Payload {
                @SerializedName("value")
                JsonTypedObject value;

                public JsonTypedObject getValue() {
                    return this.value;
                }

                public void setValue(JsonTypedObject value) {
                    this.value = value;
                }
            }

            public class Stream {

            }

            public class Error {

            }
        }
    }
}
