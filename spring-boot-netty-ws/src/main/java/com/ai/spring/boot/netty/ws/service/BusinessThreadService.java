package com.ai.spring.boot.netty.ws.service;

import com.ai.spring.boot.netty.ws.conf.ServerProperties;
import com.ai.spring.boot.netty.ws.thread.BusinessThreadPoolExcutor;
import com.ai.spring.boot.netty.ws.thread.BusinessThreadTask;
import com.ai.spring.boot.netty.ws.thread.ThreadDiscardPolicy;
import com.ai.spring.boot.netty.ws.util.Consts;
import com.ai.spring.boot.netty.ws.util.WebClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
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
    @Autowired
    private WebClient webClient;
    @Autowired
    private RegistHostService registHostService;

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

        List<String> hosts = registHostService.getBindHosts();
        hosts.stream().forEach(host -> {
            String remoteUrl = Consts.HTTP_SCHEMA + host + Consts.HOME_METHOD;

            Mono<String> bodyToMono = webClient.get().uri(remoteUrl).retrieve().bodyToMono(String.class);
            bodyToMono.doOnError(WebClientResponseException.class, err -> {
                log.warn("---------remoteUrl:{}不过达...------------",remoteUrl);
                registHostService.unBindHost(host);
            });
            bodyToMono.subscribe(result -> {
                log.warn("---------result:{}------------",result);
            });
        });
    }

    @PreDestroy
    public void destroy(){
        threadPoolExecutor.shutdown();
    }
}
