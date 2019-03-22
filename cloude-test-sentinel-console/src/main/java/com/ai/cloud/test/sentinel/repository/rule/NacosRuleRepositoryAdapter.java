package com.ai.cloud.test.sentinel.repository.rule;

import com.ai.cloud.test.sentinel.datasource.entity.rule.RuleEntity;
import com.ai.cloud.test.sentinel.discovery.MachineInfo;

import java.util.List;

/**
 * TODO
 *
 * @Author 石头
 * @Date 2019/3/19
 * @Version 1.0
 **/
public abstract class NacosRuleRepositoryAdapter <T extends RuleEntity> implements RuleRepository<T, Long>{
    static final String DATAID = "com.alibaba.csp.sentinel.demo.flow.rule.json";
    static final String GROUP  = "Sentinel:Demo";
    static final Long TIMEOUT  = 1000l;
    @Override
    public T save(T entity) {
        return null;
    }

    @Override
    public List<T> saveAll(List<T> rules) {
        return null;
    }

    @Override
    public T delete(Long aLong) {
        return null;
    }

    @Override
    public T findById(Long aLong) {
        return null;
    }

    @Override
    public List<T> findAllByMachine(MachineInfo machineInfo) {
        return null;
    }

    @Override
    public List<T> findAllByApp(String appName) {
        return null;
    }
}
