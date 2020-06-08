package com.ai.spring.boot.ds.jpa.criteria;

import com.ai.spring.boot.ds.annotation.Query;
import com.ai.spring.boot.ds.jpa.BaseCriteriaBuilder;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;

/**
 * LIKE
 *
 * @author 石头
 * @Date 2019/10/31
 * @Version 1.0
 **/
@Service("criteria_LIKE")
public class LikeCriteriaBuilder<T> extends BaseCriteriaBuilder<T> {
    public LikeCriteriaBuilder() {
        super(Query.Type.LIKE);
    }

    protected Predicate getPredicate(){
        return cb.like(root.get(propName).as(String.class),"%" + fieldVal.toString() + "%");
    }
}
