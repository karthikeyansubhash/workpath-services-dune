// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.workpath.api.statistics.jobinfo.print;

import androidx.annotation.Keep;

import com.google.gson.annotations.SerializedName;
import com.hp.workpath.api.statistics.jobinfo.StatisticsAttributes;
import com.hp.jetadvantage.link.common.annotation.DeviceApi;

/**
 * Provides PrintAgent information
 *
 * @since API 5
 */
@DeviceApi
public class PrintAgentInfo {
    private String description;
    private String agentId;
    private AgentUsed agentUsed;
    private Capacity capacity;
    private AgentManufacturer manufacturer;
    private StatisticsAttributes.Color markerColor;
    private String productNumber;
    private String serialNumber;
    private AgentConsumableContentType consumableContentType;
    private AgentConsumableType consumableType;

    /**
     * Returns AgentUsed
     *
     * @since API 5
     */
    public class AgentUsed {
        private PrintAgentInfo.AgentUnit unit;
        private int amount;

        /**
         * Returns unit for AgentUsed
         *
         * @return unit
         * <p>
         * <ul>
         * <li>Return can be null if the unit is null</li>
         * <li>Return can be null if the unit is empty</li>
         * </ul>
         * </p>
         * @since API 5
         */
        public AgentUnit getUnit() {
            return unit;
        }
        public void setUnit(AgentUnit unit) {
            this.unit = unit;
        }

        /**
         * Returns amount for AgentUsed
         *
         * @return amount
         * <p>
         * <ul>
         * <li>Ensures that the return numeric value is non-negative.</li>
         * </ul>
         * </p>
         * @since API 5
         */
        public int getAmount() {
            return amount;
        }
        public void setAmount(int amount) {
            this.amount = amount;
        }
    }

    /**
     * Returns AgentCapacity
     *
     * @since API 5
     */
    public class Capacity {
        private PrintAgentInfo.AgentUnit unit;
        private int maxCapacity;

        /**
         * Returns Agent unit for Capacity
         *
         * @return unit
         * <p>
         * <ul>
         * <li>Return can be null if the unit is null</li>
         * <li>Return can be null if the unit is empty</li>
         * </ul>
         * </p>
         * @since API 5
         */
        public AgentUnit getUnit() {
            return unit;
        }

        public void setUnit(AgentUnit unit) {
            this.unit = unit;
        }

        /**
         * Returns maxCapacity for Capacity
         *
         * @return maxCapacity
         * <p>
         * <ul>
         * <li>Ensures that the return numeric value is non-negative.</li>
         * </ul>
         * </p>
         * @since API 5
         */
        public int getMaxCapacity() {
            return maxCapacity;
        }
        public void setMaxCapacity(int maxCapacity) {
            this.maxCapacity = maxCapacity;
        }
    }

    /**
     * Returns AgentManufacturer
     *
     * @since API 5
     */
    public class AgentManufacturer {
        private String date;
        private String name;

        /**
         * Returns date of AgentManufacturer
         *
         * @return date
         * <p>
         * <ul>
         * <li>Return can be null if the date is null</li>
         * <li>Return can be null if the date is empty</li>
         * </ul>
         * </p>
         * @since API 5
         */
        public String getDate() {
            return date;
        }
        public void setDate(String date) {
            this.date = date;
        }

        /**
         * Returns name of AgentManufacturer
         *
         * @return name
         * <p>
         * <ul>
         * <li>Return can be null if the name is null</li>
         * <li>Return can be null if the name is empty</li>
         * </ul>
         * </p>
         * @since API 5
         */
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
    }

    /**
     * Enums of AgentUnit
     *
     * @since API 5
     */
    @Keep
    public enum AgentUnit {
        /**
         * Drops
         *
         * @since API 5
         */
        @SerializedName("Drops")
        DROPS,

        /**
         * Pixels
         *
         * @since API 5
         */
        @SerializedName("Pixels")
        PIXELS,

        /**
         * Impressions
         *
         * @since API 5
         */
        @SerializedName("Impressions")
        IMPRESSIONS,

        /**
         * Other
         *
         * @since API 5
         */
        @SerializedName("Other")
        OTHER
    }

    /**
     * Enums of AgentConsumableContentType
     *
     * @since API 5
     */
    @Keep
    public enum AgentConsumableContentType {
        /**
         * FixingAgent
         *
         * @since API 5
         */
        @SerializedName("FixingAgent")
        FIXINGAGENT,

        /**
         * GlossingAgent
         *
         * @since API 5
         */
        @SerializedName("GlossingAgent")
        GLOSSINGAGENT,

        /**
         * MarkingAgent
         *
         * @since API 5
         */
        @SerializedName("MarkingAgent")
        MARKINGAGENT,

        /**
         * Other
         *
         * @since API 5
         */
        @SerializedName("Other")
        OTHER
    }

    /**
     * Enums of AgentConsumableType
     *
     * @since API 5
     */
    @Keep
    public enum AgentConsumableType {
        /**
         * InkCartridge
         *
         * @since API 5
         */
        @SerializedName("InkCartridge")
        INKCARTRIDGE,

        /**
         * TonerCartridge
         *
         * @since API 5
         */
        @SerializedName("TonerCartridge")
        TONERCARTRIDGE,

        /**
         * Other
         *
         * @since API 5
         */
        @SerializedName("Other")
        OTHER
    }

    /**
     * Returns description for PrintAgent information
     *
     * @return description
     * <p>
     * <ul>
     * <li>Return can be null if the description is null</li>
     * <li>Return can be null if the description is empty</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns agentId for PrintAgent information
     *
     * @return agentId
     * <p>
     * <ul>
     * <li>Return can be null if the agentId is null</li>
     * <li>Return can be null if the agentId is empty</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public String getAgentId() {
        return agentId;
    }

    /**
     * Returns agentUsed for PrintAgent information
     *
     * @return agentUsed
     * <p>
     * <ul>
     * <li>Return can be null if the agentUsed is null</li>
     * <li>Return can be null if the agentUsed is empty</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public AgentUsed getAgentUsed() {
        return agentUsed;
    }

    /**
     * Returns agentCapacity for PrintAgent information
     *
     * @return agentCapacity
     * <p>
     * <ul>
     * <li>Return can be null if the capacity is null</li>
     * <li>Return can be null if the capacity is empty</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public Capacity getCapacity() {
        return capacity;
    }

    /**
     * Returns manufacturer for PrintAgent information
     *
     * @return manufacturer
     * <p>
     * <ul>
     * <li>Return can be null if the manufacturer is null</li>
     * <li>Return can be null if the manufacturer is empty</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public AgentManufacturer getManufacturer() {
        return manufacturer;
    }

    /**
     * Returns markerColor for PrintAgent information
     *
     * @return markerColor
     * <p>
     * <ul>
     * <li>Return can be null if the markerColor is null</li>
     * <li>Return can be null if the markerColor is empty</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public StatisticsAttributes.Color getMarkerColor() {
        return markerColor;
    }

    /**
     * Returns productNumber for PrintAgent information
     *
     * @return productNumber
     * <p>
     * <ul>
     * <li>Return can be null if the productNumber is null</li>
     * <li>Return can be null if the productNumber is empty</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public String getProductNumber() {
        return productNumber;
    }

    /**
     * Returns serialNumber for PrintAgent information
     *
     * @return serialNumber
     * <p>
     * <ul>
     * <li>Return can be null if the serialNumber is null</li>
     * <li>Return can be null if the serialNumber is empty</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public String getSerialNumber() {
        return serialNumber;
    }

    /**
     * Returns consumableContentType for PrintAgent information
     *
     * @return consumableContentType
     * <p>
     * <ul>
     * <li>Return can be null if the consumableContentType is null</li>
     * <li>Return can be null if the consumableContentType is empty</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public AgentConsumableContentType getConsumableContentType() {
        return consumableContentType;
    }

    /**
     * Returns consumableType for PrintAgent information
     *
     * @return consumableType
     * <p>
     * <ul>
     * <li>Return can be null if the consumableType is null</li>
     * <li>Return can be null if the consumableType is empty</li>
     * </ul>
     * </p>
     * @since API 5
     */
    public AgentConsumableType getConsumableType() {
        return consumableType;
    }

    /**
     * Set the description
     * @param description the description to set
     * @since API 5
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Set the agent ID
     * @param agentId the agentId to set
     * @since API 5
     */
    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    /**
     * Set the agent used
     * @param agentUsed the agentUsed to set
     * @since API 5
     */
    public void setAgentUsed(AgentUsed agentUsed) {
        this.agentUsed = agentUsed;
    }

    /**
     * Set the capacity
     * @param capacity the capacity to set
     * @since API 5
     */
    public void setCapacity(Capacity capacity) {
        this.capacity = capacity;
    }

    /**
     * Set the manufacturer
     * @param manufacturer the manufacturer to set
     * @since API 5
     */
    public void setManufacturer(AgentManufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    /**
     * Set the marker color
     * @param markerColor the markerColor to set
     * @since API 5
     */
    public void setMarkerColor(StatisticsAttributes.Color markerColor) {
        this.markerColor = markerColor;
    }

    /**
     * Set the product number
     * @param productNumber the productNumber to set
     * @since API 5
     */
    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    /**
     * Set the serial number
     * @param serialNumber the serialNumber to set
     * @since API 5
     */
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    /**
     * Set the consumable content type
     * @param consumableContentType the consumableContentType to set
     * @since API 5
     */
    public void setConsumableContentType(AgentConsumableContentType consumableContentType) {
        this.consumableContentType = consumableContentType;
    }

    /**
     * Set the consumable type
     * @param consumableType the consumableType to set
     * @since API 5
     */
    public void setConsumableType(AgentConsumableType consumableType) {
        this.consumableType = consumableType;
    }
}
