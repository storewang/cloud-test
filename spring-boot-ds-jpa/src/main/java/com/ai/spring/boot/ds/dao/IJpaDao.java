package com.ai.spring.boot.ds.dao;

import com.ai.spring.boot.ds.dao.bean.Page;
import com.ai.spring.boot.ds.jpa.IQueryCriteria;

import java.util.List;

/**
 * Jpa操作类
 *
 * @author 石头
 * @Date 2020/6/4
 * @Version 1.0
 **/
public interface IJpaDao<T> {
    T save(T entity);
    T updateBySenstive(T entity);
    T findById(Long id);
    List<T> findByIds(List<Long> ids);
    boolean removeById(Long id);
    boolean removeByIds(List<Long> ids);

    List<T> findBySenstive(T entity);
    List<T> findAll();

    List<T> queryByCriteria(IQueryCriteria queryCriteria, T entity);
    Page<T> queryByCriteria(IQueryCriteria queryCriteria, T entity, Page<?> page);
}
