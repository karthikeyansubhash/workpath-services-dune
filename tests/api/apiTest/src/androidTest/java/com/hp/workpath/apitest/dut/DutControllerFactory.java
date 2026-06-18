package com.hp.workpath.apitest.dut;

public class DutControllerFactory {

    public static DutController getController(DutConfig.PlatformType platform) {
        switch (platform) {
            case DUNE:
                return new DuneDutController();
            case JEDI:
            case JOLT:
                //dutController = new JediDutController();
                //break;
            default:
                throw new IllegalArgumentException("Invalid platform");
        }
    }
}
