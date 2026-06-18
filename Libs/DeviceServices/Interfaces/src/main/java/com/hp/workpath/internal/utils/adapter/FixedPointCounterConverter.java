/**
 * (C) Copyright 2026 HP Development Company, L.P.
 * All rights reserved.
 * */
package com.hp.workpath.internal.utils.adapter;

import android.util.Log;
import com.hp.ext.types.usage.FixedPointCounter;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Common utility for converting FixedPointCounter to a Workpath deci-impression int value.
 *
 * [Background]
 * Device (E2 API) reports counters as fixed-point:  realValue = significand × 10^exponent
 *   e.g. significand=20000, exponent=-4 → realValue = 2.0
 *
 * [Workpath Deci-Impression Convention]
 * Workpath API uses int, so we return deci-impressions (realValue × 10, rounded):
 *   deciImpressions = round(significand × 10^(exponent + 1))
 *
 * [Examples]
 *   sig=15001, exp=-4 → real=1.5001 → deci=15  (fractional digits rounded)
 *   sig=25900, exp=-4 → real=2.59   → deci=26  (0.09 rounded up)
 *   sig=10000, exp=-4 → real=1.0    → deci=10
 *   sig=50000, exp=-4 → real=5.0    → deci=50
 *   sig=10,    exp=-1 → real=1.0    → deci=10
 *   sig=5,     exp= 0 → real=5.0    → deci=50
 *   sig=3,     exp= 2 → real=300.0  → deci=3000
 *
 * Used by StatisticsAdapter, DeviceUsageAdapter, etc.
 */
public class FixedPointCounterConverter {
    private static final String TAG = "FixedPointCounterConverter";

    /**
     * Converts FixedPointCounter to Workpath deci-impression int.
     * <p>
     * Formula: round(significand × 10^(exponent + 1))
     * - exponent + 1 : keeps one decimal place in the integer (deci-impression)
     * - RoundingMode.HALF_UP : rounds remaining fractional digits
     *
     * @param counter FixedPointCounter from device (E2 API)
     * @return deci-impression int, or 0 if counter is null
     */
    public static int getFixedPointCounterValue(FixedPointCounter counter) {
        if (counter == null || counter.getSignificand() == null || counter.getExponent() == null) {
            return 0;
        }
        // significand × 10^(exponent + 1), then round fractional digits.
        BigDecimal scaled = BigDecimal.valueOf(counter.getSignificand())
                .scaleByPowerOfTen(counter.getExponent() + 1)
                .setScale(0, RoundingMode.HALF_UP);
        if (scaled.compareTo(BigDecimal.valueOf(Integer.MAX_VALUE)) > 0) {
            Log.i(TAG, "Counter value exceeds Integer.MAX_VALUE, returning Integer.MAX_VALUE");
            return Integer.MAX_VALUE;
        }
        if (scaled.compareTo(BigDecimal.valueOf(Integer.MIN_VALUE)) < 0) {
            Log.i(TAG, "Counter value below Integer.MIN_VALUE, returning Integer.MIN_VALUE");
            return Integer.MIN_VALUE;
        }
        return scaled.intValue();
    }
}
