package com.ai.spring.boot.netty.ws.service;

import com.ai.spring.boot.netty.ws.model.ClientChannel;
import com.ai.spring.boot.netty.ws.model.DispatchMsgRequest;
import com.ai.spring.boot.netty.ws.model.UserDTO;

import java.util.List;

/**
 * channel handler 相关处理service
 *
 * @author 石头
 * @Date 2020/6/28
 * @Version 1.0
 **/
public interface ServerHandlerService {
    /**
     * token验证
     * @param token
     * @return
     */
    Boolean checkToken(String token);

    /**
     * 根据登录token获取用户信息
     * @param token
     * @return
     */
    UserDTO getUserByToken(String token);

    /**
     * 根据用户code获取用户信息
     * @param code
     * @return
     */
    UserDTO getUserByCode(String code);

    /**
     * 批量获取用户信息
     * @param codes
     * @return
     */
    List<UserDTO> getUserByCode(List<String> codes);
    /**
     * 注册客户端信息
     * @param clientChannel
     */
    void register(ClientChannel clientChannel);

    /**
     * 消息发送处理
     * @param request
     */
    void dispatcher(DispatchMsgRequest request);

    /**
     * 监控资源信息
     */
    void monitorResources();

    /**
     * 根据用户code获取与本机连接的channel
     * @param userCode
     * @return
     */
    ClientChannel getClientChannelByUcode(String userCode);
}
