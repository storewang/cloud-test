package com.ai.spring.im.common.bean;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * 接口通用返回类
 *
 * @author 石头
 * @Date 2020/3/25
 * @Version 1.0
 **/
@Data
@ToString
public class Response<T> {
    private String code;
    private String msg;
    private T data;

    public static <T>Response<T> builder(){
        return new Response();
    }

    public Response<T> code(String code){
        setCode(code);
        return this;
    }

    public Response<T> msg(String msg){
        setMsg(msg);
        return this;
    }
    public Response<T> data(T data){
        setData(data);
        return this;
    }
}
