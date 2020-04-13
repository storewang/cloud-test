package com.ai.spring.boot.test.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;

import java.util.List;
import java.util.Map;

/**
 * TODO
 *
 * @author 石头
 * @Date 2020/4/13
 * @Version 1.0
 **/
@Slf4j
public class HashPartioner implements Partitioner{
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        List<PartitionInfo> partitionInfos = cluster.partitionsForTopic(topic);
        Integer numPartitions = partitionInfos.size();
        if (keyBytes!=null){
            int hashCode = 0;
            if (key instanceof Integer || key instanceof Long){
                hashCode = (int)key;
            }else {
                hashCode = key.hashCode();
            }

            hashCode = hashCode & 0x7ffffff;
            return hashCode % numPartitions;
        }

        return 0;
    }

    @Override
    public void close() {
    }

    @Override
    public void configure(Map<String, ?> map) {
    }
}
