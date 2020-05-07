package com.alibaba.csp.sentinel.dashboard.vo;

import com.alibaba.csp.sentinel.dashboard.dao.entity.MetricEntity;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author 石头
 * @Date 2020/5/6
 * @Version 1.0
 **/
@Data
public class MetricVo implements Comparable<MetricVo>{
    private Long id;
    private String app;
    private Long timestamp;
    private Long gmtCreate = System.currentTimeMillis();
    private String resource;
    private Long passQps;
    private Long blockQps;
    private Long successQps;
    private Long exceptionQps;
    /**
     * average rt
     */
    private Double rt;
    private Integer count;

    public MetricVo() {
    }

    public static List<MetricVo> fromMetricEntities(Collection<MetricEntity> entities) {
        List<MetricVo> list = new ArrayList<>();
        if (entities != null) {
            for (MetricEntity entity : entities) {
                list.add(fromMetricEntity(entity));
            }
        }
        return list;
    }

    /**
     * 保留资源名为identity的结果。
     *
     * @param entities 通过hashCode查找到的MetricEntities
     * @param identity 真正需要查找的资源名
     * @return
     */
    public static List<MetricVo> fromMetricEntities(Collection<MetricEntity> entities, String identity) {
        List<MetricVo> list = new ArrayList<>();
        if (entities != null) {
            for (MetricEntity entity : entities) {
                if (entity.getResource().equals(identity)) {
                    list.add(fromMetricEntity(entity));
                }
            }
        }
        return list;
    }

    public static MetricVo fromMetricEntity(MetricEntity entity) {
        MetricVo vo = new MetricVo();
        vo.id = entity.getId();
        vo.app = entity.getApp();
        vo.timestamp = entity.getTimestamp().getTime();
        vo.gmtCreate = entity.getGmtCreate().getTime();
        vo.resource = entity.getResource();
        vo.passQps = entity.getPassQps();
        vo.blockQps = entity.getBlockQps();
        vo.successQps = entity.getSuccessQps();
        vo.exceptionQps = entity.getExceptionQps();
        if (entity.getSuccessQps() != 0) {
            vo.rt = entity.getRt() / entity.getSuccessQps();
        } else {
            vo.rt = 0D;
        }
        vo.count = entity.getCount();
        return vo;
    }

    public static MetricVo parse(String line) {
        String[] strs = line.split("\\|");
        long timestamp = Long.parseLong(strs[0]);
        String identity = strs[1];
        long passQps = Long.parseLong(strs[2]);
        long blockQps = Long.parseLong(strs[3]);
        long exception = Long.parseLong(strs[4]);
        double rt = Double.parseDouble(strs[5]);
        long successQps = Long.parseLong(strs[6]);
        MetricVo vo = new MetricVo();
        vo.timestamp = timestamp;
        vo.resource = identity;
        vo.passQps = passQps;
        vo.blockQps = blockQps;
        vo.successQps = successQps;
        vo.exceptionQps = exception;
        vo.rt = rt;
        vo.count = 1;
        return vo;
    }
    @Override
    public int compareTo(MetricVo o) {
        return Long.compare(this.timestamp, o.timestamp);
    }
}
