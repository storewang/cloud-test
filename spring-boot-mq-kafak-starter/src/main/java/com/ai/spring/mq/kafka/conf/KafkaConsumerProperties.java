package com.ai.spring.mq.kafka.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 属性配置
 *
 * @author 石头
 * @Date 2020/5/29
 * @Version 1.0
 **/
@ConfigurationProperties(prefix = "mq.kafka.consumer")
@Data
public class KafkaConsumerProperties {
    /**节点列表*/
    private String brokerList;
    /**消费者组*/
    private String groupId;
    /**消费者标识ID*/
    private String clientId;
    /**是否自动提交offset*/
    private Boolean autoCommit;
    /**自动提交offset的频率*/
    private Integer autoCommitInterval;
    /**当Kafka中没有初始offset或如果当前的offset不存在时,设置当前offset的策略*/
    /**latest: 自动将偏移重置为最新偏移,earliest: 自动将偏移重置为最早的偏移*/
    private String offsetReset;
    /**在单次调用poll()中返回的最大记录数。*/
    private Integer maxPoolNum;
    /**在单次调用poll(timout)中timeout值。*/
    private Integer maxPoolTimeOut;
    /**工作线程池配置*/
    /**线程池核心线程数*/
    private Integer workPoolCoreSize;
    /**线程池最大线程数*/
    private Integer workPoolMaxSize;
    /**线程空闲的最大时间(秒)*/
    private Integer workPoolThreadAlive;
    /**线程池中的任务队列中的最大任务数*/
    private Integer workPoolQueueSize;

}
