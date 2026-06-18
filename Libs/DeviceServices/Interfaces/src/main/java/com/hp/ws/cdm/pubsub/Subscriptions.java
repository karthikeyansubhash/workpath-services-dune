
package com.hp.ws.cdm.pubsub;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Subscriptions {

    @SerializedName("subscriptions")
    @Expose
    private List<Subscription> subscriptions = new ArrayList<Subscription>();

    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

}
