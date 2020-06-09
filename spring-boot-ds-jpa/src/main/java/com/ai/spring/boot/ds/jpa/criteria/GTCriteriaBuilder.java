package com.ai.spring.boot.ds.jpa.criteria;

import com.ai.spring.boot.ds.annotation.Query;
import com.ai.spring.boot.ds.jpa.BaseCriteriaBuilder;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;

/**
 * greaterThan
 *
 * @author 石头
 * @Date 2019/10/31
 * @Version 1.0
 **/
@Service("criteria_GT")
public class GTCriteriaBuilder<T> extends BaseCriteriaBuilder<T> {
    public GTCriteriaBuilder() {
        super(Query.Type.GT);
    }

    protected Predicate getPredicate(){
        return cb.greaterThan(getComparableExpression(),(Comparable)fieldVal);
    }
}