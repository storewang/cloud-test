package com.ai.spring.boot.netty.ws.dao.bean;

import com.ai.spring.boot.ds.dao.bean.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 消息发送记录
 *
 * @author 石头
 * @Date 2020/7/1
 * @Version 1.0
 **/
@Data
@Entity
@Table(name="gtway_ws_message")
public class MessageRecord extends BaseEntity {
    /**主键*/
    @Id
    private Long id;
    /**消息发送者*/
    private String sender;
    /**消息接收者*/
    private String receiver;
    /**消息内容*/
    private String msg;
    /**数据有效性*/
    private Integer status;
    /**创建时间*/
    @Column(name = "create_time",insertable = false,updatable = false)
    private Date createTime;
    /**更新时间*/
    @Column(name = "update_time",updatable = false,insertable = false)
    private Date updateTime;
}
