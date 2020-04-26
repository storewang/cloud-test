package com.alibaba.csp.sentinel.dashboard.vo.cluster.state;

import lombok.Data;

/**
 *
 * @author 石头
 * @Date 2020/4/26
 * @Version 1.0
 **/
@Data
public class AppClusterClientStateWrapVO {
    /**
     * {ip}@{transport_command_port}.
     */
    private String id;

    private Integer commandPort;
    private String ip;

    private ClusterClientStateVO state;
    public AppClusterClientStateWrapVO setId(String id) {
        this.id = id;
        return this;
    }
    public AppClusterClientStateWrapVO setIp(String ip) {
        this.ip = ip;
        return this;
    }
    public AppClusterClientStateWrapVO setState(ClusterClientStateVO state) {
        this.state = state;
        return this;
    }
    public AppClusterClientStateWrapVO setCommandPort(Integer commandPort) {
        this.commandPort = commandPort;
        return this;
    }
}
