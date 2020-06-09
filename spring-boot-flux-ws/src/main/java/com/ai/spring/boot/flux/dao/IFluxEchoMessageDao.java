package com.ai.spring.boot.flux.dao;

import com.ai.spring.boot.flux.dao.bean.FluxEchoMessage;
import com.ai.spring.boot.flux.ws.dto.EchoMessage;

import java.util.List;

/**
 * 消息发送记录操作
 *
 * @author 石头
 * @Date 2020/6/9
 * @Version 1.0
 **/
public interface IFluxEchoMessageDao {
    /**添加消息记录*/
    Long saveEchoMessage(EchoMessage echoMessage);
    /**更新消息不已发送状态*/
    Boolean updateEchoMessageSended(EchoMessage echoMessage,Long messageId);
    /**更新消息为已读状态*/
    Boolean updateEchoMessageReaded(FluxEchoMessage fluxEchoMessage);
    /**获取最新消息列表*/
    List<FluxEchoMessage> getNewListMessage(FluxEchoMessage fluxEchoMessage);
    /**
     * 获取未发送的消息
     * 获取消息的接收者为 sender的未发送状态的消息列表
     * @param sender
     * **/
    List<EchoMessage> getNotSendedMessage(String sender);
}
