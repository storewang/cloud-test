package com.ai.spring.boot.netty.ws.service;

import com.ai.spring.boot.netty.ws.conf.ServerProperties;
import com.ai.spring.boot.netty.ws.model.ClientChannel;
import com.ai.spring.boot.netty.ws.model.DispatchMsgRequest;
import com.ai.spring.boot.netty.ws.model.UserDTO;
import com.ai.spring.boot.netty.ws.util.UserCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 默认实现
 *
 * @author 石头
 * @Date 2020/6/30
 * @Version 1.0
 **/
@Slf4j
public class DefaultServerHandlerService implements ServerHandlerService{
    private static final Map<String,UserDTO> users = new HashMap<>(16);
    private static final ConcurrentHashMap<String,ClientChannel> clientRegisters = new ConcurrentHashMap<>(16);
    static {
        users.put("token01",UserDTO.builder().userId(1L).userName("用户01").build());
        users.put("token02",UserDTO.builder().userId(2L).userName("用户02").build());
        users.put("token03",UserDTO.builder().userId(3L).userName("用户03").build());
    }
    @Autowired
    private ServerProperties serverProperties;

    @Override
    public Boolean checkToken(String token) {
        return users.keySet().contains(token);
    }

    @Override
    public UserDTO getUserByToken(String token) {
        return users.get(token);
    }

    @Override
    public UserDTO getUserByCode(String code) {
        String token = UserCodeUtil.getTokenByUserCode(code);
        return getUserByToken(token);
    }

    @Override
    public List<UserDTO> getUserByCode(List<String> codes) {
        List<UserDTO> dtoList = codes.stream().map(code -> getUserByCode(code)).collect(Collectors.toList());
        return dtoList;
    }

    @Override
    public void register(ClientChannel clientChannel) {
        String userCode = clientChannel.getUser().getUserCode();
        clientRegisters.putIfAbsent(userCode,clientChannel);
    }
    @Override
    public ClientChannel getClientChannelByUcode(String userCode){
        return clientRegisters.get(userCode);
    }

    @Override
    public void dispatcher(DispatchMsgRequest request) {
        Integer msgType = request.getMessage().getMsgType();

    }

    @Override
    public void monitorResources() {
        log.info("-----{}:{}---online users:------------",serverProperties.getHost(),serverProperties.getPort(),clientRegisters.size());
    }
}
