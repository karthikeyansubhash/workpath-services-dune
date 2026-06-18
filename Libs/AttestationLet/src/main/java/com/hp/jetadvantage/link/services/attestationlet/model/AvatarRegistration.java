// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.attestationlet.model;

import java.util.List;

public class AvatarRegistration {
    public static class Hint {
        public String method;
    }

    public static class Link {
        public String rel;
        public String href;
        public List<Hint> hints;
    }

    public static class Registration {
        public String registrationState;
        public String registrationStateReason;
        public String registrationStepCompleted;
        public String signalingConnectionState;
        public List<Link> links;
    }

    public String version;
    public String stack;
    public String cloudServicesEnabled;
    public List<Link> links;
    public Registration registration;
}
