package com.alibaba.csp.sentinel.dashboard.vo.cluster.state;

import com.alibaba.csp.sentinel.dashboard.dao.entity.cluster.ClusterStateSimpleEntity;
import lombok.Data;

/**
 * TODO
 *
 * @author 石头
 * @Date 2020/4/26
 * @Version 1.0
 **/
@Data
public class ClusterUniversalStateVO {
    private ClusterStateSimpleEntity stateInfo;
    private ClusterClientStateVO client;
    private ClusterServerStateVO server;
    public ClusterUniversalStateVO setClient(ClusterClientStateVO client) {
        this.client = client;
        return this;
    }
    public ClusterUniversalStateVO setServer(ClusterServerStateVO server) {
        this.server = server;
        return this;
    }
    public ClusterUniversalStateVO setStateInfo(
            ClusterStateSimpleEntity stateInfo) {
        this.stateInfo = stateInfo;
        return this;
    }
}
