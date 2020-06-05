package com.ai.spring.boot.gateway.datasource.jpa.criteria;

import com.ai.spring.boot.gateway.datasource.jpa.BaseCriteriaBuilder;
import com.ai.spring.boot.gateway.datasource.jpa.annotation.Query;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;

/**
 * greaterThanOrEqualTo
 *
 * @author 石头
 * @Date 2019/10/31
 * @Version 1.0
 **/
@Service("criteria_GTE")
public class GTECriteriaBuilder<T> extends BaseCriteriaBuilder<T> {
    public GTECriteriaBuilder() {
        super(Query.Type.GTE);
    }

    protected Predicate getPredicate(){
        return cb.greaterThanOrEqualTo(getComparableExpression(),(Comparable)fieldVal);
    }
}
