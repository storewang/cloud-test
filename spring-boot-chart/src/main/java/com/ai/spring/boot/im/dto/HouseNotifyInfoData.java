package com.ai.spring.boot.im.dto;

import lombok.Data;
import lombok.ToString;

/**
 * 房源消息通知
 *
 * @author 石头
 * @Date 2020/4/21
 * @Version 1.0
 **/
@Data
@ToString(callSuper = true)
public class HouseNotifyInfoData extends NotifyInfoData<NotifyHouseInfo> {
}
