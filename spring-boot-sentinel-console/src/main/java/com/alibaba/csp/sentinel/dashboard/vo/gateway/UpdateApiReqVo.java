package com.alibaba.csp.sentinel.dashboard.vo.gateway;

import lombok.Data;

import java.util.List;

/**
 * TODO
 *
 * @author 石头
 * @Date 2020/5/6
 * @Version 1.0
 **/
@Data
public class UpdateApiReqVo {
    private Long id;

    private String app;

    private List<ApiPredicateItemVo> predicateItems;

}
