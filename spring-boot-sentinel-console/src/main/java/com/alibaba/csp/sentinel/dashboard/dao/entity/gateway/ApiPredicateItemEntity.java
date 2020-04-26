package com.alibaba.csp.sentinel.dashboard.dao.entity.gateway;

import lombok.Data;

import java.util.Objects;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPredicateItem;
/**
 * Entity for {@link ApiPredicateItem}.
 *
 * @author 石头
 * @Date 2020/4/26
 * @Version 1.0
 **/
@Data
public class ApiPredicateItemEntity {
    private String pattern;

    private Integer matchStrategy;

    public ApiPredicateItemEntity() {
    }

    public ApiPredicateItemEntity(String pattern, int matchStrategy) {
        this.pattern = pattern;
        this.matchStrategy = matchStrategy;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        ApiPredicateItemEntity that = (ApiPredicateItemEntity) o;
        return Objects.equals(pattern, that.pattern) &&
                Objects.equals(matchStrategy, that.matchStrategy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pattern, matchStrategy);
    }

}
