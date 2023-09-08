package com.cach.CachApp.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ParseJsonClass {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static Object parseJson(String jsonString) throws IOException {
        return objectMapper.readValue(jsonString, Object.class);
    }
}
