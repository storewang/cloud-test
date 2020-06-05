package com.ai.spring.boot.gateway.datasource.jpa.criteria;

import com.ai.spring.boot.gateway.datasource.jpa.BaseCriteriaBuilder;
import com.ai.spring.boot.gateway.datasource.jpa.annotation.Query;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;

/**
 * lessThanOrEqualTo
 *
 * @author 石头
 * @Date 2019/10/31
 * @Version 1.0
 **/
@Service("criteria_LTE")
public class LTECriteriaBuilder<T> extends BaseCriteriaBuilder<T> {
    public LTECriteriaBuilder() {
        super(Query.Type.LTE);
    }

    protected Predicate getPredicate(){
        return cb.lessThanOrEqualTo(getComparableExpression(),(Comparable)fieldVal);
    }
}
