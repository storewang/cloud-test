package com.alibaba.csp.sentinel.dashboard.vo.cluster;

import lombok.Data;

import java.util.List;

/**
 *
 * @author 石头
 * @Date 2020/4/26
 * @Version 1.0
 **/
@Data
public class ConnectionGroupVO {
    private String namespace;
    private List<ConnectionDescriptorVO> connectionSet;
    private Integer connectedCount;

    public ConnectionGroupVO setNamespace(String namespace) {
        this.namespace = namespace;
        return this;
    }
    public ConnectionGroupVO setConnectionSet(
            List<ConnectionDescriptorVO> connectionSet) {
        this.connectionSet = connectionSet;
        return this;
    }
    public ConnectionGroupVO setConnectedCount(Integer connectedCount) {
        this.connectedCount = connectedCount;
        return this;
    }
}
