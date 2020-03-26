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
public class ShellHouseBodyDTO {
    /**房源code 贝壳房源唯一性标示*/
    private String houseCode;
    /**房源类型 1-分散式房源；2-集中式房源  集中式房源审核回调: 200000-集中式房间；400000-集中式房型；500000-集中式门店*/
    private String houseType;
    /**上下架状态 0 未上架，1 上架中，2 已上架，3下架中；*/
    private String onoffStatus;
    /**上下架时间 上下架时间；*/
    private String onoffTime;
    /**上下架描述 上下架描述；*/
    private String onoffDesc;
    /**上下架原因 上下架原因；*/
    private String onoffReason;
    /**上下架触发方 1-用户操作，2-问题房源操作，3-openapi操作，4-C端上下架消息，5-房源盘点、6-广厦app*/
    private String onoffSource;
    /**审核状态 0 无，1 审核中，2 未通过，3 审核通过；*/
    private String auditStatus;
    /**审核结果描述 审核结果描述*/
    private String auditDesc;
    /**审核时间*/
    private String auditTime;
    /**是否是问题房源 0 否，1 是；*/
    private String problemStatus;
    /**问题房源判定时间*/
    private String problemTime;
    /**问题房源描述*/
    private String problemDesc;
    /**审核结果*/
    private String checkResult;
    /**审核原因*/
    private String reason;
}
