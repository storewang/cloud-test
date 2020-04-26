package com.alibaba.csp.sentinel.dashboard.vo.cluster.conf;

import lombok.Data;

/**
 * ClusterClientConfig
 *
 * @author 石头
 * @Date 2020/4/26
 * @Version 1.0
 **/
@Data
public class ClusterClientConfig {
    private String serverHost;
    private Integer serverPort;

    private Integer requestTimeout;
    private Integer connectTimeout;
    public ClusterClientConfig setServerHost(String serverHost) {
        this.serverHost = serverHost;
        return this;
    }
    public ClusterClientConfig setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
        return this;
    }
    public ClusterClientConfig setRequestTimeout(Integer requestTimeout) {
        this.requestTimeout = requestTimeout;
        return this;
    }
    public ClusterClientConfig setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }
}
