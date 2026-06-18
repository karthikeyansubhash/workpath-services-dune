// Copyright 2018 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.oxpdlib.media;

import android.text.TextUtils;

public enum MediaTypeId {
    Any("Any"),
    Bond("Bond"),
    BrochureMatte("BrochureMatte"),
    Cardstock_176to220g("Cardstock_176to220g"),
    CardstockGloss_176to220g("CardstockGloss_176to220g"),
    Color("Color"),
    CoverMatte("CoverMatte"),
    Envelope("Envelope"),
    ExtraHeavy_131to175g("ExtraHeavy_131to175g"),
    ExtraHeavyGloss_131to175g("ExtraHeavyGloss_131to175g"),
    Heavy_111to130g("Heavy_111to130g"),
    HeavyEnvelope("HeavyEnvelope"),
    HeavyGloss_111to130g("HeavyGloss_111to130g"),
    HeavyPaperboard("HeavyPaperboard"),
    HeavyRough("HeavyRough"),
    HPAdvancedPhoto("HPAdvancedPhoto"),
    HPBrochureGloss_180g("HPBrochureGloss_180g"),
    HPBrochureMatte_180g("HPBrochureMatte_180g"),
    HPCoverMatte_200g("HPCoverMatte_200g"),
    HPEcoFFICIENT("HPEcoFFICIENT"),
    HPEverydayPhotoMatte("HPEverydayPhotoMatte"),
    HPGloss_130g("HPGloss_130g"),
    HPGloss_160g("HPGloss_160g"),
    HPGloss_220g("HPGloss_220g"),
    HPGlossyEdgeline180G("HPGlossyEdgeline180G"),
    HPMatte_90g("HPMatte_90g"),
    HPMatte_105g("HPMatte_105g"),
    HPMatte_120g("HPMatte_120g"),
    HPMatte_160g("HPMatte_160g"),
    HPMatte_200g("HPMatte_200g"),
    HPMatteBrochureAndFlyer_180g("HPMatteBrochureAndFlyer_180g"),
    HPPhoto("HPPhoto"),
    HPPhotoPlus("HPPhotoPlus"),
    HPPremiumInkjetTransparency("HPPremiumInkjetTransparency"),
    HPPremiumMatte_120g("HPPremiumMatte_120g"),
    HPSoftGloss_120g("HPSoftGloss_120g"),
    HPTough("HPTough"),
    HPTrifoldGloss_160g("HPTrifoldGloss_160g"),
    Intermediate_85to95g("Intermediate_85to95g"),
    Labels("Labels"),
    Letterhead("Letterhead"),
    Light_60to74g("Light_60to74g"),
    LightBond("LightBond"),
    LightPaperboard("LightPaperboard"),
    LightRough("LightRough"),
    Matte("Matte"),
    Midweight_96to110g("Midweight_96to110g"),
    MidweightGloss_96to110g("MidweightGloss_96to110g"),
    OpaqueFilm("OpaqueFilm"),
    Paperboard("Paperboard"),
    Photo("Photo"),
    Plain("Plain"),
    PremiumInkjet("PremiumInkjet"),
    Preprinted("Preprinted"),
    Prepunched("Prepunched"),
    Recycled("Recycled"),
    Rough("Rough"),
    ShelfEdgeLabels("ShelfEdgeLabels"),
    Tab("Tab"),
    ThickPlain("ThickPlain"),
    Transparency("Transparency"),
    Vellum("Vellum"),
    UserDefined1("UserDefined1"),
    UserDefined2("UserDefined2"),
    UserDefined3("UserDefined3"),
    UserDefined4("UserDefined4"),
    UserDefined5("UserDefined5"),
    UserDefined6("UserDefined6"),
    UserDefined7("UserDefined7"),
    UserDefined8("UserDefined8"),
    UserDefined9("UserDefined9"),
    UserDefined10("UserDefined10"),
    UserDefined11("UserDefined11"),
    UserDefined12("UserDefined12"),
    UserDefined13("UserDefined13"),
    UserDefined14("UserDefined14"),
    UserDefined15("UserDefined15"),
    UserDefined16("UserDefined16"),
    Auto("Auto"),
    HeavyBond("HeavyBond");

    public final String mValue;

    MediaTypeId(String value) {
        mValue = value;
    }

    public static MediaTypeId fromAttributeValue(String value) {
        for(MediaTypeId enumValue : values()) {
            if (TextUtils.equals(enumValue.mValue, value)) return enumValue;
        }
        return null;
    }
}
