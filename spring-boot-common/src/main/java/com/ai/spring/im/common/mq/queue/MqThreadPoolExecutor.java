package com.ai.spring.im.common.mq.queue;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 消息接收者处理线程池
 *
 * @author 石头
 * @Date 2020/5/28
 * @Version 1.0
 **/
@Slf4j
public class MqThreadPoolExecutor extends ThreadPoolExecutor {
    private String poolName;
    public MqThreadPoolExecutor(String poolName,int corePoolSize, int maximumPoolSize, long keepAliveTime, ArrayBlockingQueue<Runnable> workQueue, MqDiscardPolicy handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue,new NamedThreadFactory(poolName) ,handler);
        this.poolName = poolName;
    }

    @Override
    protected void terminated() {
        super.terminated();
        log.info("-->{}线程池已经关闭。",poolName);
    }

    static class NamedThreadFactory implements ThreadFactory{
        private final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;
        public NamedThreadFactory(String namePrefix){
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();

            if (namePrefix == null || namePrefix.length() == 0){
                this.namePrefix = "pool-" + poolNumber.getAndIncrement() +  "-thread-";
            }else {
                this.namePrefix =  namePrefix + poolNumber.getAndIncrement() +  "-thread-";
            }

        }
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,namePrefix + threadNumber.getAndIncrement(),0);
            if (t.isDaemon()){
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY){
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }
}
