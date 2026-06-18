/**
 * (C) Copyright 2026 HP Development Company, L.P.
 * All rights reserved.
 */
package com.hp.jetadvantage.link.services.scanlet.service.postprocessor;

import com.hp.jetadvantage.link.services.scanlet.model.ParsedName;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilenameParser {

    // Normal pattern
    // 1 page:      2025121915322068_20251219_153229.pdf
    // multi pages: 2025121915365651_20251219_153706_2-004.jpeg
    //              2025121915365651_20251219_153707_4-004.jpeg
    //              2025121915365651_20251219_153707_3-004.jpeg
    //              2025121915365651_20251219_153705_1-004.jpeg

    // Complex pattern (filename: test_case)
    // 1 page:      test_case_20251219_153933.tiff
    // multi pages: test_case_20251219_153933_2-004.tiff
    //              test_case_20251219_153933_3-004.tiff
    //              test_case_20251219_153933_4-004.tiff
    //              test_case_20251219_153933_1-004.tiff
    private static final Pattern PATTERN = Pattern.compile(
            "^(.*?)_?(\\d{16})?_?(\\d{8})_(\\d{6})(?:_(\\d+)-(\\d+))?\\.([^.]+)$"
    );

    public static ParsedName parse(String filename) {
        Matcher m = PATTERN.matcher(filename);
        if (!m.matches()) return null;

        String fn   = emptyToNull(m.group(1));
        String job    = emptyToNull(m.group(2));
        String date   = m.group(3);
        String time   = m.group(4);
        String pageS  = emptyToNull(m.group(5));
        String totalS = emptyToNull(m.group(6));
        String ext    = m.group(7);

        Integer page  = pageS  != null ? Integer.parseInt(pageS)  : 1;
        Integer total = totalS != null ? Integer.parseInt(totalS) : 1;

        String extLower = ext.toLowerCase(Locale.ROOT);

        if (fn != null && fn.endsWith("_")) {
            fn = fn.substring(0, fn.length() - 1);
        }

        if (fn != null && fn.isEmpty()) fn = null;
        return new ParsedName(fn, job, date, time, page, total, extLower);
    }

    private static String emptyToNull(String s) {
        return (s == null || s.isEmpty()) ? null : s;
    }
}
