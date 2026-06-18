package com.hp.workpath.internal.utils.adapter;

import com.hp.ext.types.usage.FixedPointCounter;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for FixedPointCounterConverter deci-impression conversion logic.
 *
 * [Conversion Formula]
 * deciImpressions = round(significand × 10^(exponent + 1))
 *
 * [Test Scenarios]
 * Validates 7 example scenarios from the method documentation:
 *   1. sig=15001, exp=-4 → real=1.5001 → deci=15
 *   2. sig=25900, exp=-4 → real=2.59 → deci=26
 *   3. sig=10000, exp=-4 → real=1.0 → deci=10
 *   4. sig=50000, exp=-4 → real=5.0 → deci=50
 *   5. sig=10, exp=-1 → real=1.0 → deci=10
 *   6. sig=5, exp=0 → real=5.0 → deci=50
 *   7. sig=3, exp=2 → real=300.0 → deci=3000
 */
public class FixedPointCounterConverterTest {

    /**
     * Scenario 1: sig=15001, exp=-4
     * real = 15001 × 10^-4 = 1.5001
     * deci = round(15001 × 10^-3) = round(15.001) = 15 (HALF_UP rounds down)
     */
    @Test
    public void testScenario1() {
        FixedPointCounter counter = createCounter(15001, -4);
        int result = FixedPointCounterConverter.getFixedPointCounterValue(counter);
        assertEquals(15, result);
    }

    /**
     * Scenario 2: sig=25900, exp=-4
     * real = 25900 × 10^-4 = 2.59
     * deci = round(25900 × 10^-3) = round(25.9) = 26 (HALF_UP rounds .9 up)
     */
    @Test
    public void testScenario2() {
        FixedPointCounter counter = createCounter(25900, -4);
        int result = FixedPointCounterConverter.getFixedPointCounterValue(counter);
        assertEquals(26, result);
    }

    /**
     * Scenario 3: sig=10000, exp=-4
     * real = 10000 × 10^-4 = 1.0
     * deci = round(10000 × 10^-3) = round(10.0) = 10
     */
    @Test
    public void testScenario3() {
        FixedPointCounter counter = createCounter(10000, -4);
        int result = FixedPointCounterConverter.getFixedPointCounterValue(counter);
        assertEquals(10, result);
    }

    /**
     * Scenario 4: sig=50000, exp=-4
     * real = 50000 × 10^-4 = 5.0
     * deci = round(50000 × 10^-3) = round(50.0) = 50
     */
    @Test
    public void testScenario4() {
        FixedPointCounter counter = createCounter(50000, -4);
        int result = FixedPointCounterConverter.getFixedPointCounterValue(counter);
        assertEquals(50, result);
    }

    /**
     * Scenario 5: sig=10, exp=-1
     * real = 10 × 10^-1 = 1.0
     * deci = round(10 × 10^0) = round(10.0) = 10
     */
    @Test
    public void testScenario5() {
        FixedPointCounter counter = createCounter(10, -1);
        int result = FixedPointCounterConverter.getFixedPointCounterValue(counter);
        assertEquals(10, result);
    }

    /**
     * Scenario 6: sig=5, exp=0
     * real = 5 × 10^0 = 5.0
     * deci = round(5 × 10^1) = round(50.0) = 50
     */
    @Test
    public void testScenario6() {
        FixedPointCounter counter = createCounter(5, 0);
        int result = FixedPointCounterConverter.getFixedPointCounterValue(counter);
        assertEquals(50, result);
    }

    /**
     * Scenario 7: sig=3, exp=2
     * real = 3 × 10^2 = 300.0
     * deci = round(3 × 10^3) = round(3000.0) = 3000
     */
    @Test
    public void testScenario7() {
        FixedPointCounter counter = createCounter(3, 2);
        int result = FixedPointCounterConverter.getFixedPointCounterValue(counter);
        assertEquals(3000, result);
    }

    /**
     * Helper method to create a FixedPointCounter with significand and exponent
     */
    private FixedPointCounter createCounter(Integer significand, Integer exponent) {
        FixedPointCounter counter = new FixedPointCounter();
        counter.setSignificand(significand);
        counter.setExponent(exponent);
        return counter;
    }
}

