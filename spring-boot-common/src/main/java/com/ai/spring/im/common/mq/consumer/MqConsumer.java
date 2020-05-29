package com.ai.spring.im.common.mq.consumer;

import com.ai.spring.im.common.mq.MqCallBack;
import com.ai.spring.im.common.mq.MqRecordMetadata;
import com.ai.spring.im.common.mq.queue.DefaultConsumerRecordTask;
import com.ai.spring.im.common.mq.queue.MqArrayBlockingQueue;
import com.ai.spring.im.common.mq.queue.MqDiscardPolicy;
import com.ai.spring.im.common.mq.queue.MqThreadPoolExecutor;
import com.ai.spring.im.common.mq.queue.ThreadQueueMonitor;
import lombok.extern.slf4j.Slf4j;

import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 消息消费者
 *
 * @author 石头
 * @Date 2020/5/28
 * @Version 1.0
 **/
@Slf4j
public abstract class MqConsumer {
    protected static final String THREAD_RUN_NAME = "consumer-runner";
    protected AtomicLong fetchNum = new AtomicLong(1);
    private MqArrayBlockingQueue blockingQueue;
    private MqThreadPoolExecutor poolExecutor;
    /**初始化消息者*/
    protected abstract void initConsumer(Properties properties);
    /**获取消息者ID*/
    protected abstract String getConsumerId();
    /**拉取消息*/
    protected abstract MqConsumerRecords pollRecords(String topic);
    protected abstract boolean needPollNext(String topic);
    protected abstract void beforeConsumer(String topic );
    public MqConsumer(Properties properties){
        initConsumer(properties);

        // 初始化队列和工作线程
        int corePoolSize    = getProperty(properties,"thread.pool.core.size",1);
        int maximumPoolSize = getProperty(properties,"thread.pool.max.pool.size",2);
        int keepAliveTime   = getProperty(properties,"thread.pool.alive.sec",10);
        int queueSize       = getProperty(properties,"thread.pool.queue.size",500);

        blockingQueue       = new MqArrayBlockingQueue(queueSize * 2);
        poolExecutor        = new MqThreadPoolExecutor(corePoolSize,maximumPoolSize,keepAliveTime,new ArrayBlockingQueue<>(queueSize),new MqDiscardPolicy(blockingQueue));
        // start monitor
        ThreadQueueMonitor.builder(poolExecutor,blockingQueue).starMonitor();
    }

    /**
     * 消息消费处理逻辑
     * @param topic
     * @param callBack
     */
    protected void subscribe(String topic, MqCallBack callBack){
        boolean pollNext = needPollNext(topic);
        while (pollNext){
            MqConsumerRecords records = pollRecords(topic);
            log.info("--------------第{}次获取消息量:{}-------------------",fetchNum.getAndIncrement(),records.getCount());

            records.forEach(record -> poolExecutor.execute(new DefaultConsumerRecordTask(getConsumerId(),record,callBack)));

            // 队列中积压的消息
            dealWithBlockQueue(getConsumerId(),callBack);

            pollNext = needPollNext(topic);
        }

    }

    /**
     * 队列中积压的消息
     * 队列中的消息为线程池队列满了执行的阻塞策略放进去的，用于再消费
     * @param clientId
     * @param callBack
     */
    private void dealWithBlockQueue(String clientId,MqCallBack callBack){
        try{
            MqRecordMetadata record = blockingQueue.poll(100, TimeUnit.MILLISECONDS);
            while (record!=null){
                poolExecutor.execute(new DefaultConsumerRecordTask(clientId,record,callBack));
                record = blockingQueue.poll(100, TimeUnit.MILLISECONDS);
            }
        }catch (Throwable e){
        }
    }

    /**
     * 消息消费入口
     * @param topic
     * @param callBack
     */
    public void consumer(String topic, MqCallBack callBack){
        if (topic == null || topic.length() == 0){
            log.warn("---------消息主题(topic)不能为空-------------");
            return;
        }
        if (callBack == null){
            callBack = new ConsumerCallBack(getConsumerId());
        }
        // 消费消息前设置
        beforeConsumer(topic);

        // 启动一个线程执行消息消费监听
        new Thread( new consumerRunTask(topic,callBack,this),THREAD_RUN_NAME).start();
    }

    private int getProperty(Properties properties,String key,int def){
        String val     = properties.getProperty(key,String.valueOf(def));
        Integer intVal = str2Int(val);
        return intVal == null?def:intVal;
    }
    private Integer str2Int(String str){
        try {
            return Integer.parseInt(str);
        }catch (Throwable e){
            return null;
        }
    }

    /**
     * 消费者启动线程任务
     */
    private class consumerRunTask implements Runnable{
        private MqConsumer consumer;
        private String topic;
        private MqCallBack callBack;
        public consumerRunTask(String topic, MqCallBack callBack,MqConsumer consumer){
            this.topic    = topic;
            this.callBack = callBack;
            this.consumer = consumer;
        }
        @Override
        public void run() {
            consumer.subscribe(topic,callBack);
        }
    }

    /**
     * 默认的消费回调
     */
    private class ConsumerCallBack implements MqCallBack {
        private String clientId;
        public ConsumerCallBack(String clientId){
            this.clientId = clientId;
        }
        @Override
        public void onCompletion(MqRecordMetadata metadata, Throwable e) {
            if (e!=null){
                log.info(clientId + "|消息消费失败:",e);
                return;
            }
            log.info("{}|收到消息 topic={},partition={},offset={},key={},msg={}",
                    clientId,
                    metadata.getTopic(),
                    metadata.getPartition(),
                    metadata.getOffset(),
                    metadata.getKey(),
                    metadata.getMsg());
        }
    }
}
