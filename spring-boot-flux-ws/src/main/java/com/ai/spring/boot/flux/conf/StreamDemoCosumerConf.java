package com.ai.spring.boot.flux.conf;

import com.ai.spring.im.common.mq.consumer.MqConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * 消息者启动测试
 *
 * @author 石头
 * @Date 2020/5/29
 * @Version 1.0
 **/
@Component
@Slf4j
public class StreamDemoCosumerConf {
    @Autowired
    private MqConsumer consumer;

    @PostConstruct
    public void init(){
        consumer.consumer("stream-demo1",(metadata,e)->{
            String clientId = metadata.getMsgId();
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
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        });
    }
}
