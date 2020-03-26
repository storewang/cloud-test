package com.ai.spring.boot.content.util;

import com.ai.spring.boot.content.service.dto.UserDTO;
import com.ai.spring.im.common.bean.Response;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * Json转换
 *
 * @author 石头
 * @Date 2020/3/25
 * @Version 1.0
 **/
public class JacksonJsonParser extends org.springframework.boot.json.JacksonJsonParser{
    private static final TypeReference<?> USERDTO_TYPE = new UserDTOTypeReference();

    public Response<UserDTO> parseUserDTO(String json) {
        try {
            return getMapper().readValue(json, USERDTO_TYPE);
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

    private static class UserDTOTypeReference extends TypeReference<Response<UserDTO>> {

    };
}
