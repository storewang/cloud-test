package com.ai.spring.boot.netty.json.test;

import com.ai.spring.boot.netty.ws.model.MessageDTO;
import com.ai.spring.boot.netty.ws.model.UserDTO;
import com.ai.spring.boot.netty.ws.util.MessageType;
import com.ai.spring.im.common.util.IdWorker;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Calendar;

/**
 * TODO
 *
 * @author 石头
 * @Date 2020/6/29
 * @Version 1.0
 **/
public class TestJosn {
    public final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testReadValue() throws IOException {
        String msgJson = mapper.writeValueAsString(getMock());

        MessageDTO messageDTO = mapper.readValue(msgJson, new TypeReference<MessageDTO>() {
        });

        Assert.assertNotNull(messageDTO);
        Assert.assertEquals("test02",messageDTO.getTos().get(1).getUserName());
        Assert.assertEquals("test01",messageDTO.getFrom().getUserName());

    }
    @Test
    public void testToJson() throws JsonProcessingException {
        String msgJson = mapper.writeValueAsString(getMock());
        System.out.println("-------json:------------"+msgJson);
    }

    private MessageDTO getMock(){
        UserDTO from = UserDTO.builder()
                .userCode("u001")
                .userId(1000001l)
                .userName("test01")
                .build();

        UserDTO toUser = UserDTO.builder()
                .userCode("u002")
                .userId(1000002l)
                .userName("test02")
                .build();
        MessageDTO messageDTO = MessageDTO.builder()
                .content("发送消息Demo")
                .createTime(Calendar.getInstance().getTime())
                .from(from)
                .msgId(IdWorker.getIdStr())
                .msgType(MessageType.USER_ONLINE.getMsgType())
                .to(toUser)
                .tos(Lists.newArrayList(from,toUser))
                .build();
        return messageDTO;
    }
}
