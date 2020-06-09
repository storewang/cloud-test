package com.ai.spring.boot.flux.ws.service.impl;

import com.ai.spring.boot.flux.ws.conf.WebSocketContext;
import com.ai.spring.boot.flux.ws.conf.WebSocketSessionContext;
import com.ai.spring.boot.flux.ws.dto.EchoMessage;
import com.ai.spring.boot.flux.ws.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 回复错误提示消息
 *
 * @author 石头
 * @Date 2020/5/29
 * @Version 1.0
 **/
@Service("MSG-SERVICE-TEXT-ERROR")
@Slf4j
public class ErrorMessageService implements MessageService {
    private static final String ERROR_MSG = "消息解释失败，不予处理。";
    @Autowired
    private WebSocketContext webSocketContext;
    @Override
    public void handlerMsg(WebSocketSessionContext session, String content) {
        log.info("--------有问题的消息:{}--------------", content);
        String from = session.getUserId();
        String sessionId = session.getSocketSession().getId();
        String errMsg = getErrorMsg(content);

        EchoMessage errorMessage = new EchoMessage();
        errorMessage.setFrom(from);
        errorMessage.setMsg(errMsg);
        webSocketContext.sendMessage(errorMessage,sessionId,null);
    }

    protected String getErrorMsg(String content){
        return ERROR_MSG;
    }
}
