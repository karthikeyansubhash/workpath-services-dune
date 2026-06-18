
package com.hp.ws.cdm.diagnostic;

import java.util.HashMap;
import java.util.Map;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/**
 * Describes a test
 * 
 */
public class TestInformation {

    /**
     * Enumeration to identify the category of the test
     * (Required)
     * 
     */
    @SerializedName("category")
    @Expose
    private TestInformation.Category category;
    /**
     * Unsigned integer to support several tests of same type / subsystem / category
     * 
     */
    @SerializedName("index")
    @Expose
    private Long index;
    /**
     * Enumeration to identify which subsystem is testing or refers to
     * (Required)
     * 
     */
    @SerializedName("subsystem")
    @Expose
    private TestInformation.Subsystem subsystem;
    /**
     * Boolean indicating whether the tests supports a finished event.
     * (Required)
     * 
     */
    @SerializedName("supportsFinishedEvent")
    @Expose
    private Boolean supportsFinishedEvent;
    /**
     * Optional textual information in English
     * 
     */
    @SerializedName("testDescription")
    @Expose
    private String testDescription;
    /**
     * Unique unsigned integer that identifies a test
     * (Required)
     * 
     */
    @SerializedName("testIdentification")
    @Expose
    private Long testIdentification;
    /**
     * Enumeration to identify if the test is a diagnostic or a service test. Supports undefined.
     * (Required)
     * 
     */
    @SerializedName("testType")
    @Expose
    private TestInformation.TestType testType;

    /**
     * Enumeration to identify the category of the test
     * (Required)
     * 
     */
    public TestInformation.Category getCategory() {
        return category;
    }

    /**
     * Enumeration to identify the category of the test
     * (Required)
     * 
     */
    public void setCategory(TestInformation.Category category) {
        this.category = category;
    }

    /**
     * Unsigned integer to support several tests of same type / subsystem / category
     * 
     */
    public Long getIndex() {
        return index;
    }

    /**
     * Unsigned integer to support several tests of same type / subsystem / category
     * 
     */
    public void setIndex(Long index) {
        this.index = index;
    }

    /**
     * Enumeration to identify which subsystem is testing or refers to
     * (Required)
     * 
     */
    public TestInformation.Subsystem getSubsystem() {
        return subsystem;
    }

    /**
     * Enumeration to identify which subsystem is testing or refers to
     * (Required)
     * 
     */
    public void setSubsystem(TestInformation.Subsystem subsystem) {
        this.subsystem = subsystem;
    }

    /**
     * Boolean indicating whether the tests supports a finished event.
     * (Required)
     * 
     */
    public Boolean getSupportsFinishedEvent() {
        return supportsFinishedEvent;
    }

    /**
     * Boolean indicating whether the tests supports a finished event.
     * (Required)
     * 
     */
    public void setSupportsFinishedEvent(Boolean supportsFinishedEvent) {
        this.supportsFinishedEvent = supportsFinishedEvent;
    }

    /**
     * Optional textual information in English
     * 
     */
    public String getTestDescription() {
        return testDescription;
    }

    /**
     * Optional textual information in English
     * 
     */
    public void setTestDescription(String testDescription) {
        this.testDescription = testDescription;
    }

    /**
     * Unique unsigned integer that identifies a test
     * (Required)
     * 
     */
    public Long getTestIdentification() {
        return testIdentification;
    }

    /**
     * Unique unsigned integer that identifies a test
     * (Required)
     * 
     */
    public void setTestIdentification(Long testIdentification) {
        this.testIdentification = testIdentification;
    }

    /**
     * Enumeration to identify if the test is a diagnostic or a service test. Supports undefined.
     * (Required)
     * 
     */
    public TestInformation.TestType getTestType() {
        return testType;
    }

    /**
     * Enumeration to identify if the test is a diagnostic or a service test. Supports undefined.
     * (Required)
     * 
     */
    public void setTestType(TestInformation.TestType testType) {
        this.testType = testType;
    }


    /**
     * Enumeration to identify the category of the test
     * 
     */
    public enum Category {

        @SerializedName("undefined")
        UNDEFINED("undefined"),
        @SerializedName("motor")
        MOTOR("motor"),
        @SerializedName("sensor")
        SENSOR("sensor"),
        @SerializedName("settings")
        SETTINGS("settings"),
        @SerializedName("status")
        STATUS("status"),
        @SerializedName("printhead")
        PRINTHEAD("printhead"),
        @SerializedName("printerCalibrationPlot")
        PRINTER_CALIBRATION_PLOT("printerCalibrationPlot"),
        @SerializedName("dropDetection")
        DROP_DETECTION("dropDetection"),
        @SerializedName("inkSystem")
        INK_SYSTEM("inkSystem"),
        @SerializedName("carriage")
        CARRIAGE("carriage"),
        @SerializedName("primer")
        PRIMER("primer"),
        @SerializedName("substratePath")
        SUBSTRATE_PATH("substratePath"),
        @SerializedName("phHealthMenu")
        PH_HEALTH_MENU("phHealthMenu"),
        @SerializedName("systemSettings")
        SYSTEM_SETTINGS("systemSettings"),
        @SerializedName("bootmode")
        BOOTMODE("bootmode"),
        @SerializedName("dropDetectionAndDatabase")
        DROP_DETECTION_AND_DATABASE("dropDetectionAndDatabase"),
        @SerializedName("resetLifeCounterMenu")
        RESET_LIFE_COUNTER_MENU("resetLifeCounterMenu"),
        @SerializedName("biosFormatter")
        BIOS_FORMATTER("biosFormatter"),
        @SerializedName("ebox")
        EBOX("ebox"),
        @SerializedName("system")
        SYSTEM("system"),
        @SerializedName("mediaPath")
        MEDIA_PATH("mediaPath"),
        @SerializedName("paperPresetUtilities")
        PAPER_PRESET_UTILITIES("paperPresetUtilities"),
        @SerializedName("accesoryUtilities")
        ACCESORY_UTILITIES("accesoryUtilities"),
        @SerializedName("hwUtilities")
        HW_UTILITIES("hwUtilities"),
        @SerializedName("electricalBox")
        ELECTRICAL_BOX("electricalBox"),
        @SerializedName("powerManagement")
        POWER_MANAGEMENT("powerManagement"),
        @SerializedName("pipeline")
        PIPELINE("pipeline"),
        @SerializedName("curing")
        CURING("curing"),
        @SerializedName("drying")
        DRYING("drying"),
        @SerializedName("serviceStation")
        SERVICE_STATION("serviceStation"),
        @SerializedName("inkSupplyStation")
        INK_SUPPLY_STATION("inkSupplyStation"),
        @SerializedName("printheads")
        PRINTHEADS("printheads"),
        @SerializedName("substrateVacuum")
        SUBSTRATE_VACUUM("substrateVacuum"),
        @SerializedName("printerId")
        PRINTER_ID("printerId"),
        @SerializedName("waterDispenser")
        WATER_DISPENSER("waterDispenser"),
        @SerializedName("carriageLineSensor")
        CARRIAGE_LINE_SENSOR("carriageLineSensor"),
        @SerializedName("userInterface")
        USER_INTERFACE("userInterface"),
        @SerializedName("scanAxis")
        SCAN_AXIS("scanAxis"),
        @SerializedName("diskUtilities")
        DISK_UTILITIES("diskUtilities"),
        @SerializedName("serviceUtilities")
        SERVICE_UTILITIES("serviceUtilities"),
        @SerializedName("calibrations")
        CALIBRATIONS("calibrations"),
        @SerializedName("maintenance")
        MAINTENANCE("maintenance"),
        @SerializedName("cutter")
        CUTTER("cutter"),
        @SerializedName("scan")
        SCAN("scan"),
        @SerializedName("folder")
        FOLDER("folder"),
        @SerializedName("integratedStacker")
        INTEGRATED_STACKER("integratedStacker"),
        @SerializedName("drawers")
        DRAWERS("drawers");
        private final String value;
        private final static Map<String, TestInformation.Category> CONSTANTS = new HashMap<String, TestInformation.Category>();

        static {
            for (TestInformation.Category c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        Category(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static TestInformation.Category fromValue(String value) {
            TestInformation.Category constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * Enumeration to identify which subsystem is testing or refers to
     * 
     */
    public enum Subsystem {

        @SerializedName("undefined")
        UNDEFINED("undefined"),
        @SerializedName("print")
        PRINT("print"),
        @SerializedName("calibrations")
        CALIBRATIONS("calibrations"),
        @SerializedName("cartridges")
        CARTRIDGES("cartridges"),
        @SerializedName("maintenance")
        MAINTENANCE("maintenance"),
        @SerializedName("mech")
        MECH("mech"),
        @SerializedName("media")
        MEDIA("media"),
        @SerializedName("scan")
        SCAN("scan"),
        @SerializedName("eBox")
        E_BOX("eBox"),
        @SerializedName("powerManagement")
        POWER_MANAGEMENT("powerManagement"),
        @SerializedName("pipeline")
        PIPELINE("pipeline"),
        @SerializedName("curing")
        CURING("curing"),
        @SerializedName("drying")
        DRYING("drying"),
        @SerializedName("serviceStation")
        SERVICE_STATION("serviceStation"),
        @SerializedName("inkSupplyStation")
        INK_SUPPLY_STATION("inkSupplyStation"),
        @SerializedName("printheads")
        PRINTHEADS("printheads"),
        @SerializedName("substrateVacuum")
        SUBSTRATE_VACUUM("substrateVacuum"),
        @SerializedName("biosFormatter")
        BIOS_FORMATTER("biosFormatter"),
        @SerializedName("printerId")
        PRINTER_ID("printerId"),
        @SerializedName("waterDispenser")
        WATER_DISPENSER("waterDispenser"),
        @SerializedName("carriageLineSensor")
        CARRIAGE_LINE_SENSOR("carriageLineSensor"),
        @SerializedName("substratePath")
        SUBSTRATE_PATH("substratePath"),
        @SerializedName("userInterface")
        USER_INTERFACE("userInterface"),
        @SerializedName("scanAxis")
        SCAN_AXIS("scanAxis"),
        @SerializedName("printheadPrimers")
        PRINTHEAD_PRIMERS("printheadPrimers"),
        @SerializedName("system")
        SYSTEM("system"),
        @SerializedName("diskUtilities")
        DISK_UTILITIES("diskUtilities"),
        @SerializedName("serviceUtilities")
        SERVICE_UTILITIES("serviceUtilities"),
        @SerializedName("hwUtilities")
        HW_UTILITIES("hwUtilities"),
        @SerializedName("mediaPath")
        MEDIA_PATH("mediaPath"),
        @SerializedName("cutter")
        CUTTER("cutter"),
        @SerializedName("integratedStacker")
        INTEGRATED_STACKER("integratedStacker"),
        @SerializedName("folder")
        FOLDER("folder"),
        @SerializedName("drawers")
        DRAWERS("drawers");
        private final String value;
        private final static Map<String, TestInformation.Subsystem> CONSTANTS = new HashMap<String, TestInformation.Subsystem>();

        static {
            for (TestInformation.Subsystem c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        Subsystem(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static TestInformation.Subsystem fromValue(String value) {
            TestInformation.Subsystem constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }


    /**
     * Enumeration to identify if the test is a diagnostic or a service test. Supports undefined.
     * 
     */
    public enum TestType {

        @SerializedName("undefined")
        UNDEFINED("undefined"),
        @SerializedName("service")
        SERVICE("service"),
        @SerializedName("diagnostics")
        DIAGNOSTICS("diagnostics"),
        @SerializedName("userMaintenance")
        USER_MAINTENANCE("userMaintenance"),
        @SerializedName("supportMenu")
        SUPPORT_MENU("supportMenu"),
        @SerializedName("customerDiagnostics")
        CUSTOMER_DIAGNOSTICS("customerDiagnostics");
        private final String value;
        private final static Map<String, TestInformation.TestType> CONSTANTS = new HashMap<String, TestInformation.TestType>();

        static {
            for (TestInformation.TestType c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        TestType(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.value;
        }

        public String value() {
            return this.value;
        }

        public static TestInformation.TestType fromValue(String value) {
            TestInformation.TestType constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
