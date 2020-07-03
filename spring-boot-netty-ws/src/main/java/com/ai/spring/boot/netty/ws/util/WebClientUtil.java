package com.ai.spring.boot.netty.ws.util;

import com.ai.spring.boot.netty.ws.model.MessageDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * http client
 *
 * @author 石头
 * @Date 2020/7/2
 * @Version 1.0
 **/
@Slf4j
public class WebClientUtil {
    public static void postMessage(WebClient client, MessageDTO message, String remoteUrl){
        Mono<String> bodyToMono = client.post()
                .uri(remoteUrl)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .syncBody(message)
                .retrieve()
                .bodyToMono(String.class);

        bodyToMono.subscribe(result -> log.info("-----------post : {} result={}------------",remoteUrl,result));
        bodyToMono.doOnError(error -> log.error("-----------post : {} error={}------------",remoteUrl,error));
    }
}
