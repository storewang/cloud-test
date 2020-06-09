package com.ai.spring.boot.gateway.dao.impl;

import com.ai.spring.boot.ds.dao.BaseJpaDao;
import com.ai.spring.boot.gateway.cache.annotation.Cacheable;
import com.ai.spring.boot.gateway.dao.RouteFilterDao;
import com.ai.spring.boot.gateway.dao.bean.RouteFilter;
import com.ai.spring.boot.gateway.dao.repository.route.RouteFilterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 路由过滤策略
 *
 * @author 石头
 * @Date 2020/6/4
 * @Version 1.0
 **/
@Component
@Slf4j
public class RouteFilterDaoImpl extends BaseJpaDao<RouteFilterRepository,RouteFilter> implements RouteFilterDao {
    @Override
    @Cacheable(key = "'gateway:route:keys:filters'+ #ids")
    public List<RouteFilter> findRouteFiltersByIds(List<Long> ids) {
        List<RouteFilter> filterList = findByIds(ids);

        List<RouteFilter> routeFilters = Optional.ofNullable(filterList).flatMap(filters -> {
            List<RouteFilter> list = filters.stream().sorted(Comparator.comparing(RouteFilter::getOrder)).collect(Collectors.toList());

            return Optional.ofNullable(list);
        }).orElseGet(() -> Collections.EMPTY_LIST);
        log.debug("----{}:------路由过滤策信息:{}-----------------",ids,routeFilters);
        return routeFilters;
    }
}
