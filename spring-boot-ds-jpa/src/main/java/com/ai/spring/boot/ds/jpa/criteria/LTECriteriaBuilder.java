package com.ai.spring.boot.ds.jpa.criteria;

import com.ai.spring.boot.ds.annotation.Query;
import com.ai.spring.boot.ds.jpa.BaseCriteriaBuilder;
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
