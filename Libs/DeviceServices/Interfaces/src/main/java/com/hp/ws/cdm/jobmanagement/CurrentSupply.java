
package com.hp.ws.cdm.jobmanagement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CurrentSupply {

    @SerializedName("colors")
    @Expose
    private List<ColorCode> colors = new ArrayList<ColorCode>();
    /**
     * for tri-color cartridges only
     * 
     */
    @SerializedName("compositeCounts")
    @Expose
    private CompositeCounts compositeCounts;
    /**
     * counter object with int64 maximum and unit string enum
     * 
     */
    @SerializedName("maintenanceServicingMarkingAgent")
    @Expose
    private LargeCounter maintenanceServicingMarkingAgent;
    /**
     * counter object with int64 maximum and unit string enum
     * 
     */
    @SerializedName("supplyMarkingAgent")
    @Expose
    private LargeCounter supplyMarkingAgent;
    /**
     * Ink supply part number currently installed. Some cartridges (IIC) have a product number, aka. SKU number. (Not all products have this)
     * 
     */
    @SerializedName("productNumber")
    @Expose
    private String productNumber;
    /**
     * number of refills over the lifetime of this supply
     * 
     */
    @SerializedName("refillCount")
    @Expose
    private Integer refillCount;
    /**
     * counter object with count and unit string enum
     * 
     */
    @SerializedName("supplyA4EquivalentImpressionsCount")
    @Expose
    private Counter supplyA4EquivalentImpressionsCount;
    /**
     * Supply label code
     * 
     */
    @SerializedName("supplyColorCode")
    @Expose
    private ColorCode supplyColorCode;
    /**
     * counter object with count and unit string enum
     * 
     */
    @SerializedName("supplyImpressionCount")
    @Expose
    private Counter supplyImpressionCount;
    /**
     * 
     * (Required)
     * 
     */
    @SerializedName("supplySlot")
    @Expose
    private Integer supplySlot;
    /**
     * The supply type - Used by clients to route and/or filter supply events by supply type. Also used as a hint for which optional properties would be present/applicable for the supply type.
     * 
     */
    @SerializedName("supplyType")
    @Expose
    private CurrentSupply.SupplyType supplyType;
    /**
     * Used by inkjet platform to indicate the unique supply ID hex string. The purpose of the supply unique id is to identify a consumable (supply) uniquely, such that SW can offer the user a reward for buying a new genuine hp supply. @http://www.hp.com/schemas/imaging/con/ledm/consumableconfigdyn/2007/11/19#ConsumableUniqueID
     * 
     */
    @SerializedName("supplyUniqueId")
    @Expose
    private String supplyUniqueId;
    /**
     * total time supply has been used - typically used for print heads - measured in seconds
     * 
     */
    @SerializedName("timeUsed")
    @Expose
    private Integer timeUsed;

    public List<ColorCode> getColors() {
        return colors;
    }

    public void setColors(List<ColorCode> colors) {
        this.colors = colors;
    }

    /**
     * for tri-color cartridges only
     * 
     */
    public CompositeCounts getCompositeCounts() {
        return compositeCounts;
    }

    /**
     * for tri-color cartridges only
     * 
     */
    public void setCompositeCounts(CompositeCounts compositeCounts) {
        this.compositeCounts = compositeCounts;
    }

    /**
     * counter object with int64 maximum and unit string enum
     * 
     */
    public LargeCounter getMaintenanceServicingMarkingAgent() {
        return maintenanceServicingMarkingAgent;
    }

    /**
     * counter object with int64 maximum and unit string enum
     * 
     */
    public void setMaintenanceServicingMarkingAgent(LargeCounter maintenanceServicingMarkingAgent) {
        this.maintenanceServicingMarkingAgent = maintenanceServicingMarkingAgent;
    }

    /**
     * counter object with int64 maximum and unit string enum
     * 
     */
    public LargeCounter getSupplyMarkingAgent() {
        return supplyMarkingAgent;
    }

    /**
     * counter object with int64 maximum and unit string enum
     * 
     */
    public void setSupplyMarkingAgent(LargeCounter supplyMarkingAgent) {
        this.supplyMarkingAgent = supplyMarkingAgent;
    }

    /**
     * Ink supply part number currently installed. Some cartridges (IIC) have a product number, aka. SKU number. (Not all products have this)
     * 
     */
    public String getProductNumber() {
        return productNumber;
    }

    /**
     * Ink supply part number currently installed. Some cartridges (IIC) have a product number, aka. SKU number. (Not all products have this)
     * 
     */
    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    /**
     * number of refills over the lifetime of this supply
     * 
     */
    public Integer getRefillCount() {
        return refillCount;
    }

    /**
     * number of refills over the lifetime of this supply
     * 
     */
    public void setRefillCount(Integer refillCount) {
        this.refillCount = refillCount;
    }

    /**
     * counter object with count and unit string enum
     * 
     */
    public Counter getSupplyA4EquivalentImpressionsCount() {
        return supplyA4EquivalentImpressionsCount;
    }

    /**
     * counter object with count and unit string enum
     * 
     */
    public void setSupplyA4EquivalentImpressionsCount(Counter supplyA4EquivalentImpressionsCount) {
        this.supplyA4EquivalentImpressionsCount = supplyA4EquivalentImpressionsCount;
    }

    /**
     * Supply label code
     * 
     */
    public ColorCode getSupplyColorCode() {
        return supplyColorCode;
    }

    /**
     * Supply label code
     * 
     */
    public void setSupplyColorCode(ColorCode supplyColorCode) {
        this.supplyColorCode = supplyColorCode;
    }

    /**
     * counter object with count and unit string enum
     * 
     */
    public Counter getSupplyImpressionCount() {
        return supplyImpressionCount;
    }

    /**
     * counter object with count and unit string enum
     * 
     */
    public void setSupplyImpressionCount(Counter supplyImpressionCount) {
        this.supplyImpressionCount = supplyImpressionCount;
    }

    /**
     * 
     * (Required)
     * 
     */
    public Integer getSupplySlot() {
        return supplySlot;
    }

    /**
     * 
     * (Required)
     * 
     */
    public void setSupplySlot(Integer supplySlot) {
        this.supplySlot = supplySlot;
    }

    /**
     * The supply type - Used by clients to route and/or filter supply events by supply type. Also used as a hint for which optional properties would be present/applicable for the supply type.
     * 
     */
    public CurrentSupply.SupplyType getSupplyType() {
        return supplyType;
    }

    /**
     * The supply type - Used by clients to route and/or filter supply events by supply type. Also used as a hint for which optional properties would be present/applicable for the supply type.
     * 
     */
    public void setSupplyType(CurrentSupply.SupplyType supplyType) {
        this.supplyType = supplyType;
    }

    /**
     * Used by inkjet platform to indicate the unique supply ID hex string. The purpose of the supply unique id is to identify a consumable (supply) uniquely, such that SW can offer the user a reward for buying a new genuine hp supply. @http://www.hp.com/schemas/imaging/con/ledm/consumableconfigdyn/2007/11/19#ConsumableUniqueID
     * 
     */
    public String getSupplyUniqueId() {
        return supplyUniqueId;
    }

    /**
     * Used by inkjet platform to indicate the unique supply ID hex string. The purpose of the supply unique id is to identify a consumable (supply) uniquely, such that SW can offer the user a reward for buying a new genuine hp supply. @http://www.hp.com/schemas/imaging/con/ledm/consumableconfigdyn/2007/11/19#ConsumableUniqueID
     * 
     */
    public void setSupplyUniqueId(String supplyUniqueId) {
        this.supplyUniqueId = supplyUniqueId;
    }

    /**
     * total time supply has been used - typically used for print heads - measured in seconds
     * 
     */
    public Integer getTimeUsed() {
        return timeUsed;
    }

    /**
     * total time supply has been used - typically used for print heads - measured in seconds
     * 
     */
    public void setTimeUsed(Integer timeUsed) {
        this.timeUsed = timeUsed;
    }


    /**
     * The supply type - Used by clients to route and/or filter supply events by supply type. Also used as a hint for which optional properties would be present/applicable for the supply type.
     * 
     */
    public enum SupplyType {

        @SerializedName("adfPickupRoller")
        ADF_PICKUP_ROLLER("adfPickupRoller"),
        @SerializedName("adfReverseRoller")
        ADF_REVERSE_ROLLER("adfReverseRoller"),
        @SerializedName("chadCollector")
        CHAD_COLLECTOR("chadCollector"),
        @SerializedName("developer")
        DEVELOPER("developer"),
        @SerializedName("forwardRoller")
        FORWARD_ROLLER("forwardRoller"),
        @SerializedName("fuser")
        FUSER("fuser"),
        @SerializedName("imageDrum")
        IMAGE_DRUM("imageDrum"),
        @SerializedName("imageTransferBelt")
        IMAGE_TRANSFER_BELT("imageTransferBelt"),
        @SerializedName("imageTransferKit")
        IMAGE_TRANSFER_KIT("imageTransferKit"),
        @SerializedName("inkCartridge")
        INK_CARTRIDGE("inkCartridge"),
        @SerializedName("inkCollectionUnit")
        INK_COLLECTION_UNIT("inkCollectionUnit"),
        @SerializedName("inkGroup")
        INK_GROUP("inkGroup"),
        @SerializedName("inkTank")
        INK_TANK("inkTank"),
        @SerializedName("liquidDispenser")
        LIQUID_DISPENSER("liquidDispenser"),
        @SerializedName("maintenanceCartridge")
        MAINTENANCE_CARTRIDGE("maintenanceCartridge"),
        @SerializedName("mpForwardRoller")
        MP_FORWARD_ROLLER("mpForwardRoller"),
        @SerializedName("mpPickupRoller")
        MP_PICKUP_ROLLER("mpPickupRoller"),
        @SerializedName("paperTransferBelt")
        PAPER_TRANSFER_BELT("paperTransferBelt"),
        @SerializedName("preventativeMaintenanceKit")
        PREVENTATIVE_MAINTENANCE_KIT("preventativeMaintenanceKit"),
        @SerializedName("printHead")
        PRINT_HEAD("printHead"),
        @SerializedName("ptbTcu")
        PTB_TCU("ptbTcu"),
        @SerializedName("rechargeableToner")
        RECHARGEABLE_TONER("rechargeableToner"),
        @SerializedName("separationRoller")
        SEPARATION_ROLLER("separationRoller"),
        @SerializedName("staples")
        STAPLES("staples"),
        @SerializedName("tonerCartridge")
        TONER_CARTRIDGE("tonerCartridge"),
        @SerializedName("tonerCollectionUnit")
        TONER_COLLECTION_UNIT("tonerCollectionUnit"),
        @SerializedName("tonerRefillKit")
        TONER_REFILL_KIT("tonerRefillKit"),
        @SerializedName("transferRoller")
        TRANSFER_ROLLER("transferRoller"),
        @SerializedName("trayPickupRoller")
        TRAY_PICKUP_ROLLER("trayPickupRoller"),
        @SerializedName("wiper")
        WIPER("wiper"),
        @SerializedName("inkMixContainer")
        INK_MIX_CONTAINER("inkMixContainer"),
        @SerializedName("imageTransferBeltCleaner")
        IMAGE_TRANSFER_BELT_CLEANER("imageTransferBeltCleaner");
        private final String value;
        private final static Map<String, CurrentSupply.SupplyType> CONSTANTS = new HashMap<String, CurrentSupply.SupplyType>();

        static {
            for (CurrentSupply.SupplyType c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        SupplyType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static CurrentSupply.SupplyType fromValue(String value) {
            CurrentSupply.SupplyType constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
