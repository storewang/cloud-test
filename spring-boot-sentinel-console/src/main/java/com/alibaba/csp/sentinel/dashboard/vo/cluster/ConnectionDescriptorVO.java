package com.alibaba.csp.sentinel.dashboard.vo.cluster;

import lombok.Data;

/**
 *
 * @author 石头
 * @Date 2020/4/26
 * @Version 1.0
 **/
@Data
public class ConnectionDescriptorVO {
    private String address;
    private String host;

    public ConnectionDescriptorVO setAddress(String address) {
        this.address = address;
        return this;
    }
    public ConnectionDescriptorVO setHost(String host) {
        this.host = host;
        return this;
    }
}
