package com.ai.spring.boot.gateway.routes;

import com.ai.spring.boot.gateway.service.RouteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * 数据库存储路由配置信息
 *
 * @author 石头
 * @Date 2020/6/2
 * @Version 1.0
 **/
@Slf4j
public class DataBaseRouteDefinitionRepository implements RouteDefinitionRepository{
    @Autowired
    private RouteService routeService;

    /**
     * 这里每次请求都会调用这个接口，所以需要做缓存
     * @return
     */
    @Override
    public Flux<RouteDefinition> getRouteDefinitions() {
        log.info("-------查询所有路由信息-------------");
//        RouteDefinitionDTO routeDefinitionDTO = routeService.findAllRouteBindings();
        List<RouteDefinition> routeDefinitionList = routeService.findAllRouteBindings();

        return Flux.fromIterable(routeDefinitionList);
    }

    @Override
    public Mono<Void> save(Mono<RouteDefinition> route) {
        return null;
    }

    @Override
    public Mono<Void> delete(Mono<String> routeId) {
        return null;
    }
}
