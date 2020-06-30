package com.ai.spring.boot.netty.ws.conf;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * websocket线程相关配置
 *
 * @author 石头
 * @Date 2020/6/30
 * @Version 1.0
 **/
@Setter
@Getter
@ConfigurationProperties(prefix = "spring.boot.ws.thread")
public class ThreadProperties {
    private Integer bossThreadNum;
    private Integer workerThreadNum;
    private Integer monitorPeriodSec;
    private Integer heartBeatTimeSec;
    private Integer businessThreadNum;
    private Integer businessQueueSize;
}
