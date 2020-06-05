package com.ai.spring.im.common.util;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

/**
 * 属性拷贝扩展
 *
 * @author 石头
 * @Date 2020/6/4
 * @Version 1.0
 **/
public class CopyUtil {
    public static void copyWithNoNullFieldVal(Object source,Object target){
        ReflectionUtils.doWithFields(source.getClass(),(field)->{
            ReflectionUtils.makeAccessible(field);
            Object fieldVal    = field.get(source);
            if (fieldVal!=null){
                String propName    = field.getName();

                Field targetField = ReflectionUtils.findField(target.getClass(),propName);
                if (targetField!=null){
                    ReflectionUtils.makeAccessible(field);
                    targetField.set(target,fieldVal);
                }
            }
        });
    }
}
