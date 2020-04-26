package com.alibaba.csp.sentinel.dashboard.vo.cluster;

import lombok.Data;

/**
 * ClusterClientInfo
 *
 * @author 石头
 * @Date 2020/4/26
 * @Version 1.0
 **/
@Data
public class ClusterClientInfoVO {
    private String serverHost;
    private Integer serverPort;

    private Integer clientState;

    private Integer requestTimeout;

    public ClusterClientInfoVO setServerHost(String serverHost) {
        this.serverHost = serverHost;
        return this;
    }
    public ClusterClientInfoVO setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
        return this;
    }
    public ClusterClientInfoVO setClientState(Integer clientState) {
        this.clientState = clientState;
        return this;
    }
    public ClusterClientInfoVO setRequestTimeout(Integer requestTimeout) {
        this.requestTimeout = requestTimeout;
        return this;
    }
}
