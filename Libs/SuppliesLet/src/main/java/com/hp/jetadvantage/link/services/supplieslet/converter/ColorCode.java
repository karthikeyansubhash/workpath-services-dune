package com.hp.jetadvantage.link.services.supplieslet.converter;

import com.hp.ext.types.supply.SupplyColorCode;

import java.util.HashMap;
import java.util.Map;

public class ColorCode {
    private static final Map<String, String> colorMap = new HashMap<>();

    static {
        colorMap.put("sccB", "Blue or Sapphire");
        colorMap.put("sccC", "Cyan");
        colorMap.put("sccCMY", "Tri-Color");
        colorMap.put("sccCMYK", "Cyan, Magenta, Yellow, and Black for printheads");
        colorMap.put("sccDG", "Dark Gray");
        colorMap.put("sccE", "Gloss Enhancer");
        colorMap.put("sccG", "Gray");
        colorMap.put("sccGN", "Green");
        colorMap.put("sccK", "Black");
        colorMap.put("sccKCM", "Photo");
        colorMap.put("sccLC", "Light Cyan");
        colorMap.put("sccLG", "Light Gray");
        colorMap.put("sccLM", "Light Magenta");
        colorMap.put("sccM", "Magenta");
        colorMap.put("sccMK", "matte Black");
        colorMap.put("sccO", "Orange");
        colorMap.put("sccOPC", "Organic Photo Conductors");
        colorMap.put("sccP", "Purple");
        colorMap.put("sccPK", "photo black");
        colorMap.put("sccR", "Red");
        colorMap.put("sccS", "Silver");
        colorMap.put("sccV", "Violet");
        colorMap.put("sccW", "White");
        colorMap.put("sccY", "Yellow");
    }

    public static String convert(SupplyColorCode colorCode) {
        return colorMap.getOrDefault(colorCode.getValue(), colorCode.getValue());
    }
}
