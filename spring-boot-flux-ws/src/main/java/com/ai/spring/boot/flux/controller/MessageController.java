package com.ai.spring.boot.flux.controller;

import com.ai.spring.boot.flux.ws.conf.WebSocketContext;
import com.ai.spring.boot.flux.ws.dto.EchoMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * 消息发送控制类
 *
 * @author 石头
 * @Date 2020/6/1
 * @Version 1.0
 **/
@RestController
@Slf4j
public class MessageController {
    @Autowired
    private ServerProperties serverProperties;
    @Autowired
    private WebSocketContext webSocketContext;

    @GetMapping("/hello")
    public Mono<String> hello(){
        String host = serverProperties.getAddress()==null?"127.0.0.1":serverProperties.getAddress().getHostAddress();
        log.info("-----------serverProperties:{}--------------------",serverProperties==null?host:serverProperties.getPort());
        return Mono.create(monoSink -> monoSink.success(host + ":" + serverProperties.getPort()));
    }

    @PostMapping("/sendMsg")
    public Mono<String> sendMessage(@RequestBody EchoMessage message){
        return Mono.create(monoSink -> {
            webSocketContext.sendMessage(message.getTo(),message.getMsg());
            monoSink.success("ok");
        });
    }
}
