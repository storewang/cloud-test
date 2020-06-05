package com.ai.spring.im.common.enums;

import lombok.Getter;

/**
 * 错误码枚举
 *
 * @author 石头
 * @Date 2020/6/4
 * @Version 1.0
 **/
@Getter
public enum ExceptionEnum {
    DATA_ATTR_ERROR(101,"数据参数不正确"),
    /**数据-有效*/
    DATA_NOT_EXIST(100,"数据不存在"),
    PARAM_ID_EXIST(200,"[参数不正确]%sid不存在"),
    PARAM_NO_EXIST(201,"[参数不正确]参数%s不能为空"),
    PARAM_BIND_ERROR(202,"[参数不正确]参数格式不正确"),
    DATA_NO_EXIST(203,"[数据不正确][%s]不存在"),
    USER_ACCESS_ERROR(403,"[权限不足]没有权限访问"),

    ERROR_REQ_SCOPE(40001,"不支持的请求参数方式[%s]"),
    ERROR_PARAM(40002,"参数解释失败"),
    ;
    private Integer code;
    private String name;

    ExceptionEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
