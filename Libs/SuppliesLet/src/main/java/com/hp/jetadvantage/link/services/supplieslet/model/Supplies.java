package com.hp.jetadvantage.link.services.supplieslet.model;

import com.hp.ext.service.supplies.SuppliesInfo;

public class Supplies {
    private AgentList AgentList;

    public Supplies(SuppliesInfo suppliesInfo, String makeAndModel) {
        super();
        this.AgentList = new AgentList(suppliesInfo, makeAndModel);
    }
}
