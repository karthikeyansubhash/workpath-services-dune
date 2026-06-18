
package com.hp.ws.cdm.jobmanagement;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UsageByPrintCategory {

    /**
     * specificially for pay per usage application
     * 
     */
    @SerializedName("printCategory")
    @Expose
    private UsageByPrintCategory.PrintCategory printCategory;
    /**
     * counter object with int64 maximum and unit string enum
     * 
     */
    @SerializedName("usedArea")
    @Expose
    private LargeCounter usedArea;
    /**
     * counter object with int64 maximum and unit string enum
     * 
     */
    @SerializedName("imagedArea")
    @Expose
    private LargeCounter imagedArea;
    /**
     * counter object with int64 maximum and unit string enum
     * 
     */
    @SerializedName("printedArea")
    @Expose
    private LargeCounter printedArea;
    /**
     * counter object with int64 maximum and unit string enum
     * 
     */
    @SerializedName("usedLength")
    @Expose
    private LargeCounter usedLength;
    /**
     * counter object with int64 maximum and unit string enum
     * 
     */
    @SerializedName("inkUsage")
    @Expose
    private LargeCounter inkUsage;
    @SerializedName("pageCount")
    @Expose
    private Integer pageCount;

    /**
     * specificially for pay per usage application
     * 
     */
    public UsageByPrintCategory.PrintCategory getPrintCategory() {
        return printCategory;
    }

    /**
     * specificially for pay per usage application
     * 
     */
    public void setPrintCategory(UsageByPrintCategory.PrintCategory printCategory) {
        this.printCategory = printCategory;
    }

    /**
     * counter object with int64 maximum and unit string enum
     * 
     */
    public LargeCounter getUsedArea() {
        return usedArea;
    }

    /**
     * counter object with int64 maximum and unit string enum
     * 
     */
    public void setUsedArea(LargeCounter usedArea) {
        this.usedArea = usedArea;
    }

    /**
     * counter object with int64 maximum and unit string enum
     * 
     */
    public LargeCounter getImagedArea() {
        return imagedArea;
    }

    /**
     * counter object with int64 maximum and unit string enum
     * 
     */
    public void setImagedArea(LargeCounter imagedArea) {
        this.imagedArea = imagedArea;
    }

    /**
     * counter object with int64 maximum and unit string enum
     * 
     */
    public LargeCounter getPrintedArea() {
        return printedArea;
    }

    /**
     * counter object with int64 maximum and unit string enum
     * 
     */
    public void setPrintedArea(LargeCounter printedArea) {
        this.printedArea = printedArea;
    }

    /**
     * counter object with int64 maximum and unit string enum
     * 
     */
    public LargeCounter getUsedLength() {
        return usedLength;
    }

    /**
     * counter object with int64 maximum and unit string enum
     * 
     */
    public void setUsedLength(LargeCounter usedLength) {
        this.usedLength = usedLength;
    }

    /**
     * counter object with int64 maximum and unit string enum
     * 
     */
    public LargeCounter getInkUsage() {
        return inkUsage;
    }

    /**
     * counter object with int64 maximum and unit string enum
     * 
     */
    public void setInkUsage(LargeCounter inkUsage) {
        this.inkUsage = inkUsage;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }


    /**
     * specificially for pay per usage application
     * 
     */
    public enum PrintCategory {

        @SerializedName("blueprint")
        BLUEPRINT("blueprint"),
        @SerializedName("colorGeneral")
        COLOR_GENERAL("colorGeneral"),
        @SerializedName("colorHighDensityImages")
        COLOR_HIGH_DENSITY_IMAGES("colorHighDensityImages"),
        @SerializedName("colorLines")
        COLOR_LINES("colorLines"),
        @SerializedName("colorLowDensityImages")
        COLOR_LOW_DENSITY_IMAGES("colorLowDensityImages"),
        @SerializedName("colorSpecial")
        COLOR_SPECIAL("colorSpecial"),
        @SerializedName("mediaClassBlueprint")
        MEDIA_CLASS_BLUEPRINT("mediaClassBlueprint"),
        @SerializedName("mediaClassGeneral")
        MEDIA_CLASS_GENERAL("mediaClassGeneral"),
        @SerializedName("mediaClassRigid")
        MEDIA_CLASS_RIGID("mediaClassRigid"),
        @SerializedName("mediaClassSpecial")
        MEDIA_CLASS_SPECIAL("mediaClassSpecial"),
        @SerializedName("monoHighDensityImages")
        MONO_HIGH_DENSITY_IMAGES("monoHighDensityImages"),
        @SerializedName("monoLines")
        MONO_LINES("monoLines"),
        @SerializedName("monoLowDensityImages")
        MONO_LOW_DENSITY_IMAGES("monoLowDensityImages"),
        @SerializedName("mpsColor")
        MPS_COLOR("mpsColor"),
        @SerializedName("mpsMono")
        MPS_MONO("mpsMono"),
        @SerializedName("mpsPro")
        MPS_PRO("mpsPro"),
        @SerializedName("rigid")
        RIGID("rigid"),
        @SerializedName("special")
        SPECIAL("special"),
        @SerializedName("whiteGeneral")
        WHITE_GENERAL("whiteGeneral"),
        @SerializedName("whiteSpecial")
        WHITE_SPECIAL("whiteSpecial");
        private final String value;
        private final static Map<String, UsageByPrintCategory.PrintCategory> CONSTANTS = new HashMap<String, UsageByPrintCategory.PrintCategory>();

        static {
            for (UsageByPrintCategory.PrintCategory c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        PrintCategory(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static UsageByPrintCategory.PrintCategory fromValue(String value) {
            UsageByPrintCategory.PrintCategory constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
