package com.ai.spring.boot.gateway.datasource.jpa;

import com.ai.spring.boot.gateway.datasource.jpa.annotation.Query;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * jpa 条件创建
 *
 * @author 石头
 * @Date 2020/6/5
 * @Version 1.0
 **/
@Setter
@Slf4j
public class BaseCriteriaBuilder<T> {
    protected List<Predicate> list;
    protected Root<T> root;
    protected CriteriaBuilder cb;
    protected Query.Type queryType;

    protected Class<?> fieldType;
    protected Object fieldVal;
    protected String propName;
    public BaseCriteriaBuilder(Query.Type queryType){
        this.queryType = queryType;
    }

    public void build(){
        if(validate()){
            list.add(getPredicate());
        }
    }
    protected Predicate getPredicate(){
        return cb.equal(getExpression(),fieldVal);
    }
    protected Expression getExpression(){
        return root.get(propName).as(fieldType);
    }
    protected Expression<? extends Comparable> getComparableExpression(){
        return root.get(propName).as((Class<? extends Comparable>)fieldType);
    }
    private boolean validate(){
        if (list == null ){
            log.warn("{} 创建器的 Predicate list,Root<T> root,CriteriaBuilder cb,Class<?> fieldType,Object fieldVal,String propName还没有设置");
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "BaseCriteriaBuilder{" +
                "queryType=" + queryType +
                ", fieldType=" + fieldType +
                ", propName='" + propName + '\'' +
                '}';
    }
}
