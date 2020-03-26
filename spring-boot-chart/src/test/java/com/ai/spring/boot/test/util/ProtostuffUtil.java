package com.ai.spring.boot.test.util;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Google的protobuf因为其编码后体积小，序列化／反序列化性能好，被广泛使用
 * 但是protobuf需要编写.proto文件，再通过protobuf转换成对应的java代码，非常不好维护
 * protostuff就是为了解决这个痛点而产生的。通过protostuff，不需要编写.proto文件，
 * 只需要编写普通的java bean就可以使用protobuf的序列化／反序列化
 * @author 石头
 * @Date 2020/3/5
 * @Version 1.0
 **/
public class ProtostuffUtil {
    /**避免每次序列化都重新申请Buffer空间*/
    private static LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
    /**缓存schema*/
    private static Map<Class<?>, Schema<?>> schemaCache = new ConcurrentHashMap<>();

    /**
     * 序列化JAVA对象
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> byte[] serialize(T obj) {
        Class<T> cls = (Class<T>)obj.getClass();
        Schema<T> schema = getSchema(cls);
        byte[] data;
        try{
            data = ProtostuffIOUtil.toByteArray(obj,schema,buffer);
        }finally {
            buffer.clear();
        }
        return data;
    }

    /**
     * 反序列化JAVA对象
     * @param data
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T deserialize(byte[] data, Class<T> clazz) {
        Schema<T> schema = getSchema(clazz);
        T obj = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(data, obj, schema);

        return obj;
    }

    private static <T> Schema<T> getSchema(Class<T> clazz) {
        Schema<T> schema = (Schema<T>) schemaCache.get(clazz);
        if (Objects.isNull(schema)){
            schema = RuntimeSchema.getSchema(clazz);
            if (Objects.nonNull(schema)){
                schemaCache.put(clazz,schema);
            }
        }
        return schema;
    }
}
