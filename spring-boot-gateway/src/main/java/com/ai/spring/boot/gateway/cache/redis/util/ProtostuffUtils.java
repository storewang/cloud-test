package com.ai.spring.boot.gateway.cache.redis.util;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 序列化工具类
 *
 * @author 石头
 * @Date 2020/6/5
 * @Version 1.0
 **/
public class ProtostuffUtils {
    /**避免每次序列化都重新申请Buffer空间*/
    private static LinkedBuffer buffer = LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE);
    /**缓存schema*/
    private static Map<Class<?>, Schema<?>> schemaCache = new ConcurrentHashMap<>();

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
    public static <T> byte[] serializeList(List<T> obj) {
        Class<T> cls = (Class<T>)obj.get(0).getClass();
        Schema<T> schema = getSchema(cls);

        byte[] data;
        ByteArrayOutputStream bos = null;
        try{
            bos = new ByteArrayOutputStream();
            ProtostuffIOUtil.writeListTo(bos, obj, schema, buffer);
            data = bos.toByteArray();
        }catch (Exception e) {
            throw new RuntimeException("Failed to serializer");
        }finally {
            buffer.clear();
        }
        return data;
    }
    public static <T> T deserialize(byte[] data, Class<T> clazz) {
        Schema<T> schema = getSchema(clazz);
        T obj = schema.newMessage();
        ProtostuffIOUtil.mergeFrom(data, obj, schema);

        return obj;
    }
    public static <T> List<T> deserializeList(byte[] data, Class<T> clazz) {
        List<T> result;
        ByteArrayInputStream inputStream;
        try{
            Schema<T> schema = getSchema(clazz);
            inputStream = new ByteArrayInputStream(data);
            result = ProtostuffIOUtil.parseListFrom(inputStream,schema);
        }catch (Exception e){
            throw new RuntimeException("Failed to deserialize");
        }
        return result;
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
