package com.ai.spring.boot.netty.ws.service;

import com.ai.spring.boot.netty.ws.model.MessageDTO;

import java.util.List;

/**
 * message record 服务
 *
 * @author 石头
 * @Date 2020/7/1
 * @Version 1.0
 **/
public interface IMessageRecordService {
    /**
     * 获取最新消息列表
     * @param sender
     * @param minMsgId
     * @return
     */
    List<MessageDTO> getNewListMessage(String sender, Long minMsgId);
}
