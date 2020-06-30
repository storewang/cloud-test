package com.ai.spring.boot.netty.ws.thread;

import com.ai.spring.boot.netty.ws.model.MessageDTO;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程弛队列满处理策略
 *
 * @author 石头
 * @Date 2020/6/30
 * @Version 1.0
 **/
@Slf4j
public class ThreadDiscardPolicy implements RejectedExecutionHandler {
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
        if (!e.isShutdown()) {
            if (r instanceof BusinessThreadTask){
                MessageDTO record = ((BusinessThreadTask)r).getMessageDTO();
                log.warn("->[{}]消息处理失败，线程池队列已满，丢弃。", record);
            }
        }
    }
}
