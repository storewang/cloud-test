package com.ai.spring.boot.netty.ws.controller;

import com.ai.spring.boot.netty.ws.conf.ServerProperties;
import com.ai.spring.boot.netty.ws.model.MessageDTO;
import com.ai.spring.boot.netty.ws.service.ServerHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 *
 * @author 石头
 * @Date 2020/7/1
 * @Version 1.0
 **/
@RestController
public class HomeController {
    @Autowired
    private ServerProperties serverProperties;
    @Autowired
    private ServerHandlerService serverHandlerService;
    @GetMapping("/")
    public Mono<String> home(){
        return Mono.create(monoSink -> monoSink.success(serverProperties.getHost() + ":" + serverProperties.getPort()));
    }

    @PostMapping("/sendMsg/{messageId}")
    public Mono<String> forwardMesage(@RequestBody MessageDTO message, @PathVariable("messageId")Long msgId){
        return Mono.create(monoSink -> monoSink.success(serverHandlerService.sendMessage(message,msgId)));
    }

    @GetMapping("/login/{userId}")
    public Mono<String> login(@PathVariable("userId")Long userId){
        return Mono.create(monoSink -> monoSink.success(serverHandlerService.login(userId)));
    }
}
