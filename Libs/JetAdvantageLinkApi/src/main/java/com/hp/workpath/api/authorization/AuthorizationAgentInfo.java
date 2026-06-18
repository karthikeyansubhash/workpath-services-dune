// Copyright 2025 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.workpath.api.authorization;

import java.util.ArrayList;
import java.util.List;

public class AuthorizationAgentInfo {
    private String uuid = "";
    private List<LocalizedString> name = new ArrayList<>();
    private List<LocalizedString> description = new ArrayList<>();

    public static class LocalizedString {
        private String code;
        private String value;

        public LocalizedString(String code, String value) {
            this.code = code;
            this.value = value;
        }

        // Getters and Setters
        public void setValue(String value) {
            this.value = value;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "LocalizedString{" +
                    "code='" + code + '\'' +
                    ", value='" + value + '\'' +
                    '}';
        }
    }

    public AuthorizationAgentInfo() {

    }

    public AuthorizationAgentInfo(String agentUuid, List<LocalizedString> agentName, List<LocalizedString> description) {
        this.uuid = agentUuid;
        this.name = agentName;
        this.description = description;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setName(List<LocalizedString> name) {
        this.name = name;
    }

    public void setDescription(List<LocalizedString> description) {
        this.description = description;
    }

    public String getUuid() {
        return uuid;
    }

    public List<LocalizedString> getName() {
        return name;
    }

    public List<LocalizedString> getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "AuthorizationAgentInfo{" +
                "uuid='" + uuid + '\'' +
                ", name=" + name +
                ", description=" + description +
                '}';
    }
}
