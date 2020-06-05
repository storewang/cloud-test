package com.ai.spring.boot.gateway.enums;

import lombok.Getter;

/**
 * 路由数据类型
 *
 * @author 石头
 * @Date 2020/6/3
 * @Version 1.0
 **/
@Getter
public enum RouteDataTypeEnum {
    ROUTE_FILTER(2,"过滤策略"),
    ROUTE_PREDICATE(1,"路由匹配策略"),
    ;
    private Integer dataType;
    private String dataName;
    RouteDataTypeEnum(Integer dataType,String dataName){
        this.dataType = dataType;
        this.dataName = dataName;
    }
}
