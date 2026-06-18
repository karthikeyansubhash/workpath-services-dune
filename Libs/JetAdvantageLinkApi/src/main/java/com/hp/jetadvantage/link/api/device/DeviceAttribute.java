// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.api.device;

import com.hp.jetadvantage.link.common.annotation.CommonApi;

/**
 * <p>The sets of attributes for retrieving the value from the device.
 * Each key has the corresponding return type, so
 * the appropriate method in {@link DeviceService}
 * must be used to get information from the device.</p>
 *
 * @since API 1
 */
@CommonApi
public enum DeviceAttribute implements DeviceAttributeBase {
    /**
     * <p>The option to retrieve hostname</p>
     *
     * @since API 1
     */
    DA_NETWORK_HOSTNAME(String.class),

    /**
     * <p>The option to retrieve IPv4 interface IP address</p>
     *
     * @since API 1
     */
    DA_NETWORK_IPADDRESS(String.class),

    /**
     * <p>The option to retrieve MAC address</p>
     *
     * @since API 1
     */
    DA_NETWORK_MACADDRESS(String.class),

    /**
     * <p>The option to retrieve model name</p>
     *
     * @since API 1
     */
    DA_SYSTEM_MODELNAME(String.class),

    /**
     * <p>The option to retrieve serial number</p>
     *
     * @since API 1
     */
    DA_SYSTEM_SERIALNUMBER(String.class),

    /**
     * <p>The option to get the current language<br/>
     * String which represents String value, in language code.
     * Different LanguageCode like "["ca-ES","cs-CZ","da-DK","de-DE","el-GR","en-US","es-ES","fi-FI","fr-FR","hr-HR","hu-HU","id-ID","it-IT","ja-JP","ko-KR","nl-NL","nn-NO","pl-PL","pt-PT","ro-RO","ru-RU","sk-SK","sl-SI","sv-SE","th-TH","tr-TR","zh-CN","zh-TW","ar-SA","he-IL"]"</p>
     *
     * @since API 1
     *
     * @deviceOnly
     */
    DA_SYSTEM_LANGUAGE(String.class),

    /**
     * <p>The option to get the lists of language supported by device<br>
     * String which represents array of String values,
     * in ISO language code like "["ca-ES","cs-CZ","da-DK","de-DE","el-GR","en-US","es-ES","fi-FI","fr-FR","hr-HR","hu-HU","id-ID","it-IT","ja-JP","ko-KR","nl-NL","nn-NO","pl-PL","pt-PT","ro-RO","ru-RU","sk-SK","sl-SI","sv-SE","th-TH","tr-TR","zh-CN","zh-TW","ar-SA","he-IL"]"</p>
     *
     * @since API 1
     *
     * @deviceOnly
     */
    DA_SYSTEM_LANGUAGE_CAPABILITY(String[].class),

    /**
     * <p>The option to get firmware version</p>
     *
     * @since API 1
     */
    DA_SYSTEM_FIRMWARE_VERSION(String.class),

    /**
     * <p>The option to get device unique ID (UUID)</p>
     *
     * @since API 1
     */
    DA_SYSTEM_DEVICE_ID(String.class),

    /**
     * <p>The option to get device formatter of the serial number</p>
     *
     * @since API 1
     */
    DA_SYSTEM_FORMATTER_SERIAL_NUMBER(String.class),

    /**
     * <p>The option to get product number</p>
     *
     * @since API 1
     */
    DA_SYSTEM_PRODUCT_NUMBER(String.class),

    /**
     * <p>The option to get vendor name</p>
     *
     * @since API 1
     */
    DA_DEVICE_VENDOR(String.class),

    /**
     * <p>The option to get asset number</p>
     *
     * @since API 9
     */
    DA_ASSET_NUMBER(String.class),

    /**
     * <p>The option to get company contact</p>
     *
     * @since API 9
     */
    DA_COMPANY_CONTACT(String.class),

    /**
     * <p>The option to get company name</p>
     *
     * @since API 9
     */
    DA_COMPANY_NAME(String.class),

    /**
     * <p>The option to get device location</p>
     *
     * @since API 9
     */
    DA_DEVICE_LOCATION(String.class),

    /**
     * <p>The option to get machine name</p>
     *
     * @since API 9
     */
    DA_MACHINE_NAME(String.class),

    /**
     * <p>The option to get the HP Future Smart Level</p>
     *
     * @since API 9
     */
    DA_SYSTEM_HP_FUTURE_SMART_LEVEL(String.class);

    private final Class mResultClass;

    DeviceAttribute() {
        mResultClass = null;
    }

    /**
     * Default constructor
     *
     * @param resultClass Of return value by this attribute
     */
    DeviceAttribute(final Class resultClass) {
        mResultClass = resultClass;
    }

    /**
     * <p>Returns value in result {@link Class} with attribute</p>
     *
     * @return {@link Class} to be received if retrieved value by the attribute
     * @hide for internal use
     */
    @Override
    public Class getResultClass() {
        return mResultClass;
    }
}
