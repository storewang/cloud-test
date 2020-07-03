package com.ai.spring.boot.netty.ws.handler.msg;

import com.ai.spring.boot.netty.ws.model.ClientChannel;
import com.ai.spring.boot.netty.ws.model.MessageDTO;
import com.ai.spring.boot.netty.ws.model.UserDTO;
import com.ai.spring.boot.netty.ws.service.ServerHandlerService;
import com.ai.spring.boot.netty.ws.thread.BusinessThreadTask;
import com.ai.spring.boot.netty.ws.util.MessageJsonUtil;
import com.ai.spring.im.common.util.IdWorker;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;

/**
 * 消息处理任务
 *
 * @author 石头
 * @Date 2020/6/30
 * @Version 1.0
 **/
@Slf4j
public class TextMsgHandlerTask implements BusinessThreadTask {
    private ServerHandlerService serverHandlerService;
    private UserDTO toUser;
    private MessageDTO message;

    public TextMsgHandlerTask(UserDTO toUser,MessageDTO message,ServerHandlerService serverHandlerService){
        this.toUser  = toUser;
        this.message = message;
        this.serverHandlerService = serverHandlerService;
    }

    @Override
    public MessageDTO getMessageDTO() {
        return message;
    }

    @Override
    public void run() {
        Long msgId = null;
        ClientChannel clientChannel = null;
        if (message.getTo()!=null || message.getTos()!=null){
            // 记录消息
            msgId = serverHandlerService.saveMessage(message);
            clientChannel = serverHandlerService.getClientChannelByUcode(toUser.getUserCode());
        }else {
            msgId = IdWorker.getId();
        }

        // 与本机进行连接
        if (clientChannel!=null){
            Channel channel = clientChannel.getChannel();

            channel.writeAndFlush(new TextWebSocketFrame(MessageJsonUtil.toJson(message)));
            // 设置消息已经发送
            serverHandlerService.makeMessageHasSended(msgId);
        }else {
            // 连接不在本机，需要消息进行转发
            serverHandlerService.forwardMesage(message,msgId);
        }
    }
}
