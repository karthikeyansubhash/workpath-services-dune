package com.hp.jetadvantage.link.opensource;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

public class GuavaIntegrationTest {

    @Test
    public void testPreconditions_checkNotNull() {
        // Arrange
        String nonNullString = "hello";

        // Act
        String result = Preconditions.checkNotNull(nonNullString);

        // Assert
        assertEquals(nonNullString, result);
    }

    @Test(expected = NullPointerException.class)
    public void testPreconditions_checkNotNull_throwsException() {
        // Arrange
        String nullString = null;

        // Act
        Preconditions.checkNotNull(nullString, "Input string cannot be null");
    }

    @Test
    public void testImmutableList() {
        // Arrange
        List<String> list = ImmutableList.of("a", "b", "c");

        // Assert
        assertEquals(3, list.size());
        assertEquals("a", list.get(0));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testImmutableList_isImmutable() {
        // Arrange
        List<String> list = ImmutableList.of("a", "b", "c");

        // Act
        list.add("d"); // This should throw an exception
    }

    @Test
    public void testStrings_isNullOrEmpty() {
        // Assert
        assertTrue(Strings.isNullOrEmpty(null));
        assertTrue(Strings.isNullOrEmpty(""));
        assertTrue(!Strings.isNullOrEmpty("hello"));
    }

    @Test
    public void testListsTransform() {
        // Arrange
        List<String> input = ImmutableList.of("a", "bb", "ccc");
        List<Integer> expected = ImmutableList.of(1, 2, 3);

        // Act
        List<Integer> result = Lists.transform(input, new Function<String, Integer>() {
            @Override
            public Integer apply(String input) {
                return input.length();
            }
        });

        // Assert
        assertEquals(expected, result);
    }

    @Test
    public void testSplitter() {
        // Arrange
        String input = "  one, two ,, three, ";
        Iterable<String> expected = ImmutableList.of("one", "two", "three");

        // Act
        Iterable<String> result = Splitter.on(',')
                .trimResults()
                .omitEmptyStrings()
                .split(input);

        // Assert
        assertEquals(expected, ImmutableList.copyOf(result));
    }

    @Test
    public void testJoiner() {
        // Arrange
        List<String> input = Lists.newArrayList("a", "b", null, "c");
        String expected = "a|b|c";

        // Act
        String result = Joiner.on("|").skipNulls().join(input);

        // Assert
        assertEquals(expected, result);
    }
}

