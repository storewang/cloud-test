package com.ai.spring.boot.netty.ws.util;

import com.ai.spring.boot.netty.ws.dao.bean.MessageRecord;
import com.ai.spring.boot.netty.ws.model.MessageDTO;
import com.ai.spring.boot.netty.ws.model.UserDTO;

/**
 * Message 工具类
 *
 * @author 石头
 * @Date 2020/7/3
 * @Version 1.0
 **/
public class MessageUtil {
    public static MessageDTO messageRecord2MessageDTO(MessageRecord messageRecord){
        MessageDTO.MessageDTOBuilder builder = MessageDTO.builder();
        builder.content(messageRecord.getMsg());
        builder.msgType(MessageType.MSG_CONTENT.getMsgType());
        builder.msgId(messageRecord.getId().toString());
        builder.from(UserDTO.builder().userCode(messageRecord.getSender()).build());
        builder.to(UserDTO.builder().userCode(messageRecord.getReceiver()).build());
        builder.createTime(messageRecord.getCreateTime());
        return builder.build();
    }
}
