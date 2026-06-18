
package com.hp.ws.cdm.system;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * resource to retrieve the device's identity information
 * 
 */
public class Identity {

    /**
     * country and region codes as per ISO 3166-1 alpha-2
     * 
     */
    @SerializedName("countryRegion")
    @Expose
    private Identity.CountryRegionIso countryRegion;
    /**
     * Provides the derivative specific product number and link directly to that product's software download page at hp.com site.
     * 
     */
    @SerializedName("derivativeNumber")
    @Expose
    private String derivativeNumber;
    /**
     * language code as per ISO 639-1. The country extension should only be used when variants or dialects are supported. This list should only have the subset of languages that are actuall supported
     * 
     */
    @SerializedName("deviceLanguage")
    @Expose
    private com.hp.ws.cdm.controlpanel.Configuration.Language deviceLanguage;
    /**
     * Universally Unique Identifier; GUID
     * 
     */
    @SerializedName("deviceUuid")
    @Expose
    private String deviceUuid;
    /**
     * Represents the bundle compatibility ID for a given product or set of products
     * 
     */
    @SerializedName("firmwareCompatibilityId")
    @Expose
    private String firmwareCompatibilityId;
    /**
     * UTC timestamp of when the build for current firmware was performed
     * 
     */
    @SerializedName("firmwareDateCode")
    @Expose
    private String firmwareDateCode;
    /**
     * the release for firmware flashed on the device
     * 
     */
    @SerializedName("firmwareRelease")
    @Expose
    private String firmwareRelease;
    /**
     * the full revision for firmware flashed on the device
     * 
     */
    @SerializedName("firmwareVersion")
    @Expose
    private String firmwareVersion;
    /**
     * standard date and time format - according to RFC 3339. The date/time when the event was produced (according to the service).  It is of the form YYYY-MM-DD'T':HH:mm:SS.sssZ, for example '2012-04-21T08:15:13.511Z'
     * 
     */
    @SerializedName("installDate")
    @Expose
    private Date installDate;
    /**
     * resource to retrieve the device's model information
     * (Required)
     * 
     */
    @SerializedName("makeAndModel")
    @Expose
    private MakeAndModel makeAndModel;
    /**
     * Basic manufacturing information.
     * 
     */
    @SerializedName("manufacturer")
    @Expose
    private String manufacturer;
    /**
     * It consists of alphanumeric characters with no space between the letters and numbers.Typical examples are '12C', 'f1703', 'N1015v', etc.
     * 
     */
    @SerializedName("productNumber")
    @Expose
    private String productNumber;
    /**
     * UTC timestamp of when the build for current recovery firmware was performed
     * 
     */
    @SerializedName("recoveryFirmwareDateCode")
    @Expose
    private String recoveryFirmwareDateCode;
    /**
     * the release for recovery firmware flashed on the device
     * 
     */
    @SerializedName("recoveryFirmwareRelease")
    @Expose
    private String recoveryFirmwareRelease;
    /**
     * the full revision for recovery firmware flashed on the device
     * 
     */
    @SerializedName("recoveryFirmwareVersion")
    @Expose
    private String recoveryFirmwareVersion;
    /**
     * serial number of the device
     * 
     */
    @SerializedName("serialNumber")
    @Expose
    private String serialNumber;
    /**
     * Born-on Date
     * 
     */
    @SerializedName("serviceId")
    @Expose
    private String serviceId;
    /**
     * This will provide the model number as shown on the front of a product
     * 
     */
    @SerializedName("skuIdentifier")
    @Expose
    private String skuIdentifier;
    @SerializedName("managementProfile")
    @Expose
    private Identity.ManagementProfile managementProfile;
    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * (Required)
     * 
     */
    @SerializedName("version")
    @Expose
    private String version;

    /**
     * country and region codes as per ISO 3166-1 alpha-2
     * 
     */
    public Identity.CountryRegionIso getCountryRegion() {
        return countryRegion;
    }

    /**
     * country and region codes as per ISO 3166-1 alpha-2
     * 
     */
    public void setCountryRegion(Identity.CountryRegionIso countryRegion) {
        this.countryRegion = countryRegion;
    }

    /**
     * Provides the derivative specific product number and link directly to that product's software download page at hp.com site.
     * 
     */
    public String getDerivativeNumber() {
        return derivativeNumber;
    }

    /**
     * Provides the derivative specific product number and link directly to that product's software download page at hp.com site.
     * 
     */
    public void setDerivativeNumber(String derivativeNumber) {
        this.derivativeNumber = derivativeNumber;
    }

    /**
     * language code as per ISO 639-1. The country extension should only be used when variants or dialects are supported. This list should only have the subset of languages that are actuall supported
     * 
     */
    public com.hp.ws.cdm.controlpanel.Configuration.Language getDeviceLanguage() {
        return deviceLanguage;
    }

    /**
     * language code as per ISO 639-1. The country extension should only be used when variants or dialects are supported. This list should only have the subset of languages that are actuall supported
     * 
     */
    public void setDeviceLanguage(com.hp.ws.cdm.controlpanel.Configuration.Language deviceLanguage) {
        this.deviceLanguage = deviceLanguage;
    }

    /**
     * Universally Unique Identifier; GUID
     * 
     */
    public String getDeviceUuid() {
        return deviceUuid;
    }

    /**
     * Universally Unique Identifier; GUID
     * 
     */
    public void setDeviceUuid(String deviceUuid) {
        this.deviceUuid = deviceUuid;
    }

    /**
     * Represents the bundle compatibility ID for a given product or set of products
     * 
     */
    public String getFirmwareCompatibilityId() {
        return firmwareCompatibilityId;
    }

    /**
     * Represents the bundle compatibility ID for a given product or set of products
     * 
     */
    public void setFirmwareCompatibilityId(String firmwareCompatibilityId) {
        this.firmwareCompatibilityId = firmwareCompatibilityId;
    }

    /**
     * UTC timestamp of when the build for current firmware was performed
     * 
     */
    public String getFirmwareDateCode() {
        return firmwareDateCode;
    }

    /**
     * UTC timestamp of when the build for current firmware was performed
     * 
     */
    public void setFirmwareDateCode(String firmwareDateCode) {
        this.firmwareDateCode = firmwareDateCode;
    }

    /**
     * the release for firmware flashed on the device
     * 
     */
    public String getFirmwareRelease() {
        return firmwareRelease;
    }

    /**
     * the release for firmware flashed on the device
     * 
     */
    public void setFirmwareRelease(String firmwareRelease) {
        this.firmwareRelease = firmwareRelease;
    }

    /**
     * the full revision for firmware flashed on the device
     * 
     */
    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    /**
     * the full revision for firmware flashed on the device
     * 
     */
    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    /**
     * standard date and time format - according to RFC 3339. The date/time when the event was produced (according to the service).  It is of the form YYYY-MM-DD'T':HH:mm:SS.sssZ, for example '2012-04-21T08:15:13.511Z'
     * 
     */
    public Date getInstallDate() {
        return installDate;
    }

    /**
     * standard date and time format - according to RFC 3339. The date/time when the event was produced (according to the service).  It is of the form YYYY-MM-DD'T':HH:mm:SS.sssZ, for example '2012-04-21T08:15:13.511Z'
     * 
     */
    public void setInstallDate(Date installDate) {
        this.installDate = installDate;
    }

    /**
     * resource to retrieve the device's model information
     * (Required)
     * 
     */
    public MakeAndModel getMakeAndModel() {
        return makeAndModel;
    }

    /**
     * resource to retrieve the device's model information
     * (Required)
     * 
     */
    public void setMakeAndModel(MakeAndModel makeAndModel) {
        this.makeAndModel = makeAndModel;
    }

    /**
     * Basic manufacturing information.
     * 
     */
    public String getManufacturer() {
        return manufacturer;
    }

    /**
     * Basic manufacturing information.
     * 
     */
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    /**
     * It consists of alphanumeric characters with no space between the letters and numbers.Typical examples are '12C', 'f1703', 'N1015v', etc.
     * 
     */
    public String getProductNumber() {
        return productNumber;
    }

    /**
     * It consists of alphanumeric characters with no space between the letters and numbers.Typical examples are '12C', 'f1703', 'N1015v', etc.
     * 
     */
    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    /**
     * UTC timestamp of when the build for current recovery firmware was performed
     * 
     */
    public String getRecoveryFirmwareDateCode() {
        return recoveryFirmwareDateCode;
    }

    /**
     * UTC timestamp of when the build for current recovery firmware was performed
     * 
     */
    public void setRecoveryFirmwareDateCode(String recoveryFirmwareDateCode) {
        this.recoveryFirmwareDateCode = recoveryFirmwareDateCode;
    }

    /**
     * the release for recovery firmware flashed on the device
     * 
     */
    public String getRecoveryFirmwareRelease() {
        return recoveryFirmwareRelease;
    }

    /**
     * the release for recovery firmware flashed on the device
     * 
     */
    public void setRecoveryFirmwareRelease(String recoveryFirmwareRelease) {
        this.recoveryFirmwareRelease = recoveryFirmwareRelease;
    }

    /**
     * the full revision for recovery firmware flashed on the device
     * 
     */
    public String getRecoveryFirmwareVersion() {
        return recoveryFirmwareVersion;
    }

    /**
     * the full revision for recovery firmware flashed on the device
     * 
     */
    public void setRecoveryFirmwareVersion(String recoveryFirmwareVersion) {
        this.recoveryFirmwareVersion = recoveryFirmwareVersion;
    }

    /**
     * serial number of the device
     * 
     */
    public String getSerialNumber() {
        return serialNumber;
    }

    /**
     * serial number of the device
     * 
     */
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    /**
     * Born-on Date
     * 
     */
    public String getServiceId() {
        return serviceId;
    }

    /**
     * Born-on Date
     * 
     */
    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    /**
     * This will provide the model number as shown on the front of a product
     * 
     */
    public String getSkuIdentifier() {
        return skuIdentifier;
    }

    /**
     * This will provide the model number as shown on the front of a product
     * 
     */
    public void setSkuIdentifier(String skuIdentifier) {
        this.skuIdentifier = skuIdentifier;
    }

    public Identity.ManagementProfile getManagementProfile() {
        return managementProfile;
    }

    public void setManagementProfile(Identity.ManagementProfile managementProfile) {
        this.managementProfile = managementProfile;
    }

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * (Required)
     * 
     */
    public String getVersion() {
        return version;
    }

    /**
     * Common Version
     * <p>
     * Conforms to the version scheme defined for all APIs
     * (Required)
     * 
     */
    public void setVersion(String version) {
        this.version = version;
    }


    /**
     * country and region codes as per ISO 3166-1 alpha-2
     * 
     */
    public enum CountryRegionIso {

        @SerializedName("AD")
        AD("AD"),
        @SerializedName("AE")
        AE("AE"),
        @SerializedName("AF")
        AF("AF"),
        @SerializedName("AG")
        AG("AG"),
        @SerializedName("AI")
        AI("AI"),
        @SerializedName("AL")
        AL("AL"),
        @SerializedName("AM")
        AM("AM"),
        @SerializedName("AO")
        AO("AO"),
        @SerializedName("AQ")
        AQ("AQ"),
        @SerializedName("AR")
        AR("AR"),
        @SerializedName("AS")
        AS("AS"),
        @SerializedName("AT")
        AT("AT"),
        @SerializedName("AU")
        AU("AU"),
        @SerializedName("AW")
        AW("AW"),
        @SerializedName("AZ")
        AZ("AZ"),
        @SerializedName("BA")
        BA("BA"),
        @SerializedName("BB")
        BB("BB"),
        @SerializedName("BD")
        BD("BD"),
        @SerializedName("BE")
        BE("BE"),
        @SerializedName("BF")
        BF("BF"),
        @SerializedName("BG")
        BG("BG"),
        @SerializedName("BH")
        BH("BH"),
        @SerializedName("BI")
        BI("BI"),
        @SerializedName("BJ")
        BJ("BJ"),
        @SerializedName("BL")
        BL("BL"),
        @SerializedName("BM")
        BM("BM"),
        @SerializedName("BN")
        BN("BN"),
        @SerializedName("BO")
        BO("BO"),
        @SerializedName("BQ")
        BQ("BQ"),
        @SerializedName("BR")
        BR("BR"),
        @SerializedName("BS")
        BS("BS"),
        @SerializedName("BT")
        BT("BT"),
        @SerializedName("BW")
        BW("BW"),
        @SerializedName("BY")
        BY("BY"),
        @SerializedName("BZ")
        BZ("BZ"),
        @SerializedName("CA")
        CA("CA"),
        @SerializedName("CC")
        CC("CC"),
        @SerializedName("CD")
        CD("CD"),
        @SerializedName("CF")
        CF("CF"),
        @SerializedName("CG")
        CG("CG"),
        @SerializedName("CH")
        CH("CH"),
        @SerializedName("CI")
        CI("CI"),
        @SerializedName("CK")
        CK("CK"),
        @SerializedName("CL")
        CL("CL"),
        @SerializedName("CM")
        CM("CM"),
        @SerializedName("CN")
        CN("CN"),
        @SerializedName("CO")
        CO("CO"),
        @SerializedName("CR")
        CR("CR"),
        @SerializedName("CU")
        CU("CU"),
        @SerializedName("CV")
        CV("CV"),
        @SerializedName("CW")
        CW("CW"),
        @SerializedName("CX")
        CX("CX"),
        @SerializedName("CY")
        CY("CY"),
        @SerializedName("CZ")
        CZ("CZ"),
        @SerializedName("DE")
        DE("DE"),
        @SerializedName("DJ")
        DJ("DJ"),
        @SerializedName("DK")
        DK("DK"),
        @SerializedName("DM")
        DM("DM"),
        @SerializedName("DO")
        DO("DO"),
        @SerializedName("DZ")
        DZ("DZ"),
        @SerializedName("EC")
        EC("EC"),
        @SerializedName("EE")
        EE("EE"),
        @SerializedName("EG")
        EG("EG"),
        @SerializedName("EH")
        EH("EH"),
        @SerializedName("ER")
        ER("ER"),
        @SerializedName("ES")
        ES("ES"),
        @SerializedName("ET")
        ET("ET"),
        @SerializedName("FI")
        FI("FI"),
        @SerializedName("FJ")
        FJ("FJ"),
        @SerializedName("FK")
        FK("FK"),
        @SerializedName("FM")
        FM("FM"),
        @SerializedName("FO")
        FO("FO"),
        @SerializedName("FR")
        FR("FR"),
        @SerializedName("GA")
        GA("GA"),
        @SerializedName("GB")
        GB("GB"),
        @SerializedName("GD")
        GD("GD"),
        @SerializedName("GE")
        GE("GE"),
        @SerializedName("GF")
        GF("GF"),
        @SerializedName("GG")
        GG("GG"),
        @SerializedName("GH")
        GH("GH"),
        @SerializedName("GI")
        GI("GI"),
        @SerializedName("GL")
        GL("GL"),
        @SerializedName("GM")
        GM("GM"),
        @SerializedName("GN")
        GN("GN"),
        @SerializedName("GP")
        GP("GP"),
        @SerializedName("GQ")
        GQ("GQ"),
        @SerializedName("GR")
        GR("GR"),
        @SerializedName("GT")
        GT("GT"),
        @SerializedName("GU")
        GU("GU"),
        @SerializedName("GW")
        GW("GW"),
        @SerializedName("GY")
        GY("GY"),
        @SerializedName("HK")
        HK("HK"),
        @SerializedName("HN")
        HN("HN"),
        @SerializedName("HR")
        HR("HR"),
        @SerializedName("HT")
        HT("HT"),
        @SerializedName("HU")
        HU("HU"),
        @SerializedName("ID")
        ID("ID"),
        @SerializedName("IE")
        IE("IE"),
        @SerializedName("IL")
        IL("IL"),
        @SerializedName("IM")
        IM("IM"),
        @SerializedName("IN")
        IN("IN"),
        @SerializedName("IO")
        IO("IO"),
        @SerializedName("IQ")
        IQ("IQ"),
        @SerializedName("IS")
        IS("IS"),
        @SerializedName("IT")
        IT("IT"),
        @SerializedName("JE")
        JE("JE"),
        @SerializedName("JM")
        JM("JM"),
        @SerializedName("JO")
        JO("JO"),
        @SerializedName("JP")
        JP("JP"),
        @SerializedName("KE")
        KE("KE"),
        @SerializedName("KG")
        KG("KG"),
        @SerializedName("KH")
        KH("KH"),
        @SerializedName("KI")
        KI("KI"),
        @SerializedName("KM")
        KM("KM"),
        @SerializedName("KN")
        KN("KN"),
        @SerializedName("KP")
        KP("KP"),
        @SerializedName("KR")
        KR("KR"),
        @SerializedName("KW")
        KW("KW"),
        @SerializedName("KY")
        KY("KY"),
        @SerializedName("KZ")
        KZ("KZ"),
        @SerializedName("LA")
        LA("LA"),
        @SerializedName("LB")
        LB("LB"),
        @SerializedName("LC")
        LC("LC"),
        @SerializedName("LI")
        LI("LI"),
        @SerializedName("LK")
        LK("LK"),
        @SerializedName("LR")
        LR("LR"),
        @SerializedName("LS")
        LS("LS"),
        @SerializedName("LT")
        LT("LT"),
        @SerializedName("LU")
        LU("LU"),
        @SerializedName("LV")
        LV("LV"),
        @SerializedName("LY")
        LY("LY"),
        @SerializedName("MA")
        MA("MA"),
        @SerializedName("MC")
        MC("MC"),
        @SerializedName("MD")
        MD("MD"),
        @SerializedName("ME")
        ME("ME"),
        @SerializedName("MF")
        MF("MF"),
        @SerializedName("MG")
        MG("MG"),
        @SerializedName("MH")
        MH("MH"),
        @SerializedName("MK")
        MK("MK"),
        @SerializedName("ML")
        ML("ML"),
        @SerializedName("MM")
        MM("MM"),
        @SerializedName("MN")
        MN("MN"),
        @SerializedName("MO")
        MO("MO"),
        @SerializedName("MP")
        MP("MP"),
        @SerializedName("MQ")
        MQ("MQ"),
        @SerializedName("MR")
        MR("MR"),
        @SerializedName("MS")
        MS("MS"),
        @SerializedName("MT")
        MT("MT"),
        @SerializedName("MU")
        MU("MU"),
        @SerializedName("MV")
        MV("MV"),
        @SerializedName("MW")
        MW("MW"),
        @SerializedName("MX")
        MX("MX"),
        @SerializedName("MY")
        MY("MY"),
        @SerializedName("MZ")
        MZ("MZ"),
        @SerializedName("NA")
        NA("NA"),
        @SerializedName("NC")
        NC("NC"),
        @SerializedName("NE")
        NE("NE"),
        @SerializedName("NF")
        NF("NF"),
        @SerializedName("NG")
        NG("NG"),
        @SerializedName("NI")
        NI("NI"),
        @SerializedName("NL")
        NL("NL"),
        @SerializedName("NO")
        NO("NO"),
        @SerializedName("NP")
        NP("NP"),
        @SerializedName("NR")
        NR("NR"),
        @SerializedName("NU")
        NU("NU"),
        @SerializedName("NZ")
        NZ("NZ"),
        @SerializedName("OM")
        OM("OM"),
        @SerializedName("PA")
        PA("PA"),
        @SerializedName("PE")
        PE("PE"),
        @SerializedName("PF")
        PF("PF"),
        @SerializedName("PG")
        PG("PG"),
        @SerializedName("PH")
        PH("PH"),
        @SerializedName("PK")
        PK("PK"),
        @SerializedName("PL")
        PL("PL"),
        @SerializedName("PM")
        PM("PM"),
        @SerializedName("PN")
        PN("PN"),
        @SerializedName("PR")
        PR("PR"),
        @SerializedName("PS")
        PS("PS"),
        @SerializedName("PT")
        PT("PT"),
        @SerializedName("PW")
        PW("PW"),
        @SerializedName("PY")
        PY("PY"),
        @SerializedName("QA")
        QA("QA"),
        @SerializedName("RE")
        RE("RE"),
        @SerializedName("RO")
        RO("RO"),
        @SerializedName("RS")
        RS("RS"),
        @SerializedName("RU")
        RU("RU"),
        @SerializedName("RW")
        RW("RW"),
        @SerializedName("SA")
        SA("SA"),
        @SerializedName("SB")
        SB("SB"),
        @SerializedName("SC")
        SC("SC"),
        @SerializedName("SE")
        SE("SE"),
        @SerializedName("SG")
        SG("SG"),
        @SerializedName("SH")
        SH("SH"),
        @SerializedName("SI")
        SI("SI"),
        @SerializedName("SJ")
        SJ("SJ"),
        @SerializedName("SK")
        SK("SK"),
        @SerializedName("SL")
        SL("SL"),
        @SerializedName("SM")
        SM("SM"),
        @SerializedName("SN")
        SN("SN"),
        @SerializedName("SO")
        SO("SO"),
        @SerializedName("SR")
        SR("SR"),
        @SerializedName("SS")
        SS("SS"),
        @SerializedName("ST")
        ST("ST"),
        @SerializedName("SV")
        SV("SV"),
        @SerializedName("SX")
        SX("SX"),
        @SerializedName("SZ")
        SZ("SZ"),
        @SerializedName("TC")
        TC("TC"),
        @SerializedName("TD")
        TD("TD"),
        @SerializedName("TF")
        TF("TF"),
        @SerializedName("TG")
        TG("TG"),
        @SerializedName("TH")
        TH("TH"),
        @SerializedName("TJ")
        TJ("TJ"),
        @SerializedName("TK")
        TK("TK"),
        @SerializedName("TL")
        TL("TL"),
        @SerializedName("TM")
        TM("TM"),
        @SerializedName("TN")
        TN("TN"),
        @SerializedName("TO")
        TO("TO"),
        @SerializedName("TR")
        TR("TR"),
        @SerializedName("TT")
        TT("TT"),
        @SerializedName("TV")
        TV("TV"),
        @SerializedName("TW")
        TW("TW"),
        @SerializedName("TZ")
        TZ("TZ"),
        @SerializedName("UA")
        UA("UA"),
        @SerializedName("UG")
        UG("UG"),
        @SerializedName("UM")
        UM("UM"),
        @SerializedName("US")
        US("US"),
        @SerializedName("UY")
        UY("UY"),
        @SerializedName("UZ")
        UZ("UZ"),
        @SerializedName("VA")
        VA("VA"),
        @SerializedName("VC")
        VC("VC"),
        @SerializedName("VE")
        VE("VE"),
        @SerializedName("VG")
        VG("VG"),
        @SerializedName("VI")
        VI("VI"),
        @SerializedName("VN")
        VN("VN"),
        @SerializedName("VU")
        VU("VU"),
        @SerializedName("WF")
        WF("WF"),
        @SerializedName("WS")
        WS("WS"),
        @SerializedName("XK")
        XK("XK"),
        @SerializedName("YE")
        YE("YE"),
        @SerializedName("YT")
        YT("YT"),
        @SerializedName("ZA")
        ZA("ZA"),
        @SerializedName("ZM")
        ZM("ZM"),
        @SerializedName("ZW")
        ZW("ZW"),
        @SerializedName("ZZ")
        ZZ("ZZ");
        private final String value;
        private final static Map<String, Identity.CountryRegionIso> CONSTANTS = new HashMap<String, Identity.CountryRegionIso>();

        static {
            for (Identity.CountryRegionIso c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        CountryRegionIso(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Identity.CountryRegionIso fromValue(String value) {
            Identity.CountryRegionIso constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

    public enum ManagementProfile {

        @SerializedName("mpInkConsumer")
        MP_INK_CONSUMER("mpInkConsumer"),
        @SerializedName("mpInkDistributed")
        MP_INK_DISTRIBUTED("mpInkDistributed"),
        @SerializedName("mpInkProduction")
        MP_INK_PRODUCTION("mpInkProduction"),
        @SerializedName("mpLaserConsumer")
        MP_LASER_CONSUMER("mpLaserConsumer"),
        @SerializedName("mpLaserDistributed")
        MP_LASER_DISTRIBUTED("mpLaserDistributed"),
        @SerializedName("mpLaserEnterprise")
        MP_LASER_ENTERPRISE("mpLaserEnterprise"),
        @SerializedName("mpScanEnterprise")
        MP_SCAN_ENTERPRISE("mpScanEnterprise"),
        @SerializedName("mpOther")
        MP_OTHER("mpOther");
        private final String value;
        private final static Map<String, Identity.ManagementProfile> CONSTANTS = new HashMap<String, Identity.ManagementProfile>();

        static {
            for (Identity.ManagementProfile c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        ManagementProfile(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static Identity.ManagementProfile fromValue(String value) {
            Identity.ManagementProfile constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
