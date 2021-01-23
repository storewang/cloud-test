package com.ai.spring.boot.im.dto.wxchart;

import lombok.Data;

/**
 * Created by 石头 on 2020/9/12.
 */
@Data
public class WxEventDTO {
    /**开发者微信号*/
    private String toUserName;
    /**发送方帐号（一个OpenID）*/
    private String fromUserName;
    /**消息创建时间 （整型）*/
    private Long createTime;
    /**消息类型，event*/
    private String msgType;
    /**事件类型，subscribe(订阅)、unsubscribe(取消订阅)*/
    private String event;
    /**事件KEY值，qrscene_为前缀，后面为二维码的参数值*/
    private String eventKey;
    /**二维码的ticket，可用来换取二维码图片*/
    private String ticket;

    public void setCreateTime(Long createTime){
        this.createTime = createTime;
    }
}
