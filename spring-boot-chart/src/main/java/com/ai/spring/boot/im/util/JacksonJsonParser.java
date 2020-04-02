package com.ai.spring.boot.im.util;

import com.ai.spring.boot.im.dto.CharMessageDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * Json转换
 *
 * @author 石头
 * @Date 2020/4/2
 * @Version 1.0
 **/
public class JacksonJsonParser extends org.springframework.boot.json.JacksonJsonParser{
    private JacksonJsonParser(){}
    static class InnerHolder{
        private static final JacksonJsonParser JSONPARSER = new JacksonJsonParser();
    }

    public static JacksonJsonParser getInstance(){
        return InnerHolder.JSONPARSER;
    }

    private static final TypeReference<?> CharMessageDTO_TYPE = new CharMessageDTOTypeReference();

    public CharMessageDTO parseCharMessageDTO(String json) {
        try {
            return getMapper().readValue(json, CharMessageDTO_TYPE);
        }
        catch (Exception ex) {
            throw new IllegalArgumentException("Cannot parse JSON", ex);
        }
    }

    private ObjectMapper getMapper() {
        Method method = ReflectionUtils.findMethod(this.getClass(), "getObjectMapper");
        method.setAccessible(true);
        return (ObjectMapper)ReflectionUtils.invokeMethod(method,this);
    }
    private static class CharMessageDTOTypeReference extends TypeReference<CharMessageDTO> {
    };
}
