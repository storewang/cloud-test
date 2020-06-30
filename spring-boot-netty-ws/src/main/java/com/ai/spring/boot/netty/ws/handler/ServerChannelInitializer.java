package com.ai.spring.boot.netty.ws.handler;

import com.ai.spring.boot.netty.ws.conf.ServerProperties;
import com.ai.spring.boot.netty.ws.service.ServerHandlerService;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * Channel Hanlder初始化
 *
 * @author 石头
 * @Date 2020/6/28
 * @Version 1.0
 **/
@Slf4j
public class ServerChannelInitializer extends ChannelInitializer<Channel> {
    private ServerProperties serverProperties;
    private ServerHandlerService serverHandlerService;
    public ServerChannelInitializer(ServerProperties serverProperties,ServerHandlerService serverHandlerService){
        this.serverProperties = serverProperties;
        this.serverHandlerService = serverHandlerService;
    }
    @Override
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        //处理日志
        pipeline.addLast(new LoggingHandler(LogLevel.INFO));
        // 处理心跳，3小时没有读写操作，则关闭连接
        pipeline.addLast(new IdleStateHandler(0, 0, serverProperties.getHeartBeatTime(), TimeUnit.SECONDS));
        pipeline.addLast(new HeartbeatHandler(serverProperties.getHeartBeatTime()));

        // ws conf
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new ChunkedWriteHandler());
        pipeline.addLast(new HttpObjectAggregator(64 * 1024));
        pipeline.addLast(new HttpRequestChannelHandler(serverProperties.getContextPath(),serverHandlerService));
        pipeline.addLast(new WebSocketServerProtocolHandler(serverProperties.getContextPath()));
        pipeline.addLast(new TextWebSocketFrameHandler(serverHandlerService,serverProperties));
    }
}
