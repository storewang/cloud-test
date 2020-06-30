package com.ai.spring.boot.netty.ws.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 业务消息处理线程池
 *
 * @author 石头
 * @Date 2020/6/30
 * @Version 1.0
 **/
@Slf4j
public class BusinessThreadPoolExcutor extends ThreadPoolExecutor {
    public BusinessThreadPoolExcutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, ArrayBlockingQueue<Runnable> workQueue, ThreadDiscardPolicy handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS, workQueue, handler);
    }

    @Override
    public void execute(Runnable command){
        if (command == null || !(command instanceof BusinessThreadTask)){
            log.warn("-------执行任务不能为空，且必须是BusinessThreadTask类型的任务----------");
            return;
        }
        if(this.getQueue().contains(command)){
            log.warn("-------执行任务已经存在线程队列中:{}----------",command);
            return;
        }

        super.execute(command);
    }
}
