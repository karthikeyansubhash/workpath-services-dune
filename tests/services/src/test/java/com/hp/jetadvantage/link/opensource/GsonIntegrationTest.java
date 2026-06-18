package com.hp.jetadvantage.link.opensource;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class GsonIntegrationTest {

    private Gson gson;

    @Before
    public void setUp() {
        gson = new GsonBuilder().create();
    }

    @Test
    public void testObjectSerialization() {
        // Arrange
        TestObject obj = new TestObject("test", 123, "should not be seen");
        String expectedJson = "{\"stringValue\":\"test\",\"intValue\":123}";

        // Act
        String json = gson.toJson(obj);

        // Assert
        assertEquals(expectedJson, json);
    }

    @Test
    public void testObjectDeserialization() {
        // Arrange
        String json = "{\"stringValue\":\"test\",\"intValue\":123}";

        // Act
        TestObject obj = gson.fromJson(json, TestObject.class);

        // Assert
        assertEquals("test", obj.getStringValue());
        assertEquals(123, obj.getIntValue());
        assertNull(obj.transientValue); // Transient fields are not deserialized
    }

    @Test
    public void testNullFieldSerialization() {
        // Arrange
        Gson gsonWithNulls = new GsonBuilder().serializeNulls().create();
        TestObject obj = new TestObject(null, 1, "transient");
        String expectedJson = "{\"stringValue\":null,\"intValue\":1}";

        // Act
        String json = gsonWithNulls.toJson(obj);

        // Assert
        assertEquals(expectedJson, json);
    }

    @Test
    public void testComplexObjectSerializationAndDeserialization() {
        // Arrange
        NestedObject nested1 = new NestedObject("item1", 1.1);
        NestedObject nested2 = new NestedObject("item2", 2.2);
        java.util.List<NestedObject> itemList = java.util.Arrays.asList(nested1, nested2);
        ComplexObject complexObject = new ComplexObject("complex-id-123", true, nested1, itemList);

        // Act: Serialize
        String json = gson.toJson(complexObject);

        // Assert: Check serialization result
        String expectedJson = "{\"id\":\"complex-id-123\",\"active\":true,\"nested\":{\"name\":\"item1\",\"value\":1.1},\"items\":[{\"name\":\"item1\",\"value\":1.1},{\"name\":\"item2\",\"value\":2.2}]}";
        assertEquals(expectedJson, json);

        // Act: Deserialize
        ComplexObject deserializedObject = gson.fromJson(json, ComplexObject.class);

        // Assert: Check deserialization result
        assertEquals(complexObject.id, deserializedObject.id);
        assertEquals(complexObject.active, deserializedObject.active);
        assertEquals(complexObject.nested.name, deserializedObject.nested.name);
        assertEquals(complexObject.items.size(), deserializedObject.items.size());
        assertEquals(complexObject.items.get(1).name, deserializedObject.items.get(1).name);
    }

    @Test
    public void testComprehensiveObjectSerializationAndDeserialization() {
        // Arrange
        ComprehensiveObject obj = new ComprehensiveObject(
                "test-string",
                12345,
                67890L,
                true,
                TestEnum.VALUE_B,
                new TestObject("nested", 1, "transient")
        );

        // Act
        String json = gson.toJson(obj);

        // Assert Serialization
        String expectedJson = "{\"stringValue\":\"test-string\",\"intValue\":12345,\"longValue\":67890,\"booleanValue\":true,\"enumValue\":\"VALUE_B\",\"nestedObject\":{\"stringValue\":\"nested\",\"intValue\":1}}";
        assertEquals(expectedJson, json);

        // Act
        ComprehensiveObject deserializedObj = gson.fromJson(json, ComprehensiveObject.class);

        // Assert Deserialization
        assertEquals(obj.stringValue, deserializedObj.stringValue);
        assertEquals(obj.intValue, deserializedObj.intValue);
        assertEquals(obj.longValue, deserializedObj.longValue);
        assertEquals(obj.booleanValue, deserializedObj.booleanValue);
        assertEquals(obj.enumValue, deserializedObj.enumValue);
        assertEquals(obj.nestedObject.getStringValue(), deserializedObj.nestedObject.getStringValue());
    }

    // =================================================================================
    // Helper Classes
    // =================================================================================

    private enum TestEnum {
        VALUE_A,
        VALUE_B,
        VALUE_C
    }

    private static class ComprehensiveObject {
        private String stringValue;
        private int intValue;
        private long longValue;
        private boolean booleanValue;
        private TestEnum enumValue;
        private TestObject nestedObject;

        public ComprehensiveObject(String stringValue, int intValue, long longValue, boolean booleanValue, TestEnum enumValue, TestObject nestedObject) {
            this.stringValue = stringValue;
            this.intValue = intValue;
            this.longValue = longValue;
            this.booleanValue = booleanValue;
            this.enumValue = enumValue;
            this.nestedObject = nestedObject;
        }
    }

    private static class TestObject {
        private String stringValue;
        private int intValue;
        private transient String transientValue; // This should not be serialized

        public TestObject(String stringValue, int intValue, String transientValue) {
            this.stringValue = stringValue;
            this.intValue = intValue;
            this.transientValue = transientValue;
        }

        // Getters are needed for comparison, but not for gson serialization/deserialization
        public String getStringValue() {
            return stringValue;
        }

        public int getIntValue() {
            return intValue;
        }
    }

    private static class ComplexObject {
        private String id;
        private boolean active;
        private NestedObject nested;
        private java.util.List<NestedObject> items;

        public ComplexObject(String id, boolean active, NestedObject nested, java.util.List<NestedObject> items) {
            this.id = id;
            this.active = active;
            this.nested = nested;
            this.items = items;
        }
    }

    private static class NestedObject {
        private String name;
        private double value;

        public NestedObject(String name, double value) {
            this.name = name;
            this.value = value;
        }
    }
}
