package com.ai.spring.boot.flux.dao.repository;

import com.ai.spring.boot.flux.dao.bean.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * JPA操作父类
 *
 * @author 石头
 * @Date 2020/6/4
 * @Version 1.0
 **/
public interface BaseJpaRepository<T extends BaseEntity> extends JpaRepository<T,Long>,JpaSpecificationExecutor<T> {
}
