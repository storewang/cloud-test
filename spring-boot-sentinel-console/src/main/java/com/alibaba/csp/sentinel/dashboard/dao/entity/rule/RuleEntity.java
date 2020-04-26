package com.alibaba.csp.sentinel.dashboard.dao.entity.rule;

import com.alibaba.csp.sentinel.slots.block.Rule;

import java.util.Date;

/**
 * 规则
 *
 * @author 石头
 * @Date 2020/4/26
 * @Version 1.0
 **/
public interface RuleEntity {
    Long getId();

    void setId(Long id);

    String getApp();

    String getIp();

    Integer getPort();

    Date getGmtCreate();

    Rule toRule();
}
