package com.ai.spring.boot.gateway.dao.repository.route;

import com.ai.spring.boot.gateway.dao.bean.RouteBinding;
import com.ai.spring.boot.gateway.dao.bean.RouteFilter;
import com.ai.spring.boot.gateway.dao.bean.RoutePredicate;
import com.ai.spring.boot.gateway.dao.repository.BaseJpaRepository;

import java.util.List;

/**
 * 路由信息存储相关操作
 *
 * @author 石头
 * @Date 2020/6/3
 * @Version 1.0
 **/
public interface RouteBindingRepository extends BaseJpaRepository<RouteBinding> {
}
