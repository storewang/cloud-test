package com.ai.spring.boot.netty.ws.service.impl;

import com.ai.spring.boot.netty.ws.dao.IMessageRecordDao;
import com.ai.spring.boot.netty.ws.dao.bean.MessageRecord;
import com.ai.spring.boot.netty.ws.model.MessageDTO;
import com.ai.spring.boot.netty.ws.model.UserDTO;
import com.ai.spring.boot.netty.ws.service.IMessageRecordService;
import com.ai.spring.boot.netty.ws.util.MessageType;
import com.ai.spring.boot.netty.ws.util.MessageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * message record 服务
 *
 * @author 石头
 * @Date 2020/7/1
 * @Version 1.0
 **/
@Service
public class MessageRecordServiceImpl implements IMessageRecordService {
    @Autowired
    private IMessageRecordDao messageRecordDao;
    @Override
    public List<MessageDTO> getNewListMessage(String sender,Long minMsgId){
        List<MessageRecord> recordList = messageRecordDao.getNewListMessage(sender, minMsgId);

        return Optional.ofNullable(recordList).flatMap(records -> {
            List<MessageDTO> messageDTOS = records
                    .stream()
                    .map(record -> MessageUtil.messageRecord2MessageDTO(record))
                    .collect(Collectors.toList());
            return Optional.ofNullable(messageDTOS);
        }).orElse(Collections.EMPTY_LIST);
    }
}
