package com.ai.spring.boot.stream.consumer.thread;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.time.Duration;

/**
 * 线程队列监控
 *
 * @author 石头
 * @Date 2020/5/27
 * @Version 1.0
 **/
@Slf4j
public class ThreadQueueMonitor {
    private MsgThreadPoolExecutor poolExecutor;
    private MsgArrayBlockingQueue blockingQueue;

    private ThreadQueueMonitor(MsgThreadPoolExecutor poolExecutor,MsgArrayBlockingQueue blockingQueue){
        this.blockingQueue = blockingQueue;
        this.poolExecutor  = poolExecutor;
    }

    public static ThreadQueueMonitor newBuilder(MsgThreadPoolExecutor poolExecutor,MsgArrayBlockingQueue blockingQueue){
        return new ThreadQueueMonitor(poolExecutor,blockingQueue);
    }
    public void starMonitor(){
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(1);
        scheduler.setThreadNamePrefix("monitor-");
        scheduler.initialize();
        scheduler.scheduleAtFixedRate(()->{
            log.info("-------blockingQueue.size={}---------",blockingQueue.size());
            log.info("-------thread.pool.queue.size={}---------",poolExecutor.getQueue().size());
            log.info("-------thread.pool.active.size={}---------",poolExecutor.getActiveCount());
        }, Duration.ofMillis(5000));
    }
}
