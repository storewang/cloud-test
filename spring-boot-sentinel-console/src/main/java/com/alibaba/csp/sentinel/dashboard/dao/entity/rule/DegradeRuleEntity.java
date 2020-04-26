package com.alibaba.csp.sentinel.dashboard.dao.entity.rule;

import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import lombok.Data;

import java.util.Date;

/**
 * DegradeRule
 *
 * @author 石头
 * @Date 2020/4/26
 * @Version 1.0
 **/
@Data
public class DegradeRuleEntity implements RuleEntity{
    private Long id;
    private String app;
    private String ip;
    private Integer port;
    private String resource;
    private String limitApp;
    private Double count;
    private Integer timeWindow;
    /**
     * 0 rt 限流; 1为异常;
     */
    private Integer grade;
    private Date gmtCreate;
    private Date gmtModified;

    public static DegradeRuleEntity fromDegradeRule(String app, String ip, Integer port, DegradeRule rule) {
        DegradeRuleEntity entity = new DegradeRuleEntity();
        entity.setApp(app);
        entity.setIp(ip);
        entity.setPort(port);
        entity.setResource(rule.getResource());
        entity.setLimitApp(rule.getLimitApp());
        entity.setCount(rule.getCount());
        entity.setTimeWindow(rule.getTimeWindow());
        entity.setGrade(rule.getGrade());
        return entity;
    }

    @Override
    public DegradeRule toRule() {
        DegradeRule rule = new DegradeRule();
        rule.setResource(resource);
        rule.setLimitApp(limitApp);
        rule.setCount(count);
        rule.setTimeWindow(timeWindow);
        rule.setGrade(grade);
        return rule;
    }
}
