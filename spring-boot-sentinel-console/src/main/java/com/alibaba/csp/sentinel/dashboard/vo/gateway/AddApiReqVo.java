package com.alibaba.csp.sentinel.dashboard.vo.gateway;

import lombok.Data;

import java.util.List;

/**
 * Value Object for add gateway api.
 *
 * @author 石头
 * @Date 2020/5/6
 * @Version 1.0
 **/
@Data
public class AddApiReqVo {
    private String app;

    private String ip;

    private Integer port;

    private String apiName;

    private List<ApiPredicateItemVo> predicateItems;
}
