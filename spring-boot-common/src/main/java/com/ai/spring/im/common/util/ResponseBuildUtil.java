package com.ai.spring.im.common.util;

import com.ai.spring.im.common.bean.Response;

/**
 * Response创建类
 *
 * @author 石头
 * @Date 2020/3/25
 * @Version 1.0
 **/
public class ResponseBuildUtil {
    public static final String SUC_CODE = "1";
    public static final String ERR_CODE = "1";

    /**
     * 返回成功响应对象
     * @param data
     * @param <T>
     * @return
     */
    public static <T>Response<T> buildSuccess(T data){
        return Response.<T>builder().code(SUC_CODE).data(data);
    }

    /**
     * 返回成功响应对象
     * @param msg
     * @param <T>
     * @return
     */
    public static <T>Response<T> buildError(String msg){
        return buildError(ERR_CODE,msg);
    }

    /**
     * 返回成功响应对象
     * @param msg
     * @param <T>
     * @return
     */
    public static <T>Response<T> buildError(String code,String msg){
        if (code == null || code.length() == 0){
            code = ERR_CODE;
        }
        return Response.<T>builder().code(code).msg(msg);
    }
}
