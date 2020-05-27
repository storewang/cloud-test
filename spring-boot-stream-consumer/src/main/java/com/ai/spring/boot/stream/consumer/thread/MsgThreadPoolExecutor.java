package com.ai.spring.boot.stream.consumer.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 消息接收者处理线程池
 *
 * @author 石头
 * @Date 2020/5/27
 * @Version 1.0
 **/
@Slf4j
public class MsgThreadPoolExecutor extends ThreadPoolExecutor {
    public MsgThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime,ArrayBlockingQueue<Runnable> workQueue, MsgDiscardPolicy handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue, handler);
    }
}
