
package com.hp.ws.cdm.certificate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CertificateAttributes {

    @SerializedName("commonName")
    @Expose
    private String commonName;
    @SerializedName("organization")
    @Expose
    private String organization;
    @SerializedName("organizationalUnit")
    @Expose
    private List<String> organizationalUnit = new ArrayList<String>();
    @SerializedName("cityOrLocality")
    @Expose
    private String cityOrLocality;
    @SerializedName("stateOrProvince")
    @Expose
    private String stateOrProvince;
    @SerializedName("countryOrRegion")
    @Expose
    private CertificateAttributes.Country countryOrRegion;

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public List<String> getOrganizationalUnit() {
        return organizationalUnit;
    }

    public void setOrganizationalUnit(List<String> organizationalUnit) {
        this.organizationalUnit = organizationalUnit;
    }

    public String getCityOrLocality() {
        return cityOrLocality;
    }

    public void setCityOrLocality(String cityOrLocality) {
        this.cityOrLocality = cityOrLocality;
    }

    public String getStateOrProvince() {
        return stateOrProvince;
    }

    public void setStateOrProvince(String stateOrProvince) {
        this.stateOrProvince = stateOrProvince;
    }

    public CertificateAttributes.Country getCountryOrRegion() {
        return countryOrRegion;
    }

    public void setCountryOrRegion(CertificateAttributes.Country countryOrRegion) {
        this.countryOrRegion = countryOrRegion;
    }

    public enum Country {

        @SerializedName("AR")
        AR("AR"),
        @SerializedName("AU")
        AU("AU"),
        @SerializedName("AT")
        AT("AT"),
        @SerializedName("BY")
        BY("BY"),
        @SerializedName("BE")
        BE("BE"),
        @SerializedName("BR")
        BR("BR"),
        @SerializedName("BG")
        BG("BG"),
        @SerializedName("CA")
        CA("CA"),
        @SerializedName("CL")
        CL("CL"),
        @SerializedName("CN")
        CN("CN"),
        @SerializedName("HR")
        HR("HR"),
        @SerializedName("CZ")
        CZ("CZ"),
        @SerializedName("DK")
        DK("DK"),
        @SerializedName("EE")
        EE("EE"),
        @SerializedName("FI")
        FI("FI"),
        @SerializedName("FR")
        FR("FR"),
        @SerializedName("DE")
        DE("DE"),
        @SerializedName("GR")
        GR("GR"),
        @SerializedName("HK")
        HK("HK"),
        @SerializedName("HU")
        HU("HU"),
        @SerializedName("IS")
        IS("IS"),
        @SerializedName("IN")
        IN("IN"),
        @SerializedName("ID")
        ID("ID"),
        @SerializedName("IE")
        IE("IE"),
        @SerializedName("IL")
        IL("IL"),
        @SerializedName("IT")
        IT("IT"),
        @SerializedName("JP")
        JP("JP"),
        @SerializedName("LV")
        LV("LV"),
        @SerializedName("LI")
        LI("LI"),
        @SerializedName("LT")
        LT("LT"),
        @SerializedName("LU")
        LU("LU"),
        @SerializedName("MY")
        MY("MY"),
        @SerializedName("MX")
        MX("MX"),
        @SerializedName("MA")
        MA("MA"),
        @SerializedName("NL")
        NL("NL"),
        @SerializedName("NZ")
        NZ("NZ"),
        @SerializedName("NO")
        NO("NO"),
        @SerializedName("PK")
        PK("PK"),
        @SerializedName("PE")
        PE("PE"),
        @SerializedName("PH")
        PH("PH"),
        @SerializedName("PL")
        PL("PL"),
        @SerializedName("PT")
        PT("PT"),
        @SerializedName("RO")
        RO("RO"),
        @SerializedName("RU")
        RU("RU"),
        @SerializedName("SG")
        SG("SG"),
        @SerializedName("SK")
        SK("SK"),
        @SerializedName("SI")
        SI("SI"),
        @SerializedName("ZA")
        ZA("ZA"),
        @SerializedName("KR")
        KR("KR"),
        @SerializedName("ES")
        ES("ES"),
        @SerializedName("LK")
        LK("LK"),
        @SerializedName("SE")
        SE("SE"),
        @SerializedName("CH")
        CH("CH"),
        @SerializedName("TW")
        TW("TW"),
        @SerializedName("TH")
        TH("TH"),
        @SerializedName("TR")
        TR("TR"),
        @SerializedName("UA")
        UA("UA"),
        @SerializedName("GB")
        GB("GB"),
        @SerializedName("US")
        US("US"),
        @SerializedName("VN")
        VN("VN");
        private final String value;
        private final static Map<String, CertificateAttributes.Country> CONSTANTS = new HashMap<String, CertificateAttributes.Country>();

        static {
            for (CertificateAttributes.Country c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        Country(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static CertificateAttributes.Country fromValue(String value) {
            CertificateAttributes.Country constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
