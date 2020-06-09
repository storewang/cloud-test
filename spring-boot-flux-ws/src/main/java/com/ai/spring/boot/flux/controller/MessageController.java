package com.ai.spring.boot.flux.controller;

import com.ai.spring.boot.flux.dto.FluxEchoMessageDTO;
import com.ai.spring.boot.flux.service.FluxEchoMessageService;
import com.ai.spring.boot.flux.ws.conf.WebSocketContext;
import com.ai.spring.boot.flux.ws.dto.EchoMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

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
    @Autowired
    private FluxEchoMessageService messageService;
    @GetMapping("/hello")
    public Mono<String> hello(){
        String host = serverProperties.getAddress()==null?"127.0.0.1":serverProperties.getAddress().getHostAddress();
        log.info("-----------serverProperties:{}--------------------",serverProperties==null?host:serverProperties.getPort());
        return Mono.create(monoSink -> monoSink.success(host + ":" + serverProperties.getPort()));
    }

    @PostMapping("/sendMsg/{messageId}")
    public Mono<String> sendMessage(@PathVariable("messageId") Long messageId, @RequestBody EchoMessage message){
        return Mono.create(monoSink -> {
            webSocketContext.sendMessage(message.getTo(),message.getMsg(),messageId);
            monoSink.success("ok");
        });
    }

    @GetMapping("/listMsg/{userId}")
    public Flux<FluxEchoMessageDTO> getNewListMessage(@PathVariable("userId") Long userId, @RequestParam("messageId") Long messageId){
        List<FluxEchoMessageDTO> messageDTOS = messageService.getNewListMessage(userId, messageId);
        return Flux.fromIterable(messageDTOS);
    }
}
