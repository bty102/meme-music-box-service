package com.bty.karaoke.mememusicboxservice.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class JsonUtil {
    private final ObjectMapper objectMapper;

    /**
     * Chuyển một Object bất kỳ thành chuỗi JSON dạng inline
     */
    public String toJson(Object object) {
        if (object == null) return null;
        try {
            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {
//            throw new RuntimeException("Parse object to json failed.");
            throw new RuntimeException(e);
        }
    }
}
