package com.ai.spring.boot.flux.ws.service.impl;

import org.springframework.stereotype.Service;

/**
 * 不支持的消息类型
 *
 * @author 石头
 * @Date 2020/5/29
 * @Version 1.0
 **/
@Service("MSG-SERVICE-TYPE-ERROR")
public class TypeNotSuportMessageService extends ErrorMessageService{
    private static final String ERROR_MSG = "不支持的消息类型，目前只支持文件消息和心跳消息";
    @Override
    protected String getErrorMsg(String content){
        return ERROR_MSG;
    }
}
