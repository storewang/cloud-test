package com.ai.spring.boot.gateway.dao.impl;

import com.ai.spring.boot.ds.dao.BaseJpaDao;
import com.ai.spring.boot.gateway.cache.annotation.Cacheable;
import com.ai.spring.boot.gateway.dao.RoutePredicateDao;
import com.ai.spring.boot.gateway.dao.bean.RoutePredicate;
import com.ai.spring.boot.gateway.dao.repository.route.RoutePredicateRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 路由策略
 *
 * @author 石头
 * @Date 2020/6/4
 * @Version 1.0
 **/
@Component
@Slf4j
public class RoutePredicateDaoImpl extends BaseJpaDao<RoutePredicateRepository,RoutePredicate> implements RoutePredicateDao {
    @Override
    @Cacheable(key = "'gateway:route:keys:predicates'+ #ids")
    public List<RoutePredicate> findRoutePredicatesByIds(List<Long> ids) {
        List<RoutePredicate> predicateList = findByIds(ids);

        List<RoutePredicate> routePredicates = Optional.ofNullable(predicateList).flatMap(predicates -> {
            List<RoutePredicate> list = predicates.stream().sorted(Comparator.comparing(RoutePredicate::getOrder)).collect(Collectors.toList());

            return Optional.ofNullable(list);
        }).orElseGet(() -> Collections.EMPTY_LIST);
        log.debug("----{}------路由策略信息:{}-----------------",ids,routePredicates);
        return routePredicates;
    }
}
