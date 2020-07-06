package com.ai.spring.boot.netty.ws.handler;

import com.ai.spring.boot.netty.ws.conf.ServerProperties;
import com.ai.spring.boot.netty.ws.model.ClientChannel;
import com.ai.spring.boot.netty.ws.model.DispatchMsgRequest;
import com.ai.spring.boot.netty.ws.model.MessageDTO;
import com.ai.spring.boot.netty.ws.model.UserDTO;
import com.ai.spring.boot.netty.ws.service.ServerHandlerService;
import com.ai.spring.boot.netty.ws.util.Consts;
import com.ai.spring.boot.netty.ws.util.MessageJsonUtil;
import com.ai.spring.boot.netty.ws.util.MessageType;
import com.ai.spring.boot.netty.ws.util.UserCodeUtil;
import com.ai.spring.im.common.util.StringUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

/**
 * wesocket 消息处理handler
 *
 * @author 石头
 * @Date 2020/6/29
 * @Version 1.0
 **/
@Slf4j
public class TextWebSocketFrameHandler extends UserChannelHandler<TextWebSocketFrame>{
    private ServerHandlerService serverHandlerService;
    private ServerProperties serverProperties;
    public TextWebSocketFrameHandler(ServerHandlerService serverHandlerService,ServerProperties serverProperties){
        this.serverHandlerService = serverHandlerService;
        this.serverProperties     = serverProperties;
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        Channel channel = ctx.channel();
        String token = channel.attr(AttributeKey.<String>valueOf(Consts.CHANNEL_TOKEN_KEY)).get();
        log.info("--------收到消息:{}-----------",msg.text());
        UserDTO from  = serverHandlerService.getUserByToken(token);

        ClientChannel.ClientChannelBuilder builder = ClientChannel.builder();
        builder.channel(channel)
                .ctx(ctx)
                .channelId(channel.id().toString())
                .host(serverProperties.getHost())
                .port(serverProperties.getPort());

        MessageDTO message = null;
        DispatchMsgRequest request = null;
        if (from == null){
            message = new MessageDTO(null,"权限不足", MessageType.ACCESS_DENIED.getMsgType());
            request = DispatchMsgRequest.builder().channel(builder.build()).message(message).build();
        }else {
            message = MessageJsonUtil.readJson(msg.text());
            if (message == null){
                message = new MessageDTO(from,"消息格式不是json格式", MessageType.MSG_JSON_ERROR.getMsgType());
            }else {
                if (MessageType.USER_GROUP.getMsgType().equals(message.getMsgType())){
                    List<String> userCodes = message.getTos().stream().map(UserDTO::getUserCode).collect(Collectors.toList());
                    List<UserDTO> toUsers  = serverHandlerService.getUserByCode(userCodes);
                    if (toUsers == null || toUsers.size() == 0){
                        message = new MessageDTO(from,"接收者不存在", MessageType.USER_ERROR.getMsgType());
                    }else {
                        message.setTos(toUsers);
                    }
                }else {
                    UserDTO toUser = serverHandlerService.getUserByCode(message.getTo().getUserCode());
                    if (toUser == null){
                        message = new MessageDTO(from,"接收者不存在", MessageType.USER_ERROR.getMsgType());
                    }else {
                        message.setTo(toUser);
                    }
                }
            }

            builder.token(token).user(from);
            ClientChannel clientChannel = builder.build();
            request = DispatchMsgRequest.builder().message(message).channel(clientChannel).token(token).build();
        }
        serverHandlerService.dispatcher(request);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        // websocket握手完成，在这里可以注册客户端的连接信息，并保存
        if(evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            ctx.pipeline().remove(HttpRequestChannelHandler.class);

            Channel channel = ctx.channel();
            String token = channel.attr(AttributeKey.<String>valueOf(Consts.CHANNEL_TOKEN_KEY)).get();
            log.info("用户登录的token:{}",token);
            MessageDTO message = null;
            DispatchMsgRequest request = null;
            ClientChannel.ClientChannelBuilder builder = ClientChannel.builder();
            builder.channel(channel)
                    .ctx(ctx)
                    .channelId(channel.id().toString())
                    .host(serverProperties.getHost())
                    .port(serverProperties.getPort());
            // 此处的验证不通过，概率非常小，只有当token过期时才会触发
            if (!serverHandlerService.checkToken(token)){
                // 发送访问受限消息提醒
                message = new MessageDTO(null,"权限不足", MessageType.ACCESS_DENIED.getMsgType());
                request = DispatchMsgRequest.builder().channel(builder.build()).message(message).build();
            }else {
                // 权限验证通过，获取用户信息
                UserDTO user = serverHandlerService.getUserByToken(token);

                // 判断用户是否已经在本机器上进行连接
                String hashLoginedToken = serverHandlerService.getHashLoginedToken(UserCodeUtil.getUserIdByToken(token).toString());
                if (!StringUtil.isEmpty(hashLoginedToken)){
                    message = new MessageDTO(null,"用户已经建立连接了，不需要重复连接。", MessageType.ACCESS_DENIED.getMsgType());
                    request = DispatchMsgRequest.builder().channel(builder.build()).message(message).build();
                }else {
                    user.setUserCode(UserCodeUtil.getUserCode(token,channel.id().toString(),user));
                    // 1. 注册客户连接信息
                    builder.token(token).user(user);
                    ClientChannel clientChannel = builder.build();
                    serverHandlerService.register(clientChannel);
                    // 2. 发送客户连接上线通知信息
                    message = new MessageDTO(user,String.format("%s已上线",user.getUserName()),MessageType.USER_ONLINE.getMsgType());

                    request = DispatchMsgRequest.builder().message(message).channel(clientChannel).token(token).build();
                }
            }
            serverHandlerService.dispatcher(request);
        }else {
            super.userEventTriggered(ctx,evt);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("-----------channelInactive---------------");
        super.channelInactive(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        String token = channel.attr(AttributeKey.<String>valueOf(Consts.CHANNEL_TOKEN_KEY)).get();
        log.info("-----------{}:用户断开连接---------------",token);
        String deniedVal = channel.attr(AttributeKey.<String>valueOf(Consts.ACCESS_DENIED_KEY)).get();
        if (Consts.RSP_OK.equals(deniedVal)){
            super.channelUnregistered(ctx);
            return;
        }
        serverHandlerService.unRegister(token,channel.id().toString());

        // 下线通知
        MessageDTO message = null;
        DispatchMsgRequest request = null;
        ClientChannel.ClientChannelBuilder builder = ClientChannel.builder();
        builder.channel(channel)
                .ctx(ctx)
                .channelId(channel.id().toString())
                .host(serverProperties.getHost())
                .port(serverProperties.getPort());

        UserDTO user = serverHandlerService.getUserByToken(token);
        user.setUserCode(UserCodeUtil.getUserCode(token,channel.id().toString(),user));
        builder.token(token).user(user);
        message = new MessageDTO(user,String.format("%s已下线",user.getUserName()),MessageType.USER_OFFLINE.getMsgType());
        ClientChannel clientChannel = builder.build();
        request = DispatchMsgRequest.builder().message(message).channel(clientChannel).token(token).build();
        serverHandlerService.dispatcher(request);

        super.channelUnregistered(ctx);
    }
}
