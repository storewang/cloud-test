package com.ai.spring.boot.flux.dao.impl;

import com.ai.spring.boot.ds.annotation.Query;
import com.ai.spring.boot.ds.dao.BaseJpaDao;
import com.ai.spring.boot.ds.dao.bean.Page;
import com.ai.spring.boot.ds.jpa.IQueryCriteria;
import com.ai.spring.boot.flux.dao.IFluxEchoMessageDao;
import com.ai.spring.boot.flux.dao.bean.FluxEchoMessage;
import com.ai.spring.boot.flux.dao.repository.FluxEchoMessageRepository;
import com.ai.spring.boot.flux.ws.dto.EchoMessage;
import com.ai.spring.boot.flux.ws.util.Constans;
import lombok.Data;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 消息发送记录操作
 *
 * @author 石头
 * @Date 2020/6/9
 * @Version 1.0
 **/
@Component
public class FluxEchoMessageDaoImpl extends BaseJpaDao<FluxEchoMessageRepository,FluxEchoMessage> implements IFluxEchoMessageDao {

    public Long saveEchoMessage(EchoMessage echoMessage){
        FluxEchoMessage fluxEchoMessage = new FluxEchoMessage();
        fluxEchoMessage.setSender(echoMessage.getFrom());
        fluxEchoMessage.setReceiver(echoMessage.getTo());
        fluxEchoMessage.setMsg(echoMessage.getMsg());
        fluxEchoMessage.setStatus(Constans.MSG_STATUS_NO_SEND);
        FluxEchoMessage addedData = save(fluxEchoMessage);
        return addedData.getId();
    }

    public Boolean updateEchoMessageSended(EchoMessage echoMessage,Long messageId){
        FluxEchoMessage fluxEchoMessage = new FluxEchoMessage();
        fluxEchoMessage.setId(messageId);
        fluxEchoMessage.setStatus(Constans.MSG_STATUS_SENDED);

        FluxEchoMessage updData = updateBySenstive(fluxEchoMessage);
        return updData!=null && updData.getId().equals(messageId);
    }

    public Boolean updateEchoMessageReaded(FluxEchoMessage fluxEchoMessage){
        fluxEchoMessage.setStatus(Constans.MSG_STATUS_READED);
        FluxEchoMessage updData = updateBySenstive(fluxEchoMessage);
        return updData!=null && updData.getId().equals(fluxEchoMessage.getId());
    }

    public List<EchoMessage> getNotSendedMessage(String sender){
        MessageQuery query = new MessageQuery();
        query.setReceiver(sender);
        query.setStatus(Lists.newArrayList(Constans.MSG_STATUS_NO_SEND));
        List<FluxEchoMessage> fluxEchoMessages = queryByCriteria(query, null);

        return Optional.ofNullable(fluxEchoMessages).flatMap(msgs ->
                Optional.ofNullable(msgs.stream().map(msg -> {
                    EchoMessage message = new EchoMessage();
                    message.setFrom(msg.getSender());
                    message.setTo(msg.getReceiver());
                    message.setMsg(msg.getMsg());
                    message.setMessageId(msg.getId());

                    return message;
                }).collect(Collectors.toList()))
        ).orElse(Collections.EMPTY_LIST);
    }

    public List<FluxEchoMessage> getNewListMessage(FluxEchoMessage fluxEchoMessage){
        List<FluxEchoMessage> allMessages = new ArrayList<>();
        // 1. 查询接收的消息
        MessageQuery query = new MessageQuery();
        query.setReceiver(fluxEchoMessage.getSender());
        query.setStatus(Lists.newArrayList(Constans.MSG_STATUS_SENDED));
        query.setMinId(fluxEchoMessage.getId());

        Page<?> page = Page.builder().currentPage(0).pageSize(10).build();
        Page<FluxEchoMessage> fluxEchoMessages = queryByCriteria(query, null,page);
        if (!CollectionUtils.isEmpty(fluxEchoMessages.getData())){
            allMessages.addAll(fluxEchoMessages.getData());
        }

        // 2. 查询发送的消息
        query = new MessageQuery();
        query.setSender(fluxEchoMessage.getSender());
        query.setStatus(Lists.newArrayList(Constans.MSG_STATUS_SENDED,Constans.MSG_STATUS_NO_SEND));
        query.setMinId(fluxEchoMessage.getId());
        fluxEchoMessages = queryByCriteria(query, null,page);
        if (!CollectionUtils.isEmpty(fluxEchoMessages.getData())){
            allMessages.addAll(fluxEchoMessages.getData());
        }

        return allMessages;
    }

    @Data
    private class MessageQuery implements IQueryCriteria {
        /**消息发送者*/
        @Query
        private String sender;
        /**消息接收者*/
        @Query
        private String receiver;
        /**数据有效性*/
        @Query(propName="status",type= Query.Type.IN)
        private List<Integer> status;
        /**最小Id*/
        @Query(propName="id",type= Query.Type.GT)
        private Long minId;

        @Override
        public String groupBy() {
            return null;
        }
    }
}
