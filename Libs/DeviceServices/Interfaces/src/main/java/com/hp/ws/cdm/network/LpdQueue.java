
package com.hp.ws.cdm.network;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LpdQueue {

    @SerializedName("appendString")
    @Expose
    private String appendString;
    @SerializedName("prependString")
    @Expose
    private String prependString;
    @SerializedName("queueName")
    @Expose
    private String queueName;
    @SerializedName("queueType")
    @Expose
    private LpdQueue.LpdQueueType queueType;

    public String getAppendString() {
        return appendString;
    }

    public void setAppendString(String appendString) {
        this.appendString = appendString;
    }

    public String getPrependString() {
        return prependString;
    }

    public void setPrependString(String prependString) {
        this.prependString = prependString;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public LpdQueue.LpdQueueType getQueueType() {
        return queueType;
    }

    public void setQueueType(LpdQueue.LpdQueueType queueType) {
        this.queueType = queueType;
    }

    public enum LpdQueueType {

        @SerializedName("raw")
        RAW("raw"),
        @SerializedName("text")
        TEXT("text"),
        @SerializedName("auto")
        AUTO("auto"),
        @SerializedName("binps")
        BINPS("binps");
        private final String value;
        private final static Map<String, LpdQueue.LpdQueueType> CONSTANTS = new HashMap<String, LpdQueue.LpdQueueType>();

        static {
            for (LpdQueue.LpdQueueType c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        LpdQueueType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static LpdQueue.LpdQueueType fromValue(String value) {
            LpdQueue.LpdQueueType constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
