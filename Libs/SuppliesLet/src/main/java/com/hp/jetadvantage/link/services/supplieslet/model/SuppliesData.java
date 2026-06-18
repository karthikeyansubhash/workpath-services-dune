package com.hp.jetadvantage.link.services.supplieslet.model;


import com.hp.ext.service.supplies.SuppliesInfo;

public class SuppliesData {
    private Supplies Supplies;

    public SuppliesData(SuppliesInfo suppliesInfo, String makeAndModel) {
        super();
        this.Supplies = new Supplies(suppliesInfo, makeAndModel);
    }
}
