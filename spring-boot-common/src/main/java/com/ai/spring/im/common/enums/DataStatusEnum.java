package com.ai.spring.im.common.enums;

import lombok.Getter;

/**
 * 数据状态枚举
 *
 * @author 石头
 * @Date 2020/6/4
 * @Version 1.0
 **/
@Getter
public enum DataStatusEnum {
    /**数据-有效*/
    VALID(1,"有效"),
    /**数据-无效*/
    INVALID(0,"无效"),
    ;
    private Integer code;
    private String name;

    DataStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
