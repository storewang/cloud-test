package com.ai.spring.boot.gateway.datasource.jpa;

import com.ai.spring.boot.gateway.dao.bean.BaseEntity;
import com.ai.spring.boot.gateway.datasource.jpa.annotation.Query;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Criteria Builder Factory
 *
 * @author 石头
 * @Date 2020/6/5
 * @Version 1.0
 **/
@Component
public class CriteriaBuilderFactory implements ApplicationContextAware {
    private static final String PREFIX = "criteria_";
    /**spring 容器上下文对象实例*/
    private static ApplicationContext context;

    /**入口*/
    public static <T extends BaseEntity> void buildCriteria(Query.Type queryType, List<Predicate> list, Root<T> root, CriteriaBuilder cb, Class<?> fieldType, Object fieldVal, String propName){
        String beanName = PREFIX + queryType.name();
        BaseCriteriaBuilder<T> builder = context.getBean(beanName,BaseCriteriaBuilder.class);
        builder.setList(list);
        builder.setRoot(root);
        builder.setCb(cb);
        builder.setFieldType(fieldType);
        builder.setFieldVal(fieldVal);
        builder.setPropName(propName);

        builder.build();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        CriteriaBuilderFactory.context = applicationContext;
    }
}
