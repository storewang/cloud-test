package com.alibaba.csp.sentinel.dashboard.dao.entity.cluster;

import lombok.Data;

/**
 * ClusterState
 *
 * @author 石头
 * @Date 2020/4/26
 * @Version 1.0
 **/
@Data
public class ClusterStateSimpleEntity {
    private Integer mode;
    private Long lastModified;
    private Boolean clientAvailable;
    private Boolean serverAvailable;

    public ClusterStateSimpleEntity setMode(Integer mode) {
        this.mode = mode;
        return this;
    }
    public ClusterStateSimpleEntity setLastModified(Long lastModified) {
        this.lastModified = lastModified;
        return this;
    }
    public ClusterStateSimpleEntity setClientAvailable(Boolean clientAvailable) {
        this.clientAvailable = clientAvailable;
        return this;
    }
    public ClusterStateSimpleEntity setServerAvailable(Boolean serverAvailable) {
        this.serverAvailable = serverAvailable;
        return this;
    }
}
