package com.ai.spring.boot.gateway.datasource.jpa.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 条件查询
 *
 * @author 石头
 * @Date 2020/6/5
 * @Version 1.0
 **/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Query {
    /**
     * LIKE,RIGHT_LIKE时这参是String类型
     * @return
     */
    String propName() default "";
    Type type() default Type.EQ;

    enum Type{
        EQ,
        GT,
        LT,
        GTE,
        LTE,
        RIGHT_LIKE,
        LIKE,
        IN
    }
}
