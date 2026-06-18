package com.hp.oxpdlib.options;

public enum StampType {

    NONE("None"),
    CONFIDENTIAL("Confidential"),
    CUSTOM("Custom"),
    CUSTOMSTRING1("CustomString1"),
    CUSTOMSTRING2("CustomString2"),
    CUSTOMSTRING3("CustomString3"),
    CUSTOMSTRING4("CustomString4"),
    CUSTOMSTRING5("CustomString5"),
    CUSTOMSTRING6("CustomString6"),
    CUSTOMSTRING7("CustomString7"),
    CUSTOMSTRING8("CustomString8"),
    CUSTOMSTRING9("CustomString9"),
    CUSTOMSTRING10("CustomString10"),
    CUSTOMSTRING11("CustomString11"),
    CUSTOMSTRING12("CustomString12"),
    CUSTOMSTRING13("CustomString13"),
    CUSTOMSTRING14("CustomString14"),
    CUSTOMSTRING15("CustomString15"),
    CUSTOMSTRING16("CustomString16"),
    DATEANDTIME("DateAndTime"),
    DRAFT("Draft"),
    IPADDRESS("IpAddress"),
    PAGENUMBER("PageNumber"),
    PRODUCTINFORMATION("ProductInformation"),
    SECRET("Secret"),
    TOPSECRET("TopSecret"),
    URGENT("Urgent"),
    USERNAME("UserName"),
    OTHER("Other");

    public final String mValue;

    StampType(String v) {
        mValue = v;
    }

    public static StampType fromAttributeValue(String v) {
        for (StampType c: StampType.values()) {
            if (c.mValue.equals(v)) {
                return c;
            }
        }
        return null;
    }

    /**
     * String representation of SheetCollationMode
     *
     */
    @Override
    public String toString() {
        return mValue;
    }
}
