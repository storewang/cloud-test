package com.ai.spring.boot.gateway.service;

import com.ai.spring.boot.gateway.cache.annotation.Cacheable;
import com.ai.spring.boot.gateway.dao.RouteBindingDao;
import com.ai.spring.boot.gateway.dao.RouteFilterDao;
import com.ai.spring.boot.gateway.dao.RoutePredicateDao;
import com.ai.spring.boot.gateway.dao.bean.RouteBinding;
import com.ai.spring.boot.gateway.dao.bean.RouteFilter;
import com.ai.spring.boot.gateway.dao.bean.RoutePredicate;
import com.ai.spring.boot.gateway.dao.repository.route.RouteBindingRepository;
import com.ai.spring.boot.gateway.dto.RouteDefinitionDTO;
import com.ai.spring.boot.gateway.enums.RouteDataTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.tokenizeToStringArray;

/**
 * 路由信息服务
 *
 * @author 石头
 * @Date 2020/6/3
 * @Version 1.0
 **/
@Service
@Slf4j
public class RouteService {
    @Autowired
    private RouteBindingDao bindingDao;
    @Autowired
    private RouteFilterDao filterDao;
    @Autowired
    private RoutePredicateDao predicateDao;

    /**
     * 获取所有路由规则信息列表
     * 这个返回值的类型太复杂不好做缓存
     * @return
     */
    public List<RouteDefinition> findAllRouteBindings(){
        // 1. 获取路由绑定信息
        List<RouteBinding> bindingList = bindingDao.findAllRouteBindings();
        // 2. 按路由Id聚合,一个路由对应多条过滤器和多条匹配策略数据
        Map<String, List<RouteBinding>> routeMap = bindingList.stream().collect(Collectors.groupingBy(RouteBinding::getRouteId));

        List<RouteDefinition> routeDefinitions =  routeMap.keySet().stream().map(routeId -> {
            List<RouteBinding> routeBindings = routeMap.get(routeId);
            RouteBinding routeBinding = routeBindings.get(0);

            RouteDefinition routeDefinition = new RouteDefinition();
            routeDefinition.setId(routeBinding.getRouteId());
            routeDefinition.setUri(URI.create(routeBinding.getUrl()));
            routeDefinition.setOrder(routeBinding.getOrder());

            routeDefinition.setPredicates(getPredicatedfs(routeBindings));
            routeDefinition.setFilters(getFilterdefs(routeBindings));

            return routeDefinition;
        }).collect(Collectors.toList());
         // 排序
        routeDefinitions.sort(Comparator.comparing(RouteDefinition::getOrder));
//        return RouteDefinitionDTO.builder().routeDefinitions(routeDefinitions).build();
        return routeDefinitions;
    }

    /**
     * 根据路由绑定信息，获取绑定的路由过滤器列表
     * @param routeBindings
     * @return
     */
    private List<FilterDefinition> getFilterdefs(List<RouteBinding> routeBindings){
        List<Long> filterIds = routeBindings.stream()
                .filter(bind -> RouteDataTypeEnum.ROUTE_FILTER.getDataType().equals(bind.getDataType()))
                .map(bind -> bind.getDataId())
                .collect(Collectors.toList());

        return getFilters(filterIds);
    }

    /**
     * 根据路由绑定信息，获取绑定的路由策略列表
     * @param routeBindings
     * @return
     */
    private List<PredicateDefinition> getPredicatedfs(List<RouteBinding> routeBindings){
        List<Long> predicateIds = routeBindings.stream()
                .filter(bind -> RouteDataTypeEnum.ROUTE_PREDICATE.getDataType().equals(bind.getDataType()))
                .map(bind -> bind.getDataId())
                .collect(Collectors.toList());
        return getPredicates(predicateIds);
    }

    /**
     * 根据过滤器Id列表，获取路由过滤器列表
     * @param filterIds
     * @return
     */
    private List<FilterDefinition> getFilters(List<Long> filterIds){
        return Optional.ofNullable(filterIds).flatMap(ids -> getFilterDefinitions(ids)).orElse(Collections.EMPTY_LIST);
    }

    /**
     * 根据策略Id列表，获取路由策略列表
     * @param predicateIds
     * @return
     */
    private List<PredicateDefinition> getPredicates(List<Long> predicateIds){
        return Optional.ofNullable(predicateIds).flatMap(ids -> getPridicatedDefinition(ids)).orElse(Collections.EMPTY_LIST);
    }

    private Optional<List<PredicateDefinition>> getPridicatedDefinition(List<Long> ids){
        List<RoutePredicate> predicates = predicateDao.findRoutePredicatesByIds(ids);
        List<PredicateDefinition> predicateDefinitions = predicates.stream().map(predicate -> {
            PredicateDefinition predicateDefinition = new PredicateDefinition();
            predicateDefinition.setName(predicate.getName());
            predicateDefinition.setArgs(getArgs(predicate.getPredicate()));
            return predicateDefinition;
        }).collect(Collectors.toList());
        return Optional.ofNullable(predicateDefinitions);
    }
    private Optional<List<FilterDefinition>> getFilterDefinitions(List<Long> ids){
        List<RouteFilter> filterList = filterDao.findRouteFiltersByIds(ids);
        List<FilterDefinition> filterDefinitions = filterList.stream().map(filter -> {
            FilterDefinition filterDefinition = new FilterDefinition();
            filterDefinition.setName(filter.getName());
            filterDefinition.setArgs(getArgs(filter.getFilter()));
            return filterDefinition;
        }).collect(Collectors.toList());

        return Optional.ofNullable(filterDefinitions);
    }
    private Map<String, String> getArgs(String args){
        Map<String, String> argsMap = new LinkedHashMap<>();
        String[] argsArray = tokenizeToStringArray(args, ",");
        for (int i = 0; i < argsArray.length; i++) {
            argsMap.put(NameUtils.generateName(i), argsArray[i]);
        }
        return argsMap;
    }
}
