package com.ai.spring.boot.gateway.cache.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 缓存标识
 *
 * @author 石头
 * @Date 2020/6/5
 * @Version 1.0
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Cacheable {
    String key ();
    /**过期时间（秒），默认7200秒，2小时　*/
    int expireSec() default 7200;
}
