package com.hp.jetadvantage.link.services.supplieslet.model;


import com.hp.ext.types.usage.UnitCounter;

public class Capacity {

    private String MaxCapacity = "0";

    private String Unit;

    public Capacity(UnitCounter capacity) {
        super();
        if (capacity != null) {
            this.MaxCapacity = String.valueOf(capacity.getCount());
            this.Unit = capacity.getUnit().getValue();
        }
    }
}
