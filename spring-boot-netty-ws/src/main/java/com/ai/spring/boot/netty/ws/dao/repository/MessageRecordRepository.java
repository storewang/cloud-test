package com.ai.spring.boot.netty.ws.dao.repository;

import com.ai.spring.boot.ds.dao.repository.BaseJpaRepository;
import com.ai.spring.boot.netty.ws.dao.bean.MessageRecord;

/**
 * 消息发送记录Jpa操作服务
 *
 * @author 石头
 * @Date 2020/7/1
 * @Version 1.0
 **/
public interface MessageRecordRepository extends BaseJpaRepository<MessageRecord> {
}
