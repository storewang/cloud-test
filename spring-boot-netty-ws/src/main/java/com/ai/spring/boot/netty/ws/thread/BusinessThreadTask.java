package com.ai.spring.boot.netty.ws.thread;

import com.ai.spring.boot.netty.ws.model.MessageDTO;

/**
 * 业务线程任务
 *
 * @author 石头
 * @Date 2020/6/30
 * @Version 1.0
 **/
public interface BusinessThreadTask extends Runnable{

    MessageDTO getMessageDTO();
}
