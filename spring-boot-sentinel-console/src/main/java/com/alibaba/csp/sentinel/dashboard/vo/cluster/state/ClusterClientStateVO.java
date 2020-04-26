package com.alibaba.csp.sentinel.dashboard.vo.cluster.state;

import com.alibaba.csp.sentinel.dashboard.vo.cluster.ClusterClientInfoVO;
import lombok.Data;

/**
 *
 * @author 石头
 * @Date 2020/4/26
 * @Version 1.0
 **/
@Data
public class ClusterClientStateVO {
    /**
     * Cluster token client state.
     */
    private ClusterClientInfoVO clientConfig;
    public ClusterClientStateVO setClientConfig(ClusterClientInfoVO clientConfig) {
        this.clientConfig = clientConfig;
        return this;
    }
}
