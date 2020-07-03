package com.ai.spring.boot.netty.ws.handler.msg;

import com.ai.spring.boot.netty.ws.handler.MessageHandler;
import com.ai.spring.boot.netty.ws.model.DispatchMsgRequest;
import com.ai.spring.boot.netty.ws.model.MessageDTO;
import com.ai.spring.boot.netty.ws.model.UserDTO;
import com.ai.spring.boot.netty.ws.service.BusinessThreadService;
import com.ai.spring.boot.netty.ws.service.ServerHandlerService;
import com.ai.spring.boot.netty.ws.util.MessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户上线通知消息,群发
 *
 * @author 石头
 * @Date 2020/7/3
 * @Version 1.0
 **/
@Service("MSG-SERVICE-1")
public class OnlineMsgHandler implements MessageHandler {
    @Autowired
    private ServerHandlerService serverHandlerService;
    @Autowired
    private BusinessThreadService threadService;

    @Override
    public void handler(DispatchMsgRequest request) {
        MessageDTO message = request.getMessage();
        message.setMsgType(MessageType.USER_GROUP.getMsgType());

        threadService.execute(new TextMsgHandlerTask(UserDTO.builder().build(),message,serverHandlerService));
    }
}
