package com.ai.spring.boot.flux.ws.conf;

import lombok.Getter;
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
    @Getter
    private WebSocketSession socketSession;
    @Getter
    private String userId;
    public WebSocketSessionContext(WebSocketSession socketSession,String userId) {
        this.socketSession = socketSession;
        this.userId = userId;
    }

    public void sendData(String mesg){
        socketSession.send(Flux.just(socketSession.textMessage(mesg))).then().toProcessor();
    }
}
