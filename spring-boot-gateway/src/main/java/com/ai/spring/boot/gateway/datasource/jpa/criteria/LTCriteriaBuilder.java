package com.ai.spring.boot.gateway.datasource.jpa.criteria;

import com.ai.spring.boot.gateway.datasource.jpa.BaseCriteriaBuilder;
import com.ai.spring.boot.gateway.datasource.jpa.annotation.Query;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;

/**
 * lessThan
 *
 * @author 石头
 * @Date 2019/10/31
 * @Version 1.0
 **/
@Service("criteria_LT")
public class LTCriteriaBuilder<T> extends BaseCriteriaBuilder<T> {
    public LTCriteriaBuilder() {
        super(Query.Type.LT);
    }

    protected Predicate getPredicate(){
        return cb.lessThan(getComparableExpression(),(Comparable)fieldVal);
    }
}

