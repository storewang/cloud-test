package com.ai.spring.mq.kafka.conf;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 属性配置
 *
 * @author 石头
 * @Date 2020/5/28
 * @Version 1.0
 **/
@ConfigurationProperties(prefix = "mq.kafka.produce")
@Data
public class KafkaProducerProperties {
    /**节点列表*/
    private String brokerList;
    /**ack机制*/
    private String ack;
    /**重试次数*/
    private String retries;
    /**分批送消息字节数,一旦我们达到分批的batch.size值的记录，将立即发送,但是，如果比这个小，我们将在指定的“linger”时间内等待更多的消息加入*/
    private Integer batchSize;
    /**生产者将等待一个给定的延迟，以便和其他的消息可以组合成一个批次 ms*/
    private Integer linger;
    /**生产者用来缓存等待发送到服务器的消息的内存总字节数,如果消息发送比可传递到服务器的快，生产者将阻塞max.block.ms之后，抛出异常。*/
    private Integer bufferSize;
    /**分区策略*/
    private String partitioner;
    /**阻塞多长时间,可能是因为缓冲区已满或元数据不可用*/
    private Integer maxBlockMs;
}
