package com.ai.spring.boot.gateway.pridicate;

import com.ai.spring.boot.gateway.pridicate.beans.TimeBetween;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * 自定义时间段谓词工厂
 *
 * @author 石头
 * @Date 2020/3/30
 * @Version 1.0
 **/
@Component
@Slf4j
public class TimeBetweenRoutePredicateFactory extends AbstractRoutePredicateFactory<TimeBetween>{
    public TimeBetweenRoutePredicateFactory() {
        super(TimeBetween.class);
    }

    @Override
    public Predicate<ServerWebExchange> apply(TimeBetween config) {
        LocalTime start = config.getStart();
        LocalTime end = config.getEnd();

        return exchange -> {
            LocalTime now = LocalTime.now();
            return now.isAfter(start) && now.isBefore(end);
        };
    }

    /**读取配置信息*/
    @Override
    public List<String> shortcutFieldOrder() {
        return Arrays.asList("start","end");
    }
}
