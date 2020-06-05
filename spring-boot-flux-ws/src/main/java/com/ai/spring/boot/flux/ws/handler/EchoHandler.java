package com.ai.spring.boot.flux.ws.handler;

import com.ai.spring.boot.flux.ws.annotion.WebSoketMapping;
import com.ai.spring.boot.flux.ws.conf.WebSocketContext;
import com.ai.spring.boot.flux.ws.conf.WebSocketSessionContext;
import com.ai.spring.boot.flux.ws.dto.EchoMessage;
import com.ai.spring.boot.flux.ws.util.Constans;
import com.ai.spring.boot.flux.ws.util.EchoMessageJsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.socket.HandshakeInfo;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 消息回显
 *
 * @author 石头
 * @Date 2020/5/28
 * @Version 1.0
 **/
@Component
@Slf4j
@WebSoketMapping("/echo")
public class EchoHandler implements WebSocketHandler{
    @Autowired
    private WebSocketContext webSocketContext;
    @Autowired
    @Qualifier("messageServiceFactory")
    private MessageService messageService;
    @Override
    public Mono<Void> handle(WebSocketSession session) {
        HandshakeInfo handshakeInfo = session.getHandshakeInfo();
        String token = getToken4Header(handshakeInfo);
        if (StringUtils.isEmpty(token)){
            token = getToken2Queryparam(handshakeInfo);
        }

        log.info("--------login token:{}--------",token);
        final String from = token;

        Mono<Void> mono = session.receive().doOnSubscribe(s -> {
            log.info("--------发起连接:{}--------------", session.getId());
            // 添加用户连接注册信息
            webSocketContext.addSocketSession(session.getId(),from,new WebSocketSessionContext(session,from));
            // TODO 拉取用户对应的还未发送的消息，进行消息推送(这里只拉取那些消息发送给自己的，也就是to指向自己的消息)
        })

        .doOnTerminate(() -> {
            log.info("--------关闭连接:{}--------------", session.getId());
            webSocketContext.removeSocketSessionWithSessionId(session.getId()); })

        .doOnComplete(() -> log.info("--------连接完成:{}--------------", session.getId()) )

        .doOnCancel(() -> log.info("--------连接取消:{}--------------", session.getId()) )

        .doOnNext(message -> {
            WebSocketSessionContext socketSessionContext = webSocketContext.getSocketSessionWithSessionId(session.getId());
            messageService.handlerMsg(socketSessionContext,message.getType(),message.getPayloadAsText()); })

        .doOnError(e -> log.info("--------发生错误.--------------", e) )

        .doOnRequest(r -> log.info("--------发送请求:{}.--------------", session.getId()) )
        .then();

        return mono;
    }

    private String getToken4Header(HandshakeInfo handshakeInfo){
        List<String> list = handshakeInfo.getHeaders().entrySet().stream().filter(entry -> {
            if(Constans.PARAM_TOKEN.equals(entry.getKey()) && !CollectionUtils.isEmpty(entry.getValue())){
                return true;
            }
            return false;
        }).map(entry -> entry.getValue().get(0)).collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(list)){
            return list.get(0);
        }
        return Constans.EMPTY_STR;
    }
    private String getToken2Queryparam(HandshakeInfo handshakeInfo){
        Map<String,String>  queryMap= getQueryMap(handshakeInfo.getUri().getQuery());
        String token = queryMap.get(Constans.PARAM_TOKEN);
        return token;
    }
    private Map<String,String> getQueryMap(String query){
        Map<String,String> queryMap = new HashMap<>();
        if (!StringUtils.isEmpty(query)){
            String[] queryParam = query.split(Constans.PARAM_SPLIT);
            Arrays.stream(queryParam).forEach(param -> {
                String[] kv  = param.split(Constans.PARAM_KV_SPLIT,2);
                String value = kv.length == 2? kv[1] : Constans.EMPTY_STR;
                queryMap.put(kv[0],value);
            });
        }
        return queryMap;
    }
}
