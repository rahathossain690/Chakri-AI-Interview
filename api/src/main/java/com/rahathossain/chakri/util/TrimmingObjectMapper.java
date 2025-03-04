package com.rahathossain.chakri.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class TrimmingObjectMapper extends ObjectMapper {

    public TrimmingObjectMapper() {
        super();

        SimpleModule module = new SimpleModule();
        module.addDeserializer(JsonNode.class, new TrimmingJsonDeserializer());
        module.addSerializer(JsonNode.class, new TrimmingJsonSerializer());

        this.registerModule(module);
    }

    static class TrimmingJsonDeserializer extends StdDeserializer<JsonNode> {
        protected TrimmingJsonDeserializer() {
            super(JsonNode.class);
        }

        @Override
        public JsonNode deserialize(JsonParser parser, DeserializationContext context) throws IOException {
            ObjectMapper defaultMapper = new ObjectMapper();
            JsonNode root = defaultMapper.readTree(parser);

            return trimJsonNode(root);
        }

        private JsonNode trimJsonNode(JsonNode node) {
            if (node.isObject()) {
                ObjectNode trimmedNode = JsonNodeFactory.instance.objectNode();
                Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
                while (fields.hasNext()) {
                    Map.Entry<String, JsonNode> entry = fields.next();
                    String trimmedKey = entry.getKey().trim();
                    trimmedNode.set(trimmedKey, trimJsonNode(entry.getValue()));
                }
                return trimmedNode;
            } else if (node.isTextual()) {
                return JsonNodeFactory.instance.textNode(node.asText().trim());
            } else if (node.isArray()) {
                ArrayNode trimmedArray = JsonNodeFactory.instance.arrayNode();
                for (JsonNode item : node) {
                    trimmedArray.add(trimJsonNode(item));
                }
                return trimmedArray;
            } else {
                return node;
            }
        }
    }

    static class TrimmingJsonSerializer extends StdSerializer<JsonNode> {
        protected TrimmingJsonSerializer() {
            super(JsonNode.class);
        }

        @Override
        public void serialize(JsonNode node, JsonGenerator generator, SerializerProvider provider) throws IOException {
            ObjectMapper defaultMapper = new ObjectMapper();
            JsonNode trimmedNode = trimJsonNode(node);
            defaultMapper.writeValue(generator, trimmedNode);
        }

        private JsonNode trimJsonNode(JsonNode node) {
            if (node.isObject()) {
                ObjectNode trimmedNode = JsonNodeFactory.instance.objectNode();
                Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
                while (fields.hasNext()) {
                    Map.Entry<String, JsonNode> entry = fields.next();
                    String trimmedKey = entry.getKey().trim();
                    trimmedNode.set(trimmedKey, trimJsonNode(entry.getValue()));
                }
                return trimmedNode;
            } else if (node.isTextual()) {
                return JsonNodeFactory.instance.textNode(node.asText().trim());
            } else if (node.isArray()) {
                ArrayNode trimmedArray = JsonNodeFactory.instance.arrayNode();
                for (JsonNode item : node) {
                    trimmedArray.add(trimJsonNode(item));
                }
                return trimmedArray;
            } else {
                return node;
            }
        }
    }
}
