package com.alibaba.csp.sentinel.dashboard.dao.entity;

import lombok.Data;

import java.util.Date;

/**
 * 监控信息
 *
 * @author 石头
 * @Date 2020/4/26
 * @Version 1.0
 **/
@Data
public class MetricEntity {
    private Long id;
    private Date gmtCreate;
    private Date gmtModified;
    private String app;
    /**
     * 监控信息的时间戳
     */
    private Date timestamp;
    private String resource;
    private Long passQps;
    private Long successQps;
    private Long blockQps;
    private Long exceptionQps;

    /**
     * summary rt of all success exit qps.
     */
    private double rt;

    /**
     * 本次聚合的总条数
     */
    private int count;

    private int resourceCode;

    public static MetricEntity copyOf(MetricEntity oldEntity) {
        MetricEntity entity = new MetricEntity();
        entity.setId(oldEntity.getId());
        entity.setGmtCreate(oldEntity.getGmtCreate());
        entity.setGmtModified(oldEntity.getGmtModified());
        entity.setApp(oldEntity.getApp());
        entity.setTimestamp(oldEntity.getTimestamp());
        entity.setResource(oldEntity.getResource());
        entity.setPassQps(oldEntity.getPassQps());
        entity.setBlockQps(oldEntity.getBlockQps());
        entity.setSuccessQps(oldEntity.getSuccessQps());
        entity.setExceptionQps(oldEntity.getExceptionQps());
        entity.setRt(oldEntity.getRt());
        entity.setCount(oldEntity.getCount());
        entity.setResource(oldEntity.getResource());
        return entity;
    }

    public synchronized void addPassQps(Long passQps) {
        this.passQps += passQps;
    }

    public synchronized void addBlockQps(Long blockQps) {
        this.blockQps += blockQps;
    }

    public synchronized void addExceptionQps(Long exceptionQps) {
        this.exceptionQps += exceptionQps;
    }

    public synchronized void addCount(int count) {
        this.count += count;
    }

    public synchronized void addRtAndSuccessQps(double avgRt, Long successQps) {
        this.rt += avgRt * successQps;
        this.successQps += successQps;
    }

    /**
     * {@link #rt} = {@code avgRt * successQps}
     *
     * @param avgRt      average rt of {@code successQps}
     * @param successQps
     */
    public synchronized void setRtAndSuccessQps(double avgRt, Long successQps) {
        this.rt = avgRt * successQps;
        this.successQps = successQps;
    }
}
