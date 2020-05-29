package com.ai.spring.im.common.mq.queue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 消息接收者处理线程池
 *
 * @author 石头
 * @Date 2020/5/28
 * @Version 1.0
 **/
public class MqThreadPoolExecutor extends ThreadPoolExecutor {
    public MqThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, ArrayBlockingQueue<Runnable> workQueue, MqDiscardPolicy handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue, handler);
    }
}
