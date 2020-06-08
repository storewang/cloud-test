package com.ai.spring.boot.ds.jpa.criteria;

import com.ai.spring.boot.ds.annotation.Query;
import com.ai.spring.boot.ds.jpa.BaseCriteriaBuilder;
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
