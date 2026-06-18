package com.hp.jetadvantage.link.services.supplieslet.model;

import com.hp.ext.types.supply.Supply;
import com.hp.ext.types.supply.SupplyColorCode;
import com.hp.ext.types.supply.SupplyProductNumber;
import com.hp.ext.types.supply.SupplyType;
import com.hp.ext.types.usage.Counter;
import com.hp.ext.types.usage.UnitCounter;
import com.hp.jetadvantage.link.services.supplieslet.converter.ColorCode;
import com.hp.jetadvantage.link.services.supplieslet.converter.Type;

public class Agent {
    private Capacity Capacity;
    private String ConsumableTypeEnum;
    private String MakeAndModel;
    private String ProductNumber;
    private String SerialNumber;
    private String Description;
    private String MarkerColor;
    private String ApproxPercentRemaining;

    public Agent(Supply supply, String makeAndModel) {
        super();
        this.ApproxPercentRemaining = getApproxPercentRemaining(supply.getApproximatePagesRemaining());
        this.Capacity = getCapacity(supply.getCapacity());
        this.ConsumableTypeEnum = getConsumableTypeEnum(supply.getSupplyType());
        this.MakeAndModel = makeAndModel;
        this.Description = supply.getSupplyDescription();
        this.MarkerColor = getMarkerColor(supply.getSupplyColor());
        this.ProductNumber = getProductNumber(supply.getProductNumber());
        this.SerialNumber = supply.getSerialNumber();
    }

    private String getApproxPercentRemaining(Counter counter) {
        if (counter != null && TYPE_GUN_COUNTER.equals(counter.getTypeGUN())) {
            return String.valueOf(counter.getValue());
        }
        return null;
    }

    private Capacity getCapacity(UnitCounter capacity) {
        return new Capacity(capacity);
    }

    private String getConsumableTypeEnum(SupplyType supplyType) {
        if (supplyType != null && TYPE_GUN_SUPPLY_TYPE.equals(supplyType.getTypeGUN())) {
            return Type.convert(supplyType);
        }
        return null;
    }

    private String getMarkerColor(SupplyColorCode colorCode) {
        if (colorCode != null && TYPE_GUN_SUPPLY_COLOR.equals(colorCode.getTypeGUN())) {
            return ColorCode.convert(colorCode);
        }
        return null;
    }

    private String getProductNumber(SupplyProductNumber productNumber) {
        if (productNumber != null && TYPE_GUN_PRODUCT_NUMBER.equals(productNumber.getTypeGUN())) {
            return productNumber.getValue();
        }
        return null;
    }

    private static final String TYPE_GUN_COUNTER = "com.hp.ext.types.usage.version.1.type.counter";
    private static final String TYPE_GUN_SUPPLY_TYPE = "com.hp.ext.types.supply.version.1.type.supplyType";
    private static final String TYPE_GUN_SUPPLY_COLOR = "com.hp.ext.types.supply.version.1.type.supplyColorCode";
    private static final String TYPE_GUN_PRODUCT_NUMBER = "com.hp.ext.types.supply.version.1.type.supplyProductNumber";
}
