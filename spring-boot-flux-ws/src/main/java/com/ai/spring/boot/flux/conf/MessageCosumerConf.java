package com.ai.spring.boot.flux.conf;

import com.ai.spring.boot.flux.ws.conf.WebSocketContext;
import com.ai.spring.boot.flux.ws.util.Constans;
import com.ai.spring.im.common.mq.consumer.MqConsumer;
import com.ai.spring.mq.kafka.EnableKafkaMq;
import com.ai.spring.mq.kafka.enums.KafkaTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * 消息消费，这里是由其他机器分发出来的消息。
 * 这里有个问题，这个消息消费是本机器全局
 * 一旦用户下线，再上线，之前发送的消息，
 * 由于已经消费了，但是用户下线了还没有推送到客户，
 * 这些数据就丢失了,所以用户离线判断还是很重要，
 * 所以在消息发送的时候判断用记是否已经离线，
 * 如果离线就要做消息存储,或是所有消息都进行存储，
 * 一旦发送成功，如果在线的客户，就做消息已推送标识.
 * 离线的客户在下次重连接的时候，需要拉取离线消息进行推送，标记消息已推送。
 *
 * @author 石头
 * @Date 2020/5/29
 * @Version 1.0
 **/
@Configuration
@EnableKafkaMq(KafkaTypeEnum.CONSUMER)
@Slf4j
public class MessageCosumerConf {
    @Autowired
    private MqConsumer consumer;
    @Autowired
    private WebSocketContext webSocketContext;
    @PostConstruct
    public void init(){
        consumer.consumer(Constans.MESSAGE_TOPIC,(metadata, e)->{
            String clientId = metadata.getMsgId();
            if (e!=null){
                log.info(clientId + "|消息消费失败:",e);
                return;
            }

            String userId = metadata.getKey();

            webSocketContext.sendMessage(userId,metadata.getMsg());
        });
    }
}
