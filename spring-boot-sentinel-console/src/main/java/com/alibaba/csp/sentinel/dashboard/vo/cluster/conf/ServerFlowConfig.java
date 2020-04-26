package com.alibaba.csp.sentinel.dashboard.vo.cluster.conf;

import lombok.Data;

/**
 *
 * @author 石头
 * @Date 2020/4/26
 * @Version 1.0
 **/
@Data
public class ServerFlowConfig {
    public static final double DEFAULT_EXCEED_COUNT = 1.0d;
    public static final double DEFAULT_MAX_OCCUPY_RATIO = 1.0d;

    public static final int DEFAULT_INTERVAL_MS = 1000;
    public static final int DEFAULT_SAMPLE_COUNT= 10;
    public static final double DEFAULT_MAX_ALLOWED_QPS= 30000;

    private final String namespace;

    private Double exceedCount = DEFAULT_EXCEED_COUNT;
    private Double maxOccupyRatio = DEFAULT_MAX_OCCUPY_RATIO;
    private Integer intervalMs = DEFAULT_INTERVAL_MS;
    private Integer sampleCount = DEFAULT_SAMPLE_COUNT;

    private Double maxAllowedQps = DEFAULT_MAX_ALLOWED_QPS;

    public ServerFlowConfig() {
        this("default");
    }
    public ServerFlowConfig(String namespace) {
        this.namespace = namespace;
    }

    public ServerFlowConfig setExceedCount(Double exceedCount) {
        this.exceedCount = exceedCount;
        return this;
    }

    public ServerFlowConfig setMaxOccupyRatio(Double maxOccupyRatio) {
        this.maxOccupyRatio = maxOccupyRatio;
        return this;
    }

    public ServerFlowConfig setIntervalMs(Integer intervalMs) {
        this.intervalMs = intervalMs;
        return this;
    }

    public ServerFlowConfig setSampleCount(Integer sampleCount) {
        this.sampleCount = sampleCount;
        return this;
    }

    public ServerFlowConfig setMaxAllowedQps(Double maxAllowedQps) {
        this.maxAllowedQps = maxAllowedQps;
        return this;
    }
}
