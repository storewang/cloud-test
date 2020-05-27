package com.ai.spring.boot.stream.consumer.thread;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程弛队列满处理策略
 *
 * @author 石头
 * @Date 2020/5/27
 * @Version 1.0
 **/
@Slf4j
public class MsgDiscardPolicy implements RejectedExecutionHandler {
    private MsgArrayBlockingQueue blockingQueue;
    public MsgDiscardPolicy(MsgArrayBlockingQueue arrayBlockingQueue) {
        this.blockingQueue = arrayBlockingQueue;
    }

    public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
        if (!e.isShutdown()) {
            if (r instanceof MsgRecieveTask){
                ConsumerRecord<String, String> record = ((MsgRecieveTask) r).getRecord();
                try {
                    boolean offer = blockingQueue.offer(record, 100, TimeUnit.MILLISECONDS);
                    if (!offer){
                        log.warn("->[{}]消息处理失败，线程池队列和消息处理队列已满，丢弃。blockQueue.size={}", record,blockingQueue.size());
                    }
                }catch (Exception e1) {
                    log.warn("->[{}]消息处理失败，线程池队列已满，丢弃。", record);
                }
            }
        }
    }
}
