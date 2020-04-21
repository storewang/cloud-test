package com.ai.spring.boot.im.dto;

import lombok.Data;

/**
 * TODO
 *
 * @author 石头
 * @Date 2020/4/20
 * @Version 1.0
 **/
@Data
public class NotifyHouseInfo {
    /**房源ID*/
    private String houseId;
    /**房间ID*/
    private String roomId;
    /**房源状态值*/
    private String houseStatus;
    /**出租状态值*/
    private String rentStatus;
}
