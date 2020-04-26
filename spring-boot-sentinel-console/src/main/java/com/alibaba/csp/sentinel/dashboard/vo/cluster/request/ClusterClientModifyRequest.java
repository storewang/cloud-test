package com.alibaba.csp.sentinel.dashboard.vo.cluster.request;

import com.alibaba.csp.sentinel.dashboard.vo.cluster.conf.ClusterClientConfig;
import lombok.Data;

/**
 * ClusterClientModifyRequest
 *
 * @author 石头
 * @Date 2020/4/26
 * @Version 1.0
 **/
@Data
public class ClusterClientModifyRequest implements ClusterModifyRequest{
    private String app;
    private String ip;
    private Integer port;

    private Integer mode;
    private ClusterClientConfig clientConfig;

    public ClusterClientModifyRequest setApp(String app) {
        this.app = app;
        return this;
    }

    public ClusterClientModifyRequest setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public ClusterClientModifyRequest setPort(Integer port) {
        this.port = port;
        return this;
    }

    public ClusterClientModifyRequest setMode(Integer mode) {
        this.mode = mode;
        return this;
    }

    public ClusterClientModifyRequest setClientConfig(ClusterClientConfig clientConfig) {
        this.clientConfig = clientConfig;
        return this;
    }
}
