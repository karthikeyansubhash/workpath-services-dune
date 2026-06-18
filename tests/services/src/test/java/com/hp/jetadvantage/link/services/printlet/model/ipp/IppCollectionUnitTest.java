// Copyright 2026 HP Development Company, L.P.
// SPDX-License-Identifier: MIT
package com.hp.jetadvantage.link.services.printlet.model.ipp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

import android.text.TextUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockedStatic;

/**
 * Unit tests for {@link IppCollection#findAttributeByAnyTag(int[], String)}.
 *
 * Background: IPP attributes such as media-type and job-name can be encoded with different
 * tag types (e.g. KEYWORD vs NAME, or NAMELANG vs NAME) depending on the printer firmware.
 * findAttributeByAnyTag was added to handle these cross-platform differences without
 * requiring per-device workarounds.
 *
 * Referenced bugs: JALPINF-2597, JALPINF-2600~2603
 */
public class IppCollectionUnitTest {

    private static final String ATTR_NAME = "media-type";
    private static final int TAG_KEYWORD  = IppConstants.IppTag.IPP_TAG_KEYWORD.getValue();   // 0x44
    private static final int TAG_NAME     = IppConstants.IppTag.IPP_TAG_NAME.getValue();      // 0x42
    private static final int TAG_NAMELANG = IppConstants.IppTag.IPP_TAG_NAMELANG.getValue();  // 0x36
    private static final int TAG_TEXT     = IppConstants.IppTag.IPP_TAG_TEXT.getValue();

    private IppAttribute keywordAttr;
    private IppAttribute nameAttr;
    private MockedStatic<TextUtils> mMockedTextUtils;

    @Before
    public void setUp() {
        // TextUtils.equals() is an Android API — stub it with real String equality for JVM tests
        mMockedTextUtils = mockStatic(TextUtils.class);
        mMockedTextUtils.when(() -> TextUtils.equals(any(CharSequence.class), any(CharSequence.class)))
                .thenAnswer(inv -> {
                    CharSequence a = inv.getArgument(0);
                    CharSequence b = inv.getArgument(1);
                    if (a == b) return true;
                    if (a == null || b == null) return false;
                    return a.toString().equals(b.toString());
                });

        keywordAttr = new IppStringAttribute.Builder(TAG_KEYWORD, ATTR_NAME)
                .addValue("plain")
                .build();
        nameAttr = new IppStringAttribute.Builder(TAG_NAME, ATTR_NAME)
                .addValue("plain")
                .build();
    }

    @After
    public void tearDown() {
        mMockedTextUtils.close();
    }

    // ───────── findAttributeByAnyTag: 정상 케이스 ─────────

    /**
     * 첫 번째 tag(KEYWORD)로 인코딩된 속성을 찾아야 한다.
     * 회귀 케이스: JEDI 프린터는 media-type-supported 를 KEYWORD 로 반환한다.
     */
    @Test
    public void findAttributeByAnyTag_firstTagMatchesKeyword_returnsAttribute() {
        IppCollection collection = new IppCollection.Builder()
                .addAttribute(keywordAttr)
                .build();

        int[] tags = {TAG_KEYWORD, TAG_NAME};
        IppAttribute result = collection.findAttributeByAnyTag(tags, ATTR_NAME);

        assertNotNull(result);
        assertEquals(TAG_KEYWORD, result.mTag);
    }

    /**
     * 두 번째 tag(NAME)로 인코딩된 속성도 찾을 수 있어야 한다.
     * 회귀 케이스: 일부 DUNE 기기는 동일 속성을 NAME 으로 반환한다.
     */
    @Test
    public void findAttributeByAnyTag_secondTagMatchesName_returnsAttribute() {
        IppCollection collection = new IppCollection.Builder()
                .addAttribute(nameAttr)
                .build();

        int[] tags = {TAG_KEYWORD, TAG_NAME};
        IppAttribute result = collection.findAttributeByAnyTag(tags, ATTR_NAME);

        assertNotNull(result);
        assertEquals(TAG_NAME, result.mTag);
    }

    /**
     * NAMELANG / NAME 배열로 job-name 속성(NAMELANG 인코딩)을 찾아야 한다.
     * 회귀 케이스: JALPINF-2600 — job-name 이 NAMELANG 으로 오는 경우.
     */
    @Test
    public void findAttributeByAnyTag_namelangTag_returnsAttribute() {
        IppAttribute namelangAttr = new IppStringAttribute.Builder(TAG_NAMELANG, "job-name")
                .addValue("My Print Job")
                .build();
        IppCollection collection = new IppCollection.Builder()
                .addAttribute(namelangAttr)
                .build();

        int[] tags = {TAG_NAMELANG, TAG_NAME};
        IppAttribute result = collection.findAttributeByAnyTag(tags, "job-name");

        assertNotNull(result);
        assertEquals(TAG_NAMELANG, result.mTag);
    }

    // ───────── findAttributeByAnyTag: null / 미발견 케이스 ─────────

    /**
     * 배열의 어떤 tag 와도 일치하지 않으면 null 을 반환해야 한다.
     */
    @Test
    public void findAttributeByAnyTag_noTagMatches_returnsNull() {
        IppCollection collection = new IppCollection.Builder()
                .addAttribute(keywordAttr)
                .build();

        int[] tags = {TAG_TEXT};  // KEYWORD 속성에 맞지 않는 tag
        IppAttribute result = collection.findAttributeByAnyTag(tags, ATTR_NAME);

        assertNull(result);
    }

    /**
     * tag 는 일치하지만 attribute 이름이 다르면 null 을 반환해야 한다.
     */
    @Test
    public void findAttributeByAnyTag_wrongAttributeName_returnsNull() {
        IppCollection collection = new IppCollection.Builder()
                .addAttribute(keywordAttr)
                .build();

        int[] tags = {TAG_KEYWORD, TAG_NAME};
        IppAttribute result = collection.findAttributeByAnyTag(tags, "no-such-attribute");

        assertNull(result);
    }

    /**
     * 빈 tag 배열로 검색하면 null 을 반환해야 한다.
     */
    @Test
    public void findAttributeByAnyTag_emptyTagArray_returnsNull() {
        IppCollection collection = new IppCollection.Builder()
                .addAttribute(keywordAttr)
                .build();

        IppAttribute result = collection.findAttributeByAnyTag(new int[]{}, ATTR_NAME);

        assertNull(result);
    }

    /**
     * 컬렉션이 비어 있으면 null 을 반환해야 한다.
     */
    @Test
    public void findAttributeByAnyTag_emptyCollection_returnsNull() {
        IppCollection collection = new IppCollection.Builder().build();

        int[] tags = {TAG_KEYWORD, TAG_NAME};
        IppAttribute result = collection.findAttributeByAnyTag(tags, ATTR_NAME);

        assertNull(result);
    }

    // ───────── findAttributeByAnyTag: 여러 속성이 있을 때 ─────────

    /**
     * 컬렉션에 여러 속성이 있을 때 name 과 tag 가 모두 일치하는 것을 반환해야 한다.
     */
    @Test
    public void findAttributeByAnyTag_multipleAttributes_returnsCorrectOne() {
        IppAttribute otherAttr = new IppStringAttribute.Builder(TAG_KEYWORD, "other-attribute")
                .addValue("value")
                .build();

        IppCollection collection = new IppCollection.Builder()
                .addAttribute(otherAttr)
                .addAttribute(nameAttr)   // media-type, NAME 태그
                .build();

        int[] tags = {TAG_KEYWORD, TAG_NAME};
        IppAttribute result = collection.findAttributeByAnyTag(tags, ATTR_NAME);

        assertNotNull(result);
        assertEquals(TAG_NAME, result.mTag);
    }

    // ───────── 기존 findAttribute 와의 호환성 ─────────

    /**
     * findAttributeByAnyTag 로 찾은 결과가 단일 tag 검색(findAttribute)과 동일해야 한다.
     */
    @Test
    public void findAttributeByAnyTag_singleTagArray_sameResultAsFindAttribute() {
        IppCollection collection = new IppCollection.Builder()
                .addAttribute(keywordAttr)
                .build();

        IppAttribute byAnyTag    = collection.findAttributeByAnyTag(new int[]{TAG_KEYWORD}, ATTR_NAME);
        IppAttribute bySingleTag = collection.findAttribute(TAG_KEYWORD, ATTR_NAME);

        assertEquals(bySingleTag, byAnyTag);
    }
}