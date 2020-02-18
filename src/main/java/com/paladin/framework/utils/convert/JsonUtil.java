package com.paladin.framework.utils.convert;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.List;

public class JsonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static void writeJson(OutputStream out, Object value) throws IOException {
        objectMapper.writeValue(out, value);
    }

    public static <T> T parseJson(String json, Class<T> valueType) throws IOException {
        return objectMapper.readValue(json, valueType);
    }

    public static <T> T parseJson(InputStream input, Class<T> valueType) throws IOException {
        return objectMapper.readValue(input, valueType);
    }

    public static String getJson(Object value) throws IOException {
        return objectMapper.writeValueAsString(value);
    }

    public static void writeJson(Writer writer, Object value) throws IOException {
        objectMapper.writeValue(writer, value);
    }

    public static <T> List<T> parseJsonList(String json, Class<T> valueType) throws IOException {
        return objectMapper.readValue(json, new TypeReference<List<T>>() {
        });
    }


}
