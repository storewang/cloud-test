package com.ai.spring.boot.netty.ws.model;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 客户端连接信息
 *
 * @author 石头
 * @Date 2020/6/29
 * @Version 1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientChannel {
    private String host;
    private Integer port;
    private String token;
    private UserDTO user;
    private String channelId;
    private Channel channel;
    private ChannelHandlerContext ctx;
}
