package com.ai.spring.boot.flux.service;

import com.ai.spring.boot.flux.ws.dto.EchoMessage;
import com.ai.spring.boot.flux.ws.util.EchoMessageJsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 消息了送服务
 *
 * @author 石头
 * @Date 2020/6/1
 * @Version 1.0
 **/
@Service
@Slf4j
public class MessageRouteService {
    private static final String HTTP_SCHEMA = "http://";
    private static final String SEND_MSG_METHOD = "/sendMsg";
    @Autowired
    private RedisService redisService;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private WebClient.Builder webClientBuilder;
    public void sendMessage(EchoMessage message,Long messageId){
        // 过滤掉本进程的数据，与本进程关联的数据，之前的接口已经发送了。
        List<String> registHosts = redisService.getRegistHostsWithoutLocal(message.getTo());
        log.info("-----------转发消息messageId : {} registHosts={}------------",messageId,registHosts);
        if (CollectionUtils.isEmpty(registHosts)){
            return;
        }
        registHosts.stream().forEach(host -> {

            String remoteUrl = HTTP_SCHEMA + host + SEND_MSG_METHOD + "/" + messageId;
            //postMessageWithRestTemplate(message,remoteUrl);
            postMessageWithWebClient(message,remoteUrl);

        });
    }

    /**
     * 使用webClient请求客户端
     * @param message
     * @param remoteUrl
     */
    private void postMessageWithWebClient(EchoMessage message,String remoteUrl){
        log.info("-----------转发消息post : {} remoteUrl={}------------",message,remoteUrl);
        Mono<String> bodyToMono = webClientBuilder.build().post().uri(remoteUrl).contentType(MediaType.APPLICATION_JSON_UTF8).syncBody(message).retrieve().bodyToMono(String.class);
        bodyToMono.subscribe(result -> log.info("-----------post : {} result={}------------",remoteUrl,result));
        bodyToMono.doOnError(error -> log.error("-----------post : {} error={}------------",remoteUrl,error));
    }

    /**
     * 使用restTemplate客户端
     * @param message
     * @param remoteUrl
     */
    private void postMessageWithRestTemplate(EchoMessage message,String remoteUrl){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        HttpEntity<String> postEntity = new HttpEntity(EchoMessageJsonUtil.toJson(message),headers);
        String result = restTemplate.postForEntity(remoteUrl,postEntity,String.class).getBody();
        log.info("-----------post : {} result={}------------",remoteUrl,result);
    }
}
