package com.ai.spring.boot.gateway.datasource.jpa.criteria;

import com.ai.spring.boot.gateway.datasource.jpa.BaseCriteriaBuilder;
import com.ai.spring.boot.gateway.datasource.jpa.annotation.Query;
import org.springframework.stereotype.Service;

/**
 * EQ
 *
 * @author 石头
 * @Date 2019/10/31
 * @Version 1.0
 **/
@Service("criteria_EQ")
public class EQCriteriaBuilder<T> extends BaseCriteriaBuilder<T> {
    public EQCriteriaBuilder() {
        super(Query.Type.EQ);
    }
}
