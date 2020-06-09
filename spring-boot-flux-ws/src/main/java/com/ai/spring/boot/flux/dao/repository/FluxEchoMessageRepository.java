package com.ai.spring.boot.flux.dao.repository;

import com.ai.spring.boot.ds.dao.repository.BaseJpaRepository;
import com.ai.spring.boot.flux.dao.bean.FluxEchoMessage;

/**
 * 消息发送记录Jpa操作服务
 *
 * @author 石头
 * @Date 2020/6/9
 * @Version 1.0
 **/
public interface FluxEchoMessageRepository extends BaseJpaRepository<FluxEchoMessage> {

}
