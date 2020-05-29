package com.ai.spring.im.common.mq.queue;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.time.Duration;

/**
 * 线程队列监控
 *
 * @author 石头
 * @Date 2020/5/28
 * @Version 1.0
 **/
@Slf4j
public class ThreadQueueMonitor {
    protected static final String THREAD_MONITOR_NAME = "consumer-monitor-";
    private MqThreadPoolExecutor poolExecutor;
    private MqArrayBlockingQueue blockingQueue;

    private ThreadQueueMonitor(MqThreadPoolExecutor poolExecutor,MqArrayBlockingQueue blockingQueue){
        this.blockingQueue = blockingQueue;
        this.poolExecutor  = poolExecutor;
    }

    public static ThreadQueueMonitor builder(MqThreadPoolExecutor poolExecutor,MqArrayBlockingQueue blockingQueue){
        return new ThreadQueueMonitor(poolExecutor,blockingQueue);
    }

    public void starMonitor(){
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(1);
        scheduler.setThreadNamePrefix(THREAD_MONITOR_NAME);
        scheduler.initialize();
        scheduler.scheduleAtFixedRate(()->{
            log.info("-------blockingQueue.size={}---------",blockingQueue.size());
            log.info("-------thread.pool.queue.size={}---------",poolExecutor.getQueue().size());
            log.info("-------thread.pool.active.size={}---------",poolExecutor.getActiveCount());
        }, Duration.ofMillis(60000));
    }
}
