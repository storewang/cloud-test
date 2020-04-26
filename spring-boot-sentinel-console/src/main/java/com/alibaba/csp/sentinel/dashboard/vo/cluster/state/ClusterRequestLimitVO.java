package com.alibaba.csp.sentinel.dashboard.vo.cluster.state;

import lombok.Data;

/**
 *
 * @author 石头
 * @Date 2020/4/26
 * @Version 1.0
 **/
@Data
public class ClusterRequestLimitVO {
    private String namespace;
    private Double currentQps;
    private Double maxAllowedQps;

    public ClusterRequestLimitVO setNamespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    public ClusterRequestLimitVO setCurrentQps(Double currentQps) {
        this.currentQps = currentQps;
        return this;
    }

    public ClusterRequestLimitVO setMaxAllowedQps(Double maxAllowedQps) {
        this.maxAllowedQps = maxAllowedQps;
        return this;
    }
}
