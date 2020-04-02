package com.ai.spring.boot.im.ws;

import com.ai.spring.boot.im.util.WebSocketUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.HashMap;
import java.util.Map;

/**
 * ws握手拦截器，用于鉴权
 *
 * @author 石头
 * @Date 2020/3/3
 * @Version 1.0
 **/
@Component
@Slf4j
public class MessageHandshakeInterceptor implements HandshakeInterceptor {
    private static final String DELIMITER = "/";
    private static final Map<String,Long> USER_TOKENS = new HashMap<>(5);
    static {
        USER_TOKENS.put("abc001",1L);
        USER_TOKENS.put("abc002",2L);
        USER_TOKENS.put("abc003",3L);
    }
    /**
     * 握手成功前，可在此进行用户鉴权处理
     * @param serverHttpRequest
     * @param serverHttpResponse
     * @param webSocketHandler
     * @param attributes     这里可以存放鉴权成功或是失败的数据，用于向下传播(afterConnectionEstablished这个方法中可以获取)
     * @return
     * @throws Exception
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Map<String, Object> attributes) throws Exception {
        String path = serverHttpRequest.getURI().getPath();
        log.info("------握手,path={}------",path);
        if (path.startsWith(DELIMITER)){
            path = path.substring(DELIMITER.length());
        }
        String[] params = StringUtils.split(path,DELIMITER);
        log.info("------握手,params.length={}------",params.length);
        if (params.length!=2){
            WebSocketUtil.setWebSocketSessionErrCode(attributes);
        }else {
            String token = params[1];
            Long  userId = USER_TOKENS.get(token);
            if (userId!=null){
                WebSocketUtil.setWebSocketSessionUid(attributes,userId);
                WebSocketUtil.setWebSocketSessionSucCode(attributes);
            }else {
                WebSocketUtil.setWebSocketSessionErrCode(attributes);
            }
        }

        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, @Nullable Exception e) {
        log.info("------握手成功------");
    }
}
