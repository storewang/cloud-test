package com.ai.spring.boot.netty.ws.service;

import com.ai.spring.boot.netty.ws.conf.ServerProperties;
import com.ai.spring.boot.netty.ws.thread.BusinessThreadPoolExcutor;
import com.ai.spring.boot.netty.ws.thread.BusinessThreadTask;
import com.ai.spring.boot.netty.ws.thread.ThreadDiscardPolicy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程服务
 *
 * @author 石头
 * @Date 2020/6/30
 * @Version 1.0
 **/
@Service
@Slf4j
public class BusinessThreadService {
    private ThreadPoolExecutor threadPoolExecutor;
    @Autowired
    private ServerProperties serverProperties;

    @PostConstruct
    public void init(){
        threadPoolExecutor = new BusinessThreadPoolExcutor(1,serverProperties.getBusinessThreadNum(),600,new ArrayBlockingQueue<>(serverProperties.getBusinessQueueSize()),new ThreadDiscardPolicy());
    }

    public void execute(BusinessThreadTask task){
        threadPoolExecutor.execute(task);
    }

    public void logInfos(){
        log.info("-------thread.pool.queue.size={}---------",threadPoolExecutor.getQueue().size());
        log.info("-------thread.pool.active.size={}---------",threadPoolExecutor.getActiveCount());
    }

    @PreDestroy
    public void destroy(){
        threadPoolExecutor.shutdown();
    }
}
