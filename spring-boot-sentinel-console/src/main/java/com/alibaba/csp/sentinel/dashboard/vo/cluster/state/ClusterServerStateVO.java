package com.alibaba.csp.sentinel.dashboard.vo.cluster.state;

import com.alibaba.csp.sentinel.dashboard.vo.cluster.ConnectionGroupVO;
import com.alibaba.csp.sentinel.dashboard.vo.cluster.conf.ServerFlowConfig;
import com.alibaba.csp.sentinel.dashboard.vo.cluster.conf.ServerTransportConfig;
import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 *
 * @author 石头
 * @Date 2020/4/26
 * @Version 1.0
 **/
@Data
public class ClusterServerStateVO {
    private String appName;

    private ServerTransportConfig transport;
    private ServerFlowConfig flow;
    private Set<String> namespaceSet;

    private Integer port;

    private List<ConnectionGroupVO> connection;
    private List<ClusterRequestLimitVO> requestLimitData;

    private Boolean embedded;

    public ClusterServerStateVO setAppName(String appName) {
        this.appName = appName;
        return this;
    }
    public ClusterServerStateVO setTransport(ServerTransportConfig transport) {
        this.transport = transport;
        return this;
    }
    public ClusterServerStateVO setFlow(ServerFlowConfig flow) {
        this.flow = flow;
        return this;
    }
    public ClusterServerStateVO setNamespaceSet(Set<String> namespaceSet) {
        this.namespaceSet = namespaceSet;
        return this;
    }
    public ClusterServerStateVO setPort(Integer port) {
        this.port = port;
        return this;
    }
    public ClusterServerStateVO setConnection(List<ConnectionGroupVO> connection) {
        this.connection = connection;
        return this;
    }
    public ClusterServerStateVO setRequestLimitData(List<ClusterRequestLimitVO> requestLimitData) {
        this.requestLimitData = requestLimitData;
        return this;
    }
    public ClusterServerStateVO setEmbedded(Boolean embedded) {
        this.embedded = embedded;
        return this;
    }
}
