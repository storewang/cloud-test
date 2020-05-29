package com.ai.spring.boot.flux.ws.util;

import com.ai.spring.boot.flux.ws.dto.EchoMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * echo message josn util
 *
 * @author 石头
 * @Date 2020/5/28
 * @Version 1.0
 **/
public final class EchoMessageJsonUtil {
    private static final GsonBuilder INSTANCE = new GsonBuilder();

    private static Gson create() {
        return INSTANCE.create();
    }

    public static <T>String toJson(T data){
        return create().toJson(data);
    }

    public static EchoMessage parser(String json){
        return EchoMessageJsonParser.getInstance().parseEchoMessage(json);
    }
}
