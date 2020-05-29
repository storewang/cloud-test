package com.ai.spring.boot.flux.ws.service.impl;

import com.ai.spring.boot.flux.ws.conf.WebSocketContext;
import com.ai.spring.boot.flux.ws.conf.WebSocketSessionContext;
import com.ai.spring.boot.flux.ws.dto.EchoMessage;
import com.ai.spring.boot.flux.ws.service.MessageService;
import com.ai.spring.boot.flux.ws.util.Constans;
import com.ai.spring.boot.flux.ws.util.EchoMessageJsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * TEXT 消息
 *
 * @author 石头
 * @Date 2020/5/29
 * @Version 1.0
 **/
@Service("MSG-SERVICE-TEXT")
@Slf4j
public class TXTMessageService implements MessageService,ApplicationContextAware {
    private static final String TEXT_ERROR = "TEXT-ERROR";
    private ApplicationContext context;
    @Autowired
    private WebSocketContext webSocketContext;

    @Override
    public void handlerMsg(WebSocketSessionContext session, String content) {
        log.info("--------收到消息:--------------", content);
        EchoMessage echoMessage;
        try{
            echoMessage = EchoMessageJsonUtil.parser(content);
        }catch (Throwable e){
            echoMessage = null;
        }
        if (echoMessage == null){
            log.info("--------消息解释失败:{}--------------", content);
            MessageService messageService = context.getBean(getBeanName(TEXT_ERROR),com.ai.spring.boot.flux.ws.service.MessageService.class);
            messageService.handlerMsg(session,content);
        }else {
            EchoMessage toMessage = new EchoMessage();
            toMessage.setFrom(session.getUserId());
            toMessage.setMsg(echoMessage.getMsg());
            toMessage.setMsgType(echoMessage.getMsgType());
            toMessage.setTo(echoMessage.getTo());

            webSocketContext.sendMessage(toMessage);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    private String getBeanName(String name){
        return Constans.SERVICE_NAME + name;
    }
}
