package com.ai.spring.boot.flux.ws.service.impl;

import com.ai.spring.boot.flux.ws.conf.WebSocketSessionContext;
import com.ai.spring.boot.flux.ws.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;

/**
 * PING 消息
 *
 * @author 石头
 * @Date 2020/5/29
 * @Version 1.0
 **/
@Service("MSG-SERVICE-PING")
@Slf4j
public class PingMessageService implements MessageService {
    private static final byte[] PING_DATA = new byte[256];
    @Override
    public void handlerMsg(WebSocketSessionContext session, String content) {
        log.info("--------收到ping消息:{}--------------", content);
        WebSocketSession socketSession = session.getSocketSession();
        socketSession.send(Flux.just(socketSession.pongMessage(s -> s.wrap(PING_DATA)))).then().toProcessor();
    }
}
