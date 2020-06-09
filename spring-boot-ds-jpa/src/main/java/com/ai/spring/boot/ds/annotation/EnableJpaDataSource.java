package com.ai.spring.boot.ds.annotation;

import com.ai.spring.boot.ds.conf.DatasourceConfigurationRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 开启JPA数据源
 *
 * @author 石头
 * @Date 2020/6/9
 * @Version 1.0
 **/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(DatasourceConfigurationRegistrar.class)
public @interface EnableJpaDataSource{
    /**entity 扫描包*/
    String[] entityBasePackages() default {};
    /**repository 扫描包*/
    String[] basePackages() default {};
}
