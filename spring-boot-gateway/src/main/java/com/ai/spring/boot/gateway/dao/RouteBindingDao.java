package com.ai.spring.boot.gateway.dao;

import com.ai.spring.boot.gateway.dao.bean.RouteBinding;

import java.util.List;

/**
 * 路由信息存储相关操作
 *
 * @author 石头
 * @Date 2020/6/4
 * @Version 1.0
 **/
public interface RouteBindingDao {
    List<RouteBinding> findAllRouteBindings();
}
