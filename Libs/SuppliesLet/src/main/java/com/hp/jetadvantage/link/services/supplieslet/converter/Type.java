package com.hp.jetadvantage.link.services.supplieslet.converter;

import com.hp.ext.types.supply.SupplyType;

import java.util.HashMap;
import java.util.Map;

public class Type {
    private static final Map<String, String> typeMap = new HashMap<>();

    static {
        typeMap.put("stAdfPickupRoller", "adfPickupRoller");
        typeMap.put("stDeveloper", "developer");
        typeMap.put("stFuser", "fuser");
        typeMap.put("stImageDrum", "imageDrum");
        typeMap.put("stImageTransferKit", "imageTransferKit");
        typeMap.put("stInkCartridge", "inkCartridge");
        typeMap.put("stInkCollectionUnit", "inkCollectionUnit");
        typeMap.put("stMaintenanceCartridge", "maintenanceCartridge");
        typeMap.put("stPaperTransferBelt", "paperTransferBelt");
        typeMap.put("stPreventativeMaintenanceKit", "preventiveMaintenanceKit");
        typeMap.put("stPrintHead", "printhead");
        typeMap.put("stTonerCartridge", "tonerCartridge");
        typeMap.put("stTonerCollectionUnit", "tonerCollectionUnit");
    }

    public static String convert(SupplyType type) {
        String supplyType = type.getValue();
        if (typeMap.containsKey(supplyType)) {
            return typeMap.get(supplyType);
        } else if (supplyType.startsWith("st")) {
            return supplyType.replaceFirst("st", "");
        }
        return supplyType;
    }
}
