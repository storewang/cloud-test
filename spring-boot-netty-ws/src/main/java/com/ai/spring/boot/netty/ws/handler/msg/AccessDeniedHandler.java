package com.ai.spring.boot.netty.ws.handler.msg;

import com.ai.spring.boot.netty.ws.handler.MessageHandler;
import com.ai.spring.boot.netty.ws.model.DispatchMsgRequest;
import com.ai.spring.boot.netty.ws.model.MessageDTO;
import com.ai.spring.boot.netty.ws.util.Consts;
import com.ai.spring.boot.netty.ws.util.MessageJsonUtil;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.AttributeKey;
import org.springframework.stereotype.Service;

/**
 * 权限不足
 *
 * @author 石头
 * @Date 2020/7/6
 * @Version 1.0
 **/
@Service("MSG-SERVICE-10")
public class AccessDeniedHandler implements MessageHandler {
    @Override
    public void handler(DispatchMsgRequest request) {
        MessageDTO message = request.getMessage();
        Channel channel = request.getChannel().getChannel();

        channel.writeAndFlush(new TextWebSocketFrame(MessageJsonUtil.toJson(message)));

        channel.attr(AttributeKey.<String>valueOf(Consts.ACCESS_DENIED_KEY)).getAndSet(Consts.RSP_OK);
        channel.close();
    }
}
