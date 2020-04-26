package com.alibaba.csp.sentinel.dashboard.vo.cluster.request;

import com.alibaba.csp.sentinel.dashboard.vo.cluster.conf.ServerFlowConfig;
import com.alibaba.csp.sentinel.dashboard.vo.cluster.conf.ServerTransportConfig;
import lombok.Data;

import java.util.Set;

/**
 *
 * @author 石头
 * @Date 2020/4/26
 * @Version 1.0
 **/
@Data
public class ClusterServerModifyRequest implements ClusterModifyRequest{
    private String app;
    private String ip;
    private Integer port;

    private Integer mode;
    private ServerFlowConfig flowConfig;
    private ServerTransportConfig transportConfig;
    private Set<String> namespaceSet;

    public ClusterServerModifyRequest setApp(String app) {
        this.app = app;
        return this;
    }
    public ClusterServerModifyRequest setIp(String ip) {
        this.ip = ip;
        return this;
    }
    public ClusterServerModifyRequest setPort(Integer port) {
        this.port = port;
        return this;
    }
    public ClusterServerModifyRequest setMode(Integer mode) {
        this.mode = mode;
        return this;
    }
    public ClusterServerModifyRequest setFlowConfig(
            ServerFlowConfig flowConfig) {
        this.flowConfig = flowConfig;
        return this;
    }
    public ClusterServerModifyRequest setTransportConfig(
            ServerTransportConfig transportConfig) {
        this.transportConfig = transportConfig;
        return this;
    }
    public ClusterServerModifyRequest setNamespaceSet(Set<String> namespaceSet) {
        this.namespaceSet = namespaceSet;
        return this;
    }
}
