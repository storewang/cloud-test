package com.alibaba.csp.sentinel.dashboard.dao.entity;

import lombok.Data;

import java.util.Date;

/**
 * 应用实体
 *
 * @author 石头
 * @Date 2020/4/26
 * @Version 1.0
 **/
@Data
public class ApplicationEntity {
    /**主键*/
    private Long id;

    private Date gmtCreate;
    private Date gmtModified;
    /**应用名称*/
    private String app;
    /**应用类型*/
    private Integer appType;
    private String activeConsole;
    private Date lastFetch;
}
