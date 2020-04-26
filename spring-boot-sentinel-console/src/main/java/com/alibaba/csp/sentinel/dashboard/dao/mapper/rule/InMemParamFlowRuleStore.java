package com.alibaba.csp.sentinel.dashboard.dao.mapper.rule;

import com.alibaba.csp.sentinel.dashboard.dao.entity.rule.ParamFlowRuleEntity;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowClusterConfig;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

/**
 * InMemParamFlowRule
 *
 * @author 石头
 * @Date 2020/4/26
 * @Version 1.0
 **/
@Component
public class InMemParamFlowRuleStore extends InMemoryRuleRepositoryAdapter<ParamFlowRuleEntity>{
    private static AtomicLong ids = new AtomicLong(0);

    @Override
    protected long nextId() {
        return ids.incrementAndGet();
    }

    @Override
    protected ParamFlowRuleEntity preProcess(ParamFlowRuleEntity entity) {
        if (entity != null && entity.isClusterMode()) {
            ParamFlowClusterConfig config = entity.getClusterConfig();
            if (config == null) {
                config = new ParamFlowClusterConfig();
            }
            // Set cluster rule id.
            config.setFlowId(entity.getId());
        }
        return entity;
    }
}
