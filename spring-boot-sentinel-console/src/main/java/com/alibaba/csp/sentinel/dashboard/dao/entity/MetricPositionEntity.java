package com.alibaba.csp.sentinel.dashboard.dao.entity;

import lombok.Data;

import java.util.Date;

/**
 * 监控点信息
 *
 * @author 石头
 * @Date 2020/4/26
 * @Version 1.0
 **/
@Data
public class MetricPositionEntity {
    private long id;
    private Date gmtCreate;
    private Date gmtModified;
    private String app;
    private String ip;
    /**
     * Sentinel在该应用上使用的端口
     */
    private int port;

    /**
     * 机器名，冗余字段
     */
    private String hostname;

    /**
     * 上一次拉取的最晚时间戳
     */
    private Date lastFetch;
}
