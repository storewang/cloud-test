package com.alibaba.csp.sentinel.dashboard.dao.entity;

import lombok.Data;

import java.util.Date;

/**
 * 机器信息
 *
 * @author 石头
 * @Date 2020/4/26
 * @Version 1.0
 **/
@Data
public class MachineEntity {
    private Long id;
    private Date gmtCreate;
    private Date gmtModified;
    /**应用名称*/
    private String app;
    /**IP*/
    private String ip;
    /**主机*/
    private String hostname;
    /**端口*/
    private Integer port;
    private Date timestamp;

}
