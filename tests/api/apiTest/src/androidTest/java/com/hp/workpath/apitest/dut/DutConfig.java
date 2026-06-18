package com.hp.workpath.apitest.dut;

public class DutConfig {
    public static PlatformType PLATFORM = PlatformType.DUNE;
    public static String IP = "15.26.182.185";
    public static String HOST_HEADER_IP = "15.26.182.185";
    public enum PlatformType {
        JEDI,
        JOLT,
        DUNE
    }
}
