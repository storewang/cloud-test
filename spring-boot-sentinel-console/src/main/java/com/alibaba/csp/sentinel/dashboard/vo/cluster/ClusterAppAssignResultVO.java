package com.alibaba.csp.sentinel.dashboard.vo.cluster;

import lombok.Data;

import java.util.Set;

/**
 * ClusterAppAssignResult
 * @author 石头
 * @Date 2020/4/26
 * @Version 1.0
 **/
@Data
public class ClusterAppAssignResultVO {
    private Set<String> failedServerSet;
    private Set<String> failedClientSet;

    private Integer totalCount;
    public ClusterAppAssignResultVO setFailedServerSet(Set<String> failedServerSet) {
        this.failedServerSet = failedServerSet;
        return this;
    }
    public ClusterAppAssignResultVO setFailedClientSet(Set<String> failedClientSet) {
        this.failedClientSet = failedClientSet;
        return this;
    }
    public ClusterAppAssignResultVO setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
        return this;
    }
}
