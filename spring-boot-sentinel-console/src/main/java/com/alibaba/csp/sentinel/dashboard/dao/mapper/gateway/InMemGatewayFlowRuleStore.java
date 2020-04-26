package com.alibaba.csp.sentinel.dashboard.dao.mapper.gateway;

import com.alibaba.csp.sentinel.dashboard.dao.entity.gateway.GatewayFlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.dao.mapper.rule.InMemoryRuleRepositoryAdapter;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

/**
 * InMemGatewayFlowRule
 *
 * @author 石头
 * @Date 2020/4/26
 * @Version 1.0
 **/
@Component
public class InMemGatewayFlowRuleStore extends InMemoryRuleRepositoryAdapter<GatewayFlowRuleEntity> {
    private static AtomicLong ids = new AtomicLong(0);

    @Override
    protected long nextId() {
        return ids.incrementAndGet();
    }
}
