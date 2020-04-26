package com.alibaba.csp.sentinel.dashboard.dao.mapper.rule;

import com.alibaba.csp.sentinel.dashboard.dao.entity.rule.AuthorityRuleEntity;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

/**
 * InMemAuthorityRule
 *
 * @author 石头
 * @Date 2020/4/26
 * @Version 1.0
 **/
@Component
public class InMemAuthorityRuleStore extends InMemoryRuleRepositoryAdapter<AuthorityRuleEntity>{
    private static AtomicLong ids = new AtomicLong(0);

    @Override
    protected long nextId() {
        return ids.incrementAndGet();
    }
}
