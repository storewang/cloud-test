package com.ai.spring.boot.netty.ws.dao.impl;

import com.ai.spring.boot.ds.annotation.Query;
import com.ai.spring.boot.ds.dao.BaseJpaDao;
import com.ai.spring.boot.ds.dao.bean.Page;
import com.ai.spring.boot.ds.jpa.IQueryCriteria;
import com.ai.spring.boot.netty.ws.dao.IMessageRecordDao;
import com.ai.spring.boot.netty.ws.dao.bean.MessageRecord;
import com.ai.spring.boot.netty.ws.dao.repository.MessageRecordRepository;
import com.ai.spring.boot.netty.ws.util.Consts;
import lombok.Getter;
import lombok.Setter;
import org.assertj.core.util.Lists;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 消息发送记录操作
 *
 * @author 石头
 * @Date 2020/7/1
 * @Version 1.0
 **/
@Component
public class MessageRecordDaoImpl extends BaseJpaDao<MessageRecordRepository,MessageRecord>  implements IMessageRecordDao {
    @Override
    public Long saveMessage(MessageRecord echoMessage) {
        echoMessage.setStatus(Consts.MSG_STATUS_NO_SEND);
        MessageRecord record = save(echoMessage);

        return Optional.ofNullable(record).map(MessageRecord::getId).orElse(null);
    }

    @Override
    public Boolean updateMessageSended(Long messageId) {
        return updateMessageStatus(messageId,Consts.MSG_STATUS_SENDED);
    }

    @Override
    public Boolean updateMessageReaded(Long messageId) {
        return updateMessageStatus(messageId,Consts.MSG_STATUS_READED);
    }
    @Override
    public Boolean updateMessageStatus(Long messageId,Integer status){
        MessageRecord record = new MessageRecord();
        record.setId(messageId);
        record.setStatus(status);

        MessageRecord updrecored = updateBySenstive(record);
        return Optional.ofNullable(updrecored).map(msg -> msg.getId().equals(messageId)).orElse(Boolean.FALSE);
    }
    @Override
    public List<MessageRecord> getNewListMessage(String sender,Long minMsgId) {
        List<MessageRecord> allMessages = new ArrayList<>();
        // 查询接收的消息
        MessageQuery query = new MessageQuery();
        query.setReceiver(sender);
        query.setStatus(Lists.newArrayList(Consts.MSG_STATUS_SENDED));
        query.setMinId(minMsgId);

        Page<?> page = Page.builder().currentPage(0).pageSize(10).build();
        Page<MessageRecord> messageRecords = queryByCriteria(query, null,page);
        List<MessageRecord> records = Optional.ofNullable(messageRecords).map(rs -> rs.getData()).orElse(Collections.EMPTY_LIST);
        allMessages.addAll(records);

        // 查询自己发送的消息(包括成功和未成功(离线)的消息)
        query = new MessageQuery();
        query.setSender(sender);
        query.setStatus(Lists.newArrayList(Consts.MSG_STATUS_SENDED,Consts.MSG_STATUS_NO_SEND));
        query.setMinId(minMsgId);
        messageRecords = queryByCriteria(query, null,page);
        records = Optional.ofNullable(messageRecords).map(rs -> rs.getData()).orElse(Collections.EMPTY_LIST);
        allMessages.addAll(records);

        return allMessages;
    }

    @Override
    public List<MessageRecord> getNotSendedMessage(String sender) {
        // 获取发送给自己，但自己还没有进行接收的消息(发送给自己的离线消息)
        MessageQuery query = new MessageQuery();
        query.setReceiver(sender);
        query.setStatus(Lists.newArrayList(Consts.MSG_STATUS_NO_SEND));
        List<MessageRecord> fluxEchoMessages = queryByCriteria(query, null);

        return Optional.ofNullable(fluxEchoMessages).orElse(Collections.EMPTY_LIST);
    }

    @Setter
    @Getter
    private class MessageQuery implements IQueryCriteria{
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
