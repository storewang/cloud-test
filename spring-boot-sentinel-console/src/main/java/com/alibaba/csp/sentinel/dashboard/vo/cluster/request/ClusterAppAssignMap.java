package com.alibaba.csp.sentinel.dashboard.vo.cluster.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 *
 * @author 石头
 * @Date 2020/4/26
 * @Version 1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClusterAppAssignMap {
    private String machineId;
    private String ip;
    private Integer port;

    private Boolean belongToApp;

    private Set<String> clientSet;

    private Set<String> namespaceSet;
    private Double maxAllowedQps;
}
