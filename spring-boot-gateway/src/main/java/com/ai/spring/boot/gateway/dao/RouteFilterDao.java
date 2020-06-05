package com.ai.spring.boot.gateway.dao;

import com.ai.spring.boot.gateway.dao.bean.RouteFilter;

import java.util.List;

/**
 * 路由过滤策略
 *
 * @author 石头
 * @Date 2020/6/4
 * @Version 1.0
 **/
public interface RouteFilterDao {
    List<RouteFilter> findRouteFiltersByIds(List<Long> ids);
}
