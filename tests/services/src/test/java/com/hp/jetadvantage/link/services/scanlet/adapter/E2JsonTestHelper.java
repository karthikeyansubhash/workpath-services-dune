package com.hp.jetadvantage.link.services.scanlet.adapter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Test utility to convert full E2 protocol JSON format to simplified format
 * that can be deserialized by E2 model classes (DefaultOptions, Profile, etc.).
 *
 * Full E2 format (device response):
 *   "colorMode": {"jsonLiteral": "cmColor", "value": "cmColor", "typeGUN": "...", ...}
 *   "contrast": {"value": 4, "typeGUN": "...", ...}
 *
 * Simplified format (model-compatible):
 *   "colorMode": "cmColor"
 *   "contrast": 4
 */
class E2JsonTestHelper {

    private static final Set<String> E2_META_FIELDS = Set.of(
            "typeGUN", "typeName", "versionAdded", "majorVersion"
    );

    /**
     * Simplify a full E2 protocol JSON string to the simplified format
     * that E2 model classes can deserialize.
     * Safe to call on already-simplified JSON (idempotent).
     */
    static String simplifyE2Json(String rawJson) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(rawJson);
        JsonNode simplified = simplifyNode(root, mapper);
        return mapper.writeValueAsString(simplified);
    }

    private static JsonNode simplifyNode(JsonNode node, ObjectMapper mapper) {
        if (node.isArray()) {
            ArrayNode arr = mapper.createArrayNode();
            for (JsonNode item : node) {
                arr.add(simplifyNode(item, mapper));
            }
            return arr;
        }

        if (!node.isObject()) {
            return node;
        }

        ObjectNode obj = (ObjectNode) node;

        // E2 enum type: has "jsonLiteral" → extract as string
        if (obj.has("jsonLiteral")) {
            return new TextNode(obj.get("jsonLiteral").asText());
        }

        // E2 polymorphInstance binding (e.g., fileName) → convert to explicit format
        if (obj.has("polymorphInstance")) {
            ObjectNode result = mapper.createObjectNode();
            ObjectNode explicit = mapper.createObjectNode();
            JsonNode poly = obj.get("polymorphInstance");
            if (poly.has("explicitValue")) {
                explicit.put("explicitValue", poly.get("explicitValue").asText());
            }
            result.set("explicit", explicit);
            return result;
        }

        // E2 collection wrapper: has "items" array + "typeGUN" → flatten to array
        if (obj.has("items") && obj.get("items").isArray() && obj.has("typeGUN")) {
            ArrayNode arr = mapper.createArrayNode();
            for (JsonNode item : obj.get("items")) {
                arr.add(simplifyNode(item, mapper));
            }
            return arr;
        }

        // E2 alias type (Unsigned32 etc.): has "value" + "typeGUN" → extract value
        if (obj.has("value") && obj.has("typeGUN")) {
            return obj.get("value");
        }

        // Generic object: recurse children, skip E2 metadata fields, rename opMeta
        ObjectNode result = mapper.createObjectNode();
        Iterator<Map.Entry<String, JsonNode>> fields = obj.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            String key = entry.getKey();
            if (E2_META_FIELDS.contains(key)) continue;
            String outKey = key.equals("opMeta") ? "$opMeta" : key;
            result.set(outKey, simplifyNode(entry.getValue(), mapper));
        }
        return result;
    }
}
