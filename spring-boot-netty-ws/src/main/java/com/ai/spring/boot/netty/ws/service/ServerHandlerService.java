package com.ai.spring.boot.netty.ws.service;

import com.ai.spring.boot.netty.ws.model.ClientChannel;
import com.ai.spring.boot.netty.ws.model.DispatchMsgRequest;
import com.ai.spring.boot.netty.ws.model.MessageDTO;
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
     * 用户登录
     * @param userId
     * @return
     */
    String login(Long userId);
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
     * 注销账户信息
     * @param token
     * @param channelId
     */
    void unRegister(String token,String channelId);
    /**
     * 消息发送处理
     * @param request
     */
    void dispatcher(DispatchMsgRequest request);

    /**
     * 消息转发
     * @param message
     */
    void forwardMesage(MessageDTO message,Long msgId);

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

    /**
     * 添加消息记录
     * @param message
     * @return
     */
    Long saveMessage(MessageDTO message);

    /**
     * 设置消息已经发送
     * @param msgId
     */
    void makeMessageHasSended(Long msgId);

    /**
     * 消息发送，只发本机的消息，不进行消息转发
     * @param message
     * @param msgId
     * @return
     */
    String sendMessage(MessageDTO message,Long msgId);
}
