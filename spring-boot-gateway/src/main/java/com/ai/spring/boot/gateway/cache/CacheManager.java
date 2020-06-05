package com.ai.spring.boot.gateway.cache;

import com.ai.spring.boot.gateway.cache.annotation.CacheEvict;
import com.ai.spring.boot.gateway.cache.annotation.Cacheable;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 缓存处理
 *
 * @author 石头
 * @Date 2020/6/5
 * @Version 1.0
 **/
@Slf4j
@Aspect
@Component
public class CacheManager {
    @Autowired
    private CacheTemplate redisTemplate;

    /**
     * 1. 获取数据先从缓存中获取，
     * 2. 如果获取不到再调用接口获取
     * 3. 如果调用接口成功并且返回数据，把返回的数据添加到缓存中。
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("@annotation(com.ai.spring.boot.gateway.cache.annotation.Cacheable)")
    public Object cacheable(ProceedingJoinPoint point) throws Throwable {
        try {
            Method method   = getTargetMethod(point);
            Class returnType = method.getReturnType();
            Cacheable annotation = method.getAnnotation(Cacheable.class);
            String cacheKey      = getCacheKey(point,method,annotation.key());
            int expireSec        = annotation.expireSec();
            boolean isarray      = returnType.isAssignableFrom(List.class);
            Object rtn           = null;
            if (isarray){
                Type genericType = method.getGenericReturnType();
                if(genericType instanceof ParameterizedType){
                    ParameterizedType pt = (ParameterizedType) genericType;
                    rtn = redisTemplate.getList(cacheKey,(Class)pt.getActualTypeArguments()[0]);
                }
            }else {
                rtn = redisTemplate.get(cacheKey, returnType);
            }
            if (rtn == null){
                rtn = point.proceed();
                // 添加缓存
                if (!StringUtils.isEmpty(cacheKey) && rtn!=null){
                    redisTemplate.set2Cache(cacheKey,rtn,isarray,expireSec);
                }
            }else {
                log.info("-------数据从缓存中获取 cacheKey:{}-----return:{}----------",cacheKey,rtn);
            }
            return rtn;
        }catch (Throwable e){
            throw e;
        }
    }

    /**
     * 删除缓存处理
     * 1。 调用接口逻辑
     * 2。 接口调用成功后，不敢有没有数据返回，都进行缓存清理。
     * @param point
     * @return
     * @throws Throwable
     */
    @Around("@annotation(com.ai.spring.boot.gateway.cache.annotation.CacheEvict)")
    public Object cacheClear(ProceedingJoinPoint point) throws Throwable {
        try {
            Method method = getTargetMethod(point);
            CacheEvict annotation = method.getAnnotation(CacheEvict.class);
            String cacheKey       = getCacheKey(point,method,annotation.key());

            Object rtn = point.proceed();

            // 清除缓存
            if (!StringUtils.isEmpty(cacheKey)){
                log.info("-------清空缓存 cacheKey:{}-----return:{}----------",cacheKey,rtn);
                redisTemplate.remove(cacheKey);
            }
            return rtn;
        }catch (Throwable e){
            throw e;
        }
    }
    private String getCacheKey(ProceedingJoinPoint point,Method method,String cacheKey){
        LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();
        String[] params = discoverer.getParameterNames(method);
        Object[] args   = point.getArgs();
        EvaluationContext context = new StandardEvaluationContext();
        for (int len = 0; len < params.length; len++) {
            context.setVariable(params[len], args[len]);
        }

        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression(cacheKey);
        cacheKey = expression.getValue(context, String.class);

        return cacheKey;
    }
    private Method getTargetMethod(ProceedingJoinPoint point) throws NoSuchMethodException{
        MethodSignature signature = (MethodSignature)point.getSignature();
        Method method   = signature.getMethod();
        return point.getTarget().getClass().getMethod(method.getName(),method.getParameterTypes());
    }
}
