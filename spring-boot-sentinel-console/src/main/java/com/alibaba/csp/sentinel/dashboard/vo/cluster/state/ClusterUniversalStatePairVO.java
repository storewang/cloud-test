package com.alibaba.csp.sentinel.dashboard.vo.cluster.state;

import lombok.Data;

/**
 *
 * @author 石头
 * @Date 2020/4/26
 * @Version 1.0
 **/
@Data
public class ClusterUniversalStatePairVO {
    private String ip;
    private Integer commandPort;

    private ClusterUniversalStateVO state;

    public ClusterUniversalStatePairVO() {}

    public ClusterUniversalStatePairVO(String ip, Integer commandPort, ClusterUniversalStateVO state) {
        this.ip = ip;
        this.commandPort = commandPort;
        this.state = state;
    }
    public ClusterUniversalStatePairVO setIp(String ip) {
        this.ip = ip;
        return this;
    }
    public ClusterUniversalStatePairVO setCommandPort(Integer commandPort) {
        this.commandPort = commandPort;
        return this;
    }
    public ClusterUniversalStatePairVO setState(ClusterUniversalStateVO state) {
        this.state = state;
        return this;
    }
}
