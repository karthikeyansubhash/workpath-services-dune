package com.hp.jetadvantage.link.services.supplieslet.model;

import com.hp.ext.service.supplies.SuppliesInfo;
import com.hp.ext.types.supply.Supply;

import java.util.ArrayList;
import java.util.List;

public class AgentList {

    private List<Agent> Agent;

    public AgentList(SuppliesInfo suppliesInfo, String makeAndModel) {
        super();
        this.Agent = new ArrayList<Agent>();
        for (Supply supply : suppliesInfo.getSuppliesList()) {
            this.Agent.add(new Agent(supply, makeAndModel));
        }
    }
}
