package com.ai.spring.boot.netty.ws.handler.msg;

import com.ai.spring.boot.netty.ws.handler.MessageHandler;
import com.ai.spring.boot.netty.ws.model.DispatchMsgRequest;
import com.ai.spring.boot.netty.ws.model.MessageDTO;
import com.ai.spring.boot.netty.ws.util.MessageJsonUtil;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.stereotype.Service;

/**
 * 用户上线消息处理
 *
 * @author 石头
 * @Date 2020/6/30
 * @Version 1.0
 **/
@Service("MSG-SERVICE-DEF")
public class DefaultMsgHandler implements MessageHandler {
    @Override
    public void handler(DispatchMsgRequest request) {
        MessageDTO message = request.getMessage();
        Channel channel = request.getChannel().getChannel();

        channel.writeAndFlush(new TextWebSocketFrame(MessageJsonUtil.toJson(message)));
    }
}
