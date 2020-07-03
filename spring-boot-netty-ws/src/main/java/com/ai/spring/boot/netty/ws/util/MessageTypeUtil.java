package com.ai.spring.boot.netty.ws.util;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 工具类
 *
 * @author 石头
 * @Date 2020/6/30
 * @Version 1.0
 **/
public final class MessageTypeUtil {
    public static MessageType getMessageTypeByType(Integer type){
        MessageType[] types = MessageType.values();
        List<MessageType> typeList = Arrays.stream(types).filter(messageType -> messageType.getMsgType().equals(type)).collect(Collectors.toList());
        return Optional.ofNullable(typeList).map(list ->{
            if (list.size()>0){
                return list.get(0);
            }
            return null;
        }).orElse(null);
    }

    public static void main(String[] args) {
        MessageType type = getMessageTypeByType(2);
        System.out.println("1====>"+type);
        type = getMessageTypeByType(5);
        System.out.println("1====>"+type);
    }
}
