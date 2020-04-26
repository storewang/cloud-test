package com.alibaba.csp.sentinel.dashboard.vo.cluster.state;

import lombok.Data;

/**
 *
 * @author 石头
 * @Date 2020/4/26
 * @Version 1.0
 **/
@Data
public class AppClusterServerStateWrapVO {
    /**
     * {ip}@{transport_command_port}.
     */
    private String id;

    private String ip;
    private Integer port;

    private Integer connectedCount;

    private Boolean belongToApp;

    private ClusterServerStateVO state;
    public AppClusterServerStateWrapVO setId(String id) {
        this.id = id;
        return this;
    }
    public AppClusterServerStateWrapVO setIp(String ip) {
        this.ip = ip;
        return this;
    }
    public AppClusterServerStateWrapVO setPort(Integer port) {
        this.port = port;
        return this;
    }
    public AppClusterServerStateWrapVO setBelongToApp(Boolean belongToApp) {
        this.belongToApp = belongToApp;
        return this;
    }
    public AppClusterServerStateWrapVO setConnectedCount(Integer connectedCount) {
        this.connectedCount = connectedCount;
        return this;
    }
    public AppClusterServerStateWrapVO setState(ClusterServerStateVO state) {
        this.state = state;
        return this;
    }


}
