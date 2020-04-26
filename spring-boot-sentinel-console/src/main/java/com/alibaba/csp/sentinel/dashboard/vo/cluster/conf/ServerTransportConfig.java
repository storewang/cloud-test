package com.alibaba.csp.sentinel.dashboard.vo.cluster.conf;

import lombok.Data;

/**
 *
 * @author 石头
 * @Date 2020/4/26
 * @Version 1.0
 **/
@Data
public class ServerTransportConfig {
    public static final int DEFAULT_PORT = 18730;
    public static final int DEFAULT_IDLE_SECONDS = 600;

    private Integer port;
    private Integer idleSeconds;

    public ServerTransportConfig() {
        this(DEFAULT_PORT, DEFAULT_IDLE_SECONDS);
    }

    public ServerTransportConfig(Integer port, Integer idleSeconds) {
        this.port = port;
        this.idleSeconds = idleSeconds;
    }

    public ServerTransportConfig setPort(Integer port) {
        this.port = port;
        return this;
    }

    public ServerTransportConfig setIdleSeconds(Integer idleSeconds) {
        this.idleSeconds = idleSeconds;
        return this;
    }
}
