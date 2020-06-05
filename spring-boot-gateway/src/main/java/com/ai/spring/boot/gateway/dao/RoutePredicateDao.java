package com.ai.spring.boot.gateway.dao;

import com.ai.spring.boot.gateway.dao.bean.RoutePredicate;

import java.util.List;

/**
 * 路由策略
 *
 * @author 石头
 * @Date 2020/6/4
 * @Version 1.0
 **/
public interface RoutePredicateDao {
    List<RoutePredicate> findRoutePredicatesByIds(List<Long> ids);
}
