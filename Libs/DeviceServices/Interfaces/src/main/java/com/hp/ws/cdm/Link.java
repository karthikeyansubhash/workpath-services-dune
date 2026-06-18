
package com.hp.ws.cdm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * link
 * <p>
 * 
 * 
 */
public class Link {

    /**
     * encoding format of the data envelop. A MIME type.
     * 
     */
    @SerializedName("enctype")
    @Expose
    private String enctype;
    /**
     * provides some information on how to manipulate the resource. it shouldn't be used if the resource responds to simple CRUD operations.
     * 
     */
    @SerializedName("hints")
    @Expose
    private List<Hint> hints = new ArrayList<Hint>();
    /**
     * URL to the resource defined by the relation name
     * 
     */
    @SerializedName("href")
    @Expose
    private String href;
    /**
     * contains a URL Template. The variable name (inside curly brackets) must be replaced by the variable value. 
     * 
     */
    @SerializedName("hrefTemplate")
    @Expose
    private String hrefTemplate;
    /**
     * Relation nane. Each service must define (or at least clarify) the semantic definition of each relation.
     * (Required)
     * 
     */
    @SerializedName("rel")
    @Expose
    private String rel;
    /**
     * service may advertize a resource or action even though it isn't available. When this element is ommitted, the implicit state is available.
     * 
     */
    @SerializedName("state")
    @Expose
    private Link.State state = Link.State.fromValue("available");
    /**
     * When the state is unavailable, the service may provide a reason.
     * 
     */
    @SerializedName("stateReason")
    @Expose
    private Link.StateReason stateReason;

    /**
     * encoding format of the data envelop. A MIME type.
     * 
     */
    public String getEnctype() {
        return enctype;
    }

    /**
     * encoding format of the data envelop. A MIME type.
     * 
     */
    public void setEnctype(String enctype) {
        this.enctype = enctype;
    }

    /**
     * provides some information on how to manipulate the resource. it shouldn't be used if the resource responds to simple CRUD operations.
     * 
     */
    public List<Hint> getHints() {
        return hints;
    }

    /**
     * provides some information on how to manipulate the resource. it shouldn't be used if the resource responds to simple CRUD operations.
     * 
     */
    public void setHints(List<Hint> hints) {
        this.hints = hints;
    }

    /**
     * URL to the resource defined by the relation name
     * 
     */
    public String getHref() {
        return href;
    }

    /**
     * URL to the resource defined by the relation name
     * 
     */
    public void setHref(String href) {
        this.href = href;
    }

    /**
     * contains a URL Template. The variable name (inside curly brackets) must be replaced by the variable value. 
     * 
     */
    public String getHrefTemplate() {
        return hrefTemplate;
    }

    /**
     * contains a URL Template. The variable name (inside curly brackets) must be replaced by the variable value. 
     * 
     */
    public void setHrefTemplate(String hrefTemplate) {
        this.hrefTemplate = hrefTemplate;
    }

    /**
     * Relation nane. Each service must define (or at least clarify) the semantic definition of each relation.
     * (Required)
     * 
     */
    public String getRel() {
        return rel;
    }

    /**
     * Relation nane. Each service must define (or at least clarify) the semantic definition of each relation.
     * (Required)
     * 
     */
    public void setRel(String rel) {
        this.rel = rel;
    }

    /**
     * service may advertize a resource or action even though it isn't available. When this element is ommitted, the implicit state is available.
     * 
     */
    public Link.State getState() {
        return state;
    }

    /**
     * service may advertize a resource or action even though it isn't available. When this element is ommitted, the implicit state is available.
     * 
     */
    public void setState(Link.State state) {
        this.state = state;
    }

    /**
     * When the state is unavailable, the service may provide a reason.
     * 
     */
    public Link.StateReason getStateReason() {
        return stateReason;
    }

    /**
     * When the state is unavailable, the service may provide a reason.
     * 
     */
    public void setStateReason(Link.StateReason stateReason) {
        this.stateReason = stateReason;
    }


    /**
     * service may advertize a resource or action even though it isn't available. When this element is ommitted, the implicit state is available.
     * 
     */
    public enum State {

        @SerializedName("available")
        AVAILABLE("available"),
        @SerializedName("unavailable")
        UNAVAILABLE("unavailable");
        private final String value;
        private final static Map<String, Link.State> CONSTANTS = new HashMap<String, Link.State>();

        static {
            for (Link.State c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        State(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Link.State fromValue(String value) {
            Link.State constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * When the state is unavailable, the service may provide a reason.
     * 
     */
    public enum StateReason {

        @SerializedName("noHardware")
        NO_HARDWARE("noHardware"),
        @SerializedName("unlicensed")
        UNLICENSED("unlicensed");
        private final String value;
        private final static Map<String, Link.StateReason> CONSTANTS = new HashMap<String, Link.StateReason>();

        static {
            for (Link.StateReason c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        StateReason(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Link.StateReason fromValue(String value) {
            Link.StateReason constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
