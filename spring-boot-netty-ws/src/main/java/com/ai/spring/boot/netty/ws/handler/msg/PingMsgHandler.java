package com.ai.spring.boot.netty.ws.handler.msg;

import com.ai.spring.boot.netty.ws.handler.MessageHandler;
import com.ai.spring.boot.netty.ws.model.DispatchMsgRequest;
import com.ai.spring.boot.netty.ws.model.MessageDTO;
import com.ai.spring.boot.netty.ws.util.MessageJsonUtil;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.stereotype.Service;

/**
 * 心跳消息
 *
 * @author 石头
 * @Date 2020/6/30
 * @Version 1.0
 **/
@Service("MSG-SERVICE-PONG")
public class PingMsgHandler implements MessageHandler {
    private static final String PONG_MSG = "PONG";
    @Override
    public void handler(DispatchMsgRequest request) {
        MessageDTO message = request.getMessage();
        message.setContent(PONG_MSG);
        Channel channel = request.getChannel().getChannel();

        channel.writeAndFlush(new TextWebSocketFrame(MessageJsonUtil.toJson(message)));
    }
}
