
package com.hp.ws.cdm.jobmanagement;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MoveOperation {

    /**
     * The type of move operation to perform.
     * 
     */
    @SerializedName("operationType")
    @Expose
    private MoveOperation.OperationType operationType;
    /**
     * The job identifier of the property that is used with operationType.before and operationType.after
     * 
     */
    @SerializedName("jobId")
    @Expose
    private String jobId;

    /**
     * The type of move operation to perform.
     * 
     */
    public MoveOperation.OperationType getOperationType() {
        return operationType;
    }

    /**
     * The type of move operation to perform.
     * 
     */
    public void setOperationType(MoveOperation.OperationType operationType) {
        this.operationType = operationType;
    }

    /**
     * The job identifier of the property that is used with operationType.before and operationType.after
     * 
     */
    public String getJobId() {
        return jobId;
    }

    /**
     * The job identifier of the property that is used with operationType.before and operationType.after
     * 
     */
    public void setJobId(String jobId) {
        this.jobId = jobId;
    }


    /**
     * The type of move operation to perform.
     * 
     */
    public enum OperationType {

        @SerializedName("promote")
        PROMOTE("promote"),
        @SerializedName("before")
        BEFORE("before"),
        @SerializedName("after")
        AFTER("after");
        private final String value;
        private final static Map<String, MoveOperation.OperationType> CONSTANTS = new HashMap<String, MoveOperation.OperationType>();

        static {
            for (MoveOperation.OperationType c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        OperationType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static MoveOperation.OperationType fromValue(String value) {
            MoveOperation.OperationType constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
