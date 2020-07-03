package com.ai.spring.boot.netty.ws.server;

import com.ai.spring.boot.netty.ws.conf.ServerProperties;
import com.ai.spring.boot.netty.ws.handler.ServerChannelInitializer;
import com.ai.spring.boot.netty.ws.service.ServerHandlerService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

/**
 * netty websocket server
 *
 * @author 石头
 * @Date 2020/6/28
 * @Version 1.0
 **/
@Slf4j
public class NettyWebsocketServer implements Server{
    /**服务端*/
    private ServerBootstrap bootstrap;
    /**主线程池*/
    private EventLoopGroup bossGroup;
    /**工作线程池*/
    private EventLoopGroup workerGroup;
    private volatile boolean isRunning = false;
    private ChannelFuture channelFuture;
    /**依赖相关*/
    private ServerProperties serverProperties;
    private ServerHandlerService serverHandlerService;
    private ThreadQueueMonitor threadQueueMonitor;
    public NettyWebsocketServer(ServerProperties serverProperties,ServerHandlerService serverHandlerService){
        this.serverHandlerService = serverHandlerService;
        this.serverProperties     = serverProperties;
        threadQueueMonitor        = ThreadQueueMonitor.builder().serverHandlerService(serverHandlerService).serverProperties(serverProperties).build();
        threadQueueMonitor.starMonitor();
    }

    @Override
    public void start() {
        if (!isRunning){
            isRunning = true;
            // 创建服务端
            bootstrap   = new ServerBootstrap();
            // 主线程池,只需要一个就可以，主要处理accepter
            bossGroup = new NioEventLoopGroup(serverProperties.getBossThreadNum(), new DefaultThreadFactory("NettyServerBoss", true));
            // 工作线程池
            workerGroup = new NioEventLoopGroup(serverProperties.getWorkerThreadNum(),new DefaultThreadFactory("NettyServerWorker", true));
            bootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childOption(ChannelOption.TCP_NODELAY, Boolean.TRUE)
                    .childOption(ChannelOption.SO_REUSEADDR, Boolean.TRUE)
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    .childHandler(new ServerChannelInitializer(serverProperties,serverHandlerService));

            String host  = serverProperties.getHost();
            Integer port = serverProperties.getPort();
            InetSocketAddress bindAddress = new InetSocketAddress(host, port);
            try {
                channelFuture = bootstrap.bind(bindAddress).sync();
                log.info("----------websocket bind in {}:{}-------------",host,port);
                serverHandlerService.bindHost();

                channelFuture.channel().closeFuture().addListener(ChannelFutureListener.CLOSE);
            } catch (InterruptedException e) {
                log.error("websocket bind error,on " + host + ":" + port,e);
            }
        }
    }

    @Override
    public void stop() {
        try {
            if (channelFuture!=null){
                channelFuture.channel().close().addListener(ChannelFutureListener.CLOSE);
                channelFuture.awaitUninterruptibly();
            }
            if (bootstrap != null) {
                bossGroup.shutdownGracefully();
            }
            if (workerGroup!=null){
                workerGroup.shutdownGracefully();
            }
            threadQueueMonitor.stopMonitor();
            serverHandlerService.unBindHost();
        }catch (Throwable e){
            log.error("---server stopped error!!---",e);
        }

        log.info("--------------------------");
        log.info("---server has stopped!!---");
        log.info("--------------------------");
    }
}
