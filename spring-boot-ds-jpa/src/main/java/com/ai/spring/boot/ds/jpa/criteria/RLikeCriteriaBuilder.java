package com.ai.spring.boot.ds.jpa.criteria;

import com.ai.spring.boot.ds.annotation.Query;
import com.ai.spring.boot.ds.jpa.BaseCriteriaBuilder;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;


/**
 * RIGHT_LIKE
 *
 * @author 石头
 * @Date 2019/10/31
 * @Version 1.0
 **/
@Service("criteria_RIGHT_LIKE")
public class RLikeCriteriaBuilder<T> extends BaseCriteriaBuilder<T> {
    public RLikeCriteriaBuilder() {
        super(Query.Type.RIGHT_LIKE);
    }

    protected Predicate getPredicate(){
        return cb.like(root.get(propName).as(String.class),fieldVal.toString() + "%");
    }
}
