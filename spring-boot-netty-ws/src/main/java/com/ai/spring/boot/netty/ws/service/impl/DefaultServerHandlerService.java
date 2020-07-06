package com.ai.spring.boot.netty.ws.service.impl;

import com.ai.spring.boot.netty.ws.conf.ServerProperties;
import com.ai.spring.boot.netty.ws.dao.IMessageRecordDao;
import com.ai.spring.boot.netty.ws.dao.bean.MessageRecord;
import com.ai.spring.boot.netty.ws.handler.MessageHandler;
import com.ai.spring.boot.netty.ws.model.ClientChannel;
import com.ai.spring.boot.netty.ws.model.DispatchMsgRequest;
import com.ai.spring.boot.netty.ws.model.MessageDTO;
import com.ai.spring.boot.netty.ws.model.UserDTO;
import com.ai.spring.boot.netty.ws.service.BusinessThreadService;
import com.ai.spring.boot.netty.ws.service.RegistHostService;
import com.ai.spring.boot.netty.ws.service.ServerHandlerService;
import com.ai.spring.boot.netty.ws.util.Consts;
import com.ai.spring.boot.netty.ws.util.MessageJsonUtil;
import com.ai.spring.boot.netty.ws.util.MessageType;
import com.ai.spring.boot.netty.ws.util.UserCodeUtil;
import com.ai.spring.boot.netty.ws.util.WebClientUtil;
import com.ai.spring.im.common.util.StringUtil;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
public class DefaultServerHandlerService implements ServerHandlerService {
    private static final Map<Long,UserDTO> users = new HashMap<>(16);
    private static final ConcurrentHashMap<String,ClientChannel> clientRegisters = new ConcurrentHashMap<>(16);
    private static final ConcurrentHashMap<String,String> userTokens = new ConcurrentHashMap<>(16);
    static {
        users.put(1L,UserDTO.builder().userId(1L).userName("用户01").build());
        users.put(2L,UserDTO.builder().userId(2L).userName("用户02").build());
        users.put(3L,UserDTO.builder().userId(3L).userName("用户03").build());
        users.put(4L,UserDTO.builder().userId(4L).userName("用户04").build());
        users.put(5L,UserDTO.builder().userId(5L).userName("用户05").build());
        users.put(6L,UserDTO.builder().userId(6L).userName("用户06").build());
    }
    @Autowired
    private ServerProperties serverProperties;
    @Autowired
    private BusinessThreadService businessThreadService;
    @Autowired
    @Qualifier("messageHandlerFactory")
    private MessageHandler messageHandler;
    @Autowired
    private IMessageRecordDao messageRecordDao;
    @Autowired
    private RegistHostService registHostService;
    @Autowired
    private WebClient webClient;
    @Override
    public String login(Long userId){
        UserDTO userDTO = users.get(userId);

        return Optional.ofNullable(userDTO).map(user -> UserCodeUtil.getUserToken(user)).orElse(null);
    }
    @Override
    public Boolean checkToken(String token) {
        Long userId = UserCodeUtil.getUserIdByToken(token);

        return Optional.ofNullable(userId).map(id -> users.keySet().contains(id)).orElse(Boolean.FALSE);
    }

    public String getHashLoginedToken(String userId){
        return userTokens.get(userId);
    }

    @Override
    public UserDTO getUserByToken(String token) {
        Long userId = UserCodeUtil.getUserIdByToken(token);
        return Optional.ofNullable(userId).map(id -> users.get(id)).orElse(null);
    }

    @Override
    public UserDTO getUserByCode(String code) {
        String token    = UserCodeUtil.getTokenByUserCode(code);
        UserDTO userDTO = getUserByToken(token);
        userDTO.setUserCode(code);
        return userDTO;
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
        userTokens.putIfAbsent(clientChannel.getUser().getUserId().toString(),clientChannel.getToken());

        registHostService.registWsHost(userCode,serverProperties.getWebHost());
    }
    @Override
    public void bindHost(){
        registHostService.bindHost(serverProperties.getWebHost());
    }
    @Override
    public void unBindHost(){
        registHostService.unBindHost(serverProperties.getWebHost());
    }
    @Override
    public void unRegister(String token,String channelId){
        String userCode = UserCodeUtil.getUserCode(token,channelId);

        clientRegisters.remove(userCode);
        userTokens.remove(UserCodeUtil.getUserIdByToken(token).toString());

        registHostService.unRegistHost(userCode,serverProperties.getWebHost());
    }

    @Override
    public ClientChannel getClientChannelByUcode(String userCode){
        return clientRegisters.get(userCode);
    }

    @Override
    public void dispatcher(DispatchMsgRequest request) {
        messageHandler.handler(request);
    }

    @Override
    public void monitorResources() {
        log.info("-----{}:{}---online users:------------",serverProperties.getHost(),serverProperties.getPort(),clientRegisters.size());
        businessThreadService.logInfos();
    }

    @Override
    public void forwardMesage(MessageDTO message,Long msgId){
        // 消息转发到其机器或是进程进行消息发送,这里的应该从全局registerHosts中获取其他hosts
        log.info("----------消息转发:{}:{}-----------",msgId,message);
        List<String> hosts;
        if (MessageType.USER_GROUP.getMsgType().equals(message.getMsgType())){
            hosts = registHostService.getBindHosts();
        }else {
            hosts = registHostService.getRegistHostsWithoutLocal(message.getTo().getUserCode(),serverProperties.getWebHost());
        }

        hosts.stream().forEach(host -> {
            String remoteUrl = Consts.HTTP_SCHEMA + host + Consts.FORWARD_METHOD;
            WebClientUtil.postMessage(webClient,message,String.format(remoteUrl,msgId));
        });
    }

    @Override
    public Long saveMessage(MessageDTO message){
        log.info("----------添加消息:{}-----------",message);
        String sender   = UserCodeUtil.getUserIdByCode(message.getFrom().getUserCode());
        String receiver = UserCodeUtil.getUserIdByCode(message.getTo().getUserCode());
        if (StringUtil.isEmpty(sender) || StringUtil.isEmpty(receiver)){
            log.warn("消息接收者或是发送者为空，忽略不进行持久化。");
            return null;
        }
        MessageRecord record = new MessageRecord();
        record.setMsg(message.getContent());
        record.setSender(sender);
        record.setReceiver(receiver);

        return messageRecordDao.saveMessage(record);
    }

    @Override
    public void makeMessageHasSended(Long msgId){
        log.info("----------修改消息为已发送状态:{}-----------",msgId);
        messageRecordDao.updateMessageSended(msgId);
    }

    @Override
    public String sendMessage(MessageDTO message,Long msgId){
        if (MessageType.USER_GROUP.getMsgType().equals(message.getMsgType())){
            // 群发
            clientRegisters.keySet().stream().forEach(userCode -> sendMessage(userCode,message,null));

            return Consts.RSP_OK;
        }else {
            String userCode = message.getTo().getUserCode();
            return sendMessage(userCode,message,msgId);
        }
    }

    private String sendMessage(String userCode,MessageDTO message,Long msgId){
        ClientChannel clientChannel = clientRegisters.get(userCode);
        if (clientChannel!=null && clientChannel.getChannel()!=null){
            clientChannel.getChannel().writeAndFlush(new TextWebSocketFrame(MessageJsonUtil.toJson(message)));

            // 标记消息已经发送
            if (msgId!=null){
                makeMessageHasSended(msgId);
            }
            return Consts.RSP_OK;
        }
        return Consts.RSP_IGNORE;
    }
}
