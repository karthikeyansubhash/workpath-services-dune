
package com.hp.ws.cdm.pubsub;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.hp.workpath.internal.utils.adapter.JsonDeserializer;

public class Message {

    @SerializedName("gSeqNum")
    @Expose
    private Integer gSeqNum;
    @SerializedName("eTag")
    @Expose
    private String eTag;
    @SerializedName("ifMatch")
    @Expose
    private String ifMatch;
    @SerializedName("ackToken")
    @Expose
    private String ackToken;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("gun")
    @Expose
    private String gun;
    @SerializedName("path")
    @Expose
    private String path;
    @SerializedName("method")
    @Expose
    private Message.Method method;
    /**
     * resource identifier for the item resource of a collection
     * 
     */
    @SerializedName("resourceId")
    @Expose
    private ResourceId resourceId;
    @SerializedName("updateType")
    @Expose
    private Message.UpdateType updateType;
    /**
     * meta data for collection item notification
     * 
     */
    @SerializedName("collectionMetaData")
    @Expose
    private CollectionMetaData collectionMetaData;
    /**
     * Request parameters for baseline notification
     * 
     */
    @SerializedName("queryParams")
    @Expose
    private String queryParams;
    /**
     * identifier of the filter to be applied for this resource
     * 
     */
    @SerializedName("filterId")
    @Expose
    private Integer filterId;
    /**
     * Filter errors
     * 
     */
    @SerializedName("filterError")
    @Expose
    private com.hp.ws.cdm.pubsub.Com_hp_cdm_service_dataValve_version_1_sharedTypes_dataValve_schema.FilterError filterError;
    /**
     * on demand information
     * <p>
     * This schema represents the pubsub on demand desire information
     * 
     */
    @SerializedName("onDemand")
    @Expose
    private OnDemandInfo onDemand;
    @SerializedName("data")
    @Expose
    @JsonAdapter(JsonDeserializer.class)
    private JsonObject data;

    public Integer getgSeqNum() {
        return gSeqNum;
    }

    public void setgSeqNum(Integer gSeqNum) {
        this.gSeqNum = gSeqNum;
    }

    public String geteTag() {
        return eTag;
    }

    public void seteTag(String eTag) {
        this.eTag = eTag;
    }

    public String getIfMatch() {
        return ifMatch;
    }

    public void setIfMatch(String ifMatch) {
        this.ifMatch = ifMatch;
    }

    public String getAckToken() {
        return ackToken;
    }

    public void setAckToken(String ackToken) {
        this.ackToken = ackToken;
    }

    /**
     * 
     * (Required)
     * 
     */
    public String getGun() {
        return gun;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setGun(String gun) {
        this.gun = gun;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Message.Method getMethod() {
        return method;
    }

    public void setMethod(Message.Method method) {
        this.method = method;
    }

    /**
     * resource identifier for the item resource of a collection
     * 
     */
    public ResourceId getResourceId() {
        return resourceId;
    }

    /**
     * resource identifier for the item resource of a collection
     * 
     */
    public void setResourceId(ResourceId resourceId) {
        this.resourceId = resourceId;
    }

    public Message.UpdateType getUpdateType() {
        return updateType;
    }

    public void setUpdateType(Message.UpdateType updateType) {
        this.updateType = updateType;
    }

    /**
     * meta data for collection item notification
     * 
     */
    public CollectionMetaData getCollectionMetaData() {
        return collectionMetaData;
    }

    /**
     * meta data for collection item notification
     * 
     */
    public void setCollectionMetaData(CollectionMetaData collectionMetaData) {
        this.collectionMetaData = collectionMetaData;
    }

    /**
     * Request parameters for baseline notification
     * 
     */
    public String getQueryParams() {
        return queryParams;
    }

    /**
     * Request parameters for baseline notification
     * 
     */
    public void setQueryParams(String queryParams) {
        this.queryParams = queryParams;
    }

    /**
     * identifier of the filter to be applied for this resource
     * 
     */
    public Integer getFilterId() {
        return filterId;
    }

    /**
     * identifier of the filter to be applied for this resource
     * 
     */
    public void setFilterId(Integer filterId) {
        this.filterId = filterId;
    }

    /**
     * Filter errors
     * 
     */
    public com.hp.ws.cdm.pubsub.Com_hp_cdm_service_dataValve_version_1_sharedTypes_dataValve_schema.FilterError getFilterError() {
        return filterError;
    }

    /**
     * Filter errors
     * 
     */
    public void setFilterError(com.hp.ws.cdm.pubsub.Com_hp_cdm_service_dataValve_version_1_sharedTypes_dataValve_schema.FilterError filterError) {
        this.filterError = filterError;
    }

    /**
     * on demand information
     * <p>
     * This schema represents the pubsub on demand desire information
     * 
     */
    public OnDemandInfo getOnDemand() {
        return onDemand;
    }

    /**
     * on demand information
     * <p>
     * This schema represents the pubsub on demand desire information
     * 
     */
    public void setOnDemand(OnDemandInfo onDemand) {
        this.onDemand = onDemand;
    }

    public JsonObject getData() {
        return data;
    }

    public void setData(JsonObject data) {
        this.data = data;
    }

    public enum Method {

        @SerializedName("add")
        ADD("add"),
        @SerializedName("update")
        UPDATE("update"),
        @SerializedName("remove")
        REMOVE("remove"),
        @SerializedName("move")
        MOVE("move"),
        @SerializedName("get")
        GET("get");
        private final String value;
        private final static Map<String, Message.Method> CONSTANTS = new HashMap<String, Message.Method>();

        static {
            for (Message.Method c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        Method(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Message.Method fromValue(String value) {
            Message.Method constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum UpdateType {

        @SerializedName("baseline")
        BASELINE("baseline"),
        @SerializedName("delta")
        DELTA("delta"),
        @SerializedName("error")
        ERROR("error");
        private final String value;
        private final static Map<String, Message.UpdateType> CONSTANTS = new HashMap<String, Message.UpdateType>();

        static {
            for (Message.UpdateType c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        UpdateType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Message.UpdateType fromValue(String value) {
            Message.UpdateType constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
