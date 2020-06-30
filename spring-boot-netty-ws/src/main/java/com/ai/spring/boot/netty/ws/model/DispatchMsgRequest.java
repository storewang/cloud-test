package com.ai.spring.boot.netty.ws.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消息转发处理
 *
 * @author 石头
 * @Date 2020/6/29
 * @Version 1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DispatchMsgRequest {
    private String        token;
    private MessageDTO    message;
    private ClientChannel channel;
}
