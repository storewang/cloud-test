package com.ai.spring.boot.flux.ws.service.impl;

import com.ai.spring.boot.flux.ws.conf.WebSocketSessionContext;
import com.ai.spring.boot.flux.ws.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * PONG 消息
 *
 * @author 石头
 * @Date 2020/5/29
 * @Version 1.0
 **/
@Service("MSG-SERVICE-PONG")
@Slf4j
public class PongMessageService implements MessageService {
    @Override
    public void handlerMsg(WebSocketSessionContext session, String content) {
        log.info("--------收到pong消息:{}--------------", content);
    }
}
