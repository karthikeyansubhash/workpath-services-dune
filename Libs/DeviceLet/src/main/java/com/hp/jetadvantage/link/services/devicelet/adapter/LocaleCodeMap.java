/**
 * (C) Copyright 2025 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.services.devicelet.adapter;

import com.hp.ws.cdm.controlpanel.Configuration.Language;

public final class LocaleCodeMap {
    public static final String DEFAULT_LOCALE_CODE = "en-US";

    private LocaleCodeMap() { /* utility class - prevent instantiation */ }

    public static String toWorkpathString(Language id) {
        if (id == null) return DEFAULT_LOCALE_CODE; // fallback for null
        return switch (id) {
            case AR -> "ar-SA";
            case BG -> "bg-BG";
            case CA -> "ca-ES";
            case CS -> "cs-CZ";
            case DA -> "da-DK";
            case DE -> "de-DE";
            case EL -> "el-GR";
            case EN -> "en-US";
            case EN_EU -> "en-EU";
            case ES -> "es-ES";
            case ET -> "et-EE";
            case EU -> "eu-ES";
            case FI -> "fi-FI";
            case FR -> "fr-FR";
            case FR_CA -> "fr-CA";
            case HE -> "he-IL";
            case HR -> "hr-HR";
            case HU -> "hu-HU";
            case ID -> "id-ID";
            case IT -> "it-IT";
            case JA -> "ja-JP";
            case KO -> "ko-KR";
            case LV -> "lv-LV";
            case LT -> "lt-LT";
            case MS -> "ms-MY"; // Common default locale for Malay
            case NB -> "nb-NO";
            case NL -> "nl-NL";
            case NN -> "nn-NO"; // Nynorsk
            case NO -> "no-NO"; // Generic Norwegian (distinct from NB/NN if needed)
            case PL -> "pl-PL";
            case PT -> "pt-BR"; // Following original mapping preference for Brazilian Portuguese
            case RO -> "ro-RO";
            case RU -> "ru-RU";
            case SK -> "sk-SK";
            case SL -> "sl-SI";
            case SV -> "sv-SE";
            case TH -> "th-TH";
            case TR -> "tr-TR";
            case UK -> "uk-UA";
            case VI -> "vi-VN";
            case ZH_CN -> "zh-CN";
            case ZH_TW -> "zh-TW";
            case ZH -> "zh"; // Ambiguous (intentionally not forcing CN/TW)
            default -> DEFAULT_LOCALE_CODE;
        };
    }
}
