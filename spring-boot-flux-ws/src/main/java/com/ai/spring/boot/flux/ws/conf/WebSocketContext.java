package com.ai.spring.boot.flux.ws.conf;

import com.ai.spring.boot.flux.dao.IFluxEchoMessageDao;
import com.ai.spring.boot.flux.service.MessageRouteService;
import com.ai.spring.boot.flux.service.RedisService;
import com.ai.spring.boot.flux.ws.dto.EchoMessage;
import com.ai.spring.boot.flux.ws.util.EchoMessageJsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * web socket context
 *
 * @author 石头
 * @Date 2020/5/28
 * @Version 1.0
 **/
@Component
@Slf4j
public class WebSocketContext {
    /**记录session与websocket的映射关系*/
    private ConcurrentHashMap<String,WebSocketSessionContext> socketContext = new ConcurrentHashMap<>(16);
    /**记录session与用户id的映射关系*/
    private ConcurrentHashMap<String,String> sessionUserIdMap = new ConcurrentHashMap<>(16);
    /**
     * 记录用户id与socket连接的session的映射关系
     * 同一用户可以多次登录连接，所以一个用户Id对应多个socket连接session信息
     * */
    private ConcurrentHashMap<String,List<String>> userSessionsMap = new ConcurrentHashMap<>(16);
    /**用户信息记录与删除时的锁*/
    private ReentrantLock lock = new ReentrantLock();
//    @Autowired
//    private MqProducer mqProducer;
    @Autowired
    private MessageRouteService messageRouteService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private IFluxEchoMessageDao messageDao;
    /**
     * 记录客户连接与session的映射关系
     * 这里可以注册到注册中心记录客户端上线信息
     * @param sessionId       session
     * @param uid             用户标识
     * @param sessionContext  websocketContext
     */
    public void addSocketSession(String sessionId,String uid,WebSocketSessionContext sessionContext){
        lock.lock();
        sessionUserIdMap.putIfAbsent(sessionId,uid);
        socketContext.putIfAbsent(sessionId,sessionContext);

        List<String> sessionList = userSessionsMap.get(uid);
        if (sessionList == null){
            sessionList = new ArrayList<>();
            userSessionsMap.putIfAbsent(uid,sessionList);
        }
        sessionList.add(sessionId);

        // 添加注册信息
        redisService.registWsHost(uid,sessionId);
        lock.unlock();
    }

    /**
     * 删除客户下线后的相关记录信息
     * @param sessionId
     */
    public void removeSocketSessionWithSessionId(String sessionId){
        lock.lock();
        String uid = sessionUserIdMap.get(sessionId);
        Optional.ofNullable(uid).ifPresent(id -> {
            sessionUserIdMap.remove(sessionId);
            socketContext.remove(sessionId);
            userSessionsMap.get(uid).remove(sessionId);
        });
        // 删除注册信息
        redisService.unRegistHost(uid,sessionId);
        lock.unlock();
    }

    public WebSocketSessionContext getSocketSessionWithSessionId(String sessionId){
        WebSocketSessionContext socketSessionContext = socketContext.get(sessionId);
        return socketSessionContext;
    }

    /**
     * 直接发送消息，忽略不在本机器的连接
     * @param userId
     * @param message
     */
    public void sendMessage(String userId,String message,Long messageId){
        List<String> sessionIds = userSessionsMap.get(userId);
        if (!CollectionUtils.isEmpty(sessionIds)){
            sessionIds.stream().forEach(sessionId -> {
                WebSocketSessionContext socketSession = getSocketSessionWithSessionId(sessionId);
                if (socketSession!=null){
                    socketSession.sendData(message);

                    Optional.ofNullable(messageId).ifPresent(id -> {
                        // 标记消息已发送
                        EchoMessage echoMessage = new EchoMessage();
                        echoMessage.setMsg(message);
                        echoMessage.setTo(userId);
                        messageDao.updateEchoMessageSended(echoMessage,id);
                    });
                }
            });
        }
    }
    /**批量发送消息*/
    public void sendMessages(List<EchoMessage> messages){
        Optional.ofNullable(messages).ifPresent(msgs -> msgs.stream().forEach(msg -> sendMessage(msg)));
    }
    /**
     * 消息发送，根据消息接收者id进行消息发送
     * 同一个消息接收者Id有可能对应多个连接session
     * 比如同一个用户在不同设备上登录，H5，IOS,ANDROID
     * @param echoMessage
     */
    public void sendMessage(EchoMessage echoMessage){
        if (!redisService.isUserOnline(echoMessage.getTo())){
            // 不在线，记录离线消息，其就是记录消息为未发送状态
            messageDao.saveEchoMessage(echoMessage);
            return;
        }

        // 消息持久化存储，默认为未发送状态，
        Long messageId = echoMessage.getMessageId();
        if (messageId == null){
            messageId = messageDao.saveEchoMessage(echoMessage);
        }
        final Long msgId = messageId;
        log.info("--------消息发送:{}--------------", msgId);
        // 当连接不在本机器上时，需要进行消息转发，如果不记录消息，转发失败时，消息就丢失了。
        List<String> sessionIds = userSessionsMap.get(echoMessage.getTo());
        log.info("-----{}---消息发送:{},{}--------------", sessionIds,echoMessage.getTo(),userSessionsMap);
        if (!CollectionUtils.isEmpty(sessionIds)){
            // 如果sessionId不为空，这些对应的sessionId一定与本机器进行了连接
            log.info("--------消息发送本机上的连接的客户端:{}--------------", msgId);
            sessionIds.stream().forEach(sessionId -> sendMessage(echoMessage,sessionId,msgId));
        }
        // 还有可能一些连接不在本机器上，所以这里还要发送消息进行消息的分发
        // 这里可以使用注册中心进行判断是还有在其他机器的连接信息
        // 这里可以使用注册中心进行判断是否在线，来判断这个消息是否需要保存到离线消息表中
        // 如果在线则转发消息.1: 使用kafka等分布式消息，2: 获取具体的连接进行消息分发.
        // 如果不在线（集群中没有一个连接信息，则为离线，只要有一个连接信息则为在线），否则存入离线消息表，等用户下次启动重新建立连接后，获取离线消息，并推送给客户端。

        // 这里简化处理逻辑，只要不在本机器上的连接都进行消息发送，不处理在其他机器上的登录
        // 也可以不用消息,改用接口直接调用，具体地址从注册中心获取，如果集群机器少，采用这种简单，不需要依赖消息中间件
//        if (CollectionUtils.isEmpty(sessionIds)){
//            MqProducerRecord record = new MqProducerRecord();
//            record.setTopic(Constans.MESSAGE_TOPIC);
//            record.setMsgKey(echoMessage.getTo());
//            record.setContent(echoMessage.getMsg());

//            mqProducer.send(record,null);
//        }

        // 不敢本机器上有没有连接，都进行转发一次，在route里判断是否有其他连接.
        log.info("-------->消息转发:{}--------------", msgId);
        messageRouteService.sendMessage(echoMessage,msgId);

    }

    /**
     * 根据sessionId进行消息回复
     * 这里基本不用判断是否在线，因为根据sessionId进行消息发送的情况，
     * 都是获取到了sessionId进行消息发发送，所以这个session肯定是在线的，且在本机上.
     * @param echoMessage
     * @param sessionId
     */
    public void sendMessage(EchoMessage echoMessage,String sessionId,Long messageId){
        WebSocketSessionContext socketSession = getSocketSessionWithSessionId(sessionId);
        if (socketSession!=null){
            socketSession.sendData(echoMessage.getMsg());
            // 标记消息已发送
            messageDao.updateEchoMessageSended(echoMessage,messageId);
        }else {
            // 这种情况的概率比较小，就是调用这个方法进行消息发送时，客户端正好关闭或是退出了。
            // 这种情况要么做离线消息，要么不用理会，让其丢失。
            // 这里不做消息存储，也就不处理丢失情况
            log.info("----------未找到{}:{}对应的socket连接信息，发送消息失败.-------------",sessionId,echoMessage.getFrom());
        }
    }
}