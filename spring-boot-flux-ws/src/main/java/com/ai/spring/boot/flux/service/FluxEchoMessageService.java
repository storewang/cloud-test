package com.ai.spring.boot.flux.service;

import com.ai.spring.boot.flux.convert.FluxEchoMessageDTOConvert;
import com.ai.spring.boot.flux.dao.IFluxEchoMessageDao;
import com.ai.spring.boot.flux.dao.bean.FluxEchoMessage;
import com.ai.spring.boot.flux.dto.FluxEchoMessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 消息发送记录服务
 *
 * @author 石头
 * @Date 2020/6/9
 * @Version 1.0
 **/
@Service
public class FluxEchoMessageService {
    @Autowired
    private IFluxEchoMessageDao messageDao;
    @Autowired
    private FluxEchoMessageDTOConvert convert;

    public List<FluxEchoMessageDTO> getNewListMessage(Long userId, Long messageId){
        FluxEchoMessage echoMessage = new FluxEchoMessage();
        echoMessage.setSender(userId.toString());
        echoMessage.setId( messageId);
        List<FluxEchoMessage> listMessage = messageDao.getNewListMessage(echoMessage);

        return convert.toDto(listMessage);
    }
}
