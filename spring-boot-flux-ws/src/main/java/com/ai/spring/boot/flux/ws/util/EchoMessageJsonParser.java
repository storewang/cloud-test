package com.ai.spring.boot.flux.ws.util;

import com.ai.spring.boot.flux.ws.dto.EchoMessage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * Json转换
 *
 * @author 石头
 * @Date 2020/5/28
 * @Version 1.0
 **/
@Slf4j
public class EchoMessageJsonParser extends org.springframework.boot.json.JacksonJsonParser{
    private static final TypeReference<?> echoMessage_TYPE = new EchoMessageTypeReference();

    private EchoMessageJsonParser(){}
    static class InnerHolder{
        private static final EchoMessageJsonParser JSONPARSER = new EchoMessageJsonParser();
    }
    public static EchoMessageJsonParser getInstance(){
        return InnerHolder.JSONPARSER;
    }

    public EchoMessage parseEchoMessage(String json) {
        try {
            return getMapper().readValue(json, echoMessage_TYPE);
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
    private static class EchoMessageTypeReference extends TypeReference<EchoMessage> {
    }
}
