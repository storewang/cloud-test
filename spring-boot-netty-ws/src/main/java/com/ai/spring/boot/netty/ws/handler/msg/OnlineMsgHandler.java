package com.ai.spring.boot.netty.ws.handler.msg;

import com.ai.spring.boot.netty.ws.dao.IMessageRecordDao;
import com.ai.spring.boot.netty.ws.dao.bean.MessageRecord;
import com.ai.spring.boot.netty.ws.handler.MessageHandler;
import com.ai.spring.boot.netty.ws.model.DispatchMsgRequest;
import com.ai.spring.boot.netty.ws.model.MessageDTO;
import com.ai.spring.boot.netty.ws.model.UserDTO;
import com.ai.spring.boot.netty.ws.service.BusinessThreadService;
import com.ai.spring.boot.netty.ws.service.ServerHandlerService;
import com.ai.spring.boot.netty.ws.util.MessageJsonUtil;
import com.ai.spring.boot.netty.ws.util.MessageType;
import com.ai.spring.boot.netty.ws.util.MessageUtil;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    @Autowired
    private IMessageRecordDao messageRecordDao;
    @Override
    public void handler(DispatchMsgRequest request) {
        MessageDTO message = request.getMessage();
        message.setMsgType(MessageType.USER_GROUP.getMsgType());
        // 发送消息通知
        threadService.execute(new TextMsgHandlerTask(UserDTO.builder().build(),message,serverHandlerService));

        // 摘取发送给自己的离线消息，进行推送
        List<MessageRecord> notSendedMessage = messageRecordDao.getNotSendedMessage(message.getFrom().getUserCode());
        notSendedMessage.stream().forEach(messageRecord -> {
            MessageDTO messageDTO = MessageUtil.messageRecord2MessageDTO(messageRecord);
            messageDTO.setMsgType(MessageType.MSG_CONTENT.getMsgType());

            Channel channel = request.getChannel().getChannel();
            channel.writeAndFlush(new TextWebSocketFrame(MessageJsonUtil.toJson(message)));
        });
    }
}
