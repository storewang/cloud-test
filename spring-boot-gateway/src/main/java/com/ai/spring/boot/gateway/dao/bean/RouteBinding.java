package com.ai.spring.boot.gateway.dao.bean;

import com.ai.spring.boot.ds.dao.bean.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 路由绑定
 *  routes:
 *    - id: content_time_between_route
 *      uri: lb://spring-boot-content
 *      predicates:
 *        - Path=/content/**
 *        - TimeBetween=下午2:50,下午11:30
 *      filters:
 *        - AddRequestHeader=X-Request-Foo, Bar
 *        - PreLog=a,b
 *        - RewritePath=/content/(?<segment>.*), /$\{segment}
 * @author 石头
 * @Date 2020/6/3
 * @Version 1.0
 **/
@Data
@Entity
@Table(name="gtway_route_bind")
public class RouteBinding extends BaseEntity {
    /**主键*/
    @Id
    private Long id;
    /**路由ID，唯一*/
    @Column(name = "route_id")
    private String routeId;
    /**路由名称*/
    @Column(name = "route_name")
    private String routeName;
    /**路由URL*/
    @Column(name = "route_url")
    private String url;
    /**优先级顺序*/
    private Integer order;
    /**
     * 关联的数据类型 1: 路由匹配策略 2: 路由过滤策略
     * @See com.ai.spring.boot.gateway.enums.RouteDataTypeEnum
     * */
    @Column(name = "data_type")
    private Integer dataType;
    /**关联数据类型相对应的数据ID*/
    @Column(name = "data_id")
    private Long dataId;
    /**数据有效性*/
    private Integer status;
    /**创建时间*/
    @Column(name = "create_time",insertable = false,updatable = false)
    private Date createTime;
    /**更新时间*/
    @Column(name = "update_time",updatable = false,insertable = false)
    private Date updateTime;
}
