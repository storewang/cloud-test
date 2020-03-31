package com.ai.spring.boot.gateway.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractNameValueGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

/**
 * 添加打印日志逻辑拦截器
 * filter过滤器执行顺序
 * <pre>
 *     1. Order值越小越先执行
 *     2. 局部过滤器的order会按配置的顺序从1开始递增
 *     3. 如果配置了默认过滤器，默认过滤器的order也会按配置的顺序从1开始递增
 *        这样会和局部过滤器的order重复，那么执行顺序就是：默认过滤器1->局部过滤器1->默认过滤器2->局部过滤器2....
 * </pre>
 *
 * @author 石头
 * @Date 2020/3/30
 * @Version 1.0
 **/
@Component
@Slf4j
public class PreLogGatewayFilterFactory extends AbstractNameValueGatewayFilterFactory{
    @Override
    public GatewayFilter apply(NameValueConfig config) {
        GatewayFilter filter =  ((exchange, chain) -> {
            log.info("请求进来了。。。{},{}",config.getName(),config.getValue());
            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate().build();

            ServerWebExchange modifiedExchange = exchange.mutate().request(modifiedRequest).build();

            return chain.filter(modifiedExchange);
        });
        // 4. 如果要指定Filter的order则要返回OrderedGatewayFilter这个实例，并指定order值
        return new OrderedGatewayFilter(filter,10000);
    }
}
