package com.ai.spring.boot.netty.ws.server;

import com.ai.spring.boot.netty.ws.conf.ServerProperties;
import com.ai.spring.boot.netty.ws.service.ServerHandlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.time.Duration;

/**
 * 后台监控
 *
 * @author 石头
 * @Date 2020/6/30
 * @Version 1.0
 **/
@Slf4j
public class ThreadQueueMonitor {
    private ServerProperties serverProperties;
    private ServerHandlerService serverHandlerService;

    private volatile boolean isRunning = false;
    private ThreadPoolTaskScheduler scheduler = null;

    private ThreadQueueMonitor(ServerProperties serverProperties,ServerHandlerService serverHandlerService){
        this.serverHandlerService = serverHandlerService;
        this.serverProperties     = serverProperties;
    }
    public static ThreadQueueMonitor.ThreadQueueMonitorBuilder builder(){
        return new ThreadQueueMonitor.ThreadQueueMonitorBuilder();
    }

    public void starMonitor(){
        if (!isRunning){
            scheduler = new ThreadPoolTaskScheduler();
            scheduler.setPoolSize(1);
            scheduler.setThreadNamePrefix("monitor-");

            if (serverProperties!=null && serverHandlerService!=null){
                isRunning = true;
                scheduler.initialize();

                scheduler.scheduleAtFixedRate(() -> serverHandlerService.monitorResources(), Duration.ofSeconds(serverProperties.getMonitorPeriod()));
            }else {
                log.warn("-------serverProperties is null or serverHandlerservice is null.----------");
            }
        }
        log.warn("--------scheduler is running...-------------");
    }

    public void stopMonitor(){
        if (isRunning && scheduler !=null){
            scheduler.shutdown();
        }
    }

    public static class ThreadQueueMonitorBuilder{
        private ServerProperties serverProperties;
        private ServerHandlerService serverHandlerService;
        public ThreadQueueMonitorBuilder(){}

        public ThreadQueueMonitor.ThreadQueueMonitorBuilder serverProperties(ServerProperties serverProperties){
            this.serverProperties = serverProperties;
            return this;
        }
        public ThreadQueueMonitor.ThreadQueueMonitorBuilder serverHandlerService(ServerHandlerService serverHandlerService){
            this.serverHandlerService = serverHandlerService;
            return this;
        }
        public ThreadQueueMonitor build(){
            return new ThreadQueueMonitor(serverProperties,serverHandlerService);
        }
        @Override
        public String toString() {
            return "ThreadQueueMonitor.ThreadQueueMonitorBuilder(serverHandlerService=" + this.serverHandlerService + ", serverProperties=" + this.serverProperties + ")";
        }
    }
}
