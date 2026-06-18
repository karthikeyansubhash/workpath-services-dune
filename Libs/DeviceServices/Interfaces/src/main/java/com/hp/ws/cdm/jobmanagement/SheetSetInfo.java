
package com.hp.ws.cdm.jobmanagement;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SheetSetInfo {

    @SerializedName("otherSheetSet")
    @Expose
    private Integer otherSheetSet;
    @SerializedName("sheetSet")
    @Expose
    private List<SheetSet> sheetSet = new ArrayList<SheetSet>();

    public Integer getOtherSheetSet() {
        return otherSheetSet;
    }

    public void setOtherSheetSet(Integer otherSheetSet) {
        this.otherSheetSet = otherSheetSet;
    }

    public List<SheetSet> getSheetSet() {
        return sheetSet;
    }

    public void setSheetSet(List<SheetSet> sheetSet) {
        this.sheetSet = sheetSet;
    }

}
