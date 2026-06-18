// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.printlet.model;

import android.text.TextUtils;

/** Enumeration of media types. */
@SuppressWarnings({"unused", "WeakerAccess"})
public enum MediaType {
    Bond("stationery-bond"),
    Cardstock_176to220g("cardstock"),
    CardstockGloss_176to220g("com.hp.cardstock-glossy"),
    Color("stationery-colored"),
    Envelope("envelope"),
    ExtraHeavy_131to175g("com.hp.extra-heavy"),
    ExtraHeavyGloss_131to175g("com.hp.extra-heavy-gloss"),
    Heavy_111to130g("stationery-heavyweight"),
    HeavyEnvelope("envelope-heavyweight"),
    HeavyGloss_111to130g("com.hp.heavy-glossy"),
    HeavyPaperboard("com.hp.heavypaperboard"),
    HeavyRough("com.hp.heavy-rough"),
    HPAdvancedPhoto("com.hp.advanced-photo"),
    HPBrochureGloss_180g("com.hp.brochure-glossy"),
    HPBrochureMatte_180g("com.hp.brochure-matte"),
    HPEcoFFICIENT("com.hp.EcoSMARTLite"),
    HPGloss_130g("com.hp.glossy-130gsm"),
    HPGloss_160g("com.hp.glossy-160gsm"),
    HPGloss_220g("com.hp.glossy-220gsm"),
    HPPremiumMatte_120g("com.hp.premium-presentation-matte"),
    HPMatte_90g("com.hp.matte-90gsm"),
    HPMatte_105g("com.hp.matte-105gsm"),
    HPMatte_120g("com.hp.matte-120gsm"),
    HPMatte_160g("com.hp.matte-160gsm"),
    HPMatte_200g("com.hp.matte-200gsm"),
    HPSoftGloss_120g("com.hp.soft-gloss-120gsm"),
    Intermediate_85to95g("com.hp.intermediate"),
    Labels("labels"),
    Letterhead("stationery-letterhead"),
    Light_60to74g("stationery-lightweight"),
    LightBond("com.hp.lightbond"),
    LightRough("com.hp.lightrough"),
    LightPaperboard("com.hp.lightpaperboard"),
    Midweight_96to110g("com.hp.midweight"),
    MidweightGloss_96to110g("com.hp.midweight-glossy"),
    OpaqueFilm("com.hp.film-opaque"),
    Paperboard("com.hp.paperboard"),
    Plain("stationery"),
    Preprinted("stationery-preprinted"),
    Prepunched("stationery-prepunched"),
    Recycled("com.hp.recycled"),
    Rough("com.hp.rough"),
    Tab("tab-stock"),
    Transparency("transparency"),
    Vellum("stationery-fine");

    /**
     * SOAP value associated with enum
     */
    public final String mValue;

    /**
     * MediaType constructor
     * @param value
     *              SOAP value associated with enum
     */
    MediaType(String value) {
        mValue = value;
    }

    /**
     * Convert SOAP value string to MediaType
     * @param value
     *              SOAP value string
     * @return
     *              Matching MediaType enum or null if no match is found
     */
    static MediaType fromAttributeValue(String value) {
        for(MediaType enumValue : values()) {
            if (TextUtils.equals(enumValue.mValue, value)) {
                return enumValue;
            }
        }
        return null;
    }
}
