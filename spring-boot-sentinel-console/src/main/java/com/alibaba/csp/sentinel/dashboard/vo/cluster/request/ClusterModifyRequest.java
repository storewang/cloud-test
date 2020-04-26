package com.alibaba.csp.sentinel.dashboard.vo.cluster.request;

/**
 * ClusterModifyRequest
 *
 * @author 石头
 * @Date 2020/4/26
 * @Version 1.0
 **/
public interface ClusterModifyRequest {
    String getApp();

    String getIp();

    Integer getPort();

    Integer getMode();
}
