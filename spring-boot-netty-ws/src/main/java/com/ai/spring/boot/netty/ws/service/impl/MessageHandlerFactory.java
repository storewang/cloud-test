package com.ai.spring.boot.netty.ws.service.impl;

import com.ai.spring.boot.netty.ws.handler.MessageHandler;
import com.ai.spring.boot.netty.ws.model.DispatchMsgRequest;
import com.ai.spring.boot.netty.ws.util.MessageType;
import com.ai.spring.boot.netty.ws.util.MessageTypeUtil;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * 消息处理工厂类
 *
 * @author 石头
 * @Date 2020/6/30
 * @Version 1.0
 **/
@Service("messageHandlerFactory")
public class MessageHandlerFactory implements MessageHandler,ApplicationContextAware {
    private static final String DEF_HANDER_NAME = "MSG-SERVICE-DEF";
    private static final String TXT_HANDER_NAME = "MSG-SERVICE-TEXT";
    private static final String BEAT_HANDER_NAME = "MSG-SERVICE-PONG";
    private ApplicationContext context;
    @Override
    public void handler(DispatchMsgRequest request) {
        MessageType msgType = MessageTypeUtil.getMessageTypeByType(request.getMessage().getMsgType());
        MessageHandler messageHandler;

        switch (msgType){
            case USER_ERROR:
            case ACCESS_DENIED:
            case MSG_JSON_ERROR:
            case USER_ONLINE:
            case USER_OFFLINE:
                messageHandler = context.getBean(DEF_HANDER_NAME,MessageHandler.class);
                break;
            case HEART_BEAT:
                messageHandler = context.getBean(BEAT_HANDER_NAME,MessageHandler.class);
                break;
            default:
                messageHandler = context.getBean(TXT_HANDER_NAME,MessageHandler.class);
                break;
        }
        messageHandler.handler(request);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }
}
