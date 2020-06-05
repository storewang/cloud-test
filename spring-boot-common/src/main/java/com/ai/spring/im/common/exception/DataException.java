package com.ai.spring.im.common.exception;

import com.ai.spring.im.common.enums.ExceptionEnum;
import lombok.Getter;

/**
 * 数据类异常
 *
 * @author 石头
 * @Date 2020/6/4
 * @Version 1.0
 **/
@Getter
public class DataException extends RuntimeException{
    /**错误码*/
    private Integer code;
    public DataException(ExceptionEnum exceptionEnum){
        super(exceptionEnum.getName());
        this.code = exceptionEnum.getCode();
    }

    public DataException(Integer code, String msg){
        super(msg);
        this.code = code;
    }
    public DataException(Integer code, String message, Throwable throwable){
        super(message, throwable);
        this.code = code;
    }
}
