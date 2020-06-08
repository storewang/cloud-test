package com.ai.spring.boot.ds.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 排序
 *
 * @author 石头
 * @Date 2020/6/5
 * @Version 1.0
 **/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OrderBy {
    String propName() default "";
    Type type() default Type.ASC;
    enum Type{
        ASC,
        DESC
    }
}
