
package com.hp.ws.cdm.jobmanagement;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.SerializedName;


/**
 * Supply label code
 * 
 */
public enum ColorCode {

    @SerializedName("B")
    B("B"),
    @SerializedName("C")
    C("C"),
    @SerializedName("CMY")
    CMY("CMY"),
    @SerializedName("CMYK")
    CMYK("CMYK"),
    @SerializedName("dg")
    DG("dg"),
    @SerializedName("E")
    E("E"),
    @SerializedName("F")
    F("F"),
    @SerializedName("G")
    G("G"),
    @SerializedName("GN")
    GN("GN"),
    @SerializedName("K")
    K("K"),
    @SerializedName("KCM")
    KCM("KCM"),
    @SerializedName("lc")
    LC("lc"),
    @SerializedName("lg")
    LG("lg"),
    @SerializedName("lm")
    LM("lm"),
    @SerializedName("M")
    M("M"),
    @SerializedName("mK")
    M_K("mK"),
    @SerializedName("multiColor")
    MULTI_COLOR("multiColor"),
    @SerializedName("O")
    O("O"),
    @SerializedName("OPC")
    OPC("OPC"),
    @SerializedName("P")
    P("P"),
    @SerializedName("pK")
    P_K("pK"),
    @SerializedName("R")
    R("R"),
    @SerializedName("S")
    S("S"),
    @SerializedName("V")
    V("V"),
    @SerializedName("W")
    W("W"),
    @SerializedName("Y")
    Y("Y"),
    @SerializedName("OP")
    OP("OP"),
    @SerializedName("OC")
    OC("OC"),
    @SerializedName("KM")
    KM("KM"),
    @SerializedName("YC")
    YC("YC"),
    @SerializedName("MK")
    MK("MK"),
    @SerializedName("OPOP")
    OPOP("OPOP"),
    @SerializedName("OCOC")
    OCOC("OCOC"),
    @SerializedName("KC")
    KC("KC"),
    @SerializedName("LcLm")
    LC_LM("LcLm"),
    @SerializedName("MY")
    MY("MY"),
    @SerializedName("WW")
    WW("WW");
    private final String value;
    private final static Map<String, ColorCode> CONSTANTS = new HashMap<String, ColorCode>();

    static {
        for (ColorCode c: values()) {
            CONSTANTS.put(c.value, c);
        }
    }

    ColorCode(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    public String value() {
        return this.value;
    }

    public static ColorCode fromValue(String value) {
        ColorCode constant = CONSTANTS.get(value);
        if (constant == null) {
            throw new IllegalArgumentException(value);
        } else {
            return constant;
        }
    }

}
