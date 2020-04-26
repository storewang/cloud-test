package com.alibaba.csp.sentinel.dashboard.dao.entity.cluster;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author 石头
 * @Date 2020/4/26
 * @Version 1.0
 **/
@Data
public class ClusterGroupEntity {
    private String machineId;

    private String ip;
    private Integer port;

    private Set<String> clientSet = new HashSet<>();

    private Boolean belongToApp;

    public ClusterGroupEntity setMachineId(String machineId) {
        this.machineId = machineId;
        return this;
    }
    public ClusterGroupEntity setIp(String ip) {
        this.ip = ip;
        return this;
    }
    public ClusterGroupEntity setPort(Integer port) {
        this.port = port;
        return this;
    }
    public ClusterGroupEntity setClientSet(Set<String> clientSet) {
        this.clientSet = clientSet;
        return this;
    }
    public ClusterGroupEntity setBelongToApp(Boolean belongToApp) {
        this.belongToApp = belongToApp;
        return this;
    }
}
