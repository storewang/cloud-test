package com.ai.spring.boot.gateway.dao.bean;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 路由策略
 *
 * @author 石头
 * @Date 2020/6/3
 * @Version 1.0
 **/
@Data
@Entity
@Table(name="gtway_route_predicate")
public class RoutePredicate extends BaseEntity{
    /**主键*/
    @Id
    private Long id;
    /**路由策略名称*/
    @Column(name = "predicate_name")
    private String name;
    /**路由策略,多个以','隔开*/
    @Column(name = "route_predicate")
    private String predicate;
    /**优先级顺序*/
    private Integer order;
    /**数据有效性*/
    private Integer status;
    /**创建时间*/
    @Column(name = "create_time",insertable = false,updatable = false)
    private Date createTime;
    /**更新时间*/
    @Column(name = "update_time",updatable = false,insertable = false)
    private Date updateTime;
}
