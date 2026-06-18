// Copyright 2026 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.printlet.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

import android.text.TextUtils;

import com.hp.jetadvantage.link.services.printlet.model.ipp.IppAttribute;
import com.hp.jetadvantage.link.services.printlet.model.ipp.IppCollection;
import com.hp.jetadvantage.link.services.printlet.model.ipp.IppCollectionAttribute;
import com.hp.jetadvantage.link.services.printlet.model.ipp.IppConstants;
import com.hp.jetadvantage.link.services.printlet.model.ipp.IppResponse;
import com.hp.jetadvantage.link.services.printlet.model.ipp.IppStringAttribute;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;

/**
 * Unit tests for print default value mapping.
 *
 * Covers:
 * - MediaType.fromAttributeValue() mapping IPP values (DUNE-315330)
 * - MediaSource.fromAttributeValue() mapping IPP values
 * - media-type-default parsed via findAttributeByAnyTag (keyword + name tags)
 * - media-col-default → media-source parsing
 */
public class PrintDefaultsUnitTest {

    private static final int TAG_KEYWORD = IppConstants.IppTag.IPP_TAG_KEYWORD.getValue();
    private static final int TAG_NAME = IppConstants.IppTag.IPP_TAG_NAME.getValue();
    private static final int TAG_BEGIN_COLLECTION = IppConstants.IppTag.IPP_TAG_BEGIN_COLLECTION.getValue();

    private MockedStatic<TextUtils> mMockedTextUtils;

    @Before
    public void setUp() {
        mMockedTextUtils = mockStatic(TextUtils.class);
        mMockedTextUtils.when(() -> TextUtils.equals(any(CharSequence.class), any(CharSequence.class)))
                .thenAnswer(inv -> {
                    CharSequence a = inv.getArgument(0);
                    CharSequence b = inv.getArgument(1);
                    if (a == b) return true;
                    if (a == null || b == null) return false;
                    return a.toString().equals(b.toString());
                });
    }

    @After
    public void tearDown() {
        mMockedTextUtils.close();
    }

    // ═══════════════════════════════════════════════════════════════
    // MediaType.fromAttributeValue() — IPP keyword value mapping
    // ═══════════════════════════════════════════════════════════════

    @Test
    public void mediaType_fromAttributeValue_stationery_returnsPlain() {
        MediaType result = MediaType.fromAttributeValue("stationery");
        assertEquals(MediaType.Plain, result);
    }

    @Test
    public void mediaType_fromAttributeValue_comHpRecycled_returnsRecycled() {
        MediaType result = MediaType.fromAttributeValue("com.hp.recycled");
        assertEquals(MediaType.Recycled, result);
    }

    @Test
    public void mediaType_fromAttributeValue_stationeryBond_returnsBond() {
        MediaType result = MediaType.fromAttributeValue("stationery-bond");
        assertEquals(MediaType.Bond, result);
    }

    @Test
    public void mediaType_fromAttributeValue_stationeryHeavyweight_returnsHeavy() {
        MediaType result = MediaType.fromAttributeValue("stationery-heavyweight");
        assertEquals(MediaType.Heavy_111to130g, result);
    }

    @Test
    public void mediaType_fromAttributeValue_comHpMidweight_returnsMidweight() {
        MediaType result = MediaType.fromAttributeValue("com.hp.midweight");
        assertEquals(MediaType.Midweight_96to110g, result);
    }

    @Test
    public void mediaType_fromAttributeValue_comHpEcoSMARTLite_returnsHPEcoFFICIENT() {
        MediaType result = MediaType.fromAttributeValue("com.hp.EcoSMARTLite");
        assertEquals(MediaType.HPEcoFFICIENT, result);
    }

    @Test
    public void mediaType_fromAttributeValue_envelope_returnsEnvelope() {
        MediaType result = MediaType.fromAttributeValue("envelope");
        assertEquals(MediaType.Envelope, result);
    }

    @Test
    public void mediaType_fromAttributeValue_labels_returnsLabels() {
        MediaType result = MediaType.fromAttributeValue("labels");
        assertEquals(MediaType.Labels, result);
    }

    @Test
    public void mediaType_fromAttributeValue_transparency_returnsTransparency() {
        MediaType result = MediaType.fromAttributeValue("transparency");
        assertEquals(MediaType.Transparency, result);
    }

    @Test
    public void mediaType_fromAttributeValue_unknownValue_returnsNull() {
        MediaType result = MediaType.fromAttributeValue("unknown-media-type");
        assertNull(result);
    }

    @Test
    public void mediaType_fromAttributeValue_null_returnsNull() {
        MediaType result = MediaType.fromAttributeValue(null);
        assertNull(result);
    }

    // ═══════════════════════════════════════════════════════════════
    // MediaSource.fromAttributeValue() — IPP keyword value mapping
    // ═══════════════════════════════════════════════════════════════

    @Test
    public void mediaSource_fromAttributeValue_auto_returnsAuto() {
        MediaSource result = MediaSource.fromAttributeValue("auto");
        assertEquals(MediaSource.Auto, result);
    }

    @Test
    public void mediaSource_fromAttributeValue_tray1_returnsTray1() {
        MediaSource result = MediaSource.fromAttributeValue("tray-1");
        assertEquals(MediaSource.Tray1, result);
    }

    @Test
    public void mediaSource_fromAttributeValue_tray2_returnsTray2() {
        MediaSource result = MediaSource.fromAttributeValue("tray-2");
        assertEquals(MediaSource.Tray2, result);
    }

    @Test
    public void mediaSource_fromAttributeValue_tray3_returnsTray3() {
        MediaSource result = MediaSource.fromAttributeValue("tray-3");
        assertEquals(MediaSource.Tray3, result);
    }

    @Test
    public void mediaSource_fromAttributeValue_unknownValue_returnsNull() {
        MediaSource result = MediaSource.fromAttributeValue("unknown-source");
        assertNull(result);
    }

    // ═══════════════════════════════════════════════════════════════
    // media-type-default: findAttributeByAnyTag (keyword + name tag)
    // Printer may return as keyword or nameWithoutLanguage
    // ═══════════════════════════════════════════════════════════════

    @Test
    public void mediaTypeDefault_keywordTag_parsedCorrectly() {
        // Printer returns media-type-default as keyword "stationery"
        IppAttribute attr = new IppStringAttribute.Builder(TAG_KEYWORD, IppConstants.IPP_ATTRIBUTE_NAME__MEDIA_TYPE_DEFAULT)
                .addValue("stationery")
                .build();
        IppCollection printerGroup = new IppCollection.Builder()
                .addAttribute(attr)
                .build();

        int[] tags = { TAG_KEYWORD, TAG_NAME };
        IppAttribute result = printerGroup.findAttributeByAnyTag(tags, IppConstants.IPP_ATTRIBUTE_NAME__MEDIA_TYPE_DEFAULT);

        assertNotNull(result);
        String value = ((IppStringAttribute) result).get(0);
        assertEquals("stationery", value);
        assertEquals(MediaType.Plain, MediaType.fromAttributeValue(value));
    }

    @Test
    public void mediaTypeDefault_nameTag_parsedCorrectly() {
        // Printer returns media-type-default as nameWithoutLanguage "com.hp.recycled"
        IppAttribute attr = new IppStringAttribute.Builder(TAG_NAME, IppConstants.IPP_ATTRIBUTE_NAME__MEDIA_TYPE_DEFAULT)
                .addValue("com.hp.recycled")
                .build();
        IppCollection printerGroup = new IppCollection.Builder()
                .addAttribute(attr)
                .build();

        int[] tags = { TAG_KEYWORD, TAG_NAME };
        IppAttribute result = printerGroup.findAttributeByAnyTag(tags, IppConstants.IPP_ATTRIBUTE_NAME__MEDIA_TYPE_DEFAULT);

        assertNotNull(result);
        String value = ((IppStringAttribute) result).get(0);
        assertEquals("com.hp.recycled", value);
        assertEquals(MediaType.Recycled, MediaType.fromAttributeValue(value));
    }

    @Test
    public void mediaTypeDefault_neitherTag_returnsNull() {
        // Attribute exists with a different tag (TEXT) — should not match
        int tagText = IppConstants.IppTag.IPP_TAG_TEXT.getValue();
        IppAttribute attr = new IppStringAttribute.Builder(tagText, IppConstants.IPP_ATTRIBUTE_NAME__MEDIA_TYPE_DEFAULT)
                .addValue("stationery")
                .build();
        IppCollection printerGroup = new IppCollection.Builder()
                .addAttribute(attr)
                .build();

        int[] tags = { TAG_KEYWORD, TAG_NAME };
        IppAttribute result = printerGroup.findAttributeByAnyTag(tags, IppConstants.IPP_ATTRIBUTE_NAME__MEDIA_TYPE_DEFAULT);

        assertNull(result);
    }

    // ═══════════════════════════════════════════════════════════════
    // media-col-default → media-source parsing
    // IPP: media-col-default = {media-source=auto, media-type=stationery, ...}
    // ═══════════════════════════════════════════════════════════════

    @Test
    public void mediaColDefault_mediaSource_auto_parsedCorrectly() {
        // Build inner collection: {media-source=auto}
        IppAttribute mediaSourceAttr = new IppStringAttribute.Builder(TAG_KEYWORD, IppConstants.IPP_ATTRIBUTE_NAME__MEDIA_SOURCE)
                .addValue("auto")
                .build();
        IppCollection innerCollection = new IppCollection.Builder()
                .addAttribute(mediaSourceAttr)
                .build();

        // Verify media-source can be found in the inner collection
        IppAttribute found = innerCollection.findAttribute(TAG_KEYWORD, IppConstants.IPP_ATTRIBUTE_NAME__MEDIA_SOURCE);
        assertNotNull(found);
        String value = ((IppStringAttribute) found).get(0);
        assertEquals("auto", value);
        assertEquals(MediaSource.Auto, MediaSource.fromAttributeValue(value));
    }

    @Test
    public void mediaColDefault_mediaSource_tray2_parsedCorrectly() {
        IppAttribute mediaSourceAttr = new IppStringAttribute.Builder(TAG_KEYWORD, IppConstants.IPP_ATTRIBUTE_NAME__MEDIA_SOURCE)
                .addValue("tray-2")
                .build();
        IppCollection innerCollection = new IppCollection.Builder()
                .addAttribute(mediaSourceAttr)
                .build();

        IppAttribute found = innerCollection.findAttribute(TAG_KEYWORD, IppConstants.IPP_ATTRIBUTE_NAME__MEDIA_SOURCE);
        assertNotNull(found);
        String value = ((IppStringAttribute) found).get(0);
        assertEquals("tray-2", value);
        assertEquals(MediaSource.Tray2, MediaSource.fromAttributeValue(value));
    }

    @Test
    public void mediaColDefault_noMediaSource_returnsNull() {
        // media-col-default with no media-source attribute
        IppAttribute mediaTypeAttr = new IppStringAttribute.Builder(TAG_KEYWORD, IppConstants.IPP_ATTRIBUTE_NAME__MEDIA_TYPE_DEFAULT)
                .addValue("stationery")
                .build();
        IppCollection innerCollection = new IppCollection.Builder()
                .addAttribute(mediaTypeAttr)
                .build();

        IppAttribute found = innerCollection.findAttribute(TAG_KEYWORD, IppConstants.IPP_ATTRIBUTE_NAME__MEDIA_SOURCE);
        assertNull(found);
    }

    // ═══════════════════════════════════════════════════════════════
    // Real printer response simulation: HP Color LaserJet Flow E78625
    // media-type-default = stationery (nameWithoutLanguage)
    // media-col-default = {media-source=auto, media-type=stationery}
    // ═══════════════════════════════════════════════════════════════

    @Test
    public void realPrinter_mediaTypeDefault_stationery_mapsToPlain() {
        // This printer returns media-type-default as nameWithoutLanguage (TAG_NAME)
        MediaType result = MediaType.fromAttributeValue("stationery");
        assertEquals(MediaType.Plain, result);
    }

    @Test
    public void realPrinter_mediaColDefault_mediaSourceAuto_mapsToAuto() {
        MediaSource result = MediaSource.fromAttributeValue("auto");
        assertEquals(MediaSource.Auto, result);
    }

    /**
     * Verify all media-type-supported values from real printer can be mapped.
     * Based on: HP Color LaserJet Flow E78625 IPP response.
     */
    @Test
    public void realPrinter_allSupportedMediaTypes_canBeMapped() {
        String[] printerMediaTypes = {
                "cardstock",
                "com.hp.EcoSMARTLite",
                "com.hp.extra-heavy",
                "com.hp.extra-heavy-gloss",
                "com.hp.glossy-130gsm",
                "com.hp.glossy-160gsm",
                "com.hp.heavy-glossy",
                "com.hp.lightpaperboard",
                "com.hp.matte-105gsm",
                "com.hp.matte-120gsm",
                "com.hp.matte-160gsm",
                "com.hp.matte-200gsm",
                "com.hp.matte-90gsm",
                "com.hp.midweight",
                "com.hp.midweight-glossy",
                "com.hp.paperboard",
                "com.hp.recycled",
                "com.hp.soft-gloss-120gsm",
                "envelope",
                "labels",
                "stationery",
                "stationery-bond",
                "stationery-colored",
                "stationery-heavyweight",
                "stationery-letterhead",
                "stationery-lightweight",
                "stationery-preprinted",
                "stationery-prepunched",
                "transparency"
        };

        for (String mediaType : printerMediaTypes) {
            MediaType result = MediaType.fromAttributeValue(mediaType);
            assertNotNull("MediaType mapping missing for: " + mediaType, result);
        }
    }

    /**
     * Verify that any of the media-type-supported values can serve as
     * media-type-default and be correctly mapped (the fix for DUNE-315330).
     */
    @Test
    public void mediaTypeDefault_comHpRecycled_previouslyFailedNowWorks() {
        // Before fix: MediaTypeDefault had "recycled" but printer returns "com.hp.recycled"
        // After fix: Using MediaType.fromAttributeValue directly handles the correct IPP value
        MediaType result = MediaType.fromAttributeValue("com.hp.recycled");
        assertNotNull("com.hp.recycled should map to MediaType.Recycled", result);
        assertEquals(MediaType.Recycled, result);
    }

    @Test
    public void mediaTypeDefault_comHpMidweight_previouslyFailedNowWorks() {
        MediaType result = MediaType.fromAttributeValue("com.hp.midweight");
        assertNotNull("com.hp.midweight should map to MediaType.Midweight_96to110g", result);
        assertEquals(MediaType.Midweight_96to110g, result);
    }

    @Test
    public void mediaTypeDefault_stationeryHeavyweight_previouslyFailedNowWorks() {
        // Before fix: MediaTypeDefault had "heavy" but printer returns "stationery-heavyweight"
        MediaType result = MediaType.fromAttributeValue("stationery-heavyweight");
        assertNotNull("stationery-heavyweight should map to MediaType.Heavy_111to130g", result);
        assertEquals(MediaType.Heavy_111to130g, result);
    }
}
