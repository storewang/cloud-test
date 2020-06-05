package com.ai.spring.boot.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cloud.gateway.route.RouteDefinition;

import java.util.List;

/**
 * 所有路由规则信息列表
 *
 * @author 石头
 * @Date 2020/6/5
 * @Version 1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RouteDefinitionDTO {
    private String code;
    private List<RouteDefinition> routeDefinitions;
}
