package com.ai.spring.boot.netty.ws.dao;

import com.ai.spring.boot.netty.ws.dao.bean.MessageRecord;

import java.util.List;

/**
 * 消息发送记录操作
 *
 * @author 石头
 * @Date 2020/7/1
 * @Version 1.0
 **/
public interface IMessageRecordDao {
    /**添加消息记录*/
    Long saveMessage(MessageRecord echoMessage);
    /**更新消息不已发送状态*/
    Boolean updateMessageSended(Long messageId);
    /**更新消息为已读状态*/
    Boolean updateMessageReaded(Long messageId);

    /**
     * 更新消息记录的状态
     * @param messageId
     * @param status
     * @return
     */
    Boolean updateMessageStatus(Long messageId,Integer status);
    /**获取最新消息列表*/
    List<MessageRecord> getNewListMessage(String sender,Long minMsgId);
    /**
     * 获取未发送的消息
     * 获取消息的接收者为 sender的未发送状态的消息列表
     * @param sender
     * **/
    List<MessageRecord> getNotSendedMessage(String sender);
}
