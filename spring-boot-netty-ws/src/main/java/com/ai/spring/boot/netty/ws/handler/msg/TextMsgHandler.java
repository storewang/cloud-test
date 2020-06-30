package com.ai.spring.boot.netty.ws.handler.msg;

import com.ai.spring.boot.netty.ws.handler.MessageHandler;
import com.ai.spring.boot.netty.ws.model.DispatchMsgRequest;
import com.ai.spring.boot.netty.ws.model.MessageDTO;
import com.ai.spring.boot.netty.ws.model.UserDTO;
import com.ai.spring.boot.netty.ws.service.BusinessThreadService;
import com.ai.spring.boot.netty.ws.service.ServerHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 消息处理
 *
 * @author 石头
 * @Date 2020/6/30
 * @Version 1.0
 **/
@Service("MSG-SERVICE-TEXT")
public class TextMsgHandler implements MessageHandler {
    @Autowired
    private ServerHandlerService serverHandlerService;
    @Autowired
    private BusinessThreadService threadService;

    @Override
    public void handler(DispatchMsgRequest request) {
        MessageDTO message = request.getMessage();
        UserDTO to = message.getTo();
        List<UserDTO> tos = message.getTos();
        // 单回
        if (to!=null){
            sendMsg(to,message);
        }else if (tos!=null && tos.size()>0){
            // 群回
            tos.stream().forEach(toUser -> sendMsg(toUser,message));
        }
    }

    private void sendMsg(UserDTO to,MessageDTO message){
        message.setTo(to);
        message.setTos(null);

        threadService.execute(new TextMsgHandlerTask(to,message,serverHandlerService));
    }
}
