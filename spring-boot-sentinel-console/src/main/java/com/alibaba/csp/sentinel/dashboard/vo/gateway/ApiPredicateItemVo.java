package com.alibaba.csp.sentinel.dashboard.vo.gateway;

import lombok.Data;

/**
 * Value Object for add or update gateway api.
 *
 * @author 石头
 * @Date 2020/5/6
 * @Version 1.0
 **/
@Data
public class ApiPredicateItemVo {
    private String pattern;

    private Integer matchStrategy;
}
