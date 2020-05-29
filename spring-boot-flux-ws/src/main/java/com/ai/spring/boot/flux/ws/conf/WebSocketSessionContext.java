package com.ai.spring.boot.flux.ws.conf;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;

/**
 * web socket session
 *
 * @author 石头
 * @Date 2020/5/28
 * @Version 1.0
 **/
@Slf4j
public class WebSocketSessionContext {
    private WebSocketSession socketSession;

    public WebSocketSessionContext(WebSocketSession socketSession) {
        this.socketSession = socketSession;
    }

    public void sendData(String mesg){
        socketSession.send(Flux.just(socketSession.textMessage(mesg))).then().toProcessor();
    }
}
