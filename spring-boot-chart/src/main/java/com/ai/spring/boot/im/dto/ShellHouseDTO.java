package com.ai.spring.boot.im.dto;

import lombok.Data;
import lombok.ToString;

/**
 * TODO
 *
 * @author 石头
 * @Date 2020/3/16
 * @Version 1.0
 **/
@Data
@ToString
public class ShellHouseDTO {
    /**回调类型 200-上下架状态回调；300-集中式房源审核回调*/
    private String eventType;
    /**回调数据信息 不同回调类型的字段信息如下*/
    private Object eventData;
}
