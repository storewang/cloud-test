package com.ai.spring.boot.gateway.datasource.jpa.criteria;

import com.ai.spring.boot.gateway.datasource.jpa.BaseCriteriaBuilder;
import com.ai.spring.boot.gateway.datasource.jpa.annotation.Query;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.Collection;

/**
 * IN
 *
 * @author 石头
 * @Date 2019/10/31
 * @Version 1.0
 **/
@Service("criteria_IN")
public class INCriteriaBuilder<T> extends BaseCriteriaBuilder<T> {
    public INCriteriaBuilder() {
        super(Query.Type.IN);
    }

    protected Predicate getPredicate(){
        if (fieldVal instanceof Collection){
            return root.get(propName).in((Collection)fieldVal);
        }else {
            return root.get(propName).in(fieldVal);
        }
    }
}
