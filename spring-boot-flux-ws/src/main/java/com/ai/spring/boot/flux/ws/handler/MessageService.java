package com.ai.spring.boot.flux.ws.handler;

import com.ai.spring.boot.flux.ws.conf.WebSocketSessionContext;
import com.ai.spring.boot.flux.ws.util.Constans;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketMessage;

import java.util.Arrays;
import java.util.List;

/**
 * 消息服务
 *
 * @author 石头
 * @Date 2020/5/29
 * @Version 1.0
 **/
@Service("messageServiceFactory")
@Slf4j
public class MessageService implements ApplicationContextAware{
    private static final String TYPE_ERROR = "TYPE-ERROR";
    private List<WebSocketMessage.Type> suportedTypes = Arrays.asList(WebSocketMessage.Type.PING,WebSocketMessage.Type.PONG,WebSocketMessage.Type.TEXT);
    private ApplicationContext context;

    public void handlerMsg(WebSocketSessionContext session, WebSocketMessage.Type type, String content){
        com.ai.spring.boot.flux.ws.service.MessageService messageService = null;
        if (!suportedTypes.contains(type)){
            log.info("--------收到不支持的消息类型：{}--------------", type);
            messageService = context.getBean(getBeanName(TYPE_ERROR),com.ai.spring.boot.flux.ws.service.MessageService.class);
        }else {
            messageService = context.getBean(getBeanName(type.name()),com.ai.spring.boot.flux.ws.service.MessageService.class);
        }
        // 处理消息
        messageService.handlerMsg(session,content);
    }
    private String getBeanName(String name){
        return Constans.SERVICE_NAME + name;
    }
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
