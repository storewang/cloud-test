package com.ai.spring.boot.netty.ws.util;

import com.ai.spring.boot.netty.ws.model.MessageDTO;
import com.ai.spring.im.common.util.StringUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * message dto json 转换
 *
 * @author 石头
 * @Date 2020/6/29
 * @Version 1.0
 **/
public final class MessageJsonUtil {
    private static final String EMPTY_JSON = "{}";
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final TypeReference<MessageDTO> MSGTYPE = new TypeReference<MessageDTO>(){};

    public static String toJson(MessageDTO messageDTO){
        try {
            String msgJson = MAPPER.writeValueAsString(messageDTO);
            return msgJson;
        }catch (IOException e){
            return EMPTY_JSON;
        }
    }

    public static MessageDTO readJson(String json){
        if (StringUtil.isEmpty(json)){
            return null;
        }

        try{
            MessageDTO messageDTO = MAPPER.readValue(json,MSGTYPE);
            return messageDTO;
        }catch (IOException e){
            return null;
        }
    }

}
