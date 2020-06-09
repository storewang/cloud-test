package com.ai.spring.boot.gateway.dao.impl;

import com.ai.spring.boot.ds.annotation.OrderBy;
import com.ai.spring.boot.ds.annotation.Query;
import com.ai.spring.boot.ds.dao.BaseJpaDao;
import com.ai.spring.boot.ds.jpa.IQueryCriteria;
import com.ai.spring.boot.gateway.cache.annotation.Cacheable;
import com.ai.spring.boot.gateway.dao.RouteBindingDao;
import com.ai.spring.boot.gateway.dao.bean.RouteBinding;
import com.ai.spring.boot.gateway.dao.repository.route.RouteBindingRepository;
import com.ai.spring.im.common.enums.DataStatusEnum;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 路由信息存储相关操作
 *
 * @author 石头
 * @Date 2020/6/4
 * @Version 1.0
 **/
@Component
@Slf4j
public class RouteBindingDaoImpl extends BaseJpaDao<RouteBindingRepository,RouteBinding> implements RouteBindingDao {

    @Override
    @Cacheable(key = "'gateway:route:keys:allbindings'")
    public List<RouteBinding> findAllRouteBindings() {
        RouteBindQuery query = new RouteBindQuery();
        query.setStatus(DataStatusEnum.VALID.getCode());
        query.setOrder("order");

        List<RouteBinding> routeBindings = super.queryByCriteria(query,null);

        log.debug("----------路由绑定信息:{}-----------------",routeBindings);
        return Optional.ofNullable(routeBindings).orElseGet(() -> Collections.EMPTY_LIST);
    }
    @Data
    private class RouteBindQuery implements IQueryCriteria {
        @Query
        private String routeId;
        @Query
        private Integer status;
        @OrderBy(type=OrderBy.Type.ASC)
        private String order;
        @Override
        public String groupBy() {
            return null;
        }
    }
}
